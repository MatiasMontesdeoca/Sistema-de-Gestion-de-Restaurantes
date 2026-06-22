package GestionDePedidos;

import GestionDelMenu.Plato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDePedidos {

    // Lista de pedidos registrados en el sistema
    private ArrayList<Pedido> pedidos;

    // Lista de clientes del sistema
    private ArrayList<Cliente> clientes;

    // Lista de meseros disponibles
    private ArrayList<Mesero> meseros;

    // Lista de mesas del restaurante
    private ArrayList<Mesa> mesas;

    // Lista de platos del menú
    private ArrayList<Plato> platos;

    // Scanner para entrada de datos por consola
    private Scanner sc;

    // Contador para generar IDs de pedidos
    private int contadorPedidos = 1;

    // Constructor: recibe todas las estructuras del sistema
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
        this.sc = new Scanner(System.in);
    }

    // Inicia el menú principal de gestión de pedidos
    public void iniciarMenu() {

        int opcion;

        do {

            // Menú de opciones
            System.out.println("\n===== GESTION DE PEDIDOS =====");
            System.out.println("1. Registrar pedido");
            System.out.println("2. Editar pedido");
            System.out.println("3. Mostrar pedidos activos");
            System.out.println("4. Buscar pedido por mesa");
            System.out.println("5. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

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
                    System.out.println("Regresando al menu principal...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 5);
    }

    // Lee un entero desde consola con validación
    private int leerEntero() {

        while (true) {

            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }
        }
    }

    // Busca un pedido activo asociado a una mesa específica
    private Pedido buscarPedidoPorMesa(Mesa mesa) {

        for (Pedido p : pedidos) {
            if (p.getMesa().equals(mesa)) {
                return p;
            }
        }

        return null;
    }

    // Busca una mesa por número ingresado por el usuario
    private Mesa buscarMesa() {

        System.out.print("Numero de mesa: ");
        int numero = Integer.parseInt(sc.nextLine());

        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }

        return null;
    }

    // Agrega un plato al pedido seleccionado
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
        int cantidad = Integer.parseInt(sc.nextLine());

        pedido.agregarPlato(plato, cantidad);
    }

    // Busca un plato por nombre dentro del menú
    private Plato buscarPlato() {

        System.out.print("Nombre del plato: ");
        String nombre = sc.nextLine();

        for (Plato plato : platos) {

            if (plato.getNombre().equalsIgnoreCase(nombre)) {
                return plato;
            }
        }

        return null;
    }

    // Elimina un plato del pedido
    private void eliminarPlato(Pedido pedido) {

        Plato plato = buscarPlato();

        if (plato == null) {
            System.out.println("Plato no encontrado.");
            return;
        }

        pedido.eliminarPlato(plato);

        System.out.println("Plato eliminado del pedido.");
    }

    // Cambia el estado del pedido (PENDIENTE, PREPARANDO, LISTO, etc.)
    private void cambiarEstado(Pedido pedido) {

        System.out.println("Estados disponibles:");

        for (EstadoPedido e : EstadoPedido.values()) {
            System.out.println("- " + e);
        }

        System.out.print("Nuevo estado: ");
        String estadoStr = sc.nextLine();

        try {

            EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoStr.toUpperCase());

            // Valida si la transición de estado es válida antes de cambiarlo
            pedido.validarTransicionEstado(nuevoEstado);
            pedido.cambiarEstado(nuevoEstado);

            System.out.println("Estado actualizado.");

        } catch (Exception e) {
            System.out.println("Estado invalido.");
        }
    }

    // ---------------- OPCIÓN 1: REGISTRAR PEDIDO ----------------
    private void registrarPedido() {

        Mesa mesa = buscarMesa();

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        // Solo se pueden crear pedidos en mesas ocupadas
        if (mesa.getEstado() != EstadoMesa.OCUPADA) {
            System.out.println("La mesa no esta ocupada.");
            return;
        }

        // Evita duplicar pedidos en la misma mesa
        Pedido existente = buscarPedidoPorMesa(mesa);

        if (existente != null) {
            System.out.println("Ya existe un pedido activo para esta mesa.");
            return;
        }

        Pedido pedido = new Pedido();

        // Genera número de pedido único
        pedido.setNumeroPedido("P" + String.valueOf(contadorPedidos++));

        pedido.setMesa(mesa);

        // Asocia cliente actualmente sentado en la mesa
        pedido.setCliente(mesa.getClienteActual());

        pedidos.add(pedido);

        System.out.println("Pedido creado: " + pedido.getNumeroPedido());
    }

    // ---------------- OPCIÓN 2: EDITAR PEDIDO ----------------
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

            int op;

            do {

                // Submenú de edición del pedido
                System.out.println("\n===== EDITAR PEDIDO =====");
                System.out.println("1. Agregar plato");
                System.out.println("2. Eliminar plato");
                System.out.println("3. Cambiar estado");
                System.out.println("4. Ver pedido");
                System.out.println("5. Salir");
                System.out.print("Seleccione: ");

                op = leerEntero();

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
            System.out.println(e.getMessage());
        }
    }

    // ---------------- OPCIÓN 3: MOSTRAR PEDIDOS ACTIVOS ----------------
    private void mostrarPedidosActivos() {

        boolean hayPedidos = false;

        // Recorre todos los pedidos y filtra los activos
        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == EstadoPedido.PENDIENTE ||
                pedido.getEstado() == EstadoPedido.PREPARANDO ||
                pedido.getEstado() == EstadoPedido.LISTO ||
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

    // ---------------- OPCIÓN 4: BUSCAR PEDIDOS POR MESA ----------------
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