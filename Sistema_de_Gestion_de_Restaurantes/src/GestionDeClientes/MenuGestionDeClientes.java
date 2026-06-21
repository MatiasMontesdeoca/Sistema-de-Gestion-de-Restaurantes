package GestionDeClientes;
import java.util.ArrayList;
import java.util.Scanner;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;

public class MenuGestionDeClientes {
    //Atributos
    private ArrayList<Cliente> clientes;
    private ArrayList<Mesa> mesas;
    private Scanner sc;

    //Constructor
    public MenuGestionDeClientes(ArrayList<Cliente> clientes, ArrayList<Mesa> mesas) {
        this.clientes = clientes;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    //Inicio de Menu
    public void iniciarMenu() {
        
        int opcion;

        do {
            //Interfaz del menu
            System.out.println("\n===== GESTION DE CLIENTES =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Sentar Cliente");
            System.out.println("3. Listar Cliente");
            System.out.println("4. Buscar cliente por cedula");
            System.out.println("5. Verificar descuento disponible");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

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
    
    private boolean clienteExiste(String cedula, String telefono, String correo) {

    for (Cliente c : clientes) {

        if (c.getCedula().equals(cedula)) return true;
        if (c.getTelefono().equals(telefono)) return true;
        if (c.getCorreoElectronico().equalsIgnoreCase(correo)) return true;
    }

    return false;
    }


    // ---------------- OPCIÓN 1 ----------------
    private void registrarCliente() {

        Cliente c = new Cliente();

        try {
            System.out.print("Cedula: ");
            c.setcedula(sc.nextLine().trim());

            System.out.print("Nombre: ");
            c.setNombre(sc.nextLine().trim());

            System.out.print("Telefono: ");
            c.setTelefono(sc.nextLine().trim());

            System.out.print("Correo: ");
            c.setCorreoElectronico(sc.nextLine().trim());

        } catch (IllegalArgumentException e) {
            System.out.println("Error de datos: " + e.getMessage());
            return;
        }

        if (clienteExiste(c.getCedula(), c.getTelefono(), c.getCorreoElectronico())) {
            System.out.println("Error: ya existe un cliente con esa cedula, telefono o correo.");
            return;
        }

        clientes.add(c);
        System.out.println("Cliente registrado correctamente.");
    }
 
    // ---------------- OPCIÓN 2 ----------------
    private void sentarCliente() {

        System.out.print("Cedula del cliente: ");
        String cedula = sc.nextLine().trim();

        Cliente cliente = null;

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
        int numeroMesa = Integer.parseInt(sc.nextLine());

        Mesa mesa = null;

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

        if (mesa.getEstado() == EstadoMesa.OCUPADA) {
            System.out.println("La mesa ya está ocupada.");
            return;
        }

        System.out.print("Cantidad de personas: ");
        int personas = Integer.parseInt(sc.nextLine());

        if (personas <= 0 || personas > mesa.getCapacidad()) {
            System.out.println("Cantidad inválida para la capacidad de la mesa.");
            return;
        }

        // CAMBIO DE ESTADO DE MESA
        mesa.setEstado(EstadoMesa.OCUPADA);
        mesa.setPersonasOcupando(personas);
        mesa.setClienteActual(cliente);

        // incrementar visitas automáticamente
        cliente.incrementarVisitas();

        System.out.println("Cliente sentado correctamente en la mesa.");
    }
 
    
    // ---------------- OPCIÓN 3 ----------------
    private void listarClientes() {

        System.out.println("\n--- LISTA DE CLIENTES ---");

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    // ---------------- OPCIÓN 4 ----------------
    private void buscarClientePorcedula() {

        System.out.print("Ingrese cedula: ");
        String cedula = sc.nextLine().trim();

         if (!cedula.matches("\\d{10}")) {
        System.out.println("La cedula ingresada debe contener 10 digitos numericos.");
        return;}
        
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                System.out.println("Cliente encontrado:");
                System.out.println(c);
                return;
            }
        }

        System.out.println("Cliente no encontrado.");
    }

    // ---------------- OPCIÓN 5 ----------------
    private void verificarDescuentoCliente() {

        System.out.print("Cedula del cliente: ");
        String cedula = sc.nextLine();

        for (Cliente c : clientes) {
            if (c.getCedula().equalsIgnoreCase(cedula)) {

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
