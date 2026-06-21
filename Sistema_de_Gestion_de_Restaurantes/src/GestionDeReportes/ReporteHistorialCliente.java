package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDeClientes.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class ReporteHistorialCliente {

    private LocalDateTime fechaGeneracion;

    public ReporteHistorialCliente() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    // HISTORIAL INDIVIDUAL (por si lo usas en otra parte)
    public ArrayList<Pedido> obtenerHistorialCliente(ArrayList<Pedido> pedidos, Cliente cliente) {

        if (pedidos == null || cliente == null) {
            throw new IllegalArgumentException("Datos inválidos.");
        }

        ArrayList<Pedido> resultado = new ArrayList<>();

        for (Pedido p : pedidos) {
            if (p != null && cliente.equals(p.getCliente())) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    public String filtrarPedidos(ArrayList<Pedido> pedidos, Cliente cliente) {
        return obtenerHistorialCliente(pedidos, cliente).toString();
    }

    // 🔥 REPORTE GENERAL (EL QUE NECESITAS)
    public String generarReporteGeneral(ArrayList<Pedido> pedidos,
                                       ArrayList<Factura> facturas) {

        if (pedidos == null || facturas == null) {
            throw new IllegalArgumentException("Listas inválidas.");
        }

        // 1. Clientes únicos que realmente comieron
        long clientesAtendidos = pedidos.stream()
                .map(Pedido::getCliente)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        // 2. Consumo total del restaurante
        double consumoTotal = facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();

        // 3. Promedio de consumo por factura
        double promedio = facturas.isEmpty()
                ? 0
                : consumoTotal / facturas.size();

        StringBuilder sb = new StringBuilder();

        sb.append("\n=== REPORTE GENERAL DE CLIENTES ===\n");
        sb.append("Clientes que comieron: ").append(clientesAtendidos).append("\n");
        sb.append("Consumo total: ").append(consumoTotal).append("\n");
        sb.append("Consumo promedio: ").append(promedio).append("\n");
        sb.append("Fecha: ").append(fechaGeneracion.toLocalDate()).append("\n");

        return sb.toString();
    }
}