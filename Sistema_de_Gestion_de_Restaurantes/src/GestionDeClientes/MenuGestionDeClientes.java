package GestionDeClientes;

import ExcepcionesPersonalizadas.ClienteDuplicadoException;
import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.NoHayMesasRegistradasException;
import java.util.ArrayList;
import java.util.Scanner;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;
import Serializacion.ArchivoDatos;

public class MenuGestionDeClientes {

    private ArrayList<Cliente> clientes;
    private ArrayList<Mesa> mesas;
    private Scanner sc;

    // Constructor que inicializa el menú de gestión de clientes con sus dependencias
    public MenuGestionDeClientes(ArrayList<Cliente> clientes, ArrayList<Mesa> mesas) {
        this.clientes = clientes;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    // Despliega e interactúa con el menú principal del módulo de clientes
    public void iniciarMenu() {
        int opcion;
        do {
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
                case 1 -> registrarCliente();
                case 2 -> sentarCliente();
                case 3 -> listarClientes();
                case 4 -> buscarClientePorcedula();
                case 5 -> verificarDescuentoCliente();
                case 6 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 6);
    }

    // Lee un entero desde la consola manejando posibles errores de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Comprueba la existencia de duplicados en cédula, teléfono o correo
    private boolean clienteExiste(String cedula, String telefono, String correo) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                throw new ClienteDuplicadoException("Ya existe un cliente registrado con este numero de cedula");
            }
            if (c.getTelefono().equals(telefono)) {
                throw new ClienteDuplicadoException("Ya existe un cliente registrado con este numero de telefono");
            }
            if (c.getCorreoElectronico().equalsIgnoreCase(correo)) {
                throw new ClienteDuplicadoException("Ya exiiste un cliente registrado con esta direccion de correo electronico");
            }
        }
        return false;
    }

    // Registra un nuevo cliente validando sus datos y guardándolo en el sistema
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar cliente
    ////////////////////////////////////////////////////
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
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos " + "\n" + e.getMessage());
            return;
        }
        try {
            clienteExiste(c.getCedula(), c.getTelefono(), c.getCorreoElectronico());
        } catch (ClienteDuplicadoException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe un cliente registrado" + "\n" + e.getMessage());
            return;
        }
        clientes.add(c);
        ArchivoDatos.guardar(clientes, "clientes.dat");
        System.out.println("Cliente registrado correctamente.");
    }

    // Asigna un cliente a una mesa libre del restaurante
    ////////////////////////////////////////////////////
    // Opcion #2: Sentar Cliente
    ////////////////////////////////////////////////////
    private void sentarCliente() {
        if (mesas == null || mesas.isEmpty()) {
            try {
                throw new NoHayMesasRegistradasException();
            } catch (NoHayMesasRegistradasException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
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
            try {
                throw new ElementoNoEncontradoException("Cliente con cedula " + cedula + " no fue encontrado.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        System.out.print("Numero de mesa: ");
        int numeroMesa = leerEntero();
        Mesa mesa = null;
        for (Mesa m : mesas) {
            if (m.getNumero() == numeroMesa) {
                mesa = m;
                break;
            }
        }
        if (mesa == null) {
            try {
                throw new ElementoNoEncontradoException("Mesa #" + numeroMesa + " no fue encontrada.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        if (mesa.getEstado() == EstadoMesa.OCUPADA) {
            System.out.println("La mesa ya está ocupada.");
            return;
        }
        System.out.print("Cantidad de personas: ");
        int personas = leerEntero();
        if (personas <= 0 || personas > mesa.getCapacidad()) {
            System.out.println("Cantidad inválida para la capacidad de la mesa.");
            return;
        }
        mesa.setEstado(EstadoMesa.OCUPADA);
        mesa.setPersonasOcupando(personas);
        mesa.setClienteActual(cliente);
        ArchivoDatos.guardar(mesas, "mesas.dat");
        cliente.incrementarVisitas();
        ArchivoDatos.guardar(clientes, "clientes.dat");
        System.out.println("Cliente sentado correctamente en la mesa.");
    }

    // Muestra por consola el listado de todos los clientes registrados
    ////////////////////////////////////////////////////
    // Opcion #3: Listar Cliente
    ////////////////////////////////////////////////////
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

    // Busca y muestra la información de un cliente a través de su número de cédula
    ////////////////////////////////////////////////////
    // Opcion #4: Buscar cliente por cedula
    ////////////////////////////////////////////////////
    private void buscarClientePorcedula() {
        System.out.print("Ingrese cedula: ");
        String cedula = sc.nextLine().trim();
        if (!cedula.matches("\\d{10}")) {
            System.out.println("La cedula ingresada debe contener 10 digitos numericos.");
            return;
        }
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                System.out.println("Cliente encontrado:");
                System.out.println(c);
                return;
            }
        }
        try {
            throw new ElementoNoEncontradoException("Cliente con cedula " + cedula + " no fue encontrado.");
        } catch (ElementoNoEncontradoException e) {
            MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
        }
    }

    // Verifica si un cliente dispone de descuentos por fidelidad
    ////////////////////////////////////////////////////
    // Opcion #5: Verificar descuento disponible
    ////////////////////////////////////////////////////
    private void verificarDescuentoCliente() {
        System.out.print("Cedula del cliente: ");
        String cedula = sc.nextLine().trim();
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
        try {
            throw new ElementoNoEncontradoException("Cliente con cedula " + cedula + " no fue encontrado.");
        } catch (ElementoNoEncontradoException e) {
            MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
        }
    }
}