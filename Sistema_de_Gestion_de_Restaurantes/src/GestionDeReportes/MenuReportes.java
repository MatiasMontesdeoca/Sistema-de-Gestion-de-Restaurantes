package GestionDeReportes;

import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.CategoriaPlato;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuReportes {

    private Scanner teclado;

    private ArrayList<Factura> facturas;
    private ArrayList<Pedido> pedidos;
    private ArrayList<DetallePedido> detalles;

    private ReporteVentas reporteVentas;
    private ReportePlatosMasVendidos reportePlatos;
    private ReporteMesasMasUtilizadas reporteMesas;
    private ReporteConsumoPorMesa reporteConsumo;
    private ReporteHistorialCliente reporteHistorial;

    public MenuReportes(ArrayList<Factura> facturas,
                        ArrayList<Pedido> pedidos,
                        ArrayList<DetallePedido> detalles) {

        this.teclado = new Scanner(System.in);

        this.facturas = facturas;
        this.pedidos = pedidos;
        this.detalles = detalles;

        reporteVentas = new ReporteVentas();
        reportePlatos = new ReportePlatosMasVendidos();
        reporteMesas = new ReporteMesasMasUtilizadas();
        reporteConsumo = new ReporteConsumoPorMesa();
        reporteHistorial = new ReporteHistorialCliente();
    }

    public void iniciarMenu() {

        int opcion;

        do {

            System.out.println("\n========== REPORTES ==========");
            System.out.println("1. Reporte de ventas");
            System.out.println("2. Reporte de platos mas vendidos");
            System.out.println("3. Reporte de mesas mas utilizadas");
            System.out.println("4. Reporte de consumo por mesa");
            System.out.println("5. Reporte historial de cliente");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = Integer.parseInt(teclado.nextLine());

            switch (opcion) {

                case 1 -> generarReporteVentas();
                case 2 -> generarReportePlatosMasVendidos();
                case 3 -> generarReporteMesasMasUtilizadas();
                case 4 -> generarReporteConsumoPorMesa();
                case 5 -> generarReporteHistorialCliente();
                case 6 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 6);
    }

    private void generarReporteVentas() {

        try {
            System.out.println();
            System.out.println(reporteVentas.generarResumenVentas(facturas));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void generarReportePlatosMasVendidos() {

        try {

            System.out.println("\nSeleccione categoria:");
            System.out.println("1. Entrada");
            System.out.println("2. Plato fuerte");
            System.out.println("3. Postre");
            System.out.println("4. Bebida");

            int op = Integer.parseInt(teclado.nextLine());

            CategoriaPlato categoria = switch (op) {
                case 1 -> CategoriaPlato.ENTRADA;
                case 2 -> CategoriaPlato.PLATO_FUERTE;
                case 3 -> CategoriaPlato.POSTRE;
                case 4 -> CategoriaPlato.BEBIDA;
                default -> null;
            };

            if (categoria == null) {
                System.out.println("Categoria invalida.");
                return;
            }

            System.out.print("Top cuantos desea ver (max 10): ");
            int top = Integer.parseInt(teclado.nextLine());

            if (top > 10) top = 10;
            if (top <= 0) {
                System.out.println("Top invalido.");
                return;
            }

            System.out.println();

            System.out.println(
                    reportePlatos.generarReportePorCategoria(pedidos, categoria, top)
            );

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void generarReporteMesasMasUtilizadas() {

        try {
            System.out.println();
            System.out.println(reporteMesas.generarReporte(pedidos));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void generarReporteConsumoPorMesa() {

        try {
            System.out.println();
            System.out.println(reporteConsumo.generarReporte(facturas));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void generarReporteHistorialCliente() {

    try {

        if (facturas == null || facturas.isEmpty()) {
            System.out.println("No hay datos de facturas.");
            return;
        }

        int totalPersonas = 0;
        double totalConsumo = 0;
        int facturasValidas = 0;

        for (Factura f : facturas) {

            if (f == null) continue;

            // 🔴 AJUSTA ESTO SEGÚN TU MODELO
            double subtotal = f.getSubtotal();
            int personas = f.getPedido().getMesa().getPersonasOcupando();

            if (personas <= 0) continue;

            totalConsumo += subtotal;
            totalPersonas += personas;
            facturasValidas++;
        }

        if (facturasValidas == 0 || totalPersonas == 0) {
            System.out.println("No hay datos suficientes para el reporte.");
            return;
        }

        double consumoPromedioPorPersona = totalConsumo / totalPersonas;

        System.out.println("\n=== REPORTE HISTORIAL DE CONSUMO ===");
        System.out.println("Facturas analizadas: " + facturasValidas);
        System.out.println("Personas atendidas: " + totalPersonas);
        System.out.println("Consumo total (subtotal): " + totalConsumo);
        System.out.println("Consumo promedio por persona: " + consumoPromedioPorPersona);

    } catch (Exception e) {
        System.out.println(e.getMessage());}
    }
}
