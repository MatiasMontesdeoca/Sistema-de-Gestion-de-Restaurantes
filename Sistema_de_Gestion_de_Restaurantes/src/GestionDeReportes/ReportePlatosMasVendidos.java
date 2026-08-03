package GestionDeReportes;

import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.Plato;
import GestionDelMenu.CategoriaPlato;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class ReportePlatosMasVendidos implements Serializable {

    private LocalDateTime fecha;

    // Constructor que asigna la fecha y hora actual de emisión del reporte
    public ReportePlatosMasVendidos() {
        this.fecha = LocalDateTime.now();
    }

    // Obtiene la fecha y hora en que fue generado el reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // Acumula la cantidad total de unidades vendidas por cada plato en la lista de pedidos
    public HashMap<Plato, Integer> calcularPlatosVendidos(List<Pedido> pedidos) {
        HashMap<Plato, Integer> mapa = new HashMap<>();
        if (pedidos == null) {
            throw new IllegalArgumentException("Lista de pedidos invalida.");
        }
        for (Pedido p : pedidos) {
            if (p == null || p.getDetalles() == null) continue;
            for (DetallePedido d : p.getDetalles()) {
                if (d == null || d.getPlato() == null) continue;
                Plato plato = d.getPlato();
                mapa.put(plato, mapa.getOrDefault(plato, 0) + d.getCantidad());
            }
        }
        return mapa;
    }

    // Ordena en forma descendente los platos según sus unidades vendidas
    public ArrayList<Map.Entry<Plato, Integer>> obtenerRanking(HashMap<Plato, Integer> mapa) {
        ArrayList<Map.Entry<Plato, Integer>> lista = new ArrayList<>(mapa.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());
        return lista;
    }

    // Devuelve los primeros N platos con mayor número de ventas
    public ArrayList<Map.Entry<Plato, Integer>> obtenerTopPlatos(HashMap<Plato, Integer> mapa, int top) {
        if (top <= 0) {
            throw new IllegalArgumentException("Top invalido.");
        }
        ArrayList<Map.Entry<Plato, Integer>> ranking = obtenerRanking(mapa);
        if (top > ranking.size()) {
            top = ranking.size();
        }
        return new ArrayList<>(ranking.subList(0, top));
    }

    // Genera un informe resumido con los N platos más vendidos del restaurante
    public String generarReporte(List<Pedido> pedidos, int top) {
        HashMap<Plato, Integer> mapa = calcularPlatosVendidos(pedidos);
        return "Top platos: " + obtenerTopPlatos(mapa, top) + "\nFecha: " + fecha;
    }

    // Genera el informe del top N de platos más vendidos filtrando por una categoría del menú
    public String generarReportePorCategoria(List<Pedido> pedidos, CategoriaPlato categoria, int top) {
        Map<Plato, Integer> contador = new HashMap<>();
        for (Pedido p : pedidos) {
            if (p == null || p.getDetalles() == null) continue;
            for (DetallePedido d : p.getDetalles()) {
                if (d == null || d.getPlato() == null) continue;
                Plato plato = d.getPlato();
                if (plato.getCategoria() != categoria) {
                    continue;
                }
                contador.put(plato, contador.getOrDefault(plato, 0) + d.getCantidad());
            }
        }
        List<Map.Entry<Plato, Integer>> lista = new ArrayList<>(contador.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== TOP ").append(top).append(" PLATOS MAS VENDIDOS (").append(categoria).append(") ===\n");
        int i = 0;
        for (Map.Entry<Plato, Integer> e : lista) {
            if (i >= top) break;
            sb.append(e.getKey().getNombre()).append(" - Vendidos: ").append(e.getValue()).append("\n");
            i++;
        }
        if (i == 0) {
            sb.append("No hay ventas en esta categoria.\n");
        }
        return sb.toString();
    }
}