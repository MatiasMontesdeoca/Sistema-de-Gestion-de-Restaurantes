package GestionDeReportes;

import GestionDePedidos.Pedido;
import GestionDeMesasYReservas.Mesa;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReporteMesasMasUtilizadas implements Serializable {

    private LocalDateTime fecha;

    // Constructor que asigna la fecha y hora del sistema al momento de instanciar el reporte
    public ReporteMesasMasUtilizadas() {
        this.fecha = LocalDateTime.now();
    }

    // Obtiene la fecha en la que se generó el reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // Cuenta cuántas veces ha sido utilizada cada mesa analizando la lista de pedidos
    public HashMap<Mesa, Integer> calcularUsoMesas(ArrayList<Pedido> pedidos) {
        if (pedidos == null) {
            throw new IllegalArgumentException("Lista invalida.");
        }
        HashMap<Mesa, Integer> mapa = new HashMap<>();
        for (Pedido p : pedidos) {
            if (p == null || p.getMesa() == null) continue;
            Mesa m = p.getMesa();
            mapa.put(m, mapa.getOrDefault(m, 0) + 1);
        }
        return mapa;
    }

    // Incrementa manualmente el contador de ocupaciones de una mesa específica
    public void incrementarUsoMesa(HashMap<Mesa, Integer> mapa, Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("Mesa invalida.");
        }
        mapa.put(mesa, mapa.getOrDefault(mesa, 0) + 1);
    }

    // Retorna la representación textual del mapa de frecuencias de uso de mesas
    public String ordenarMesas(HashMap<Mesa, Integer> mapa) {
        return mapa.toString();
    }

    // Genera el informe textual formateado con el conteo de uso de cada mesa y la fecha
    public String generarReporte(ArrayList<Pedido> pedidos) {
        HashMap<Mesa, Integer> mapa = calcularUsoMesas(pedidos);
        StringBuilder sb = new StringBuilder();
        sb.append("Uso de mesas:\n");
        for (Map.Entry<Mesa, Integer> entry : mapa.entrySet()) {
            Mesa m = entry.getKey();
            Integer veces = entry.getValue();
            sb.append(m).append(", Numero de veces ocupada: ").append(veces).append("\n");
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sb.append("Fecha: ").append(fecha.format(fmt));
        return sb.toString();
    }
}