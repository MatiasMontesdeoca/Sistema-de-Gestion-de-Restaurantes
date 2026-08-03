package GestionDeReportes;

import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.Plato;
import GestionDelMenu.CategoriaPlato;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class ReportePlatosMasVendidos implements Serializable{

    // Fecha en la que se genera el reporte
    private LocalDateTime fecha;

    // Constructor: inicializa la fecha actual
    public ReportePlatosMasVendidos() {
        this.fecha = LocalDateTime.now();
    }

    // Devuelve la fecha de generación del reporte
    public LocalDateTime getFechaGeneracion() {
        return fecha;
    }

    // CÁLCULO GENERAL DE VENTAS
    // Calcula cuántas unidades se han vendido de cada plato
    public HashMap<Plato, Integer> calcularPlatosVendidos(List<Pedido> pedidos) {

        // Mapa: Plato -> cantidad vendida
        HashMap<Plato, Integer> mapa = new HashMap<>();

        // Validación de entrada
        if (pedidos == null) {
            throw new IllegalArgumentException("Lista de pedidos invalida.");
        }

        // Recorre todos los pedidos
        for (Pedido p : pedidos) {

            // Ignora pedidos nulos o sin detalles
            if (p == null || p.getDetalles() == null) continue;

            // Recorre los detalles de cada pedido
            for (DetallePedido d : p.getDetalles()) {

                // Ignora datos incompletos
                if (d == null || d.getPlato() == null) continue;

                Plato plato = d.getPlato();

                // Acumula la cantidad vendida del plato
                mapa.put(plato,
                        mapa.getOrDefault(plato, 0) + d.getCantidad());
            }
        }

        return mapa;
    }

    // ORDENAMIENTO / RANKING
    // Ordena los platos de mayor a menor ventas
    public ArrayList<Map.Entry<Plato, Integer>> obtenerRanking(HashMap<Plato, Integer> mapa) {

        // Convierte el mapa a lista para poder ordenarlo
        ArrayList<Map.Entry<Plato, Integer>> lista =
                new ArrayList<>(mapa.entrySet());

        // Orden descendente por cantidad vendida
        lista.sort((a, b) -> b.getValue() - a.getValue());

        return lista;
    }

    // Obtiene solo el top N de platos más vendidos
    public ArrayList<Map.Entry<Plato, Integer>> obtenerTopPlatos(
            HashMap<Plato, Integer> mapa, int top) {

        // Validación del parámetro
        if (top <= 0) {
            throw new IllegalArgumentException("Top invalido.");
        }

        // Obtiene ranking completo
        ArrayList<Map.Entry<Plato, Integer>> ranking = obtenerRanking(mapa);

        // Ajusta el top si es mayor al tamaño de la lista
        if (top > ranking.size()) {
            top = ranking.size();
        }

        // Devuelve solo los primeros N elementos
        return new ArrayList<>(ranking.subList(0, top));
    }

    // REPORTE GENERAL
    // Genera un resumen simple del top de platos vendidos
    public String generarReporte(List<Pedido> pedidos, int top) {

        // Calcula ventas totales
        HashMap<Plato, Integer> mapa = calcularPlatosVendidos(pedidos);

        // Construye el texto del reporte
        return "Top platos: " + obtenerTopPlatos(mapa, top)
                + "\nFecha: " + fecha;
    }

    // REPORTE POR CATEGORÍA
    // Genera un ranking filtrado por categoría de plato
    public String generarReportePorCategoria(
            List<Pedido> pedidos,
            CategoriaPlato categoria,
            int top) {

        // Mapa para contar ventas solo de una categoría
        Map<Plato, Integer> contador = new HashMap<>();

        // Recorre pedidos
        for (Pedido p : pedidos) {

            if (p == null || p.getDetalles() == null) continue;

            for (DetallePedido d : p.getDetalles()) {

                if (d == null || d.getPlato() == null) continue;

                Plato plato = d.getPlato();

                // Filtra por categoría del plato
                if (plato.getCategoria() != categoria) {
                    continue;
                }

                // Acumula cantidad vendida
                contador.put(plato,
                        contador.getOrDefault(plato, 0) + d.getCantidad());
            }
        }

        // Convierte a lista para ordenar
        List<Map.Entry<Plato, Integer>> lista =
                new ArrayList<>(contador.entrySet());

        // Ordena de mayor a menor ventas
        lista.sort((a, b) -> b.getValue() - a.getValue());

        StringBuilder sb = new StringBuilder();

        // Encabezado del reporte
        sb.append("\n=== TOP ").append(top)
                .append(" PLATOS MAS VENDIDOS (")
                .append(categoria)
                .append(") ===\n");

        int i = 0;

        // Imprime solo los primeros "top" resultados
        for (Map.Entry<Plato, Integer> e : lista) {

            if (i >= top) break;

            sb.append(e.getKey().getNombre())
                    .append(" - Vendidos: ")
                    .append(e.getValue())
                    .append("\n");

            i++;
        }

        // Caso en que no haya ventas
        if (i == 0) {
            sb.append("No hay ventas en esta categoria.\n");
        }

        return sb.toString();
    }
}