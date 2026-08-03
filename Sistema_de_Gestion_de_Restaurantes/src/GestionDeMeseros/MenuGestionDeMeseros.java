package GestionDeMeseros;

import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.MeseroDuplicadoException;
import java.util.ArrayList;
import java.util.Scanner;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;
import Serializacion.ArchivoDatos;

public class MenuGestionDeMeseros {

    private ArrayList<Mesero> meseros;
    private ArrayList<Mesa> mesas;
    private Scanner sc;

    // Constructor que inicializa las listas globales del módulo de meseros
    public MenuGestionDeMeseros(ArrayList<Mesero> meseros, ArrayList<Mesa> mesas) {
        this.meseros = meseros;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    // Despliega y gestiona las opciones del menú de administración de meseros
    public void iniciarMenu() {
        int opcion;
        do {
            System.out.println("\n===== GESTION DE MESEROS =====");
            System.out.println("1. Registrar mesero");
            System.out.println("2. Listar meseros");
            System.out.println("3. Asignar mesa");
            System.out.println("4. Retirar mesa");
            System.out.println("5. Mostrar carga del mesero");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();
            switch (opcion) {
                case 1 -> registrarMesero();
                case 2 -> listarMeseros();
                case 3 -> asignarMesaAMesero();
                case 4 -> retirarMesaDeMesero();
                case 5 -> mostrarCargaMesero();
                case 6 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opción invalida.");
            }
        } while (opcion != 6);
    }

    // Comprueba si un mesero ya existe en el sistema por su cédula
    private boolean meseroExiste(String cedula) {
        for (Mesero m : meseros) {
            if (m.getCedula().equals(cedula)) {
                throw new MeseroDuplicadoException("Ya existe un mesero registrado con ese numero de cedula");
            }
        }
        return false;
    }

    // Encuentra una mesa registrada buscando por su número identificador
    private Mesa buscarMesaPorNumero(int numero) {
        for (Mesa mesa : mesas) {
            if (mesa.getNumero() == numero) {
                return mesa;
            }
        }
        return null;
    }
    
    // Lee un entero desde la consola capturando errores de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Registra un nuevo mesero en el sistema validando duplicidad
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar mesero
    ////////////////////////////////////////////////////
    private void registrarMesero() {
        Mesero mesero = new Mesero();
        try {
            System.out.print("Cedula: ");
            mesero.setcedula(sc.nextLine().trim());
            System.out.print("Nombre: ");
            mesero.setNombre(sc.nextLine().trim());
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos\n" + e.getMessage());
            return;
        }
        try {
            if (meseroExiste(mesero.getCedula()));
        } catch (MeseroDuplicadoException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe un mesero registrado\n" + e.getMessage());
            return;
        }
        meseros.add(mesero);
        ArchivoDatos.guardar(meseros, "meseros.dat");
        System.out.println("Mesero registrado correctamente");
    }

    // Despliega la lista general de meseros y sus estadísticas
    ////////////////////////////////////////////////////
    // Opcion #2: Listar meseros
    ////////////////////////////////////////////////////
    private void listarMeseros() {
        if (meseros.isEmpty()) {
            System.out.println("No existen meseros registrados.");
            return;
        }
        for (Mesero mesero : meseros) {
            System.out.println("Cedula: " + mesero.getCedula() + " | Nombre: " + mesero.getNombre() + " | Mesas actuales: " + mesero.getCantidadMesasAsignadas() + " | Total mesas atendidas: " + mesero.getHistorialMesas().size() + " | Total personas atendidas: " + mesero.getPersonasAtendidas());
        }
    }

    // Busca y retorna un mesero registrado por su número de cédula
    private Mesero buscarMeseroPorcedula(String cedula) {
        for (Mesero mesero : meseros) {
            if (mesero.getCedula().equalsIgnoreCase(cedula)) {
                return mesero;
            }
        }
        return null;
    }

    // Asigna una mesa ocupada a un mesero específico
    ////////////////////////////////////////////////////
    // Opcion #3: Asignar mesa
    ////////////////////////////////////////////////////
    private void asignarMesaAMesero() {
        try {
            System.out.print("Cedula del mesero: ");
            String cedula = sc.nextLine().trim();
            Mesero mesero = buscarMeseroPorcedula(cedula);
            if (mesero == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesero con cedula " + cedula + " no fue encontrado.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            System.out.print("Numero de mesa: ");
            int numeroMesa = leerEntero();
            for (Mesero m : meseros) {
                for (Mesa mesaAsignada : m.getMesasAsignadas()) {
                    if (mesaAsignada.getNumero() == numeroMesa) {
                        System.out.println("Esa mesa ya esta asignada a un mesero.");
                        return;
                    }
                }
            }
            Mesa mesa = buscarMesaPorNumero(numeroMesa);
            if (mesa == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesa #" + numeroMesa + " no fue encontrada.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            int personas = mesa.getPersonasOcupando();
            if (personas <= 0) {
                System.out.println("La mesa no tiene clientes sentados.");
                return;
            }
            mesero.asignarMesa(mesa);
            mesa.setEstado(EstadoMesa.OCUPADA);
            mesero.getHistorialMesas().add(mesa);
            mesero.incrementarPersonasAtendidas(personas);
            ArchivoDatos.guardar(meseros, "meseros.dat");
            ArchivoDatos.guardar(mesas, "mesas.dat");
            System.out.println("Mesa asignada correctamente.");
        } catch (NumberFormatException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar un digito entero valido\n" + e.getMessage());
            return;
        }
    }

    // Desvincula una mesa asignada de la responsabilidad de un mesero
    ////////////////////////////////////////////////////
    // Opcion #4: Retirar mesa
    ////////////////////////////////////////////////////
    private void retirarMesaDeMesero() {
        try {
            System.out.print("cedula del mesero: ");
            String cedula = sc.nextLine().trim();
            Mesero mesero = buscarMeseroPorcedula(cedula);
            if (mesero == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesero con cedula " + cedula + " no fue encontrado.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            System.out.print("Numero de la mesa a retirar: ");
            int numeroMesa = leerEntero();
            Mesa mesa = buscarMesaPorNumero(numeroMesa);
            if (mesa == null) {
                try {
                    throw new ElementoNoEncontradoException("Mesa #" + numeroMesa + " no fue encontrada.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
            }
            if (!mesero.getMesasAsignadas().contains(mesa)) {
                System.out.println("Esa mesa no está asignada a este mesero.");
                return;
            }
            mesero.retirarMesa(mesa);
            ArchivoDatos.guardar(meseros, "meseros.dat");
            ArchivoDatos.guardar(mesas, "mesas.dat");
            System.out.println("Mesa retirada del mesero correctamente.");
        } catch (NumberFormatException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar un digito entero valido\n" + e.getMessage());
            return;
        }
    }

    // Muestra la carga actual de trabajo y mesas atendidas de un mesero
    ////////////////////////////////////////////////////
    // Opcion #5: Mostrar carga del mesero
    ////////////////////////////////////////////////////
    private void mostrarCargaMesero() {
        System.out.print("cedula del mesero: ");
        String cedula = sc.nextLine().trim();
        Mesero mesero = buscarMeseroPorcedula(cedula);
        if (mesero == null) {
            try {
                throw new ElementoNoEncontradoException("Mesero con cedula " + cedula + " no fue encontrado.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                return;
            }
        }
        System.out.println("\n========== INFORMACIÓN DEL MESERO ==========");
        System.out.println("cedula: " + mesero.getCedula());
        System.out.println("Nombre: " + mesero.getNombre());
        System.out.println("Mesas asignadas: " + mesero.getCantidadMesasAsignadas());
        System.out.println("Personas atendidas: " + mesero.getPersonasAtendidas());
        if (mesero.getMesasAsignadas().isEmpty()) {
            System.out.println("No tiene mesas asignadas.");
        } else {
            System.out.println("\nMesas asignadas:");
            for (Mesa mesa : mesero.getMesasAsignadas()) {
                System.out.println(mesa);
            }
        }
    }
}