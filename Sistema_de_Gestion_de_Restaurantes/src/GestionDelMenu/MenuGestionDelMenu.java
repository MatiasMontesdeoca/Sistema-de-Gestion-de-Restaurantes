package GestionDelMenu;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDelMenu {
    //Atributos
    private ArrayList<Plato> platos;
    private Scanner sc;

    //Constructor
    public MenuGestionDelMenu(ArrayList<Plato> platos) {
        this.platos = platos;
        this.sc = new Scanner(System.in);
    }

    //Inicio de Menu
    public void iniciarMenu() {

        int opcion;

        do {
            //Interfaz del Menu
            System.out.println("\n===== GESTION DEL MENU =====");
            System.out.println("1. Registrar plato");
            System.out.println("2. Mostrar platos");
            System.out.println("3. Buscar plato");
            System.out.println("4. Modificar plato");
            System.out.println("5. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {

                case 1:
                    registrarPlato();
                    break;

                case 2:
                    mostrarPlatos();
                    break;

                case 3:
                    buscarPlato();
                    break;

                case 4:
                    modificarPlato();
                    break;

                case 5:
                    System.out.println("Regresando...");
                    break;

                default:
                    System.out.println("Opcion invalida.");

            }

        } while (opcion != 5);

    }
    
    //Métodos
    private Plato buscarPorNombre(String nombre) {

    for (Plato plato : platos) {
        if (plato.getNombre().equalsIgnoreCase(nombre)) {
            return plato;
        }
    }

    return null;}

    private int leerEntero() {

        while (true) {

            try {

                int numero = Integer.parseInt(sc.nextLine());

                return numero;

            } catch (NumberFormatException e) {

                System.out.print("Ingrese un numero valido: ");

            }

        }

    }

    private double leerDouble() {

        while (true) {

            try {

                double numero = Double.parseDouble(sc.nextLine());

                return numero;

            } catch (NumberFormatException e) {

                System.out.print("Ingrese un numero valido: ");

            }

        }

    }
    
    // ---------------- OPCIÓN 1 ----------------
    private void registrarPlato() {

        try {

            System.out.println("\nTipo de plato");
            System.out.println("1. Entrada");
            System.out.println("2. Plato fuerte");
            System.out.println("3. Postre");
            System.out.println("4. Bebida");
            System.out.print("Seleccione: ");

            int tipo = leerEntero();

            Plato plato;

            switch (tipo) {

                case 1:
                    plato = new Entrada();
                    break;
                case 2:
                    plato = new PlatoFuerte();
                    break;
                case 3:
                    plato = new Postre();
                    break;
                case 4:
                    plato = new Bebida();
                    break;
                default:
                    System.out.println("Tipo invalido.");
                    return;
            }

            System.out.print("Nombre del plato: ");
            String nombre = sc.nextLine();

            if (buscarPorNombre(nombre) != null) {
                System.out.println("Ya existe un plato con ese nombre.");
                return;
            }

            plato.setNombre(nombre);

            System.out.print("Precio: ");
            plato.setPrecio(leerDouble());

            System.out.print("Disponible (true/false): ");
            plato.setDisponibilidad(Boolean.parseBoolean(sc.nextLine()));

            platos.add(plato);

            System.out.println("Plato registrado correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------------- OPCIÓN 2 ----------------
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

    // ---------------- OPCIÓN 3 ----------------
    private void buscarPlato() {

    System.out.print("Nombre del plato: ");
    String nombre = sc.nextLine();

    Plato plato = buscarPorNombre(nombre);

    if (plato == null) {
        System.out.println("Plato no encontrado.");
    } else {
        System.out.println(plato);}
    }
    
    // ---------------- OPCIÓN 4 ----------------    
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
               System.out.println("\n===== MODIFICAR PLATO =====");
               System.out.println("1. Cambiar nombre");
               System.out.println("2. Cambiar precio");
               System.out.println("3. Cambiar disponibilidad");
               System.out.println("4. Eliminar plato");
               System.out.println("5. Salir");
               System.out.print("Seleccione: ");

               op = leerEntero();

               switch (op) {

                   case 1:
                       System.out.print("Nuevo nombre: ");
                       String nuevoNombre = sc.nextLine();

                       if (buscarPorNombre(nuevoNombre) != null) {
                           System.out.println("Ya existe un plato con ese nombre.");
                       } else {
                           plato.setNombre(nuevoNombre);
                           System.out.println("Nombre actualizado.");
                       }
                       break;

                   case 2:
                       System.out.print("Nuevo precio: ");
                       plato.setPrecio(leerDouble());
                       System.out.println("Precio actualizado.");
                       break;

                   case 3:
                       System.out.print("Nuevo estado (true/false): ");
                       plato.setDisponibilidad(Boolean.parseBoolean(sc.nextLine()));
                       System.out.println("Disponibilidad actualizada.");
                       break;

                   case 4:
                       platos.remove(plato);
                       System.out.println("Plato eliminado.");
                       op = 5;
                       break;

                   case 5:
                       System.out.println("Saliendo...");
                       break;

                   default:
                       System.out.println("Opción inválida.");
               }

           } while (op != 5);

       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
   }
}
