package GestionDelMenu;

import java.io.Serializable;
import java.util.Objects;

public abstract class Plato implements Serializable {

    protected String nombre;
    protected double precio;
    protected boolean disponible;

    // Constructor por defecto
    public Plato() {
    }

    // Obtiene el nombre del plato
    public String getNombre() {
        return nombre;
    }

    // Establece y valida el nombre del plato
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
        }
        this.nombre = nombre.trim();
    }

    // Obtiene el precio del plato
    public double getPrecio() {
        return precio;
    }

    // Establece y valida que el precio del plato no sea negativo
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    // Obtiene el estado de disponibilidad del plato
    public boolean getDisponibilidad() {
        return disponible;
    }

    // Establece la disponibilidad del plato
    public void setDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    // Método abstracto para obtener la categoría específica del plato
    public abstract CategoriaPlato getCategoria();

    // Devuelve la representación en cadena de texto del plato
    @Override
    public String toString() {
        return "Nombre: " + nombre +
                ", Precio: " + precio +
                ", Disponible: " + disponible +
                ", Categoria: " + getCategoria();
    }

    // Genera el código hash según el nombre del plato
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    // Compara igualdad con otro plato mediante el nombre
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plato other = (Plato) obj;
        return Objects.equals(nombre, other.nombre);
    }
}