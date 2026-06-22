package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDeMesasYReservas.Mesa;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReporteConsumoPorMesa {
    //Atributos
    private LocalDateTime fecha;
    
    //Constructor
    public ReporteConsumoPorMesa() {
        this.fecha = LocalDateTime.now();
    }

    //Get de la fecha
    public LocalDateTime getFecha() {
        return fecha;
    }

    //Métodos
    public HashMap<Mesa, Double> calcularConsumoPorMesa(ArrayList<Factura> facturas) {
        if (facturas == null) {
            throw new IllegalArgumentException("Lista invalida.");
        }

        HashMap<Mesa, Double> mapa = new HashMap<>();

        for (Factura f : facturas) {
            if (f == null || f.getPedido() == null || f.getPedido().getMesa() == null) continue;

            Mesa m = f.getPedido().getMesa();
            mapa.put(m, mapa.getOrDefault(m, 0.0) + f.getTotal());
        }

        return mapa;
    }

    public HashMap<Mesa, Double> calcularPromedio(HashMap<Mesa, Double> consumo, HashMap<Mesa, Integer> usos) {
        HashMap<Mesa, Double> promedio = new HashMap<>();

        for (Mesa m : consumo.keySet()) {
            double total = consumo.getOrDefault(m, 0.0);
            int count = usos.getOrDefault(m, 1);

            promedio.put(m, total / count);
        }

        return promedio;
    }
    
    public String generarReporte(ArrayList<Factura> facturas) {

    HashMap<Mesa, Double> consumo = calcularConsumoPorMesa(facturas);

    StringBuilder sb = new StringBuilder();

    sb.append("Consumo por mesa:\n");

    for (Map.Entry<Mesa, Double> entry : consumo.entrySet()) {

        Mesa m = entry.getKey();
        Double total = entry.getValue();

        sb.append(m)
          .append(", Consumo total de la mesa: ")
          .append(String.format("%.2f", total))
          .append("\n");
    }

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    sb.append("Fecha: ")
      .append(fecha.format(fmt));

    return sb.toString();}

}
