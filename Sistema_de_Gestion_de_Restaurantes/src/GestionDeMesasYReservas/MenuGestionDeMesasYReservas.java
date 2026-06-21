package GestionDeMesasYReservas;
import GestionDeMeseros.Mesero;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDeMesasYReservas {
    //Atributos
    private ArrayList<Mesa> mesas;
    private ArrayList<Mesero> meseros;
    private Scanner sc;

    //Constructor
    public MenuGestionDeMesasYReservas(ArrayList<Mesa> mesas, ArrayList<Mesero> meseros) {
        this.mesas = mesas;
        this.meseros = meseros;
        this.sc = new Scanner(System.in);
    }

    //Inicio de Menu
    public void iniciarMenu() {

        int opcion;

        do {
            //Interfaz del menu
            System.out.println("\n===== GESTION DE MESAS Y RESERVAS =====");
            System.out.println("1. Registrar mesa");
            System.out.println("2. Mostrar mesas");
            System.out.println("3. Buscar mesa");
            System.out.println("4. Modificar mesa");
            System.out.println("5. Registrar reserva");
            System.out.println("6. Eliminar mesa");
            System.out.println("7. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();

            switch (opcion) {

                case 1:
                    registrarMesa();
                    break;

                case 2:
                    mostrarMesas();
                    break;

                case 3:
                    buscarMesa();
                    break;

                case 4:
                    modificarMesa();
                    break;

                case 5:
                    registrarReserva();
                    break;

                case 6:
                    eliminarMesa();
                    break;

                case 7:
                    System.out.println("Regresando al menu principal...");
                    break;

                default:
                    System.out.println("Opcion invalida.");

            }

        } while (opcion != 7);

    }
    
    //Métodos
    private Mesa buscarPorNumero(int numero) {

        for (Mesa mesa : mesas) {

            if (mesa.getNumero() == numero) {
                return mesa;
            }

        }

        return null;

    }

    private int leerEntero() {

        while (true) {

            try {

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.print("Ingrese un numero valido: ");

            }

        }

    }

    private LocalTime leerHora() {

        while (true) {

            try {

                return LocalTime.parse(sc.nextLine());

            } catch (Exception e) {

                System.out.print("Formato incorrecto. Ingrese la hora (HH:mm): ");

            }

        }

    }
    
    private Mesero buscarMeseroPorMesa(Mesa mesa) {

    for (Mesero m : meseros) {
        if (m.getMesasAsignadas().contains(mesa)) {
            return m;
        }
    }

    return null;}

    // ---------------- OPCIÓN 1 ----------------
    private void registrarMesa() {

        try {

            Mesa mesa = new Mesa();

            System.out.print("Numero de mesa: ");
            mesa.setNumero(leerEntero());

            if (buscarPorNumero(mesa.getNumero()) != null) {
                System.out.println("Ya existe una mesa con ese numero.");
                return;
            }

            // Solo pedir capacidad si la mesa es nueva
            if (mesa.getNumero() > 10) {
                System.out.print("Capacidad: ");
                mesa.setCapacidad(leerEntero());
            } else {
                System.out.println("La mesa " + mesa.getNumero()
                        + " tiene una capacidad predeterminada de "
                        + mesa.getCapacidad() + " personas.");
            }

            mesas.add(mesa);

            System.out.println("Mesa registrada correctamente.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    // ---------------- OPCIÓN 2 ----------------
    private void mostrarMesas() {

        if (mesas.isEmpty()) {

            System.out.println("No existen mesas registradas.");
            return;

        }

        for (Mesa mesa : mesas) {

            System.out.println(mesa);

            if (mesa.getReservaActiva() != null) {
                System.out.println("   Reserva -> "
                        + mesa.getReservaActiva());
            }

        }

    }

    // ---------------- OPCIÓN 3 ----------------
    private void buscarMesa() {

        System.out.print("Numero de mesa: ");

        int numero = leerEntero();

        Mesa mesa = buscarPorNumero(numero);

        if (mesa == null) {

            System.out.println("Mesa no encontrada.");

        } else {

            System.out.println(mesa);

            if (mesa.getReservaActiva() != null) {
                System.out.println(mesa.getReservaActiva());
            }

        }

    }
    
    // ---------------- OPCIÓN 4 ----------------
    private void modificarMesa() {

        try {

            System.out.print("Numero de mesa: ");
            int numero = leerEntero();

            Mesa mesa = buscarPorNumero(numero);

            if (mesa == null) {
                System.out.println("Mesa no encontrada.");
                return;
            }

            System.out.println("\n1. Cambiar capacidad");
            System.out.println("2. Cambiar estado a LIBRE (liberar mesa)");
            System.out.print("Seleccione una opcion: ");

            int op = leerEntero();

            switch (op) {

                case 1:
                    System.out.print("Nueva capacidad: ");
                    mesa.setCapacidad(leerEntero());
                    System.out.println("Capacidad actualizada.");
                    break;

                case 2:

                    mesa.setEstado(EstadoMesa.LIBRE);
                    mesa.setPersonasOcupando(0);
                    mesa.setReservaActiva(null);
                    mesa.setClienteActual(null);

                    Mesero mesero = buscarMeseroPorMesa(mesa);

                    if (mesero != null) {
                        mesero.retirarMesa(mesa);
                    }

                    System.out.println("Mesa liberada correctamente.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } catch (Exception e) {
        System.out.println(e.getMessage());
        }
    }
    
    // ---------------- OPCIÓN 5 ----------------
    private void registrarReserva() {

        try {

            System.out.print("Numero de mesa: ");
            int numero = leerEntero();

            Mesa mesa = buscarPorNumero(numero);

            if (mesa == null) {
                System.out.println("Mesa no encontrada.");
                return;
            }

            if (!mesa.estaDisponible()) {
                System.out.println("La mesa no se encuentra disponible.");
                return;
            }

            Reserva reserva = new Reserva();

            System.out.print("Nombre del cliente: ");
            reserva.setNombreCliente(sc.nextLine());

            System.out.print("Hora de llegada (HH:mm): ");
            reserva.setHoraLlegada(leerHora());

            mesa.setReservaActiva(reserva);

            System.out.println("Reserva registrada correctamente.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    // ---------------- OPCIÓN 6 ----------------
    private void eliminarMesa() {

        System.out.print("Numero de mesa: ");
        int numero = leerEntero();

        Mesa mesa = buscarPorNumero(numero);

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
            return;
        }

        mesas.remove(mesa);

        System.out.println("Mesa eliminada correctamente.");

    }
}
