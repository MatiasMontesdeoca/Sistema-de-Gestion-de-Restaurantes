package GestionDelMenu;

import java.util.Objects;

public abstract class Plato {
    //Atributos
    protected String nombre;
    protected double precio;
    protected boolean disponible;

    //Constructos
    public Plato() {
    }

    //Get y Set de nombre del plato
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
        }
        this.nombre = nombre.trim();
    }

    //Get y Set del precio de un plato
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    //Get y Set la disponibilidad de un plato
    public boolean getDisponibilidad() {
        return disponible;
    }

    public void setDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    //Get de la categoria del plato
    public abstract CategoriaPlato getCategoria();

    //Overrides
    @Override
    public String toString() {
        return  "Nombre: " + nombre +
                ", Precio: " + precio +
                ", Disponible: " + disponible +
                ", Categoria: " + getCategoria();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Plato other = (Plato) obj;
        return Objects.equals(nombre, other.nombre);
    }
}