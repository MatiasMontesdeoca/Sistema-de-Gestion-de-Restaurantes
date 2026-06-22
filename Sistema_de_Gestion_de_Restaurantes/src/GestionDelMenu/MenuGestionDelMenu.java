package GestionDelMenu;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDelMenu {

    // Lista donde se almacenan todos los platos del sistema
    private ArrayList<Plato> platos;

    // Scanner para leer datos ingresados por el usuario en consola
    private Scanner sc;

    // Constructor: recibe la lista de platos compartida del sistema
    public MenuGestionDelMenu(ArrayList<Plato> platos) {
        this.platos = platos;
        this.sc = new Scanner(System.in);
    }

    // Método principal del menú de gestión del menú (CRUD de platos)
    public void iniciarMenu() {

        int opcion;

        do {
            // Menú principal de opciones
            System.out.println("\n===== GESTION DEL MENU =====");
            System.out.println("1. Registrar plato");
            System.out.println("2. Mostrar platos");
            System.out.println("3. Buscar plato");
            System.out.println("4. Modificar plato");
            System.out.println("5. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {

                case 1 -> registrarPlato();
                case 2 -> mostrarPlatos();
                case 3 -> buscarPlato();
                case 4 -> modificarPlato();
                case 5 -> System.out.println("Regresando...");
                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 5);
    }

    // Busca un plato por su nombre (ignora mayúsculas/minúsculas)
    private Plato buscarPorNombre(String nombre) {

        for (Plato plato : platos) {
            if (plato.getNombre().equalsIgnoreCase(nombre)) {
                return plato;
            }
        }

        return null;
    }

    // Lee un número entero desde consola con validación
    private int leerEntero() {

        while (true) {

            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }

        }
    }

    // Lee un número decimal con validación
    private double leerDouble() {

        while (true) {

            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un numero valido: ");
            }

        }
    }

    // Registrar un nuevo plato en el sistema
    private void registrarPlato() {

        try {

            // Selección del tipo de plato (herencia)
            System.out.println("\nTipo de plato");
            System.out.println("1. Entrada");
            System.out.println("2. Plato fuerte");
            System.out.println("3. Postre");
            System.out.println("4. Bebida");
            System.out.print("Seleccione: ");

            int tipo = leerEntero();

            Plato plato;

            // Se crea el objeto según el tipo seleccionado
            switch (tipo) {
                case 1 -> plato = new Entrada();
                case 2 -> plato = new PlatoFuerte();
                case 3 -> plato = new Postre();
                case 4 -> plato = new Bebida();
                default -> {
                    System.out.println("Tipo invalido.");
                    return;
                }
            }

            // Nombre del plato
            System.out.print("Nombre del plato: ");
            String nombre = sc.nextLine();

            // Evita duplicados por nombre
            if (buscarPorNombre(nombre) != null) {
                System.out.println("Ya existe un plato con ese nombre.");
                return;
            }

            plato.setNombre(nombre);

            // Precio del plato
            System.out.print("Precio: ");
            plato.setPrecio(leerDouble());

            // Disponibilidad del plato
            System.out.print("Disponible (true/false): ");
            plato.setDisponibilidad(Boolean.parseBoolean(sc.nextLine()));

            // Se agrega a la lista global de platos
            platos.add(plato);

            System.out.println("Plato registrado correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Muestra todos los platos registrados
    private void mostrarPlatos() {

        if (platos.isEmpty()) {
            System.out.println("No existen platos registrados.");
            return;
        }

        System.out.println();

        for (Plato plato : platos) {
            System.out.println(plato);
        }
    }

    // Busca y muestra un plato específico por nombre
    private void buscarPlato() {

        System.out.print("Nombre del plato: ");
        String nombre = sc.nextLine();

        Plato plato = buscarPorNombre(nombre);

        if (plato == null) {
            System.out.println("Plato no encontrado.");
        } else {
            System.out.println(plato);
        }
    }

    // Permite modificar o eliminar un plato existente
    private void modificarPlato() {

        try {

            System.out.print("Nombre del plato: ");
            String nombre = sc.nextLine();

            Plato plato = buscarPorNombre(nombre);

            if (plato == null) {
                System.out.println("Plato no encontrado.");
                return;
            }

            int op;

            do {
                // Submenú de modificación
                System.out.println("\n===== MODIFICAR PLATO =====");
                System.out.println("1. Cambiar nombre");
                System.out.println("2. Cambiar precio");
                System.out.println("3. Cambiar disponibilidad");
                System.out.println("4. Eliminar plato");
                System.out.println("5. Salir");
                System.out.print("Seleccione: ");

                op = leerEntero();

                switch (op) {

                    case 1 -> {
                        System.out.print("Nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();

                        if (buscarPorNombre(nuevoNombre) != null) {
                            System.out.println("Ya existe un plato con ese nombre.");
                        } else {
                            plato.setNombre(nuevoNombre);
                            System.out.println("Nombre actualizado.");
                        }
                    }

                    case 2 -> {
                        System.out.print("Nuevo precio: ");
                        plato.setPrecio(leerDouble());
                        System.out.println("Precio actualizado.");
                    }

                    case 3 -> {
                        System.out.print("Nuevo estado (true/false): ");
                        plato.setDisponibilidad(Boolean.parseBoolean(sc.nextLine()));
                        System.out.println("Disponibilidad actualizada.");
                    }

                    case 4 -> {
                        platos.remove(plato);
                        System.out.println("Plato eliminado.");
                        op = 5;
                    }

                    case 5 -> System.out.println("Saliendo...");

                    default -> System.out.println("Opción inválida.");
                }

            } while (op != 5);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}