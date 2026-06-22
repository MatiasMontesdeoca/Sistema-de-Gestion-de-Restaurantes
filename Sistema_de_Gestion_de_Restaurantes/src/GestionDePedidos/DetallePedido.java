package GestionDePedidos;

import GestionDelMenu.Plato;
import java.util.Objects;

public class DetallePedido {
    //Atributos
    private Plato plato;
    private int cantidad;

    //Constructor
    public DetallePedido() {
    }

    //Get y Set de platos a añadir al pedido
    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        if (plato == null) {
            throw new IllegalArgumentException("El plato no puede ser nulo.");
        }
        this.plato = plato;
    }

    //Get y Set de cantidad de platos a añadir al pedido
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.cantidad = cantidad;
    }

    //Métodos
    public void incrementarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor a cero.");
        }
        this.cantidad += cantidad;
    }

    public double calcularSubtotal() {
        return plato != null ? plato.getPrecio() * cantidad : 0;
    }

    //Overrides
    @Override
    public String toString() {
        return "Plato: " + (plato != null ? plato.getNombre() : "null") +
                ", Cantidad: " + cantidad +
                ", Subtotal: " + calcularSubtotal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(plato, cantidad);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DetallePedido)) return false;

        DetallePedido other = (DetallePedido) obj;
        return Objects.equals(plato, other.plato);
    }
}
