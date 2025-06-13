package com.proyecto.springBoot.service;

import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario saveUsuario(Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public Optional<Usuario> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Optional<Usuario> findByCedula(String cedula){
        return  repository.findById(cedula);
    }
}
