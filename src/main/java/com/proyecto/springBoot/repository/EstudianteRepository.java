package com.proyecto.springBoot.repository;

import com.proyecto.springBoot.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    List<Estudiante> findByCedulaContaining(String cedula);
}
