package GestionDelMenu;

import java.io.Serializable;
import java.util.Objects;

public abstract class Plato implements Serializable{

    // Nombre del plato (identificador principal)
    protected String nombre;

    // Precio del plato
    protected double precio;

    // Indica si el plato está disponible para ser pedido
    protected boolean disponible;

    // Constructor vacío
    public Plato() {
    }

    // Retorna el nombre del plato
    public String getNombre() {
        return nombre;
    }

    // Asigna el nombre del plato con validación
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
        }
        this.nombre = nombre.trim();
    }

    // Retorna el precio del plato
    public double getPrecio() {
        return precio;
    }

    // Asigna el precio del plato con validación (no puede ser negativo)
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    // Retorna si el plato está disponible o no
    public boolean getDisponibilidad() {
        return disponible;
    }

    // Cambia el estado de disponibilidad del plato
    public void setDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    // Método abstracto: cada tipo de plato define su propia categoría
    public abstract CategoriaPlato getCategoria();

    // Representación en texto del plato (útil para mostrar en consola)
    @Override
    public String toString() {
        return "Nombre: " + nombre +
                ", Precio: " + precio +
                ", Disponible: " + disponible +
                ", Categoria: " + getCategoria();
    }

    // Hash basado en el nombre (identificador lógico del plato)
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    // Dos platos se consideran iguales si tienen el mismo nombre
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Plato other = (Plato) obj;
        return Objects.equals(nombre, other.nombre);
    }
}