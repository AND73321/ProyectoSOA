package com.proyecto.springBoot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "usuarios")
@Entity
public class Usuario {
    @Id
    private String cedula;

    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

    @Column(unique = true)
    private String email;
    private String password;
    private String rol;
}
