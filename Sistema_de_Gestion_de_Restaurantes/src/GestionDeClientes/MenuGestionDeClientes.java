package GestionDeClientes;

import ExcepcionesPersonalizadas.ClienteDuplicadoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import java.util.ArrayList;
import java.util.Scanner;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;

public class MenuGestionDeClientes {

    // Lista de clientes registrados en el sistema
    private ArrayList<Cliente> clientes;

    // Lista de mesas disponibles del restaurante
    private ArrayList<Mesa> mesas;

    // Scanner para entrada de datos por consola
    private Scanner sc;

    // Constructor: recibe las listas existentes de clientes y mesas
    public MenuGestionDeClientes(ArrayList<Cliente> clientes, ArrayList<Mesa> mesas) {
        this.clientes = clientes;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    // Inicia el menú principal de gestión de clientes
    public void iniciarMenu() {

        int opcion;

        do {
            // Menú de opciones
            System.out.println("\n===== GESTION DE CLIENTES =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Sentar Cliente");
            System.out.println("3. Listar Cliente");
            System.out.println("4. Buscar cliente por cedula");
            System.out.println("5. Verificar descuento disponible");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            // Ejecuta la opción seleccionada
            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    sentarCliente();
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    buscarClientePorcedula();
                    break;
                case 5:
                    verificarDescuentoCliente();
                    break;
                case 6:
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 6);
    }

    // Lee un número entero de forma segura (evita errores de formato)
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Verifica si ya existe un cliente con la misma cédula, teléfono o correo
    private boolean clienteExiste(String cedula, String telefono, String correo) {

        for (Cliente c : clientes) {

            if (c.getCedula().equals(cedula)){
                throw new ClienteDuplicadoException("Ya existe un cliente registrado con este numero de cedula");
            };
            if (c.getTelefono().equals(telefono)){
                throw new ClienteDuplicadoException("Ya existe un cliente registrado con este numero de telefono");
            };
            if (c.getCorreoElectronico().equalsIgnoreCase(correo)){
                throw new ClienteDuplicadoException("Ya exiiste un cliente registrado con esta direccion de correo electronico");
            };
        }

        return false;
    }

    
    // ---------------- OPCIÓN 1: REGISTRAR CLIENTE ----------------
    private void registrarCliente() {

        Cliente c = new Cliente();

        try {
            // Captura de datos del cliente
            System.out.print("Cedula: ");
            c.setcedula(sc.nextLine().trim());

            System.out.print("Nombre: ");
            c.setNombre(sc.nextLine().trim());

            System.out.print("Telefono: ");
            c.setTelefono(sc.nextLine().trim());

            System.out.print("Correo: ");
            c.setCorreoElectronico(sc.nextLine().trim());

        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación en setters
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos " + "\n" + e.getMessage());
            return;
        }

        // Validación de duplicados
        try{
            if(clienteExiste(c.getCedula(), c.getTelefono(), c.getCorreoElectronico()));
        } catch(ClienteDuplicadoException e){
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe un cliente registrado" + "\n" + e.getMessage());
            return;
        }

        // Agrega cliente a la lista
        clientes.add(c);
        System.out.println("Cliente registrado correctamente.");
    }

    // ---------------- OPCIÓN 2: SENTAR CLIENTE EN MESA ----------------
    private void sentarCliente() {

        System.out.print("Cedula del cliente: ");
        String cedula = sc.nextLine().trim();

        Cliente cliente = null;

        // Buscar cliente por cédula
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                cliente = c;
                break;
            }
        }

        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Numero de mesa: ");
        int numeroMesa = leerEntero();

        Mesa mesa = null;

        // Buscar mesa por número
        for (Mesa m : mesas) {
            if (m.getNumero() == numeroMesa) {
                mesa = m;
                break;
            }
        }

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        // Verificar disponibilidad de la mesa
        if (mesa.getEstado() == EstadoMesa.OCUPADA) {
            System.out.println("La mesa ya está ocupada.");
            return;
        }

        System.out.print("Cantidad de personas: ");
        int personas = leerEntero();

        // Validar capacidad de la mesa
        if (personas <= 0 || personas > mesa.getCapacidad()) {
            System.out.println("Cantidad inválida para la capacidad de la mesa.");
            return;
        }

        // Asignar cliente a la mesa
        mesa.setEstado(EstadoMesa.OCUPADA);
        mesa.setPersonasOcupando(personas);
        mesa.setClienteActual(cliente);

        // Incrementar visitas del cliente automáticamente
        cliente.incrementarVisitas();

        System.out.println("Cliente sentado correctamente en la mesa.");
    }

    // ---------------- OPCIÓN 3: LISTAR CLIENTES ----------------
    private void listarClientes() {

        System.out.println("\n--- LISTA DE CLIENTES ---");

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        // Mostrar todos los clientes
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    // ---------------- OPCIÓN 4: BUSCAR CLIENTE POR CÉDULA ----------------
    private void buscarClientePorcedula() {

        System.out.print("Ingrese cedula: ");
        String cedula = sc.nextLine().trim();

        // Validación básica de formato de cédula
        if (!cedula.matches("\\d{10}")) {
            System.out.println("La cedula ingresada debe contener 10 digitos numericos.");
            return;
        }

        // Buscar cliente
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                System.out.println("Cliente encontrado:");
                System.out.println(c);
                return;
            }
        }

        System.out.println("Cliente no encontrado.");
    }

    // ---------------- OPCIÓN 5: VERIFICAR DESCUENTO ----------------
    private void verificarDescuentoCliente() {

        System.out.print("Cedula del cliente: ");
        String cedula = sc.nextLine();

        // Buscar cliente
        for (Cliente c : clientes) {
            if (c.getCedula().equalsIgnoreCase(cedula)) {

                // Verificar si tiene descuentos disponibles
                if (c.tieneDescuentoDisponible()) {
                    System.out.println("El cliente tiene descuento disponible.");
                } else {
                    System.out.println("El cliente no tiene descuentos.");
                }
                return;
            }
        }

        System.out.println("Cliente no encontrado.");
    }
}