package GestionDeReportes;
import GestionDeFacturasYPagos.Factura;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class ReporteVentas {
    
    private LocalDateTime fechaGeneracion;

    public ReporteVentas() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public double calcularTotalVentas(ArrayList<Factura> facturas) {
        if (facturas == null) {
            throw new IllegalArgumentException("La lista de facturas no puede ser nula.");
        }

        double total = 0;
        for (Factura f : facturas) {
            if (f != null) {
                total += f.getTotal();
            }
        }
        return total;
    }

    public int contarVentas(ArrayList<Factura> facturas) {
        if (facturas == null) {
            throw new IllegalArgumentException("La lista de facturas no puede ser nula.");
        }

        int count = 0;
        for (Factura f : facturas) {
            if (f != null) {
                count++;
            }
        }
        return count;
    }

    public String generarResumenVentas(ArrayList<Factura> facturas) {
        return "Total ventas: " + calcularTotalVentas(facturas) +
               ", Cantidad ventas: " + contarVentas(facturas) +
               ", Fecha: " + fechaGeneracion;
    }
}
