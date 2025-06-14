package com.proyecto.springBoot.controller;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.repository.EstudianteRepository;
import com.proyecto.springBoot.service.EstudianteService;
import org.springframework.security.core.Authentication;
import com.proyecto.springBoot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @Autowired
    private EstudianteService estudianteService;

    private void poblarModelo(Model model, Authentication auth, String tab) {
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);
        String activeTab = (tab == null) ? "secretarias" : tab;
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("secretarias", service.getUsuarios());
        model.addAttribute("secretariaNueva", new Usuario());
        model.addAttribute("secretariaEditar", new Usuario());
        model.addAttribute("showEditModalSecretaria", false);

        model.addAttribute("estudiantes", estudianteService.getAll());
        model.addAttribute("estudianteNuevo", new Estudiante());
        model.addAttribute("estudianteEditar", new Estudiante());
        model.addAttribute("showEditModal", false);
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute("secretariaNueva") Usuario usuario, Model model, Authentication auth) {
        try {
            usuario.setRol("SECRETARIA");
            service.saveUsuario(usuario);
        } catch (IllegalArgumentException ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.contains("cédula")) {
                model.addAttribute("errorCedula", errorMsg);
            } else if (errorMsg.contains("teléfono")) {
                model.addAttribute("errorTelefono", errorMsg);
            } else if (errorMsg.contains("correo")) {
                model.addAttribute("errorEmail", errorMsg);
            }
            poblarModelo(model, auth, "secretarias");
            model.addAttribute("secretariaNueva", usuario);
            model.addAttribute("abrirModalNuevaSecretaria", true);
            return "index";
        }
        return "redirect:/?tab=secretarias";
    }


    @GetMapping("/delete")
    public String eliminarEstudiante(@RequestParam String cedula) {
        service.deleteById(cedula);
        return "redirect:/?tab=secretarias";
    }

    @GetMapping("/editar/{cedula}")
    public String mostrarModalEdicion(@PathVariable String cedula) {
        return "redirect:/?tab=secretarias&editCedulaSecretaria=" + cedula;
    }

    @PostMapping("/editar")
    public String editarSecretaria(@ModelAttribute("secretariaEditar") Usuario usuario, Model model, Authentication auth) {
        Usuario original = service.findByCedula(usuario.getCedula()).orElse(null);
        if (original == null) {
            model.addAttribute("errorCedula", "No existe la secretaria para editar");
            poblarModelo(model, auth, "secretarias");
            model.addAttribute("abrirModalEditarSecretaria", true);
            return "index";
        }
        try {
            original.setNombre(usuario.getNombre());
            original.setApellido(usuario.getApellido());
            original.setDireccion(usuario.getDireccion());
            original.setTelefono(usuario.getTelefono());

            service.save(original);
        } catch (IllegalArgumentException ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.contains("teléfono")) {
                model.addAttribute("errorTelefono", errorMsg);
            }
            poblarModelo(model, auth, "secretarias");
            model.addAttribute("secretariaEditar", usuario);
            model.addAttribute("showEditModalSecretaria", true);
            return "index";
        }
        return "redirect:/?tab=secretarias";
    }

}
