package GestionDePedidos;

import GestionDelMenu.Plato;
import java.io.Serializable;
import java.util.Objects;

public class DetallePedido implements Serializable {

    private Plato plato;
    private int cantidad;

    // Constructor por defecto
    public DetallePedido() {
    }

    // Obtiene el plato asociado a este renglón de detalle
    public Plato getPlato() {
        return plato;
    }

    // Asigna el plato al detalle comprobando que no sea nulo
    public void setPlato(Plato plato) {
        if (plato == null) {
            throw new IllegalArgumentException("El plato no puede ser nulo.");
        }
        this.plato = plato;
    }

    // Obtiene la cantidad ordenada del plato
    public int getCantidad() {
        return cantidad;
    }

    // Establece y valida que la cantidad ordenada sea mayor a cero
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.cantidad = cantidad;
    }

    // Incrementa la cantidad solicitada del plato en el renglón
    public void incrementarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor a cero.");
        }
        this.cantidad += cantidad;
    }

    // Calcula el importe subtotal multiplicando el precio del plato por la cantidad
    public double calcularSubtotal() {
        return plato != null ? plato.getPrecio() * cantidad : 0;
    }

    // Devuelve la representación en cadena de texto del detalle del pedido
    @Override
    public String toString() {
        return "Plato: " + (plato != null ? plato.getNombre() : "null") +
                ", Cantidad: " + cantidad +
                ", Subtotal: " + calcularSubtotal();
    }

    // Genera el código hash del detalle del pedido
    @Override
    public int hashCode() {
        return Objects.hash(plato, cantidad);
    }

    // Compara la igualdad con otro detalle en función del plato contenido
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DetallePedido)) return false;
        DetallePedido other = (DetallePedido) obj;
        return Objects.equals(plato, other.plato);
    }
}
