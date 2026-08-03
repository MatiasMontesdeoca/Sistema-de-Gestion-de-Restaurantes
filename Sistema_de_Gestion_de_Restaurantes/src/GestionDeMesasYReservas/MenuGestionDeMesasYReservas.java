package GestionDeMesasYReservas;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MesaDuplicadaException;
import ExcepcionesPersonalizadas.MesaReservadaException;
import GestionDeMeseros.Mesero;
import Serializacion.ArchivoDatos;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDeMesasYReservas {

    private ArrayList<Mesa> mesas;
    private ArrayList<Mesero> meseros;
    private Scanner sc;

    // Constructor que inicializa las listas de mesas, meseros y la herramienta de entrada
    public MenuGestionDeMesasYReservas(ArrayList<Mesa> mesas, ArrayList<Mesero> meseros) {
        this.mesas = mesas;
        this.meseros = meseros;
        this.sc = new Scanner(System.in);
    }

    // Inicia y gestiona el menú interactivo para el control de mesas y reservas
    public void iniciarMenu() {
        int opcion;
        do {
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
                case 1 -> registrarMesa();
                case 2 -> mostrarMesas();
                case 3 -> buscarMesa();
                case 4 -> modificarMesa();
                case 5 -> registrarReserva();
                case 6 -> eliminarMesa();
                case 7 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 7);
    }

    // Busca y retorna una mesa registrada utilizando su número identificador
    private Mesa buscarPorNumero(int numero) {
        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }
        return null;
    }

    // Lee un valor entero por consola manejando posibles excepciones de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Lee y parsea una hora en formato HH:mm validando la estructura del texto
    private LocalTime leerHora() {
        while (true) {
            try {
                return LocalTime.parse(sc.nextLine());
            } catch (DateTimeParseException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar la hora en el formato valido HH:MM (24H)");
            }
        }
    }

    // Encuentra el mesero que tiene asignada una mesa específica
    private Mesero buscarMeseroPorMesa(Mesa mesa) {
        for (Mesero m : meseros) {
            if (m.getMesasAsignadas().contains(mesa)) {
                return m;
            }
        }
        return null;
    }

    // Registra una nueva mesa solicitando número y capacidad manual
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar mesa
    ////////////////////////////////////////////////////
    private void registrarMesa() {
        try {
            Mesa mesa = new Mesa();
            System.out.print("Numero de mesa: ");
            mesa.setNumero(leerEntero());
            if (buscarPorNumero(mesa.getNumero()) != null) {
                throw new MesaDuplicadaException("Ya existe una mesa registrada con ese numero identificador");
            }
            System.out.print("Capacidad de la mesa: ");
            mesa.setCapacidad(leerEntero());
            mesas.add(mesa);
            ArchivoDatos.guardar(mesas, "mesas.dat");
            System.out.println("Mesa registrada correctamente.");
        } catch (MesaDuplicadaException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe una mesa registrado con ese numero identificador\n" + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        }
    }

    // Despliega por consola la lista completa de mesas y sus reservas si existen
    ////////////////////////////////////////////////////
    // Opcion #2: Mostrar mesas
    ////////////////////////////////////////////////////
    private void mostrarMesas() {
        if (mesas.isEmpty()) {
            System.out.println("No existen mesas registradas.");
            return;
        }
        for (Mesa mesa : mesas) {
            System.out.println(mesa);
            if (mesa.getReservaActiva() != null) {
                System.out.println("   Reserva -> " + mesa.getReservaActiva());
            }
        }
    }

    // Busca una mesa específica por número y muestra su estado actual
    ////////////////////////////////////////////////////
    // Opcion #3: Buscar mesa
    ////////////////////////////////////////////////////
    private void buscarMesa() {
        System.out.print("Numero de mesa: ");
        int numero = leerEntero();
        Mesa mesa = buscarPorNumero(numero);
        if (mesa == null) {
            try {
                throw new ElementoNoEncontradoException("Mesa #" + numero + " no fue encontrada.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
            }
        } else {
            System.out.println(mesa);
            if (mesa.getReservaActiva() != null) {
                System.out.println(mesa.getReservaActiva());
            }
        }
    }

    // Permite cambiar la capacidad de una mesa o liberarla cambiando su estado
    ////////////////////////////////////////////////////
    // Opcion #4: Modificar mesa
    ////////////////////////////////////////////////////
    private void modificarMesa() {
        try {
            System.out.print("Numero de mesa: ");
            int numero = leerEntero();
            Mesa mesa = buscarPorNumero(numero);
            if (mesa == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesa #" + numero + " no fue encontrada.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            System.out.println("\n1. Cambiar capacidad");
            System.out.println("2. Cambiar estado a LIBRE (liberar mesa)");
            System.out.print("Seleccione una opcion: ");
            int op = leerEntero();
            switch (op) {
                case 1 -> {
                    System.out.print("Nueva capacidad: ");
                    mesa.setCapacidad(leerEntero());
                    ArchivoDatos.guardar(mesas, "mesas.dat");
                    System.out.println("Capacidad actualizada.");
                }
                case 2 -> {
                    mesa.setEstado(EstadoMesa.LIBRE);
                    mesa.setPersonasOcupando(0);
                    mesa.setReservaActiva(null);
                    mesa.setClienteActual(null);
                    ArchivoDatos.guardar(mesas, "mesas.dat");
                    Mesero mesero = buscarMeseroPorMesa(mesa);
                    if (mesero != null) {
                        mesero.retirarMesa(mesa);
                        ArchivoDatos.guardar(meseros, "meseros.dat");
                    }
                    System.out.println("Mesa liberada correctamente.");
                }
                default -> System.out.println("Opcion invalida.");
            }
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        } catch (IllegalStateException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar un estado valido para la mesa (LIBRE-OCUPADA-RESERVADA)\n" + e.getMessage());
            return;
        }
    }

    // Registra una reserva para una mesa disponible especificando cliente y hora
    ////////////////////////////////////////////////////
    // Opcion #5: Registrar reserva
    ////////////////////////////////////////////////////
    private void registrarReserva() {
        try {
            System.out.print("Numero de mesa: ");
            int numero = leerEntero();
            Mesa mesa = buscarPorNumero(numero);
            if (mesa == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesa #" + numero + " no fue encontrada.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            if (!mesa.estaDisponible()) {
                throw new MesaReservadaException("Esta mesa ya se encuentra reservada");
            }
            Reserva reserva = new Reserva();
            System.out.print("Nombre del cliente: ");
            reserva.setNombreCliente(sc.nextLine());
            System.out.print("Hora de llegada (HH:mm): ");
            reserva.setHoraLlegada(leerHora());
            mesa.setReservaActiva(reserva);
            ArchivoDatos.guardar(mesas, "mesas.dat");
            System.out.println("Reserva registrada correctamente.");
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        } catch (MesaReservadaException e) {
            MensajesDeExcepciones.mostrarAdvertencia("La mesa ya se encuentra reservada\n" + e.getMessage());
            return;
        }
    }

    // Elimina una mesa registrada del listado global del sistema
    ////////////////////////////////////////////////////
    // Opcion #6: Eliminar mesa
    ////////////////////////////////////////////////////
    private void eliminarMesa() {
        System.out.print("Numero de mesa: ");
        int numero = leerEntero();
        Mesa mesa = buscarPorNumero(numero);
        if (mesa == null) {
            try {
                throw new ElementoNoEncontradoException("Mesa #" + numero + " no fue encontrada.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        mesas.remove(mesa);
        ArchivoDatos.guardar(mesas, "mesas.dat");
        System.out.println("Mesa eliminada correctamente.");
    }
}