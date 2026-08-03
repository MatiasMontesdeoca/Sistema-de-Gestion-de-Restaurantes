package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReporteVentas implements Serializable {

    private LocalDateTime fecha;

    // Constructor que asigna la fecha y hora del sistema al instanciar el reporte
    public ReporteVentas() {
        this.fecha = LocalDateTime.now();
    }

    // Obtiene la fecha y hora de emisión del reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // Suma e importe total recaudado de todas las facturas procesadas
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

    // Cuenta el número total de facturas de venta procesadas
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

    // Devuelve un resumen en cadena con el total facturado, cantidad de ventas y fecha
    public String generarResumenVentas(ArrayList<Factura> facturas) {
        return "Total ventas: " + calcularTotalVentas(facturas) +
               ", Cantidad ventas: " + contarVentas(facturas) +
               ", Fecha: " + fecha;
    }
}
