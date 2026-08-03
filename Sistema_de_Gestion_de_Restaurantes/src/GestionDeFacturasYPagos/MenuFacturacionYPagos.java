package GestionDeFacturasYPagos;

import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.MetodoDePagoInvalidoException;
import GestionDePedidos.Pedido;
import GestionDePedidos.EstadoPedido;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;
import GestionDeClientes.Cliente;
import Serializacion.ArchivoDatos;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuFacturacionYPagos {

    // Lista de pedidos activos o registrados en el sistema
    private ArrayList<Pedido> pedidos;
    
    // Lista de mesas en el sistema
    private ArrayList<Mesa> mesas;

    // Scanner usado para lectura de datos del usuario
    private Scanner teclado;

    // Lista donde se almacenan las facturas generadas
    private ArrayList<Factura> facturas;

    // Segundo Scanner (redundante, pero usado en algunos métodos)
    private Scanner sc;

    // Constructor: recibe listas existentes de pedidos, facturas y mesas
    public MenuFacturacionYPagos(ArrayList<Pedido> pedidos,
                                 ArrayList<Factura> facturas,
                                 ArrayList<Mesa> mesas) {

        this.teclado = new Scanner(System.in);
        this.pedidos = pedidos;
        this.facturas = facturas;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    // Inicia el menú principal de facturación y pagos
    public void iniciarMenu() {

        int opcion;

        do {
            // Menú de opciones
            System.out.println("\n===== FACTURACION Y PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Mostrar facturas");
            System.out.println("3. Buscar factura");
            System.out.println("4. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            // Ejecución de la opción seleccionada
            switch (opcion) {

                case 1 -> registrarPagoFactura();
                case 2 -> mostrarFacturas();
                case 3 -> buscarFactura();
                case 4 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion inválida.");
            }

        } while (opcion != 4);
    }

    // Lee un número entero de forma segura desde consola
    private int leerEntero() {

        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }
    
        // Lee un número decimal con validación
    private double leerDouble() {

        while (true) {

            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito decimal valido ");
            }

        }
    }

    // Busca un pedido activo por número de mesa
    private Pedido buscarPedidoPorMesa(int numeroMesa) {

        for (Pedido p : pedidos) {
            if (p.getMesa().getNumero() == numeroMesa &&
                p.getEstado() != EstadoPedido.PAGADO) {
                return p;
            }
        }
        return null;
    }

    // Calcula el descuento a aplicar en la factura según la opción del usuario
    private double aplicarDescuentoFactura(Pedido pedido) {

        System.out.println("1. Descuento por visitas");
        System.out.println("2. Cupón");

        int op = leerEntero();

        double descuento = 0;

        // Descuento por fidelidad del cliente
        if (op == 1) {

            Cliente c = pedido.getCliente();

            if (c != null && c.getVisitasMes() >= 10) {
                descuento = pedido.calcularTotalSinDescuento() * 0.20;
                System.out.println("Descuento del 20% aplicado por visitas.");
            } else {
                System.out.println("No cumple requisito de visitas.");
            }

        // Descuento por cupón ingresado
        } else if (op == 2) {

            System.out.print("Porcentaje de cupón: ");
            double porc = leerDouble();

            descuento = pedido.calcularTotalSinDescuento() * (porc / 100);
        }

        return descuento;
    }

    // ---------------- OPCIÓN 1: REGISTRAR PAGO Y GENERAR FACTURA ----------------
    private void registrarPagoFactura() {

        System.out.print("Número de mesa: ");
        int mesaNum = leerEntero();

        // Buscar pedido activo asociado a la mesa
        Pedido pedido = buscarPedidoPorMesa(mesaNum);

        if (pedido == null) {
            try {
                throw new ElementoNoEncontradoException("No existe un pedido activo pendiente de pago para la mesa #" + mesaNum + ".");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }

        // Calcular descuento según tipo seleccionado
        double descuento = aplicarDescuentoFactura(pedido);

        // Selección de método de pago
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. Transferencia");

        try {

            int op = leerEntero();

            if (op < 1 || op > 3) {
                throw new MetodoDePagoInvalidoException(
                        "Metodo de pago no aceptado, seleccione otro");
            }

            // Creación de la factura
            Factura factura = new Factura();
            factura.setPedido(pedido);
            factura.setNumeroFactura("F" + (facturas.size() + 1));

            // Cálculo interno de subtotal, descuento y total
            factura.cerrarFactura(descuento);

            // Obtener objeto pago asociado a la factura
            Pago pago = factura.getPago();

            // Registrar el pago según método seleccionado
            switch (op) {
                case 1 -> pago.registrarPagoEfectivo(factura.getTotal());
                case 2 -> pago.registrarPagoTarjeta(factura.getTotal());
                case 3 -> pago.registrarPagoTransferencia(factura.getTotal());
            }

            // Guardar factura en el sistema
            facturas.add(factura);
            ArchivoDatos.guardar(facturas, "facturas.dat");

            // Cambiar estado del pedido a pagado
            pedido.cambiarEstado(EstadoPedido.PAGADO);
            ArchivoDatos.guardar(pedidos, "pedidos.dat");

            // Liberar la mesa
            pedido.getMesa().setEstado(EstadoMesa.LIBRE);
            ArchivoDatos.guardar(mesas, "mesas.dat");

            System.out.println("Pago realizado. Factura generada.");

        } catch (MetodoDePagoInvalidoException e) {

            MensajesDeExcepciones.mostrarAdvertencia(
                    "El metodo de pago seleccionado no es valido, por favor seleccione un metodo de pago valido."
                    + "\n" + e.getMessage());
            return;
        }
    }
        

    // ---------------- OPCIÓN 2: MOSTRAR FACTURAS ----------------
    private void mostrarFacturas() {

        for (Factura f : facturas) {
            System.out.println(f);
        }
    }

    // ---------------- OPCIÓN 3: BUSCAR FACTURA ----------------
    private void buscarFactura() {

        System.out.print("Ingrese cedula del cliente o numero de factura: ");
        String dato = teclado.nextLine().trim();

        // Buscar coincidencia por número de factura o cédula del cliente
        for (Factura f : facturas) {

            if (f != null && (f.getNumeroFactura().equalsIgnoreCase(dato) ||
                (f.getPedido() != null && f.getPedido().getCliente() != null && f.getPedido().getCliente().getCedula().equals(dato)))) {

                System.out.println(f);
                return;
            }
        }

        try {
            throw new ElementoNoEncontradoException("Factura o cliente '" + dato + "' no fue encontrado.");
        } catch (ElementoNoEncontradoException e) {
            MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
        }
    }
}