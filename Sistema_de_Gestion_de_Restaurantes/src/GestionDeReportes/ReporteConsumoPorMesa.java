package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDeMesasYReservas.Mesa;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReporteConsumoPorMesa implements Serializable{

    // Fecha en la que se genera el reporte
    private LocalDateTime fecha;

    // Constructor: inicializa la fecha con el momento actual
    public ReporteConsumoPorMesa() {
        this.fecha = LocalDateTime.now();
    }

    // Permite obtener la fecha del reporte generado
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Calcula el consumo total acumulado por cada mesa
    public HashMap<Mesa, Double> calcularConsumoPorMesa(ArrayList<Factura> facturas) {

        // Validación básica de entrada
        if (facturas == null) {
            throw new IllegalArgumentException("Lista invalida.");
        }

        // Mapa donde se acumula el total por mesa
        HashMap<Mesa, Double> mapa = new HashMap<>();

        // Recorre todas las facturas para acumular consumos por mesa
        for (Factura f : facturas) {

            // Se omiten facturas o referencias inválidas
            if (f == null || f.getPedido() == null || f.getPedido().getMesa() == null) continue;

            Mesa m = f.getPedido().getMesa();

            // Suma incremental del total de cada factura a la mesa correspondiente
            mapa.put(m, mapa.getOrDefault(m, 0.0) + f.getTotal());
        }

        return mapa;
    }

    // Calcula el promedio de consumo por mesa en base a consumo total y número de usos
    public HashMap<Mesa, Double> calcularPromedio(HashMap<Mesa, Double> consumo,
                                                   HashMap<Mesa, Integer> usos) {

        HashMap<Mesa, Double> promedio = new HashMap<>();

        // Recorre cada mesa del mapa de consumo
        for (Mesa m : consumo.keySet()) {

            double total = consumo.getOrDefault(m, 0.0);

            // Si no hay registros de uso, se evita división por cero usando 1
            int count = usos.getOrDefault(m, 1);

            // Promedio = consumo total / número de usos
            promedio.put(m, total / count);
        }

        return promedio;
    }

    // Genera el reporte en formato de texto para mostrarlo en consola
    public String generarReporte(ArrayList<Factura> facturas) {

        // Calcula consumo total por mesa
        HashMap<Mesa, Double> consumo = calcularConsumoPorMesa(facturas);

        StringBuilder sb = new StringBuilder();

        // Encabezado del reporte
        sb.append("Consumo por mesa:\n");

        // Recorre el mapa para construir el reporte línea por línea
        for (Map.Entry<Mesa, Double> entry : consumo.entrySet()) {

            Mesa m = entry.getKey();       // Mesa asociada
            Double total = entry.getValue(); // Consumo total de esa mesa

            sb.append(m)
              .append(", Consumo total de la mesa: ")
              .append(String.format("%.2f", total))
              .append("\n");
        }

        // Formato de fecha del reporte
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Se agrega la fecha de generación del reporte
        sb.append("Fecha: ")
          .append(fecha.format(fmt));

        return sb.toString();
    }
}