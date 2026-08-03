package GestionDePedidos;

import GestionDelMenu.Plato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Pedido implements Serializable {

    private String numeroPedido;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Mesero mesero;
    private Mesa mesa;
    private EstadoPedido estado;
    private ArrayList<DetallePedido> detalles;

    // Constructor que inicializa los detalles, el estado en PENDIENTE y la fecha actual
    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.fecha = LocalDateTime.now();
    }

    // Obtiene el número identificador del pedido
    public String getNumeroPedido() {
        return numeroPedido;
    }

    // Establece y valida el número identificador del pedido
    public void setNumeroPedido(String numeroPedido) {
        if (numeroPedido == null || numeroPedido.trim().isEmpty()) {
            throw new IllegalArgumentException("El numero de pedido no puede ser vacio.");
        }
        this.numeroPedido = numeroPedido.trim();
    }

    // Obtiene la fecha y hora de creación del pedido
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Obtiene el cliente asociado al pedido
    public Cliente getCliente() {
        return cliente;
    }

    // Asigna el cliente al pedido validando que no sea nulo
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        this.cliente = cliente;
    }

    // Obtiene el mesero asignado a la atención del pedido
    public Mesero getMesero() {
        return mesero;
    }

    // Asigna el mesero al pedido validando que no sea nulo
    public void setMesero(Mesero mesero) {
        if (mesero == null) {
            throw new IllegalArgumentException("El mesero no puede ser nulo.");
        }
        this.mesero = mesero;
    }

    // Obtiene la mesa asignada al pedido
    public Mesa getMesa() {
        return mesa;
    }

    // Asigna la mesa al pedido comprobando que no sea nula
    public void setMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }
        this.mesa = mesa;
    }

    // Obtiene el estado actual del pedido
    public EstadoPedido getEstado() {
        return estado;
    }

    // Obtiene la lista de renglones de detalle del pedido
    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    // Agrega un plato al pedido o incrementa su cantidad si ya fue añadido previamente
    public void agregarPlato(Plato plato, int cantidad) {
        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }
        if (plato == null) {
            throw new IllegalArgumentException("El plato no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad invalida.");
        }
        for (DetallePedido d : detalles) {
            if (d.getPlato().equals(plato)) {
                d.incrementarCantidad(cantidad);
                return;
            }
        }
        DetallePedido nuevo = new DetallePedido();
        nuevo.setPlato(plato);
        nuevo.setCantidad(cantidad);
        detalles.add(nuevo);
    }

    // Elimina un plato de la lista de detalles del pedido
    public void eliminarPlato(Plato plato) {
        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }
        detalles.removeIf(d -> d.getPlato().equals(plato));
    }

    // Modifica la cantidad ordenada de un plato existente en el pedido
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

    // Suma y retorna el importe total del pedido sin aplicar descuentos
    public double calcularTotalSinDescuento() {
        double total = 0;
        for (DetallePedido d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    // Retorna la cantidad de tipos de platos agregados al pedido
    public int obtenerCantidadItems() {
        return detalles.size();
    }

    // Cambia el estado del pedido validando el valor ingresado
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("Estado invalido.");
        }
        this.estado = nuevoEstado;
    }

    // Verifica si el pedido está en estado PENDIENTE para permitir modificaciones
    public boolean esModificable() {
        return estado == EstadoPedido.PENDIENTE;
    }

    // Verifica si el pedido se encuentra en estado cancelable (PENDIENTE o PREPARANDO)
    public boolean esCancelable() {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.PREPARANDO;
    }

    // Retorna si el pedido ya fue pagado
    public boolean estaPagado() {
        return estado == EstadoPedido.PAGADO;
    }

    // Valida que el pedido no se encuentre en un estado final (CANCELADO o PAGADO)
    public void validarTransicionEstado(EstadoPedido nuevoEstado) {
        if (estado == EstadoPedido.CANCELADO || estado == EstadoPedido.PAGADO) {
            throw new IllegalStateException("No se puede cambiar el estado.");
        }
    }

    // Devuelve la representación en cadena de texto del pedido
    @Override
    public String toString() {
        return "Pedido #" + numeroPedido +
                ", Estado: " + estado +
                ", Total: " + calcularTotalSinDescuento();
    }

    // Genera el código hash del pedido en función de su número identificador
    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }

    // Compara igualdad entre dos pedidos mediante su número identificador
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pedido)) return false;
        Pedido other = (Pedido) obj;
        return Objects.equals(numeroPedido, other.numeroPedido);
    }
}