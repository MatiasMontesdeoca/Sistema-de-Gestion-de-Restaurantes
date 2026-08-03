package GestionDeClientes;

import java.io.Serializable;

public abstract class Persona implements Serializable {

    protected String cedula;
    protected String nombre;

    // Obtiene la cédula de la persona
    public String getCedula() {
        return cedula;
    }

    // Establece y valida el número de cédula (10 dígitos)
    public void setcedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo o vacio.");
        }
        cedula = cedula.trim();
        if (!cedula.matches("\\d{10}")) {
            throw new IllegalArgumentException("El numero de cedula ingresado debe contener 10 digitos numericos");
        }
        this.cedula = cedula;
    }

    // Obtiene el nombre completo de la persona
    public String getNombre() {
        return nombre;
    }

    // Establece y valida el nombre completo de la persona (mínimo dos palabras)
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        nombre = nombre.trim();
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+(\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";
        if (!nombre.matches(regex)) {
            throw new IllegalArgumentException("Debe ingresar nombre y apellido (solo letras, minimo dos palabras).");
        }
        this.nombre = nombre;
    }

    // Devuelve la representación en cadena de texto de la persona
    @Override
    public String toString() {
        return "cedula: " + cedula + ", Nombre: " + nombre;
    }

    // Genera el código hash según la cédula
    @Override
    public int hashCode() {
        return cedula != null ? cedula.hashCode() : 0;
    }

    // Compara igualdad con otra persona mediante la cédula
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona other = (Persona) obj;
        return cedula != null && cedula.equals(other.cedula);
    }
}