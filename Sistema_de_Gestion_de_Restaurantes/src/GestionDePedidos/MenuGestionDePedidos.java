package GestionDePedidos;
import GestionDePedidos.EstadoPedido;
import GestionDelMenu.Plato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import java.util.ArrayList;
import java.util.Scanner;
public class MenuGestionDePedidos {
    private ArrayList<Pedido> pedidos;
    private ArrayList<Cliente> clientes;
    private ArrayList<Mesero> meseros;
    private ArrayList<Mesa> mesas;
    private ArrayList<Plato> platos;
    private Scanner teclado;
    private int contadorPedidos = 1;

    public MenuGestionDePedidos(ArrayList<Pedido> pedidos,
                                ArrayList<Cliente> clientes,
                                ArrayList<Mesero> meseros,
                                ArrayList<Mesa> mesas,
                                ArrayList<Plato> platos) {

        this.pedidos = pedidos;
        this.clientes = clientes;
        this.meseros = meseros;
        this.mesas = mesas;
        this.platos = platos;
        teclado = new Scanner(System.in);
    }

    public void iniciarMenu() {

        int opcion;

        do {

            System.out.println("\n==============================");
            System.out.println(" GESTION DE PEDIDOS");
            System.out.println("==============================");
            System.out.println("1. Registrar pedido");
            System.out.println("2. Editar pedido");
            System.out.println("3. Mostrar pedidos activos");
            System.out.println("4. Buscar pedido por mesa");
            System.out.println("5. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = Integer.parseInt(teclado.nextLine());

            switch (opcion) {

                case 1:
                    registrarPedido();
                    break;

                case 2:
                    editarPedido();
                    break;

                case 3:
                    mostrarPedidosActivos();
                    break;

                case 4:
                    mostrarPedidosPorMesa();
                    break;

                case 5:
                    System.out.println("Regresando...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 5);

    }
    
    private Pedido buscarPedidoPorMesa(Mesa mesa) {

    for (Pedido p : pedidos) {
        if (p.getMesa().equals(mesa)) {
            return p;
        }
    }

    return null;}
    
    private Mesa buscarMesa() {

        System.out.print("Numero de mesa: ");
        int numero = Integer.parseInt(teclado.nextLine());

        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }

        return null;
    }
    
    private void agregarPlato(Pedido pedido) {

        Plato plato = buscarPlato();

        if (plato == null) {
            System.out.println("Plato no encontrado.");
            return;
        }

        if (!plato.getDisponibilidad()) {
            System.out.println("El plato no está disponible.");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = Integer.parseInt(teclado.nextLine());

        pedido.agregarPlato(plato, cantidad);
    }
    
    private Plato buscarPlato() {

    System.out.print("Nombre del plato: ");
    String nombre = teclado.nextLine();

    for (Plato plato : platos) {

        if (plato.getNombre().equalsIgnoreCase(nombre)) {
            return plato;
        }
    }

    return null;}
    
    private void eliminarPlato(Pedido pedido) {

    Plato plato = buscarPlato();

    if (plato == null) {
        System.out.println("Plato no encontrado.");
        return;
    }

    pedido.eliminarPlato(plato);

    System.out.println("Plato eliminado del pedido.");}
    
    private void cambiarEstado(Pedido pedido) {

    System.out.println("Estados disponibles:");

    for (EstadoPedido e : EstadoPedido.values()) {
        System.out.println("- " + e);
    }

    System.out.print("Nuevo estado: ");
    String estadoStr = teclado.nextLine();

    try {
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoStr.toUpperCase());

        pedido.validarTransicionEstado(nuevoEstado);
        pedido.cambiarEstado(nuevoEstado);

        System.out.println("Estado actualizado.");
    } catch (Exception e) {
        System.out.println("Estado invalido.");}
    }
    
    private void registrarPedido() {

        Mesa mesa = buscarMesa();

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        if (mesa.getEstado() != EstadoMesa.OCUPADA) {
            System.out.println("La mesa no esta ocupada.");
            return;
        }

        Pedido existente = buscarPedidoPorMesa(mesa);

        if (existente != null) {
            System.out.println("Ya existe un pedido activo para esta mesa.");
            return;
        }

        Pedido pedido = new Pedido();

        pedido.setNumeroPedido("P"+String.valueOf(contadorPedidos++));

        pedido.setMesa(mesa);
        
        pedido.setCliente(mesa.getClienteActual());

        pedidos.add(pedido);
        
        System.out.println("Pedido creado: " + pedido.getNumeroPedido());
    }

    private void editarPedido() {

    try {
        
        Mesa mesa = buscarMesa();

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        Pedido pedido = buscarPedidoPorMesa(mesa);

        if (pedido == null) {
            System.out.println("No hay pedido para esa mesa.");
            return;
        }

        if (pedido == null) {
            System.out.println("No hay pedido para esa mesa.");
            return;
        }

        int op;

        do {

            System.out.println("\n===== EDITAR PEDIDO =====");
            System.out.println("1. Agregar plato");
            System.out.println("2. Eliminar plato");
            System.out.println("3. Cambiar estado");
            System.out.println("4. Ver pedido");
            System.out.println("5. Salir");
            System.out.print("Seleccione: ");

            op = Integer.parseInt(teclado.nextLine());

            switch (op) {

                case 1:
                    agregarPlato(pedido);
                    break;

                case 2:
                    eliminarPlato(pedido);
                    break;

                case 3:
                    cambiarEstado(pedido);
                    break;

                case 4:
                    System.out.println(pedido);
                    break;

            }

        } while (op != 5);

    } catch (Exception e) {
        System.out.println(e.getMessage());}
    }

    private void mostrarPedidosActivos() {

        boolean hayPedidos = false;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == EstadoPedido.PENDIENTE ||
                pedido.getEstado() == EstadoPedido.PREPARANDO ||
                pedido.getEstado() == EstadoPedido.PREPARADO ||
                pedido.getEstado() == EstadoPedido.SERVIDO) {

                hayPedidos = true;

                System.out.println("\nPedido: " + pedido.getNumeroPedido());
                System.out.println("Mesa: " + pedido.getMesa().getNumero());
                System.out.println("Cliente: " +
                        (pedido.getCliente() != null ? pedido.getCliente().getCedula() : "N/A"));

                System.out.println("Total: $" + pedido.calcularTotalSinDescuento());
            }
        }

        if (!hayPedidos) {
            System.out.println("No hay pedidos activos.");
        }
    }
    
    private void mostrarPedidosPorMesa() {

        Mesa mesa = buscarMesa();

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        boolean encontrado = false;

        for (Pedido pedido : pedidos) {

            if (pedido.getMesa().equals(mesa)) {

                encontrado = true;

                System.out.println("\nPedido: " + pedido.getNumeroPedido());
                System.out.println("Estado: " + pedido.getEstado());
                System.out.println("Total: $" + pedido.calcularTotalSinDescuento());

                if (pedido.getCliente() != null) {
                    System.out.println("Cliente: " + pedido.getCliente().getCedula());
                }
            }
        }

        if (!encontrado) {
            System.out.println("No hay pedidos para esa mesa.");
        }
    }
}