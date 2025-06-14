package com.proyecto.springBoot.service;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.repository.EstudianteRepository;
import com.proyecto.springBoot.repository.UsuarioRepository;
import com.proyecto.springBoot.util.Validador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Estudiante> getAll(){
        return repository.findAll();
    }

    public Estudiante save(Estudiante estudiante) {
        Validador.validarUnicidadEstudiante(estudiante, repository, usuarioRepository);
        Validador.validarEstudiante(estudiante);
        return repository.save(estudiante);
    }

    public void deleteById(String cedula){
        repository.deleteById(cedula);
    }

    public Optional<Estudiante> findById(String cedula){
        return repository.findById(cedula);
    }

    public List<Estudiante> searchByCedula(String cedula){
        return repository.findByCedulaContaining(cedula);
    }
}
