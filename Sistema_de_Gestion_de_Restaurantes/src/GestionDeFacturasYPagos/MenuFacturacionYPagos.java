package GestionDeFacturasYPagos;

import GestionDePedidos.Pedido;
import GestionDePedidos.EstadoPedido;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeClientes.Cliente;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuFacturacionYPagos {
    //Atributos
    private ArrayList<Pedido> pedidos;
    private Scanner teclado;
    private ArrayList<Factura> facturas;
    private Scanner sc;

    //Constructor
    public MenuFacturacionYPagos(ArrayList<Pedido> pedidos,
                                 ArrayList<Factura> facturas) {

        this.teclado = new Scanner(System.in);
        this.pedidos = pedidos;
        this.facturas = facturas;
        this.sc = new Scanner(System.in);
    }

    //Inicio de Menu
    public void iniciarMenu() {

        int opcion;

        do {
            //Interfaz del menu
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

    //Métodos
    private int leerEntero() {

        while (true) {

            try {

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.print("Ingrese un numero valido: ");

            }

        }

    }    
    
    private Pedido buscarPedidoPorMesa(int numeroMesa) {

        for (Pedido p : pedidos) {
            if (p.getMesa().getNumero() == numeroMesa &&
                p.getEstado() != EstadoPedido.PAGADO) {
                return p;
            }
        }
        return null;
    }

    private double aplicarDescuentoFactura(Pedido pedido) {

        System.out.println("1. Descuento por visitas");
        System.out.println("2. Cupón");

        int op = Integer.parseInt(teclado.nextLine());

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
            double porc = Double.parseDouble(teclado.nextLine());

            descuento = pedido.calcularTotalSinDescuento() * (porc / 100);
        }

        return descuento;
    }

    // ---------------- OPCIÓN 1 ----------------
    private void registrarPagoFactura() {

        System.out.print("Número de mesa: ");
        int mesaNum = Integer.parseInt(teclado.nextLine());

        Pedido pedido = buscarPedidoPorMesa(mesaNum);

        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }

        double descuento = aplicarDescuentoFactura(pedido);

        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. Transferencia");

        int op = Integer.parseInt(teclado.nextLine());

        if (op < 1 || op > 3) {
            System.out.println("Método inválido.");
            return;
        }

        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setNumeroFactura("F" + (facturas.size() + 1));

        // 🔥 TODO SE CALCULA DENTRO DE FACTURA
        factura.cerrarFactura(descuento);

        Pago pago = factura.getPago();

        switch (op) {
            case 1 -> pago.registrarPagoEfectivo(factura.getTotal());
            case 2 -> pago.registrarPagoTarjeta(factura.getTotal());
            case 3 -> pago.registrarPagoTransferencia(factura.getTotal());
        }

        facturas.add(factura);

        pedido.cambiarEstado(EstadoPedido.PAGADO);
        pedido.getMesa().setEstado(EstadoMesa.LIBRE);

        System.out.println("Pago realizado. Factura generada.");
    }
    
    // ---------------- OPCIÓN 2  ----------------
    private void mostrarFacturas() {
        for (Factura f : facturas) {
            System.out.println(f);
        }
    }

    // ---------------- OPCIÓN 3 ----------------
    private void buscarFactura() {

        System.out.print("Ingrese cedula del cliente o numero de factura: ");
        String dato = teclado.nextLine();

        for (Factura f : facturas) {

            if (f.getNumeroFactura().equalsIgnoreCase(dato) ||
                (f.getPedido().getCliente().getCedula().equals(dato))) {

                System.out.println(f);
                return;
            }
        }

        System.out.println("Factura no encontrada.");
    }
}