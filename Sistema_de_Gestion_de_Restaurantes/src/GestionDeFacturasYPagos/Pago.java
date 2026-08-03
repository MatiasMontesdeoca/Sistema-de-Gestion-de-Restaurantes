package GestionDeFacturasYPagos;

import java.io.Serializable;
import java.util.Objects;

public class Pago implements Serializable{

    // Atributo: dinero pagado en efectivo
    private double efectivo;

    // Atributo: dinero pagado con tarjeta
    private double tarjeta;

    // Atributo: dinero pagado por transferencia bancaria
    private double transferencia;

    // Atributo: total acumulado de todos los métodos de pago
    private double totalPagado;

    // Constructor vacío
    public Pago() {
    }

    // Obtiene el monto pagado en efectivo
    public double getEfectivo() {
        return efectivo;
    }

    // Obtiene el monto pagado con tarjeta
    public double getTarjeta() {
        return tarjeta;
    }

    // Obtiene el monto pagado por transferencia
    public double getTransferencia() {
        return transferencia;
    }

    // Obtiene el total acumulado de todos los pagos
    public double getTotalPagado() {
        return totalPagado;
    }

    // Registra un pago en efectivo
    public void registrarPagoEfectivo(double monto) {

        // Validación para evitar montos negativos
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }

        // Suma el monto al efectivo
        this.efectivo += monto;

        // Actualiza el total pagado
        calcularTotalPagado();
    }

    // Registra un pago con tarjeta
    public void registrarPagoTarjeta(double monto) {

        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }

        this.tarjeta += monto;

        calcularTotalPagado();
    }

    // Registra un pago por transferencia
    public void registrarPagoTransferencia(double monto) {

        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }

        this.transferencia += monto;

        calcularTotalPagado();
    }

    // Calcula el total general pagado sumando todos los métodos
    public double calcularTotalPagado() {
        this.totalPagado = efectivo + tarjeta + transferencia;
        return totalPagado;
    }

    // Verifica si el pago cubre o supera el total de la factura
    public boolean esPagoCompleto(double totalFactura) {
        return totalPagado >= totalFactura;
    }

    // Representación en texto del objeto Pago
    @Override
    public String toString() {
        return "Efectivo: " + efectivo +
                ", Tarjeta: " + tarjeta +
                ", Transferencia: " + transferencia +
                ", Total Pagado: " + totalPagado;
    }

    // Hash basado en los métodos de pago
    @Override
    public int hashCode() {
        return Objects.hash(efectivo, tarjeta, transferencia);
    }

    // Dos pagos son iguales si coinciden los montos de cada método
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