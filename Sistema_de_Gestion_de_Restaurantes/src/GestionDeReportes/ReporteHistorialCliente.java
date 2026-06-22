package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDeClientes.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class ReporteHistorialCliente {
    //Atributos
    private LocalDateTime fecha;

    //Constructor
    public ReporteHistorialCliente() {
        this.fecha = LocalDateTime.now();
    }
    
    //Get de la fecha
    public LocalDateTime getFecha() {
        return fecha;
    }

    //Métodos
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

        sb.append("Clientes que comieron: ").append(clientesAtendidos).append("\n");
        sb.append("Consumo total: ").append(consumoTotal).append("\n");
        sb.append("Consumo promedio: ").append(promedio).append("\n");
        sb.append("Fecha: ").append(fecha.toLocalDate()).append("\n");

        return sb.toString();
    }
}