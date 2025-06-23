package com.proyecto.springBoot.service;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.repository.EstudianteRepository;
import com.proyecto.springBoot.repository.UsuarioRepository;
import com.proyecto.springBoot.util.Validador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> searchByCedulaAndRol(String cedula) {
        return repository.findByCedulaContainingAndRol(cedula, "SECRETARIA");
    }

    public List<Usuario> getUsuarios(){
        return repository.findByRol("SECRETARIA");
    }

    public Usuario saveUsuario(Usuario usuario) {
        Validador.validarUnicidadUsuario(usuario, repository, estudianteRepository);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("SECRETARIA");
        Validador.validarUsuario(usuario);
        return repository.save(usuario);
    }


    public Optional<Usuario> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Usuario save(Usuario usuario) {
        Validador.validarUsuario(usuario);
        return repository.save(usuario);
    }

    public Optional<Usuario> findByCedula(String cedula){
        return  repository.findById(cedula);
    }

    public void deleteById(String cedula){
        repository.deleteById(cedula);
    }

}
