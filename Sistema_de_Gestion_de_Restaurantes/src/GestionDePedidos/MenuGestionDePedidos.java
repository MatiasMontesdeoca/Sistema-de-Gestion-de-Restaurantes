package GestionDePedidos;

import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.NoHayMesasRegistradasException;
import GestionDelMenu.Plato;
import GestionDelMenu.CategoriaPlato;
import GestionDeMesasYReservas.Mesa;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMeseros.Mesero;
import GestionDeClientes.Cliente;
import Serializacion.ArchivoDatos;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDePedidos {

    private ArrayList<Pedido> pedidos;
    private ArrayList<Cliente> clientes;
    private ArrayList<Mesero> meseros;
    private ArrayList<Mesa> mesas;
    private ArrayList<Plato> platos;
    private Scanner sc;
    private int contadorPedidos = 1;

    // Constructor que inicializa las referencias del módulo y autoincrementa el contador de pedidos
    public MenuGestionDePedidos(ArrayList<Pedido> pedidos, ArrayList<Cliente> clientes, ArrayList<Mesero> meseros, ArrayList<Mesa> mesas, ArrayList<Plato> platos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.meseros = meseros;
        this.mesas = mesas;
        this.platos = platos;
        this.sc = new Scanner(System.in);
        this.contadorPedidos = 1;
        if (pedidos != null) {
            for (Pedido p : pedidos) {
                if (p != null && p.getNumeroPedido() != null && p.getNumeroPedido().startsWith("P")) {
                    try {
                        int num = Integer.parseInt(p.getNumeroPedido().substring(1));
                        if (num >= this.contadorPedidos) {
                            this.contadorPedidos = num + 1;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
    }

    // Inicia y controla la navegación del menú de pedidos
    public void iniciarMenu() {
        int opcion;
        do {
            System.out.println("\n===== GESTION DE PEDIDOS =====");
            System.out.println("1. Registrar pedido");
            System.out.println("2. Editar pedido");
            System.out.println("3. Mostrar pedidos activos");
            System.out.println("4. Buscar pedido por mesa");
            System.out.println("5. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();
            switch (opcion) {
                case 1 -> registrarPedido();
                case 2 -> editarPedido();
                case 3 -> mostrarPedidosActivos();
                case 4 -> mostrarPedidosPorMesa();
                case 5 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 5);
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

    // Busca un pedido activo asociado a una mesa específica
    private Pedido buscarPedidoPorMesa(Mesa mesa) {
        for (Pedido p : pedidos) {
            if (p.getMesa().getNumero() == mesa.getNumero() && p.getEstado() != EstadoPedido.PAGADO && p.getEstado() != EstadoPedido.CANCELADO) {
                return p;
            }
        }
        return null;
    }

    // Solicita el número de mesa por consola y la busca en el listado
    private Mesa buscarMesa() {
        System.out.print("Numero de mesa: ");
        int numero = leerEntero();
        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }
        return null;
    }

    // Solicita la cantidad e incorpora el plato seleccionado al pedido
    private void agregarPlato(Pedido pedido) {
        Plato plato = seleccionarPlatoDelMenu();
        if (plato == null) {
            return;
        }
        if (!plato.getDisponibilidad()) {
            System.out.println("El plato seleccionado no está disponible.");
            return;
        }
        System.out.print("Cantidad: ");
        int cantidad = leerEntero();
        if (cantidad <= 0) {
            System.out.println("Cantidad invalida.");
            return;
        }
        pedido.agregarPlato(plato, cantidad);
        ArchivoDatos.guardar(pedidos, "pedidos.dat");
        System.out.println("Plato agregado correctamente.");
    }

    // Despliega el menú de platos numéricamente por categorías para seleccionar uno
    private Plato seleccionarPlatoDelMenu() {
        if (platos == null || platos.isEmpty()) {
            System.out.println("No hay platos registrados en el menú.");
            return null;
        }
        ArrayList<Plato> listaOpciones = new ArrayList<>();
        CategoriaPlato[] categorias = {
            CategoriaPlato.ENTRADA,
            CategoriaPlato.PLATO_FUERTE,
            CategoriaPlato.POSTRE,
            CategoriaPlato.BEBIDA
        };
        String[] nombresCategoria = {
            "Entradas",
            "Plato Fuerte",
            "Postre",
            "Bebida"
        };
        System.out.println("\n===== SELECCION DE PLATOS =====");
        for (int c = 0; c < categorias.length; c++) {
            CategoriaPlato cat = categorias[c];
            boolean tituloMostrado = false;
            for (Plato p : platos) {
                if (p.getCategoria() == cat) {
                    if (!tituloMostrado) {
                        System.out.println("\n" + nombresCategoria[c] + "-");
                        tituloMostrado = true;
                    }
                    listaOpciones.add(p);
                    int numero = listaOpciones.size();
                    String estado = p.getDisponibilidad() ? "Disponible" : "No disponible";
                    System.out.printf("%d.- %s - $%.2f (%s)%n", numero, p.getNombre(), p.getPrecio(), estado);
                }
            }
        }
        if (listaOpciones.isEmpty()) {
            System.out.println("No hay platos en el menú.");
            return null;
        }
        System.out.print("\nSeleccione el numero del plato (0 para cancelar): ");
        int opcion = leerEntero();
        if (opcion == 0) {
            return null;
        }
        if (opcion < 1 || opcion > listaOpciones.size()) {
            System.out.println("Opcion invalida.");
            return null;
        }
        return listaOpciones.get(opcion - 1);
    }

    // Permite eliminar un plato del pedido seleccionándolo numéricamente
    private void eliminarPlato(Pedido pedido) {
        ArrayList<DetallePedido> detalles = pedido.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("El pedido no contiene platos.");
            return;
        }
        System.out.println("\n--- PLATOS EN EL PEDIDO ---");
        for (int i = 0; i < detalles.size(); i++) {
            DetallePedido d = detalles.get(i);
            System.out.printf("%d.- %s (Cantidad: %d)%n", (i + 1), d.getPlato().getNombre(), d.getCantidad());
        }
        System.out.print("\nSeleccione el numero del plato a eliminar (0 para cancelar): ");
        int opcion = leerEntero();
        if (opcion == 0) {
            return;
        }
        if (opcion < 1 || opcion > detalles.size()) {
            System.out.println("Opcion invalida.");
            return;
        }
        Plato platoAEliminar = detalles.get(opcion - 1).getPlato();
        pedido.eliminarPlato(platoAEliminar);
        ArchivoDatos.guardar(pedidos, "pedidos.dat");
        System.out.println("Plato eliminado del pedido.");
    }

    // Modifica el estado del pedido actual en el sistema
    private void cambiarEstado(Pedido pedido) {
        System.out.println("Estados disponibles:");
        for (EstadoPedido e : EstadoPedido.values()) {
            System.out.println("- " + e);
        }
        System.out.print("Nuevo estado: ");
        String estadoStr = sc.nextLine();
        try {
            EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoStr.toUpperCase());
            pedido.validarTransicionEstado(nuevoEstado);
            pedido.cambiarEstado(nuevoEstado);
            ArchivoDatos.guardar(pedidos, "pedidos.dat");
            System.out.println("Estado actualizado.");
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        } catch (IllegalStateException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar un estado valido para el pedido (Pendiente-Preparando-Listo-Servido-Pagado-Cancelado\n" + e.getMessage());
            return;
        }
    }

    // Registra un nuevo pedido en una mesa ocupada asignándole un ID único
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar pedido
    ////////////////////////////////////////////////////
    private void registrarPedido() {
        if (mesas == null || mesas.isEmpty()) {
            try {
                throw new NoHayMesasRegistradasException();
            } catch (NoHayMesasRegistradasException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        Mesa mesa = buscarMesa();
        if (mesa == null) {
            try {
                throw new ElementoNoEncontradoException("La mesa especificada no fue encontrada.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
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
        pedido.setNumeroPedido("P" + String.valueOf(contadorPedidos++));
        pedido.setMesa(mesa);
        pedido.setCliente(mesa.getClienteActual());
        pedidos.add(pedido);
        ArchivoDatos.guardar(pedidos, "pedidos.dat");
        System.out.println("Pedido creado: " + pedido.getNumeroPedido());
    }

    // Permite agregar, eliminar platos o modificar el estado de un pedido existente
    ////////////////////////////////////////////////////
    // Opcion #2: Editar pedido
    ////////////////////////////////////////////////////
    private void editarPedido() {
        try {
            Mesa mesa = buscarMesa();
            if (mesa == null) {
                try {
                    throw new ElementoNoEncontradoException("La mesa especificada no fue encontrada.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            Pedido pedido = buscarPedidoPorMesa(mesa);
            if (pedido == null) {
                try {
                    throw new ElementoNoEncontradoException("No existe un pedido activo para la mesa #" + mesa.getNumero() + ".");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
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
                op = leerEntero();
                switch (op) {
                    case 1 -> agregarPlato(pedido);
                    case 2 -> eliminarPlato(pedido);
                    case 3 -> cambiarEstado(pedido);
                    case 4 -> System.out.println(pedido);
                }
            } while (op != 5);
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        }
    }

    // Muestra por consola todos los pedidos que no han sido finalizados o cancelados
    ////////////////////////////////////////////////////
    // Opcion #3: Mostrar pedidos activos
    ////////////////////////////////////////////////////
    private void mostrarPedidosActivos() {
        boolean hayPedidos = false;
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.PENDIENTE ||
                pedido.getEstado() == EstadoPedido.PREPARANDO ||
                pedido.getEstado() == EstadoPedido.LISTO ||
                pedido.getEstado() == EstadoPedido.SERVIDO) {
                hayPedidos = true;
                System.out.println("\nPedido: " + pedido.getNumeroPedido());
                System.out.println("Mesa: " + pedido.getMesa().getNumero());
                System.out.println("Cliente: " + (pedido.getCliente() != null ? pedido.getCliente().getCedula() : "N/A"));
                System.out.println("Total: $" + pedido.calcularTotalSinDescuento());
            }
        }
        if (!hayPedidos) {
            System.out.println("No hay pedidos activos.");
        }
    }

    // Muestra la lista de pedidos asociados a una mesa en particular
    ////////////////////////////////////////////////////
    // Opcion #4: Buscar pedido por mesa
    ////////////////////////////////////////////////////
    private void mostrarPedidosPorMesa() {
        Mesa mesa = buscarMesa();
        if (mesa == null) {
            try {
                throw new ElementoNoEncontradoException("La mesa especificada no fue encontrada.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
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
            try {
                throw new ElementoNoEncontradoException("No existen pedidos registrados para la mesa #" + mesa.getNumero() + ".");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
            }
        }
    }
}