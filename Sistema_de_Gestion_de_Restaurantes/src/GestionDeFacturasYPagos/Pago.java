package GestionDeFacturasYPagos;
import java.util.Objects;

public class Pago {
    //Atributos
    private double efectivo;
    private double tarjeta;
    private double transferencia;
    private double totalPagado;

    //Constructor
    public Pago() {
    }

    //Get de efectivo
    public double getEfectivo() {
        return efectivo;
    }
    
    //Get de tarjeta
    public double getTarjeta() {
        return tarjeta;
    }
    
    //Get de transferencia
    public double getTransferencia() {
        return transferencia;
    }

    //Get de totalPagado 
    public double getTotalPagado() {
        return totalPagado;
    }

    //Método para registrar el pago con efectivo
    public void registrarPagoEfectivo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.efectivo += monto;
        calcularTotalPagado();
    }
    
    //Método para registrar el pago con tarjeta
    public void registrarPagoTarjeta(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.tarjeta += monto;
        calcularTotalPagado();
    }

    //Método para registrar el pago con transferencia
    public void registrarPagoTransferencia(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        this.transferencia += monto;
        calcularTotalPagado();
    }

    //Método para calcular el total pagado
    public double calcularTotalPagado() {
        this.totalPagado = efectivo + tarjeta + transferencia;
        return totalPagado;
    }

    //Método para verificar si el pago está completo
    public boolean esPagoCompleto(double totalFactura) {
        return totalPagado >= totalFactura;
    }

    //Overrides
    @Override
    public String toString() {
        return "Efectivo: " + efectivo +
                ", Tarjeta: " + tarjeta +
                ", Transferencia: " + transferencia +
                ", Total Pagado: " + totalPagado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(efectivo, tarjeta, transferencia);
    }

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
