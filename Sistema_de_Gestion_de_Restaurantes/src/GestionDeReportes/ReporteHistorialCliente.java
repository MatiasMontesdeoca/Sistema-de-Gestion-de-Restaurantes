package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDeClientes.Cliente;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class ReporteHistorialCliente implements Serializable {

    private LocalDateTime fecha;

    // Constructor que inicializa la fecha actual para la generación del reporte
    public ReporteHistorialCliente() {
        this.fecha = LocalDateTime.now();
    }

    // Obtiene la fecha y hora de emisión del reporte
    public LocalDateTime getFecha() {
        return fecha;
    }

    // Filtra y retorna los pedidos realizados por un cliente específico
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

    // Retorna la representación en formato de texto del historial de un cliente
    public String filtrarPedidos(ArrayList<Pedido> pedidos, Cliente cliente) {
        return obtenerHistorialCliente(pedidos, cliente).toString();
    }

    // Genera un resumen estadístico global con clientes atendidos, consumo total y promedio
    public String generarReporteGeneral(ArrayList<Pedido> pedidos, ArrayList<Factura> facturas) {
        if (pedidos == null || facturas == null) {
            throw new IllegalArgumentException("Listas inválidas.");
        }
        long clientesAtendidos = pedidos.stream()
                .map(Pedido::getCliente)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        double consumoTotal = facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
        double promedio = facturas.isEmpty() ? 0 : consumoTotal / facturas.size();
        StringBuilder sb = new StringBuilder();
        sb.append("Clientes que comieron: ").append(clientesAtendidos).append("\n");
        sb.append("Consumo total: ").append(consumoTotal).append("\n");
        sb.append("Consumo promedio: ").append(promedio).append("\n");
        sb.append("Fecha: ").append(fecha.toLocalDate()).append("\n");
        return sb.toString();
    }
}