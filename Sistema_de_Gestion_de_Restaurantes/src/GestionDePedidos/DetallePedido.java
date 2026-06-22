package GestionDePedidos;

import GestionDelMenu.Plato;
import java.util.Objects;

public class DetallePedido {

    // Plato asociado a este detalle del pedido (qué se está ordenando)
    private Plato plato;

    // Cantidad de ese plato solicitado en el pedido
    private int cantidad;

    // Constructor vacío
    public DetallePedido() {
    }

    // Obtiene el plato asociado al detalle del pedido
    public Plato getPlato() {
        return plato;
    }

    // Asigna un plato al detalle con validación
    public void setPlato(Plato plato) {

        // No se permite un plato nulo porque no tendría sentido en el pedido
        if (plato == null) {
            throw new IllegalArgumentException("El plato no puede ser nulo.");
        }

        this.plato = plato;
    }

    // Obtiene la cantidad solicitada del plato
    public int getCantidad() {
        return cantidad;
    }

    // Establece la cantidad del plato con validación
    public void setCantidad(int cantidad) {

        // La cantidad debe ser mayor a 0
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        this.cantidad = cantidad;
    }

    // Incrementa la cantidad de platos ya agregados
    public void incrementarCantidad(int cantidad) {

        // Validación para evitar incrementos inválidos
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor a cero.");
        }

        this.cantidad += cantidad;
    }

    // Calcula el subtotal de este detalle (precio del plato * cantidad)
    public double calcularSubtotal() {

        // Si el plato es nulo, no se puede calcular subtotal
        return plato != null ? plato.getPrecio() * cantidad : 0;
    }

    // Representación en texto del detalle del pedido
    @Override
    public String toString() {
        return "Plato: " + (plato != null ? plato.getNombre() : "null") +
                ", Cantidad: " + cantidad +
                ", Subtotal: " + calcularSubtotal();
    }

    // Hash basado en plato y cantidad
    @Override
    public int hashCode() {
        return Objects.hash(plato, cantidad);
    }

    // Dos detalles se consideran iguales si corresponden al mismo plato
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof DetallePedido)) return false;

        DetallePedido other = (DetallePedido) obj;

        return Objects.equals(plato, other.plato);
    }
}
