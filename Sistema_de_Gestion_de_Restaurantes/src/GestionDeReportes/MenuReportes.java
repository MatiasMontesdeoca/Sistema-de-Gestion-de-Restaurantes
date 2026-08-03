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

    private Scanner sc;
    private ArrayList<Factura> facturas;
    private ArrayList<Pedido> pedidos;
    private ArrayList<DetallePedido> detalles;
    private ArrayList<String> reportesGenerados;
    private ReporteVentas reporteVentas;
    private ReportePlatosMasVendidos reportePlatos;
    private ReporteMesasMasUtilizadas reporteMesas;
    private ReporteConsumoPorMesa reporteConsumo;
    private ReporteHistorialCliente reporteHistorial;

    // Constructor que inicializa las referencias globales y los generadores de reportes
    public MenuReportes(ArrayList<Factura> facturas, ArrayList<Pedido> pedidos, ArrayList<DetallePedido> detalles, ArrayList<String> reportesGenerados) {
        this.sc = new Scanner(System.in);
        this.facturas = facturas;
        this.pedidos = pedidos;
        this.detalles = detalles;
        this.reportesGenerados = (reportesGenerados != null) ? reportesGenerados : new ArrayList<>();
        reporteVentas = new ReporteVentas();
        reportePlatos = new ReportePlatosMasVendidos();
        reporteMesas = new ReporteMesasMasUtilizadas();
        reporteConsumo = new ReporteConsumoPorMesa();
        reporteHistorial = new ReporteHistorialCliente();
    }

    // Constructor sobrecargado para compatibilidad sin lista previa de reportes
    public MenuReportes(ArrayList<Factura> facturas, ArrayList<Pedido> pedidos, ArrayList<DetallePedido> detalles) {
        this(facturas, pedidos, detalles, new ArrayList<>());
    }

    // Inicia y gestiona la navegación interactiva del menú de reportes
    public void iniciarMenu() {
        int opcion;
        do {
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

    // Lee un entero desde la consola capturando posibles errores de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Filtra las facturas emitidas únicamente en la fecha de hoy
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

    // Filtra los pedidos creados únicamente en la fecha de hoy
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

    // Imprime el reporte en pantalla, lo añade al listado acumulado y lo serializa a reportes.dat
    private void guardarYMostrarReporte(String titulo, String contenido) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "=== " + titulo + " [" + timestamp + "] ===\n" + contenido;
        System.out.println("\n" + contenido);
        reportesGenerados.add(registro);
        ArchivoDatos.guardar(reportesGenerados, "reportes.dat");
    }

    // Genera y guarda el resumen general de ventas del día actual
    ////////////////////////////////////////////////////
    // Opcion #1: Reporte de ventas del dia
    ////////////////////////////////////////////////////
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
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
        }
    }

    // Genera y guarda el top de platos más vendidos en el día actual por categoría
    ////////////////////////////////////////////////////
    // Opcion #2: Reporte de platos mas vendidos del dia
    ////////////////////////////////////////////////////
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
            if (top > 10) top = 10;
            if (top <= 0) {
                System.out.println("Top invalido.");
                return;
            }
            String res = reportePlatos.generarReportePorCategoria(pedidosHoy, categoria, top);
            guardarYMostrarReporte("REPORTE PLATOS MAS VENDIDOS - " + categoria + " (" + hoy + ")", res);
        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
        }
    }

    // Genera y guarda el reporte de las mesas más ocupadas durante el día de hoy
    ////////////////////////////////////////////////////
    // Opcion #3: Reporte de mesas mas utilizadas del dia
    ////////////////////////////////////////////////////
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
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
        }
    }

    // Genera y guarda el consumo facturado por cada mesa en el día de hoy
    ////////////////////////////////////////////////////
    // Opcion #4: Reporte de consumo por mesa del dia
    ////////////////////////////////////////////////////
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
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
        }
    }

    // Genera y guarda el historial y promedios de consumo de clientes en el día de hoy
    ////////////////////////////////////////////////////
    // Opcion #5: Reporte historial de consumo del dia
    ////////////////////////////////////////////////////
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
            for (Factura f : facturasHoy) {
                if (f == null) continue;
                double subtotal = f.getSubtotal();
                int personas = f.getPedido() != null && f.getPedido().getMesa() != null ? f.getPedido().getMesa().getPersonasOcupando() : 0;
                if (personas <= 0) continue;
                totalConsumo += subtotal;
                totalPersonas += personas;
                facturasValidas++;
            }
            if (facturasValidas == 0 || totalPersonas == 0) {
                try {
                    throw new ElementoNoEncontradoException("No hay datos suficientes de consumo registradas el dia de hoy (" + hoy + ").");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            double consumoPromedioPorPersona = totalConsumo / totalPersonas;
            StringBuilder sb = new StringBuilder();
            sb.append("Fecha: ").append(hoy).append("\n");
            sb.append("Facturas analizadas: ").append(facturasValidas).append("\n");
            sb.append("Personas atendidas: ").append(totalPersonas).append("\n");
            sb.append("Consumo total (subtotal): ").append(totalConsumo).append("\n");
            sb.append("Consumo promedio por persona: ").append(consumoPromedioPorPersona);
            guardarYMostrarReporte("REPORTE CONSUMO CLIENTES (" + hoy + ")", sb.toString());
        } catch (Exception e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
        }
    }

    // Despliega en consola todo el historial acumulado de reportes almacenados en reportes.dat
    ////////////////////////////////////////////////////
    // Opcion #6: Ver historial completo de reportes guardados
    ////////////////////////////////////////////////////
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