package com.proyecto.springBoot.controller;

import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.repository.EstudianteRepository;
import com.proyecto.springBoot.service.EstudianteService;
import com.proyecto.springBoot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){
        model.addAttribute("usuario", new Usuario());
        return "secretarias_model";
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setRol("SECRETARIA");
        service.saveUsuario(usuario);
        return "redirect:/";
    }
}
