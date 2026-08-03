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

    private ArrayList<Pedido> pedidos;
    private ArrayList<Mesa> mesas;
    private Scanner teclado;
    private ArrayList<Factura> facturas;
    private Scanner sc;

    // Constructor que inicializa las listas y herramientas de entrada del módulo
    public MenuFacturacionYPagos(ArrayList<Pedido> pedidos, ArrayList<Factura> facturas, ArrayList<Mesa> mesas) {
        this.teclado = new Scanner(System.in);
        this.pedidos = pedidos;
        this.facturas = facturas;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    // Inicia y gestiona el flujo del menú de facturación y cobro
    public void iniciarMenu() {
        int opcion;
        do {
            System.out.println("\n===== FACTURACION Y PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Mostrar facturas");
            System.out.println("3. Buscar factura");
            System.out.println("4. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();
            switch (opcion) {
                case 1 -> registrarPagoFactura();
                case 2 -> mostrarFacturas();
                case 3 -> buscarFactura();
                case 4 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion inválida.");
            }
        } while (opcion != 4);
    }

    // Lee un entero desde la consola capturando posibles excepciones de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }
    
    // Lee un valor decimal desde la consola capturando posibles excepciones de formato
    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito decimal valido ");
            }
        }
    }

    // Busca un pedido activo pendiente de cobro asignado a una mesa concreta
    private Pedido buscarPedidoPorMesa(int numeroMesa) {
        for (Pedido p : pedidos) {
            if (p.getMesa().getNumero() == numeroMesa && p.getEstado() != EstadoPedido.PAGADO && p.getEstado() != EstadoPedido.CANCELADO) {
                return p;
            }
        }
        return null;
    }

    // Calcula el monto de descuento aplicable por fidelidad o por cupón
    private double aplicarDescuentoFactura(Pedido pedido) {
        System.out.println("1. Descuento por visitas");
        System.out.println("2. Cupón");
        int op = leerEntero();
        double descuento = 0;
        if (op == 1) {
            Cliente c = pedido.getCliente();
            if (c != null && c.getVisitasMes() >= 10) {
                descuento = pedido.calcularTotalSinDescuento() * 0.20;
                System.out.println("Descuento del 20% aplicado por visitas.");
            } else {
                System.out.println("No cumple requisito de visitas.");
            }
        } else if (op == 2) {
            System.out.print("Porcentaje de cupón: ");
            double porc = leerDouble();
            descuento = pedido.calcularTotalSinDescuento() * (porc / 100);
        }
        return descuento;
    }

    // Registra el pago del pedido de una mesa, genera su factura y libera la mesa
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar pago
    ////////////////////////////////////////////////////
    private void registrarPagoFactura() {
        System.out.print("Número de mesa: ");
        int mesaNum = leerEntero();
        Pedido pedido = buscarPedidoPorMesa(mesaNum);
        if (pedido == null) {
            try {
                throw new ElementoNoEncontradoException("No existe un pedido activo pendiente de pago para la mesa #" + mesaNum + ".");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        double descuento = aplicarDescuentoFactura(pedido);
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. Transferencia");
        try {
            int op = leerEntero();
            if (op < 1 || op > 3) {
                throw new MetodoDePagoInvalidoException("Metodo de pago no aceptado, seleccione otro");
            }
            Factura factura = new Factura();
            factura.setPedido(pedido);
            factura.setNumeroFactura("F" + (facturas.size() + 1));
            factura.cerrarFactura(descuento);
            Pago pago = factura.getPago();
            switch (op) {
                case 1 -> pago.registrarPagoEfectivo(factura.getTotal());
                case 2 -> pago.registrarPagoTarjeta(factura.getTotal());
                case 3 -> pago.registrarPagoTransferencia(factura.getTotal());
            }
            facturas.add(factura);
            ArchivoDatos.guardar(facturas, "facturas.dat");
            pedido.cambiarEstado(EstadoPedido.PAGADO);
            ArchivoDatos.guardar(pedidos, "pedidos.dat");
            Mesa mesa = pedido.getMesa();
            mesa.setEstado(EstadoMesa.LIBRE);
            mesa.setPersonasOcupando(0);
            mesa.setClienteActual(null);
            mesa.setReservaActiva(null);
            ArchivoDatos.guardar(mesas, "mesas.dat");
            System.out.println("Pago realizado. Factura generada.");
        } catch (MetodoDePagoInvalidoException e) {
            MensajesDeExcepciones.mostrarAdvertencia("El metodo de pago seleccionado no es valido, por favor seleccione un metodo de pago valido.\n" + e.getMessage());
            return;
        }
    }
        
    // Despliega por consola todas las facturas registradas en el sistema
    ////////////////////////////////////////////////////
    // Opcion #2: Mostrar facturas
    ////////////////////////////////////////////////////
    private void mostrarFacturas() {
        for (Factura f : facturas) {
            System.out.println(f);
        }
    }

    // Busca y muestra una factura por número de comprobante o por cédula del cliente
    ////////////////////////////////////////////////////
    // Opcion #3: Buscar factura
    ////////////////////////////////////////////////////
    private void buscarFactura() {
        System.out.print("Ingrese cedula del cliente o numero de factura: ");
        String dato = teclado.nextLine().trim();
        for (Factura f : facturas) {
            if (f != null && (f.getNumeroFactura().equalsIgnoreCase(dato) || (f.getPedido() != null && f.getPedido().getCliente() != null && f.getPedido().getCliente().getCedula().equals(dato)))) {
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