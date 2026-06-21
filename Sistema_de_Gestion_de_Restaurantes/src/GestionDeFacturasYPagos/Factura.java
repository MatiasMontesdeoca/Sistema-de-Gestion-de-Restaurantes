package GestionDeFacturasYPagos;

import GestionDePedidos.Pedido;
import java.time.LocalDateTime;
import java.util.Objects;

public class Factura {
    //Atributos
    private String numeroFactura;
    private LocalDateTime fecha;
    private Pedido pedido;
    private double subtotal;
    private double descuentoAplicado;
    private double total;
    private Pago pago;

    //Constructor
    public Factura() {
        this.fecha = LocalDateTime.now();
        this.pago = new Pago();
    }

    //Get y Set de numeroFactura
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("Numero de factura invalido.");
        }
        this.numeroFactura = numeroFactura.trim();
    }

    //Get de fecha
    public LocalDateTime getFecha() {
        return fecha;
    }

    //Get y Set de pedido
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        this.pedido = pedido;
    }

    //Get de subtotal
    public double getSubtotal() {
        return subtotal;
    }

    //Get de descuentoAplicado
    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    //Get de total
    public double getTotal() {
        return total;
    }

    //Get de pago
    public Pago getPago() {
        return pago;
    }

    // 🔥 SOLO CALCULA DESDE PEDIDO
    public void calcularSubtotal() {
        if (pedido == null) {
            throw new IllegalStateException("No hay pedido asociado.");
        }
        this.subtotal = pedido.calcularTotalSinDescuento();
    }

    // 🔥 DESCUENTO EN DINERO (NO PORCENTAJE)
    public void aplicarDescuento(double descuentoDinero) {

        if (descuentoDinero < 0 || descuentoDinero > subtotal) {
            throw new IllegalArgumentException("Descuento invalido.");
        }

        this.descuentoAplicado = descuentoDinero;
    }

    public void calcularTotalFinal() {
        this.total = subtotal - descuentoAplicado;
    }

    // 🔥 METODO ÚNICO PARA CERRAR FACTURA
    public void cerrarFactura(double descuentoDinero) {
        calcularSubtotal();
        aplicarDescuento(descuentoDinero);
        calcularTotalFinal();
    }

    public boolean estaPagada() {
        return pago != null && pago.esPagoCompleto(total);
    }

    @Override
    public String toString() {
        return "Factura #" + numeroFactura +
                ", Subtotal: " + subtotal +
                ", Descuento: " + descuentoAplicado +
                ", Total: " + total +
                ", Pagada: " + estaPagada();
    }

    //Overrides
    @Override
    public int hashCode() {
        return Objects.hash(numeroFactura);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Factura)) return false;

        Factura other = (Factura) obj;
        return Objects.equals(numeroFactura, other.numeroFactura);
    }
}