package GestionDePedidos;
import GestionDePedidos.EstadoPedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.Plato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
public class Pedido {
    
    private String numeroPedido;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Mesero mesero;
    private Mesa mesa;
    private EstadoPedido estado;
    private ArrayList<DetallePedido> detalles;

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.fecha = LocalDateTime.now();
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        if (numeroPedido == null || numeroPedido.trim().isEmpty()) {
            throw new IllegalArgumentException("El numero de pedido no puede ser vacio.");
        }
        this.numeroPedido = numeroPedido.trim();
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        this.cliente = cliente;
    }

    public Mesero getMesero() {
        return mesero;
    }

    public void setMesero(Mesero mesero) {
        if (mesero == null) {
            throw new IllegalArgumentException("El mesero no puede ser nulo.");
        }
        this.mesero = mesero;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }
        this.mesa = mesa;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

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

    public void eliminarPlato(Plato plato) {
        if (!esModificable()) {
            throw new IllegalStateException("El pedido no puede modificarse.");
        }

        detalles.removeIf(d -> d.getPlato().equals(plato));
    }

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

    public double calcularTotalSinDescuento() {
        double total = 0;
        for (DetallePedido d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    public int obtenerCantidadItems() {
        return detalles.size();
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("Estado invalido.");
        }
        this.estado = nuevoEstado;
    }

    public boolean esModificable() {
        return estado == EstadoPedido.PENDIENTE;
    }

    public boolean esCancelable() {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.PREPARANDO;
    }

    public boolean estaPagado() {
        return estado == EstadoPedido.PAGADO;
    }

    public void validarTransicionEstado(EstadoPedido nuevoEstado) {
        if (estado == EstadoPedido.CANCELADO || estado == EstadoPedido.PAGADO) {
            throw new IllegalStateException("No se puede cambiar el estado.");
        }
    }

    @Override
    public String toString() {
        return "Pedido #" + numeroPedido +
                ", Estado: " + estado +
                ", Total: " + calcularTotalSinDescuento();
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pedido)) return false;

        Pedido other = (Pedido) obj;
        return Objects.equals(numeroPedido, other.numeroPedido);
    }
}
