package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDeClientes.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class ReporteHistorialCliente {

    // Fecha en la que se genera el reporte
    private LocalDateTime fecha;

    // Constructor: inicializa la fecha con el momento actual
    public ReporteHistorialCliente() {
        this.fecha = LocalDateTime.now();
    }

    // Permite obtener la fecha del reporte
    public LocalDateTime getFecha() {
        return fecha;
    }

    // HISTORIAL INDIVIDUAL DE CLIENTE
    // Devuelve todos los pedidos realizados por un cliente específico
    public ArrayList<Pedido> obtenerHistorialCliente(ArrayList<Pedido> pedidos,
                                                      Cliente cliente) {

        // Validación de entrada
        if (pedidos == null || cliente == null) {
            throw new IllegalArgumentException("Datos inválidos.");
        }

        // Lista donde se guardan los pedidos del cliente
        ArrayList<Pedido> resultado = new ArrayList<>();

        // Filtra pedidos que pertenezcan al cliente indicado
        for (Pedido p : pedidos) {

            if (p != null && cliente.equals(p.getCliente())) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    // Devuelve el historial como texto (usado para impresión rápida)
    public String filtrarPedidos(ArrayList<Pedido> pedidos, Cliente cliente) {
        return obtenerHistorialCliente(pedidos, cliente).toString();
    }

    // REPORTE GENERAL DEL SISTEMA
    // Genera un resumen global de clientes y consumo del restaurante
    public String generarReporteGeneral(ArrayList<Pedido> pedidos,
                                       ArrayList<Factura> facturas) {

        // Validación de listas
        if (pedidos == null || facturas == null) {
            throw new IllegalArgumentException("Listas inválidas.");
        }

        // 1. Cantidad de clientes únicos que realizaron pedidos
        long clientesAtendidos = pedidos.stream()
                .map(Pedido::getCliente)     // obtiene el cliente de cada pedido
                .filter(Objects::nonNull)    // elimina valores nulos
                .distinct()                  // elimina clientes repetidos
                .count();                    // cuenta clientes únicos

        // 2. Consumo total del restaurante (suma de todas las facturas)
        double consumoTotal = facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();

        // 3. Promedio de consumo por factura
        double promedio = facturas.isEmpty()
                ? 0
                : consumoTotal / facturas.size();

        // Construcción del reporte en texto
        StringBuilder sb = new StringBuilder();

        sb.append("Clientes que comieron: ").append(clientesAtendidos).append("\n");
        sb.append("Consumo total: ").append(consumoTotal).append("\n");
        sb.append("Consumo promedio: ").append(promedio).append("\n");

        // Fecha del reporte (solo día)
        sb.append("Fecha: ").append(fecha.toLocalDate()).append("\n");

        return sb.toString();
    }
}