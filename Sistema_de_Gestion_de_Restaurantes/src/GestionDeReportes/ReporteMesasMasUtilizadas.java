package GestionDeReportes;

import GestionDePedidos.Pedido;
import GestionDeMesasYReservas.Mesa;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReporteMesasMasUtilizadas {

    // Fecha en la que se genera el reporte
    private LocalDateTime fecha;

    // Constructor: inicializa la fecha actual del sistema
    public ReporteMesasMasUtilizadas() {
        this.fecha = LocalDateTime.now();
    }

    // Devuelve la fecha de generación del reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // CÁLCULO DE USO DE MESAS
    // Cuenta cuántas veces ha sido utilizada cada mesa según los pedidos
    public HashMap<Mesa, Integer> calcularUsoMesas(ArrayList<Pedido> pedidos) {

        // Validación de entrada
        if (pedidos == null) {
            throw new IllegalArgumentException("Lista invalida.");
        }

        // Mapa donde: Mesa -> cantidad de veces usada
        HashMap<Mesa, Integer> mapa = new HashMap<>();

        // Recorre todos los pedidos para contar usos por mesa
        for (Pedido p : pedidos) {

            // Ignora pedidos o mesas nulas
            if (p == null || p.getMesa() == null) continue;

            Mesa m = p.getMesa();

            // Incrementa el contador de uso de la mesa
            mapa.put(m, mapa.getOrDefault(m, 0) + 1);
        }

        return mapa;
    }

    // Incrementa manualmente el uso de una mesa en el mapa
    public void incrementarUsoMesa(HashMap<Mesa, Integer> mapa, Mesa mesa) {

        // Validación de la mesa
        if (mesa == null) {
            throw new IllegalArgumentException("Mesa invalida.");
        }

        // Suma 1 al contador existente o lo inicializa en 1
        mapa.put(mesa, mapa.getOrDefault(mesa, 0) + 1);
    }

    // (Actualmente no ordena realmente, solo devuelve el mapa en texto)
    public String ordenarMesas(HashMap<Mesa, Integer> mapa) {
        return mapa.toString();
    }

    // GENERACIÓN DEL REPORTE FINAL
    public String generarReporte(ArrayList<Pedido> pedidos) {

        // Calcula uso de mesas a partir de los pedidos
        HashMap<Mesa, Integer> mapa = calcularUsoMesas(pedidos);

        StringBuilder sb = new StringBuilder();

        // Encabezado del reporte
        sb.append("Uso de mesas:\n");

        // Recorre cada entrada (mesa -> cantidad de usos)
        for (Map.Entry<Mesa, Integer> entry : mapa.entrySet()) {

            Mesa m = entry.getKey();        // Mesa
            Integer veces = entry.getValue(); // Número de usos

            sb.append(m)
              .append(", Numero de veces ocupada: ")
              .append(veces)
              .append("\n");
        }

        // Formato de fecha del reporte
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Agrega la fecha de generación al final
        sb.append("Fecha: ")
          .append(fecha.format(fmt));

        return sb.toString();
    }
}