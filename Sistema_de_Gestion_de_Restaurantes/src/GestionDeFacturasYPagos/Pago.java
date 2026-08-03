package GestionDeFacturasYPagos;

import java.io.Serializable;
import java.util.Objects;

public class Pago implements Serializable {

    private double efectivo;
    private double tarjeta;
    private double transferencia;
    private double totalPagado;

    // Constructor por defecto
    public Pago() {
    }

    // Obtiene el monto total abonado en efectivo
    public double getEfectivo() {
        return efectivo;
    }

    // Obtiene el monto total abonado con tarjeta
    public double getTarjeta() {
        return tarjeta;
    }

    // Obtiene el monto total abonado por transferencia bancaria
    public double getTransferencia() {
        return transferencia;
    }

    // Obtiene el total general acumulado de los pagos
    public double getTotalPagado() {
        return totalPagado;
    }

    // Registra un abono en efectivo actualizando el total pagado
    public void registrarPagoEfectivo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.efectivo += monto;
        calcularTotalPagado();
    }

    // Registra un abono con tarjeta actualizando el total pagado
    public void registrarPagoTarjeta(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.tarjeta += monto;
        calcularTotalPagado();
    }

    // Registra un abono por transferencia actualizando el total pagado
    public void registrarPagoTransferencia(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.transferencia += monto;
        calcularTotalPagado();
    }

    // Suma y calcula el total general pagado
    public double calcularTotalPagado() {
        this.totalPagado = efectivo + tarjeta + transferencia;
        return totalPagado;
    }

    // Comprueba si el total abonado cubre o supera el importe de la factura
    public boolean esPagoCompleto(double totalFactura) {
        return totalPagado >= totalFactura;
    }

    // Devuelve la representación en cadena de texto del pago
    @Override
    public String toString() {
        return "Efectivo: " + efectivo +
                ", Tarjeta: " + tarjeta +
                ", Transferencia: " + transferencia +
                ", Total Pagado: " + totalPagado;
    }

    // Genera el código hash según los importes abonados
    @Override
    public int hashCode() {
        return Objects.hash(efectivo, tarjeta, transferencia);
    }

    // Compara la igualdad de importes abonados entre objetos de pago
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pago)) return false;
        Pago other = (Pago) obj;
        return Double.compare(efectivo, other.efectivo) == 0
                && Double.compare(tarjeta, other.tarjeta) == 0
                && Double.compare(transferencia, other.transferencia) == 0;
    }
}