package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReporteVentas implements Serializable{

    // Fecha en la que se genera el reporte de ventas
    private LocalDateTime fecha;

    // Constructor: al crear el objeto se guarda la fecha actual
    public ReporteVentas() {
        this.fecha = LocalDateTime.now();
    }

    // Devuelve la fecha en la que se generó el reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // Calcula el total de dinero generado por todas las facturas
    public double calcularTotalVentas(ArrayList<Factura> facturas) {

        // Validación: la lista no puede ser nula
        if (facturas == null) {
            throw new IllegalArgumentException("La lista de facturas no puede ser nula.");
        }

        double total = 0;

        // Suma el total de cada factura
        for (Factura f : facturas) {
            if (f != null) {
                total += f.getTotal();
            }
        }

        return total;
    }

    // Cuenta cuántas ventas (facturas válidas) existen en la lista
    public int contarVentas(ArrayList<Factura> facturas) {

        // Validación de entrada
        if (facturas == null) {
            throw new IllegalArgumentException("La lista de facturas no puede ser nula.");
        }

        int count = 0;

        // Incrementa el contador por cada factura no nula
        for (Factura f : facturas) {
            if (f != null) {
                count++;
            }
        }

        return count;
    }

    // Genera un resumen general de ventas
    // Incluye total de dinero, cantidad de ventas y fecha del reporte
    public String generarResumenVentas(ArrayList<Factura> facturas) {

        return "Total ventas: " + calcularTotalVentas(facturas) +
               ", Cantidad ventas: " + contarVentas(facturas) +
               ", Fecha: " + fecha;
    }
}
