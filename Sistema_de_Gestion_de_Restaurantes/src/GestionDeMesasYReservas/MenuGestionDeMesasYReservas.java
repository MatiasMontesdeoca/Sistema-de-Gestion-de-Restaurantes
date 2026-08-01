package GestionDeMesasYReservas;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.MesaDuplicadaException;
import ExcepcionesPersonalizadas.MesaReservadaException;
import GestionDeMeseros.Mesero;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuGestionDeMesasYReservas {

    // Lista de mesas del restaurante
    private ArrayList<Mesa> mesas;

    // Lista de meseros disponibles en el sistema
    private ArrayList<Mesero> meseros;

    // Scanner para entrada de datos por consola
    private Scanner sc;

    // Constructor: recibe listas existentes de mesas y meseros
    public MenuGestionDeMesasYReservas(ArrayList<Mesa> mesas, ArrayList<Mesero> meseros) {
        this.mesas = mesas;
        this.meseros = meseros;
        this.sc = new Scanner(System.in);
    }

    // Inicia el menú principal de gestión de mesas y reservas
    public void iniciarMenu() {

        int opcion;

        do {
            // Interfaz del menú
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

            // Ejecución de opciones del menú
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

    // Busca una mesa por su número
    private Mesa buscarPorNumero(int numero) {

        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }

        return null;
    }

    // Lee un número entero de forma segura
    private int leerEntero() {

        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Lee una hora en formato HH:mm de forma segura
    private LocalTime leerHora() {

        while (true) {
            try {
                return LocalTime.parse(sc.nextLine());
            } catch (DateTimeParseException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar la hora en el formato valido HH:MM (24H)");
            }
        }
    }

    // Busca el mesero que tenga asignada una mesa específica
    private Mesero buscarMeseroPorMesa(Mesa mesa) {

        for (Mesero m : meseros) {
            if (m.getMesasAsignadas().contains(mesa)) {
                return m;
            }
        }

        return null;
    }

    // ---------------- OPCIÓN 1: REGISTRAR MESA ----------------
    private void registrarMesa() {

        try {

            Mesa mesa = new Mesa();

            System.out.print("Numero de mesa: ");
            mesa.setNumero(leerEntero());

            // Validar que no exista otra mesa con el mismo número
            if (buscarPorNumero(mesa.getNumero()) != null){
                throw new MesaDuplicadaException("Ya existe una mesa registrada con ese numero identificador");
            } 


            // Si es una mesa nueva mayor a 10, se pide capacidad manual
            if (mesa.getNumero() > 10) {
                System.out.print("Capacidad: ");
                mesa.setCapacidad(leerEntero());
            } else {
                // Mesas pequeñas tienen capacidad predeterminada
                System.out.println("La mesa " + mesa.getNumero()
                        + " tiene una capacidad predeterminada de "
                        + mesa.getCapacidad() + " personas.");
            }

            mesas.add(mesa);

            System.out.println("Mesa registrada correctamente.");
            
        } catch(MesaDuplicadaException e){
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe una mesa registrado con ese numero identificador" + "\n" + e.getMessage());
            return;

        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos"+ "\n" + e.getMessage());
            return;
        }
    }

    // ---------------- OPCIÓN 2: MOSTRAR MESAS ----------------
    private void mostrarMesas() {

        if (mesas.isEmpty()) {
            System.out.println("No existen mesas registradas.");
            return;
        }

        // Muestra todas las mesas y su estado
        for (Mesa mesa : mesas) {

            System.out.println(mesa);

            // Si tiene reserva activa, la muestra también
            if (mesa.getReservaActiva() != null) {
                System.out.println("   Reserva -> " + mesa.getReservaActiva());
            }
        }
    }

    // ---------------- OPCIÓN 3: BUSCAR MESA ----------------
    private void buscarMesa() {

        System.out.print("Numero de mesa: ");

        int numero = leerEntero();

        Mesa mesa = buscarPorNumero(numero);

        if (mesa == null) {
            System.out.println("Mesa no encontrada.");
        } else {
            System.out.println(mesa);

            // Mostrar reserva si existe
            if (mesa.getReservaActiva() != null) {
                System.out.println(mesa.getReservaActiva());
            }
        }
    }

    // ---------------- OPCIÓN 4: MODIFICAR MESA ----------------
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
                    // Cambiar capacidad de la mesa
                    System.out.print("Nueva capacidad: ");
                    mesa.setCapacidad(leerEntero());
                    System.out.println("Capacidad actualizada.");
                    break;

                case 2:

                    // Liberar mesa completamente
                    mesa.setEstado(EstadoMesa.LIBRE);
                    mesa.setPersonasOcupando(0);
                    mesa.setReservaActiva(null);
                    mesa.setClienteActual(null);

                    // Quitar mesa del mesero si estaba asignada
                    Mesero mesero = buscarMeseroPorMesa(mesa);

                    if (mesero != null) {
                        mesero.retirarMesa(mesa);
                    }

                    System.out.println("Mesa liberada correctamente.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        } catch (IllegalStateException e){
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar un estado valido para la mesa (LIBRE-OCUPADA-RESERVADA)" + "\n" + e.getMessage());
            return;
        }
    }

    // ---------------- OPCIÓN 5: REGISTRAR RESERVA ----------------
    private void registrarReserva() {

        try {

            System.out.print("Numero de mesa: ");
            int numero = leerEntero();

            Mesa mesa = buscarPorNumero(numero);

            if (mesa == null) {
                System.out.println("Mesa no encontrada.");
                return;
            }

            // Verificar disponibilidad
            if (!mesa.estaDisponible()) {
                throw new MesaReservadaException("Esta mesa ya se encuentra reservada");
            }

            // Crear nueva reserva
            Reserva reserva = new Reserva();

            System.out.print("Nombre del cliente: ");
            reserva.setNombreCliente(sc.nextLine());

            System.out.print("Hora de llegada (HH:mm): ");
            reserva.setHoraLlegada(leerHora());

            // Asignar reserva a la mesa
            mesa.setReservaActiva(reserva);

            System.out.println("Reserva registrada correctamente.");

        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        
        } catch (MesaReservadaException e) {
            MensajesDeExcepciones.mostrarAdvertencia("La mesa ya se encuentra reservada" + "\n" + e.getMessage());
            return;
        }
       
    }

    // ---------------- OPCIÓN 6: ELIMINAR MESA ----------------
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