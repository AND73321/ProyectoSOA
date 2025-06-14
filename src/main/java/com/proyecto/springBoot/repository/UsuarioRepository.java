package com.proyecto.springBoot.repository;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCedula(String cedula);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByCedulaContainingAndRol(String cedula, String rol);
}
