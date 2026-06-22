package GestionDeReportes;

import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.Plato;
import GestionDelMenu.CategoriaPlato;
import java.time.LocalDateTime;
import java.util.*;

public class ReportePlatosMasVendidos {
    //Atributos
    private LocalDateTime fecha;

    // Constructor
    public ReportePlatosMasVendidos() {
        this.fecha = LocalDateTime.now();
    }
    
    //Get de fecha
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    //Métodos
    // =========================
    // CALCULO GENERAL
    // =========================
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

                mapa.put(plato,
                        mapa.getOrDefault(plato, 0) + d.getCantidad());
            }
        }

        return mapa;
    }

    // =========================
    // RANKING
    // =========================
    public ArrayList<Map.Entry<Plato, Integer>> obtenerRanking(HashMap<Plato, Integer> mapa) {

        ArrayList<Map.Entry<Plato, Integer>> lista =
                new ArrayList<>(mapa.entrySet());

        lista.sort((a, b) -> b.getValue() - a.getValue());

        return lista;
    }

    public ArrayList<Map.Entry<Plato, Integer>> obtenerTopPlatos(
            HashMap<Plato, Integer> mapa, int top) {

        if (top <= 0) {
            throw new IllegalArgumentException("Top invalido.");
        }

        ArrayList<Map.Entry<Plato, Integer>> ranking = obtenerRanking(mapa);

        if (top > ranking.size()) {
            top = ranking.size();
        }

        return new ArrayList<>(ranking.subList(0, top));
    }

    // =========================
    // REPORTE GENERAL
    // =========================
    public String generarReporte(List<Pedido> pedidos, int top) {

        HashMap<Plato, Integer> mapa = calcularPlatosVendidos(pedidos);

        return "Top platos: " + obtenerTopPlatos(mapa, top)
                + "\nFecha: " + fecha;
    }

    // =========================
    // REPORTE POR CATEGORIA (ENUM CORRECTO)
    // =========================
    public String generarReportePorCategoria(
            List<Pedido> pedidos,
            CategoriaPlato categoria,
            int top) {

        Map<Plato, Integer> contador = new HashMap<>();

        for (Pedido p : pedidos) {

            if (p == null || p.getDetalles() == null) continue;

            for (DetallePedido d : p.getDetalles()) {

                if (d == null || d.getPlato() == null) continue;

                Plato plato = d.getPlato();

                // 🔥 FILTRO CORRECTO CON ENUM
                if (plato.getCategoria() != categoria) {
                    continue;
                }

                contador.put(plato,
                        contador.getOrDefault(plato, 0) + d.getCantidad());
            }
        }

        List<Map.Entry<Plato, Integer>> lista =
                new ArrayList<>(contador.entrySet());

        lista.sort((a, b) -> b.getValue() - a.getValue());

        StringBuilder sb = new StringBuilder();

        sb.append("\n=== TOP ").append(top)
                .append(" PLATOS MAS VENDIDOS (")
                .append(categoria)
                .append(") ===\n");

        int i = 0;

        for (Map.Entry<Plato, Integer> e : lista) {

            if (i >= top) break;

            sb.append(e.getKey().getNombre())
                    .append(" - Vendidos: ")
                    .append(e.getValue())
                    .append("\n");

            i++;
        }

        if (i == 0) {
            sb.append("No hay ventas en esta categoria.\n");
        }

        return sb.toString();
    }
}