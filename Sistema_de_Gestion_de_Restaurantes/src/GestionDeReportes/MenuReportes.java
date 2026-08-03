package GestionDeReportes;

import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.CategoriaPlato;
import Serializacion.ArchivoDatos;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuReportes {

    // Scanner para lectura de datos por consola
    private Scanner sc;

    // Listas principales del sistema usadas para generar reportes
    private ArrayList<Factura> facturas;
    private ArrayList<Pedido> pedidos;
    private ArrayList<DetallePedido> detalles;
    private ArrayList<String> reportesGenerados;

    // Objetos de reportes especializados (cada uno genera un tipo de informe)
    private ReporteVentas reporteVentas;
    private ReportePlatosMasVendidos reportePlatos;
    private ReporteMesasMasUtilizadas reporteMesas;
    private ReporteConsumoPorMesa reporteConsumo;
    private ReporteHistorialCliente reporteHistorial;

    // Constructor: recibe las listas globales del sistema y crea los reportes
    public MenuReportes(ArrayList<Factura> facturas,
                        ArrayList<Pedido> pedidos,
                        ArrayList<DetallePedido> detalles,
                        ArrayList<String> reportesGenerados) {

        this.sc = new Scanner(System.in);
        this.facturas = facturas;
        this.pedidos = pedidos;
        this.detalles = detalles;
        this.reportesGenerados = (reportesGenerados != null) ? reportesGenerados : new ArrayList<>();

        // Inicialización de los generadores de reportes
        reporteVentas = new ReporteVentas();
        reportePlatos = new ReportePlatosMasVendidos();
        reporteMesas = new ReporteMesasMasUtilizadas();
        reporteConsumo = new ReporteConsumoPorMesa();
        reporteHistorial = new ReporteHistorialCliente();
    }

    // Constructor de respaldo para compatibilidad
    public MenuReportes(ArrayList<Factura> facturas,
                        ArrayList<Pedido> pedidos,
                        ArrayList<DetallePedido> detalles) {
        this(facturas, pedidos, detalles, new ArrayList<>());
    }

    // Menú principal de reportes
    public void iniciarMenu() {

        int opcion;

        do {

            // Menú de opciones disponibles
            System.out.println("\n========== REPORTES ==========");
            System.out.println("1. Reporte de ventas del dia");
            System.out.println("2. Reporte de platos mas vendidos del dia");
            System.out.println("3. Reporte de mesas mas utilizadas del dia");
            System.out.println("4. Reporte de consumo por mesa del dia");
            System.out.println("5. Reporte historial de consumo del dia");
            System.out.println("6. Ver historial completo de reportes guardados");
            System.out.println("7. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {

                case 1 -> generarReporteVentas();
                case 2 -> generarReportePlatosMasVendidos();
                case 3 -> generarReporteMesasMasUtilizadas();
                case 4 -> generarReporteConsumoPorMesa();
                case 5 -> generarReporteHistorialCliente();
                case 6 -> mostrarHistorialReportes();
                case 7 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 7);
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

    // Obtiene únicamente las facturas emitidas en el día de hoy
    private ArrayList<Factura> obtenerFacturasDeHoy() {
        LocalDate hoy = LocalDate.now();
        ArrayList<Factura> delDia = new ArrayList<>();
        if (facturas != null) {
            for (Factura f : facturas) {
                if (f != null && f.getFecha() != null && f.getFecha().toLocalDate().equals(hoy)) {
                    delDia.add(f);
                }
            }
        }
        return delDia;
    }

    // Obtiene únicamente los pedidos registrados en el día de hoy
    private ArrayList<Pedido> obtenerPedidosDeHoy() {
        LocalDate hoy = LocalDate.now();
        ArrayList<Pedido> delDia = new ArrayList<>();
        if (pedidos != null) {
            for (Pedido p : pedidos) {
                if (p != null && p.getFecha() != null && p.getFecha().toLocalDate().equals(hoy)) {
                    delDia.add(p);
                }
            }
        }
        return delDia;
    }

    // Método auxiliar para imprimir, añadir al historial y persistir en reportes.dat
    private void guardarYMostrarReporte(String titulo, String contenido) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "=== " + titulo + " [" + timestamp + "] ===\n" + contenido;
        
        System.out.println("\n" + contenido);
        reportesGenerados.add(registro);
        ArchivoDatos.guardar(reportesGenerados, "reportes.dat");
    }

    // ---------------- OPCIÓN 1 ----------------
    // Genera el reporte general de ventas del día de hoy
    private void generarReporteVentas() {

        LocalDate hoy = LocalDate.now();
        ArrayList<Factura> facturasHoy = obtenerFacturasDeHoy();

        if (facturasHoy.isEmpty()) {
            try {
                throw new ElementoNoEncontradoException("No hay ventas registradas el dia de hoy (" + hoy + ").");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

        try {
            String res = reporteVentas.generarResumenVentas(facturasHoy);
            guardarYMostrarReporte("REPORTE DE VENTAS (" + hoy + ")", res);

        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
        }
    }

    // ---------------- OPCIÓN 2 ----------------
    // Reporte de platos más vendidos en el día de hoy filtrado por categoría
    private void generarReportePlatosMasVendidos() {

        LocalDate hoy = LocalDate.now();
        ArrayList<Pedido> pedidosHoy = obtenerPedidosDeHoy();

        if (pedidosHoy.isEmpty()) {
            try {
                throw new ElementoNoEncontradoException("No hay pedidos ni ventas registradas el dia de hoy (" + hoy + ").");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

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

            // Genera el reporte de platos más vendidos
            String res = reportePlatos.generarReportePorCategoria(pedidosHoy, categoria, top);
            guardarYMostrarReporte("REPORTE PLATOS MAS VENDIDOS - " + categoria + " (" + hoy + ")", res);

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
        }
    }

    // ---------------- OPCIÓN 3 ----------------
    // Reporte de mesas más utilizadas en el día de hoy
    private void generarReporteMesasMasUtilizadas() {

        LocalDate hoy = LocalDate.now();
        ArrayList<Pedido> pedidosHoy = obtenerPedidosDeHoy();

        if (pedidosHoy.isEmpty()) {
            try {
                throw new ElementoNoEncontradoException("No hay uso de mesas ni pedidos registrados el dia de hoy (" + hoy + ").");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

        try {
            String res = reporteMesas.generarReporte(pedidosHoy);
            guardarYMostrarReporte("REPORTE MESAS MAS UTILIZADAS (" + hoy + ")", res);

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
        }
    }

    // ---------------- OPCIÓN 4 ----------------
    // Reporte de consumo por mesa en el día de hoy
    private void generarReporteConsumoPorMesa() {

        LocalDate hoy = LocalDate.now();
        ArrayList<Factura> facturasHoy = obtenerFacturasDeHoy();

        if (facturasHoy.isEmpty()) {
            try {
                throw new ElementoNoEncontradoException("No hay ventas registradas el dia de hoy (" + hoy + ").");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

        try {
            String res = reporteConsumo.generarReporte(facturasHoy);
            guardarYMostrarReporte("REPORTE CONSUMO POR MESA (" + hoy + ")", res);

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
        }
    }

    // ---------------- OPCIÓN 5 ----------------
    // Reporte del historial de consumo por cliente en el día de hoy
    private void generarReporteHistorialCliente() {

        LocalDate hoy = LocalDate.now();
        ArrayList<Factura> facturasHoy = obtenerFacturasDeHoy();

        if (facturasHoy.isEmpty()) {
            try {
                throw new ElementoNoEncontradoException("No hay ventas ni atenciones registradas el dia de hoy (" + hoy + ").");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

        try {

            int totalPersonas = 0;
            double totalConsumo = 0;
            int facturasValidas = 0;

            // Recorrido de las facturas de hoy para acumular datos
            for (Factura f : facturasHoy) {

                if (f == null) continue;

                // Subtotal de la factura (sin descuentos)
                double subtotal = f.getSubtotal();

                // Personas atendidas en la mesa asociada a la factura
                int personas = f.getPedido() != null && f.getPedido().getMesa() != null
                        ? f.getPedido().getMesa().getPersonasOcupando() : 0;

                // Se ignoran registros inválidos
                if (personas <= 0) continue;

                totalConsumo += subtotal;
                totalPersonas += personas;
                facturasValidas++;
            }

            // Validación final antes de calcular promedios
            if (facturasValidas == 0 || totalPersonas == 0) {
                try {
                    throw new ElementoNoEncontradoException("No hay datos suficientes de consumo registradas el dia de hoy (" + hoy + ").");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }

            // Cálculo del consumo promedio por persona
            double consumoPromedioPorPersona = totalConsumo / totalPersonas;

            // Construcción del reporte final
            StringBuilder sb = new StringBuilder();
            sb.append("Fecha: ").append(hoy).append("\n");
            sb.append("Facturas analizadas: ").append(facturasValidas).append("\n");
            sb.append("Personas atendidas: ").append(totalPersonas).append("\n");
            sb.append("Consumo total (subtotal): ").append(totalConsumo).append("\n");
            sb.append("Consumo promedio por persona: ").append(consumoPromedioPorPersona);

            guardarYMostrarReporte("REPORTE CONSUMO CLIENTES (" + hoy + ")", sb.toString());

        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
        }
    }

    // ---------------- OPCIÓN 6 ----------------
    // Muestra el historial completo de todos los reportes guardados a lo largo del tiempo
    private void mostrarHistorialReportes() {

        System.out.println("\n========== HISTORIAL DE REPORTES GUARDADOS ==========");

        if (reportesGenerados == null || reportesGenerados.isEmpty()) {
            System.out.println("No existen reportes guardados en el sistema.");
            return;
        }

        for (String r : reportesGenerados) {
            System.out.println("\n" + r);
            System.out.println("-----------------------------------------------------");
        }
    }
}