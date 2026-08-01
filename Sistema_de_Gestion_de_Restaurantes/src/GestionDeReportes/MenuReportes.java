package GestionDeReportes;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.CategoriaPlato;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuReportes {

    // Scanner para lectura de datos por consola
    private Scanner sc;

    // Listas principales del sistema usadas para generar reportes
    private ArrayList<Factura> facturas;
    private ArrayList<Pedido> pedidos;
    private ArrayList<DetallePedido> detalles;

    // Objetos de reportes especializados (cada uno genera un tipo de informe)
    private ReporteVentas reporteVentas;
    private ReportePlatosMasVendidos reportePlatos;
    private ReporteMesasMasUtilizadas reporteMesas;
    private ReporteConsumoPorMesa reporteConsumo;
    private ReporteHistorialCliente reporteHistorial;

    // Constructor: recibe las listas globales del sistema y crea los reportes
    public MenuReportes(ArrayList<Factura> facturas,
                        ArrayList<Pedido> pedidos,
                        ArrayList<DetallePedido> detalles) {

        this.sc = new Scanner(System.in);
        this.facturas = facturas;
        this.pedidos = pedidos;
        this.detalles = detalles;

        // Inicialización de los generadores de reportes
        reporteVentas = new ReporteVentas();
        reportePlatos = new ReportePlatosMasVendidos();
        reporteMesas = new ReporteMesasMasUtilizadas();
        reporteConsumo = new ReporteConsumoPorMesa();
        reporteHistorial = new ReporteHistorialCliente();
    }

    // Menú principal de reportes
    public void iniciarMenu() {

        int opcion;

        do {

            // Menú de opciones disponibles
            System.out.println("\n========== REPORTES ==========");
            System.out.println("1. Reporte de ventas");
            System.out.println("2. Reporte de platos mas vendidos");
            System.out.println("3. Reporte de mesas mas utilizadas");
            System.out.println("4. Reporte de consumo por mesa");
            System.out.println("5. Reporte historial de cliente");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

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

    // Lee enteros de forma segura evitando errores de formato
    private int leerEntero() {

        while (true) {

            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido" );
            }

        }
    }

    // ---------------- OPCIÓN 1 ----------------
    // Genera el reporte general de ventas basado en facturas
    private void generarReporteVentas() {

        try {
            System.out.println();
            System.out.println(reporteVentas.generarResumenVentas(facturas));

        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }

    // ---------------- OPCIÓN 2 ----------------
    // Reporte de platos más vendidos filtrado por categoría
    private void generarReportePlatosMasVendidos() {

        try {

            System.out.println("\nSeleccione categoria:");
            System.out.println("1. Entrada");
            System.out.println("2. Plato fuerte");
            System.out.println("3. Postre");
            System.out.println("4. Bebida");

            int op = leerEntero();

            // Conversión de opción numérica a enum de categoría
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
            int top = leerEntero();

            // Limita el rango del top para evitar valores excesivos
            if (top > 10) top = 10;
            if (top <= 0) {
                System.out.println("Top invalido.");
                return;
            }

            System.out.println();

            // Genera el reporte de platos más vendidos
            System.out.println(
                    reportePlatos.generarReportePorCategoria(pedidos, categoria, top)
            );

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }

    // ---------------- OPCIÓN 3 ----------------
    // Reporte de mesas más utilizadas en base a los pedidos
    private void generarReporteMesasMasUtilizadas() {

        try {
            System.out.println();
            System.out.println(reporteMesas.generarReporte(pedidos));

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }

    // Reporte de consumo por mesa basado en facturas
    private void generarReporteConsumoPorMesa() {

        try {
            System.out.println();
            System.out.println(reporteConsumo.generarReporte(facturas));

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }

    // ---------------- OPCIÓN 4 ----------------
    // Reporte manual del historial de consumo por cliente (cálculo directo)
    private void generarReporteHistorialCliente() {

        try {

            // Validación de datos disponibles
            if (facturas == null || facturas.isEmpty()) {
                System.out.println("No hay datos de facturas.");
                return;
            }

            int totalPersonas = 0;
            double totalConsumo = 0;
            int facturasValidas = 0;

            // Recorrido de todas las facturas para acumular datos
            for (Factura f : facturas) {

                if (f == null) continue;

                // Subtotal de la factura (sin descuentos)
                double subtotal = f.getSubtotal();

                // Personas atendidas en la mesa asociada a la factura
                int personas = f.getPedido().getMesa().getPersonasOcupando();

                // Se ignoran registros inválidos
                if (personas <= 0) continue;

                totalConsumo += subtotal;
                totalPersonas += personas;
                facturasValidas++;
            }

            // Validación final antes de calcular promedios
            if (facturasValidas == 0 || totalPersonas == 0) {
                System.out.println("No hay datos suficientes para el reporte.");
                return;
            }

            // Cálculo del consumo promedio por persona
            double consumoPromedioPorPersona = totalConsumo / totalPersonas;

            // Impresión del reporte final
            System.out.println("Facturas analizadas: " + facturasValidas);
            System.out.println("Personas atendidas: " + totalPersonas);
            System.out.println("Consumo total (subtotal): " + totalConsumo);
            System.out.println("Consumo promedio por persona: " + consumoPromedioPorPersona);

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }
}