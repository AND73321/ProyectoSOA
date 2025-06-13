package com.proyecto.springBoot.service;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteRepository repository;

    public List<Estudiante> getAll(){
        return repository.findAll();
    }

    public Estudiante save(Estudiante estudiante){
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
