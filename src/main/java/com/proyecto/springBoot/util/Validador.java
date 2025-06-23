package com.proyecto.springBoot.util;

import com.proyecto.springBoot.entity.Estudiante;
import com.proyecto.springBoot.entity.Usuario;
import com.proyecto.springBoot.repository.EstudianteRepository;
import com.proyecto.springBoot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class Validador {

    public static boolean validarCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) return false;
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) return false;
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito > 6) return false;

        int[] coeficientes = {2,1,2,1,2,1,2,1,2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            if (valor > 9) valor -= 9;
            suma += valor;
        }
        int digitoVerificador = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }

    public static boolean validarCelular(String celular) {
        return celular != null && celular.matches("^09\\d{8}$");
    }

    public static boolean validarEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    public static void validarEstudiante(Estudiante estudiante) {
        if (!validarCedula(estudiante.getCedula()))
            throw new IllegalArgumentException("La cédula ingresada no es válida.");
        if (!validarCelular(estudiante.getTelefono()))
            throw new IllegalArgumentException("El teléfono debe ser de 10 dígitos y empezar con 09.");
    }

    public static void validarUsuario(Usuario usuario) {
        if (!validarCedula(usuario.getCedula()))
            throw new IllegalArgumentException("La cédula ingresada no es válida.");
        if (!validarCelular(usuario.getTelefono()))
            throw new IllegalArgumentException("El teléfono debe ser de 10 dígitos y empezar con 09.");
        if(!validarEmail(usuario.getEmail()))
            throw  new IllegalArgumentException("El email no cumple con el formato correcto.");
    }

    public static void validarUnicidadUsuario(Usuario usuario, UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        if (usuarioRepository.findByCedula(usuario.getCedula()).isPresent()) {
            throw new IllegalArgumentException("La cédula ya está registrada como secretaria.");
        }
        if (estudianteRepository.findByCedula(usuario.getCedula()).isPresent()) {
            throw new IllegalArgumentException("La cédula ya está registrada como estudiante.");
        }
    }

    public static void validarUnicidadEstudiante(Estudiante estudiante, EstudianteRepository estudianteRepository, UsuarioRepository usuarioRepository) {
        if (estudianteRepository.findByCedula(estudiante.getCedula()).isPresent()) {
            throw new IllegalArgumentException("La cédula ya está registrada como estudiante.");
        }
        if (usuarioRepository.findByCedula(estudiante.getCedula()).isPresent()) {
            throw new IllegalArgumentException("La cédula ya está registrada como secretaria.");
        }
    }

}
