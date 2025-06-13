package com.proyecto.springBoot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "estudiantes")
@Entity
public class Estudiante {
    @Id
    private String cedula;

    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
}
