package GestionDelMenu;
import GestionDelMenu.CategoriaPlato;
import java.util.Objects;

public abstract class Plato {
    protected String nombre;
    protected double precio;
    protected boolean disponible;

    public Plato() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    public boolean getDisponibilidad() {
        return disponible;
    }

    public void setDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    // 🔥 ESTO ES LO QUE TE FALTA
    public abstract CategoriaPlato getCategoria();

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