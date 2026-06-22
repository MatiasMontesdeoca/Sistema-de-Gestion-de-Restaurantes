package GestionDeFacturasYPagos;

import GestionDePedidos.Pedido;
import java.time.LocalDateTime;
import java.util.Objects;

public class Factura {

    // Atributo: identificador único de la factura
    private String numeroFactura;

    // Fecha y hora en la que se crea la factura
    private LocalDateTime fecha;

    // Pedido asociado a la factura
    private Pedido pedido;

    // Suma total de los productos antes de descuentos
    private double subtotal;

    // Monto total de descuento aplicado (en dinero)
    private double descuentoAplicado;

    // Total final a pagar después del descuento
    private double total;

    // Objeto que gestiona el pago de la factura
    private Pago pago;

    // Constructor: inicializa fecha actual y crea un objeto Pago vacío
    public Factura() {
        this.fecha = LocalDateTime.now();
        this.pago = new Pago();
    }

    // Obtiene el número de factura
    public String getNumeroFactura() {
        return numeroFactura;
    }

    // Establece el número de factura con validación
    public void setNumeroFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("Numero de factura invalido.");
        }
        this.numeroFactura = numeroFactura.trim();
    }

    // Obtiene la fecha de creación de la factura
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Obtiene el pedido asociado a la factura
    public Pedido getPedido() {
        return pedido;
    }

    // Asigna un pedido a la factura con validación
    public void setPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        this.pedido = pedido;
    }

    // Obtiene el subtotal calculado
    public double getSubtotal() {
        return subtotal;
    }

    // Obtiene el descuento aplicado en dinero
    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    // Obtiene el total final de la factura
    public double getTotal() {
        return total;
    }

    // Obtiene el objeto de pago asociado
    public Pago getPago() {
        return pago;
    }

    // Calcula el subtotal usando el total del pedido sin descuentos
    public void calcularSubtotal() {
        if (pedido == null) {
            throw new IllegalStateException("No hay pedido asociado.");
        }
        this.subtotal = pedido.calcularTotalSinDescuento();
    }

    // Aplica un descuento en dinero (no porcentaje)
    public void aplicarDescuento(double descuentoDinero) {

        // Validación: el descuento no puede ser negativo ni mayor al subtotal
        if (descuentoDinero < 0 || descuentoDinero > subtotal) {
            throw new IllegalArgumentException("Descuento invalido.");
        }

        this.descuentoAplicado = descuentoDinero;
    }

    // Calcula el total final después de aplicar el descuento
    public void calcularTotalFinal() {
        this.total = subtotal - descuentoAplicado;
    }

    // Método que ejecuta todo el proceso de cierre de factura
    public void cerrarFactura(double descuentoDinero) {
        calcularSubtotal();          // obtiene subtotal desde el pedido
        aplicarDescuento(descuentoDinero); // aplica descuento
        calcularTotalFinal();       // calcula total final
    }

    // Verifica si la factura ya está completamente pagada
    public boolean estaPagada() {
        return pago != null && pago.esPagoCompleto(total);
    }

    // Representación en texto de la factura
    @Override
    public String toString() {
        return "Factura #" + numeroFactura +
                ", Subtotal: " + subtotal +
                ", Descuento: " + descuentoAplicado +
                ", Total: " + total +
                ", Pagada: " + estaPagada();
    }

    // Hash basado en el número de factura
    @Override
    public int hashCode() {
        return Objects.hash(numeroFactura);
    }

    // Dos facturas son iguales si tienen el mismo número de factura
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Factura)) return false;

        Factura other = (Factura) obj;
        return Objects.equals(numeroFactura, other.numeroFactura);
    }
}