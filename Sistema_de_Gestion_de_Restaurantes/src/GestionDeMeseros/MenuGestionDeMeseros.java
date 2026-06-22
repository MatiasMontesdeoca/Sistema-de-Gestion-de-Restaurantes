package GestionDeMeseros;

import java.util.ArrayList;
import java.util.Scanner;
import GestionDeMesasYReservas.EstadoMesa;
import GestionDeMesasYReservas.Mesa;

public class MenuGestionDeMeseros {
    //Atributos
    private ArrayList<Mesero> meseros;
    private ArrayList<Mesa> mesas;
    private Scanner sc;

    //Constructor
    public MenuGestionDeMeseros(ArrayList<Mesero> meseros,
                                ArrayList<Mesa> mesas) {
        this.meseros = meseros;
        this.mesas = mesas;
        this.sc = new Scanner(System.in);
    }

    //Inicio de Menu
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
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {

                case 1:
                    registrarMesero();
                    break;

                case 2:
                    listarMeseros();
                    break;

                case 3:
                    asignarMesaAMesero();
                    break;

                case 4:
                    retirarMesaDeMesero();
                    break;

                case 5:
                    mostrarCargaMesero();
                    break;

                case 6:
                    System.out.println("Regresando al menu principal...");
                    break;

                default:
                    System.out.println("Opción invalida.");

            }
        } while (opcion != 6);
    }

    //Métodos
    private boolean meseroExiste(String cedula) {

    for (Mesero m : meseros) {

        if (m.getCedula().equals(cedula)) return true;
    }

    return false;
    }
    
    private Mesa buscarMesaPorNumero(int numero) {

    for (Mesa mesa : mesas) {

        if (mesa.getNumero() == numero) {
            return mesa;
        }

    }

    return null;
    }

    // ---------------- OPCIÓN 1 ----------------
    private void registrarMesero() {
        
        Mesero mesero = new Mesero();

        try {
            System.out.print("Cedula: ");
            mesero.setcedula(sc.nextLine().trim());

            System.out.print("Nombre: ");
            mesero.setNombre(sc.nextLine().trim());
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error de datos:"+e.getMessage());
            return;}

        if (meseroExiste(mesero.getCedula())){
            System.out.println("Error: Ya existe un mesero registrado con ese numero de cedula");
            return;}
        
        meseros.add(mesero);
        System.out.println("Mesero registrado correctamente");
    }

    // ---------------- OPCIÓN 2 ----------------
    private void listarMeseros() {

        if (meseros.isEmpty()) {
            System.out.println("No existen meseros registrados.");
            return;
        }

        for (Mesero mesero : meseros) {

            System.out.println(
            "Cedula: " + mesero.getCedula() +
            " | Nombre: " + mesero.getNombre() +
            " | Mesas actuales: " + mesero.getCantidadMesasAsignadas() +
            " | Total mesas atendidas: " + mesero.getHistorialMesas().size() +
            " | Total personas atendidas: " + mesero.getPersonasAtendidas());

        }
    }

    // ---------------- OPCIÓN 3 ----------------
    private Mesero buscarMeseroPorcedula(String cedula) {

        for (Mesero mesero : meseros) {

            if (mesero.getCedula().equalsIgnoreCase(cedula)) {

                return mesero;

            }

        }

        return null;

    }

    // ---------------- OPCIÓN 4 ----------------
    private void asignarMesaAMesero() {

        try {

            System.out.print("Cedula del mesero: ");
            String cedula = sc.nextLine();

            Mesero mesero = buscarMeseroPorcedula(cedula);

            if (mesero == null) {
                System.out.println("Mesero no encontrado.");
                return;
            }

            System.out.print("Numero de mesa: ");
            int numeroMesa = Integer.parseInt(sc.nextLine());

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
                System.out.println("Mesa no encontrada.");
                return;
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

            System.out.println("Mesa asignada correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------------- OPCIÓN 5 ----------------
    private void retirarMesaDeMesero() {

        try {

            System.out.print("cedula del mesero: ");
            String cedula = sc.nextLine();

            Mesero mesero = buscarMeseroPorcedula(cedula);

            if (mesero == null) {
                System.out.println("Mesero no encontrado.");
                return;
            }

            System.out.print("Numero de la mesa a retirar: ");
            int numeroMesa = Integer.parseInt(sc.nextLine());

            Mesa mesa = buscarMesaPorNumero(numeroMesa);

            if (mesa == null) {
                System.out.println("Mesa no encontrada.");
                return;
            }

            if (!mesero.getMesasAsignadas().contains(mesa)) {
                System.out.println("Esa mesa no está asignada a este mesero.");
                return;
            }

            mesero.retirarMesa(mesa);

            System.out.println("Mesa retirada del mesero correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------------- OPCIÓN 6 ----------------
    private void mostrarCargaMesero() {

        System.out.print("cedula del mesero: ");
        String cedula = sc.nextLine();

        Mesero mesero = buscarMeseroPorcedula(cedula);

        if (mesero == null) {

            System.out.println("Mesero no encontrado.");
            return;

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
