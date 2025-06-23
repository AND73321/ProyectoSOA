package com.proyecto.springBoot.controller;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.service.EstudianteService;
import com.proyecto.springBoot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EstudianteController {

    @Autowired
    private EstudianteService servicio;

    @Autowired
    private UsuarioService usuarioService;

    private void poblarModelo(Model model, Authentication auth, String tab) {
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);
        String activeTab = (tab == null) ? "estudiantes" : tab;
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("estudiantes", servicio.getAll());
        model.addAttribute("secretarias", usuarioService.getUsuarios());
        model.addAttribute("estudianteNuevo", new Estudiante());
        model.addAttribute("secretariaNueva", new Usuario());
        model.addAttribute("estudianteEditar", new Estudiante());
        model.addAttribute("showEditModal", false);
        model.addAttribute("secretariaEditar", new Usuario());
        model.addAttribute("showEditModalSecretaria", false);
    }

    @GetMapping("/")
    public String listarDatos(
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) String tab,
            Model model, Authentication auth,
            @RequestParam(required = false) String editCedula,
            @RequestParam(required = false) String editCedulaSecretaria
    ) {
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);

        // VISTA SECRETARIA
        if ("SECRETARIA".equals(rol)) {
            Usuario secretaria = usuarioService.findByEmail(auth.getName()).orElse(null);
            if (secretaria != null) {
                String nombreCompleto = secretaria.getNombre() + " " + secretaria.getApellido();
                model.addAttribute("nombreSecretaria", nombreCompleto);
            }
            model.addAttribute("activeTab", "estudiantes");
            if (cedula != null && !cedula.isEmpty()) {
                model.addAttribute("estudiantes", servicio.searchByCedula(cedula));
            } else {
                model.addAttribute("estudiantes", servicio.getAll());
            }
            model.addAttribute("estudianteNuevo", new Estudiante());
            model.addAttribute("secretariaNueva", new Usuario());
            model.addAttribute("estudianteEditar", new Estudiante());
            model.addAttribute("showEditModal", false);
            model.addAttribute("secretariaEditar", new Usuario());
            model.addAttribute("showEditModalSecretaria", false);
            return "index";
        }

        // ADMIN TODAS LAS PESTAÑAS
        String activeTab = (tab == null) ? "estudiantes" : tab;
        model.addAttribute("activeTab", activeTab);

        // Lista de estudiantes
        if ("estudiantes".equals(activeTab)) {
            if (cedula != null && !cedula.isEmpty()) {
                model.addAttribute("estudiantes", servicio.searchByCedula(cedula));
            } else {
                model.addAttribute("estudiantes", servicio.getAll());
            }
        } else {
            model.addAttribute("estudiantes", servicio.getAll());
        }

        // Lista de secretarias
        if ("secretarias".equals(activeTab)) {
            if (cedula != null && !cedula.isEmpty()) {
                model.addAttribute("secretarias", usuarioService.searchByCedulaAndRol(cedula));
            } else {
                model.addAttribute("secretarias", usuarioService.getUsuarios());
            }
        } else {
            model.addAttribute("secretarias", usuarioService.getUsuarios());
        }

        model.addAttribute("estudianteNuevo", new Estudiante());
        model.addAttribute("secretariaNueva", new Usuario());

        // Modal editar estudiante
        if (editCedula != null && !editCedula.isEmpty()) {
            model.addAttribute("estudianteEditar", servicio.findById(editCedula)
                    .orElse(new Estudiante()));
            model.addAttribute("showEditModal", true);
        } else {
            model.addAttribute("estudianteEditar", new Estudiante());
            model.addAttribute("showEditModal", false);
        }

        // Modal editar secretaria
        if (editCedulaSecretaria != null && !editCedulaSecretaria.isEmpty()) {
            model.addAttribute("secretariaEditar", usuarioService.findByCedula(editCedulaSecretaria)
                    .orElse(new Usuario()));
            model.addAttribute("showEditModalSecretaria", true);
        } else {
            model.addAttribute("secretariaEditar", new Usuario());
            model.addAttribute("showEditModalSecretaria", false);
        }

        return "index";
    }

    @PostMapping("/admin/estudiantes")
    public String guardarEstudiante(@ModelAttribute("estudianteNuevo") Estudiante estudiante, Model model, Authentication auth) {
        try {
            servicio.save(estudiante);
        } catch (IllegalArgumentException ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.contains("cédula")) {
                model.addAttribute("errorCedula", errorMsg);
            } else if (errorMsg.contains("teléfono")) {
                model.addAttribute("errorTelefono", errorMsg);
            }
            poblarModelo(model, auth, "estudiantes");
            model.addAttribute("estudianteNuevo", estudiante);
            model.addAttribute("abrirModalNuevoEstudiante", true);
            return "index";
        }
        return "redirect:/?tab=estudiantes";
    }

    @GetMapping("/admin/estudiantes/delete")
    public String eliminarEstudiante(@RequestParam String cedula) {
        servicio.deleteById(cedula);
        return "redirect:/?tab=estudiantes";
    }

    @GetMapping("/admin/estudiantes/editar/{cedula}")
    public String mostrarModalEdicion(@PathVariable String cedula) {
        return "redirect:/?editCedula=" + cedula + "&tab=estudiantes";
    }

    @PostMapping("/admin/estudiantes/editar")
    public String editarEstudiante(@ModelAttribute("estudianteEditar") Estudiante estudiante, Model model, Authentication auth) {
        try {
            servicio.save(estudiante);
        } catch (IllegalArgumentException ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.contains("cédula")) {
                model.addAttribute("errorCedula", errorMsg);
            } else if (errorMsg.contains("teléfono")) {
                model.addAttribute("errorTelefono", errorMsg);
            }
            poblarModelo(model, auth, "estudiantes");
            model.addAttribute("estudianteEditar", estudiante);
            model.addAttribute("showEditModal", true);
            return "index";
        }
        return "redirect:/?tab=estudiantes";
    }
}
