package GestionDePedidos;

import GestionDelMenu.Plato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Pedido implements Serializable{

    // Identificador único del pedido
    private String numeroPedido;

    // Fecha y hora en la que se creó el pedido
    private LocalDateTime fecha;

    // Cliente asociado al pedido
    private Cliente cliente;

    // Mesero responsable del pedido (si está asignado)
    private Mesero mesero;

    // Mesa a la que pertenece el pedido
    private Mesa mesa;

    // Estado actual del pedido (PENDIENTE, PREPARANDO, etc.)
    private EstadoPedido estado;

    // Lista de platos incluidos en el pedido
    private ArrayList<DetallePedido> detalles;

    // Constructor: inicializa lista de detalles, estado y fecha
    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.fecha = LocalDateTime.now();
    }

    // Obtiene el número del pedido
    public String getNumeroPedido() {
        return numeroPedido;
    }

    // Asigna el número del pedido con validación
    public void setNumeroPedido(String numeroPedido) {

        if (numeroPedido == null || numeroPedido.trim().isEmpty()) {
            throw new IllegalArgumentException("El numero de pedido no puede ser vacio.");
        }

        this.numeroPedido = numeroPedido.trim();
    }

    // Obtiene la fecha del pedido
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Obtiene el cliente del pedido
    public Cliente getCliente() {
        return cliente;
    }

    // Asigna el cliente con validación
    public void setCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        this.cliente = cliente;
    }

    // Obtiene el mesero asignado
    public Mesero getMesero() {
        return mesero;
    }

    // Asigna el mesero con validación
    public void setMesero(Mesero mesero) {

        if (mesero == null) {
            throw new IllegalArgumentException("El mesero no puede ser nulo.");
        }

        this.mesero = mesero;
    }

    // Obtiene la mesa asociada al pedido
    public Mesa getMesa() {
        return mesa;
    }

    // Asigna la mesa con validación
    public void setMesa(Mesa mesa) {

        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }

        this.mesa = mesa;
    }

    // Obtiene el estado del pedido
    public EstadoPedido getEstado() {
        return estado;
    }

    // Obtiene la lista de detalles del pedido
    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    // Agrega un plato al pedido o incrementa cantidad si ya existe
    public void agregarPlato(Plato plato, int cantidad) {

        // Verifica si el pedido puede modificarse
        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }

        // Validación de plato
        if (plato == null) {
            throw new IllegalArgumentException("El plato no puede ser nulo.");
        }

        // Validación de cantidad
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad invalida.");
        }

        // Si el plato ya existe, solo incrementa cantidad
        for (DetallePedido d : detalles) {
            if (d.getPlato().equals(plato)) {
                d.incrementarCantidad(cantidad);
                return;
            }
        }

        // Si no existe, crea un nuevo detalle
        DetallePedido nuevo = new DetallePedido();
        nuevo.setPlato(plato);
        nuevo.setCantidad(cantidad);
        detalles.add(nuevo);
    }

    // Elimina un plato del pedido
    public void eliminarPlato(Plato plato) {

        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }

        detalles.removeIf(d -> d.getPlato().equals(plato));
    }

    // Modifica la cantidad de un plato existente
    public void modificarCantidad(Plato plato, int cantidad) {

        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }

        for (DetallePedido d : detalles) {
            if (d.getPlato().equals(plato)) {
                d.setCantidad(cantidad);
                return;
            }
        }

        throw new IllegalArgumentException("Plato no encontrado en el pedido.");
    }

    // Calcula el total del pedido sin descuentos
    public double calcularTotalSinDescuento() {

        double total = 0;

        for (DetallePedido d : detalles) {
            total += d.calcularSubtotal();
        }

        return total;
    }

    // Retorna la cantidad de tipos de platos en el pedido
    public int obtenerCantidadItems() {
        return detalles.size();
    }

    // Cambia el estado del pedido
    public void cambiarEstado(EstadoPedido nuevoEstado) {

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("Estado invalido.");
        }

        this.estado = nuevoEstado;
    }

    // Verifica si el pedido puede ser modificado
    public boolean esModificable() {
        return estado == EstadoPedido.PENDIENTE;
    }

    // Verifica si el pedido puede ser cancelado
    public boolean esCancelable() {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.PREPARANDO;
    }

    // Verifica si el pedido está pagado
    public boolean estaPagado() {
        return estado == EstadoPedido.PAGADO;
    }

    // Valida si se puede cambiar el estado del pedido
    public void validarTransicionEstado(EstadoPedido nuevoEstado) {

        // No permite cambios si ya fue cerrado
        if (estado == EstadoPedido.CANCELADO || estado == EstadoPedido.PAGADO) {
            throw new IllegalStateException("No se puede cambiar el estado.");
        }
    }

    // Representación en texto del pedido
    @Override
    public String toString() {
        return "Pedido #" + numeroPedido +
                ", Estado: " + estado +
                ", Total: " + calcularTotalSinDescuento();
    }

    // Hash basado en número de pedido
    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }

    // Dos pedidos son iguales si tienen el mismo número
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Pedido)) return false;

        Pedido other = (Pedido) obj;
        return Objects.equals(numeroPedido, other.numeroPedido);
    }
}