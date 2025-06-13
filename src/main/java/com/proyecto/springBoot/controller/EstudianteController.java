package com.proyecto.springBoot.controller;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EstudianteController {

    @Autowired
    private EstudianteService servicio;

    @GetMapping("/")
    public String listarEstudiantes(@RequestParam(required = false) String cedula, Model model, Authentication auth){
        if(cedula != null && !cedula.isEmpty()){
            model.addAttribute("estudiantes", servicio.searchByCedula(cedula));
        } else {
            model.addAttribute("estudiantes", servicio.getAll());
        }
        model.addAttribute("rol", auth.getAuthorities().iterator().next().getAuthority());
        return "index";
    }

    @PostMapping("/admin/estudiantes")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante){
        servicio.save(estudiante);
        return "redirect:/";
    }

    @GetMapping("/admin/estudiantes/delete")
    public String eliminarEstudiante(@RequestParam String cedula){
        servicio.deleteById(cedula);
        return "redirect:/";
    }

    @GetMapping("/admin/estudiantes/editar/{cedula}")
    public String editarEstudiante(@PathVariable String cedula, Model model){
        Estudiante estudiante = servicio.findById(cedula)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado: " + cedula));
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("modoEdicion", true);
        return "formulario_estudiante";
    }

    @PostMapping("/admin/estudiantes/editar")
    public String editarEstudiante(@ModelAttribute Estudiante estudiante) {
        servicio.save(estudiante);
        return "redirect:/";
    }
}
