package GestionDeFacturasYPagos;

import GestionDePedidos.Pedido;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Factura implements Serializable {

    private String numeroFactura;
    private LocalDateTime fecha;
    private Pedido pedido;
    private double subtotal;
    private double descuentoAplicado;
    private double total;
    private Pago pago;

    // Constructor que asigna la fecha actual e inicializa un objeto de Pago
    public Factura() {
        this.fecha = LocalDateTime.now();
        this.pago = new Pago();
    }

    // Obtiene el número de factura
    public String getNumeroFactura() {
        return numeroFactura;
    }

    // Establece y valida el número identificador de la factura
    public void setNumeroFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("Numero de factura invalido.");
        }
        this.numeroFactura = numeroFactura.trim();
    }

    // Obtiene la fecha y hora de emisión de la factura
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Obtiene el pedido asociado a la factura
    public Pedido getPedido() {
        return pedido;
    }

    // Asigna un pedido a la factura validando que no sea nulo
    public void setPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        this.pedido = pedido;
    }

    // Obtiene el subtotal acumulado antes de aplicar descuentos
    public double getSubtotal() {
        return subtotal;
    }

    // Obtiene el monto total descontado
    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    // Obtiene el importe total a pagar
    public double getTotal() {
        return total;
    }

    // Obtiene el objeto de pago de la factura
    public Pago getPago() {
        return pago;
    }

    // Calcula el subtotal basándose en el pedido asociado
    public void calcularSubtotal() {
        if (pedido == null) {
            throw new IllegalStateException("No hay pedido asociado.");
        }
        this.subtotal = pedido.calcularTotalSinDescuento();
    }

    // Aplica el valor del descuento comprobando que no supere el subtotal
    public void aplicarDescuento(double descuentoDinero) {
        if (descuentoDinero < 0 || descuentoDinero > subtotal) {
            throw new IllegalArgumentException("Descuento invalido.");
        }
        this.descuentoAplicado = descuentoDinero;
    }

    // Calcula el importe total restando el descuento del subtotal
    public void calcularTotalFinal() {
        this.total = subtotal - descuentoAplicado;
    }

    // Ejecuta el cierre de la factura calculando subtotal, descuento y total
    public void cerrarFactura(double descuentoDinero) {
        calcularSubtotal();
        aplicarDescuento(descuentoDinero);
        calcularTotalFinal();
    }

    // Comprueba si la factura se encuentra pagada en su totalidad
    public boolean estaPagada() {
        return pago != null && pago.esPagoCompleto(total);
    }

    // Devuelve la representación en cadena de texto de la factura
    @Override
    public String toString() {
        return "Factura #" + numeroFactura +
                ", Subtotal: " + subtotal +
                ", Descuento: " + descuentoAplicado +
                ", Total: " + total +
                ", Pagada: " + estaPagada();
    }

    // Genera el código hash según el número de la factura
    @Override
    public int hashCode() {
        return Objects.hash(numeroFactura);
    }

    // Compara igualdad con otra factura mediante el número identificador
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Factura)) return false;
        Factura other = (Factura) obj;
        return Objects.equals(numeroFactura, other.numeroFactura);
    }
}