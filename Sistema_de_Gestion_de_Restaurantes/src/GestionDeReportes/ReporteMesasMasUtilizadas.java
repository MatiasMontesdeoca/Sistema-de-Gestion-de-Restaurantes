package GestionDeReportes;
import GestionDePedidos.Pedido;
import GestionDeMesasYReservas.Mesa;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class ReporteMesasMasUtilizadas {
    
    private LocalDateTime fechaGeneracion;

    public ReporteMesasMasUtilizadas() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

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

    public void incrementarUsoMesa(HashMap<Mesa, Integer> mapa, Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("Mesa invalida.");
        }

        mapa.put(mesa, mapa.getOrDefault(mesa, 0) + 1);
    }

    public String ordenarMesas(HashMap<Mesa, Integer> mapa) {
        return mapa.toString();
    }

public String generarReporte(ArrayList<Pedido> pedidos) {

    HashMap<Mesa, Integer> mapa = calcularUsoMesas(pedidos);

    StringBuilder sb = new StringBuilder();

    sb.append("Uso de mesas:\n");

    for (Map.Entry<Mesa, Integer> entry : mapa.entrySet()) {

        Mesa m = entry.getKey();
        Integer veces = entry.getValue();

        sb.append(m)
          .append(", Numero de veces ocupada: ")
          .append(veces)
          .append("\n");
    }

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    sb.append("Fecha: ")
      .append(fechaGeneracion.format(fmt));

    return sb.toString();}
}
