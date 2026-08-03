package GestionDelMenu;

import ExcepcionesPersonalizadas.ElementoNoEncontradoException;
import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import ExcepcionesPersonalizadas.PlatoDuplicadoException;
import Serializacion.ArchivoDatos;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGestionDelMenu {

    private ArrayList<Plato> platos;
    private Scanner sc;

    // Constructor que inicializa la lista compartida de platos y el escáner de entrada
    public MenuGestionDelMenu(ArrayList<Plato> platos) {
        this.platos = platos;
        this.sc = new Scanner(System.in);
    }

    // Inicia y gestiona la navegación del menú principal del módulo de carta
    public void iniciarMenu() {
        int opcion;
        do {
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

    // Busca un plato registrado en la lista mediante su nombre
    private Plato buscarPorNombre(String nombre) {
        for (Plato plato : platos) {
            if (plato.getNombre().equalsIgnoreCase(nombre)) {
                return plato;
            }
        }
        return null;
    }

    // Lee un entero desde la consola manejando posibles excepciones de formato
    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido");
            }
        }
    }

    // Lee un valor decimal desde la consola manejando excepciones de formato
    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito decimal valido");
            }
        }
    }
    
    // Lee un valor booleano desde la consola (1-Disponible / 0-Agotado)
    private boolean leerboolean() {
        while (true) {
            try {
                int valor = Integer.parseInt(sc.nextLine());
                if (valor == 1) {
                    return true;
                } else if (valor == 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un estado de disponibilidad valido (1-Disponible / 0-Agotado)");
            }
        }
    }
                
    // Registra un nuevo plato según su categoría validando que no esté duplicado
    ////////////////////////////////////////////////////
    // Opcion #1: Registrar plato
    ////////////////////////////////////////////////////
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
                case 1 -> plato = new Entrada();
                case 2 -> plato = new PlatoFuerte();
                case 3 -> plato = new Postre();
                case 4 -> plato = new Bebida();
                default -> {
                    System.out.println("Tipo invalido.");
                    return;
                }
            }
            System.out.print("Nombre del plato: ");
            String nombre = sc.nextLine();
            if (buscarPorNombre(nombre) != null) {
                throw new PlatoDuplicadoException("Ya existe un plato registrado con este nombre");
            }
            plato.setNombre(nombre);
            System.out.print("Precio: ");
            plato.setPrecio(leerDouble());
            System.out.println("Ingrese 1 para DISPONIBLE o 0 para AGOTADO");
            plato.setDisponibilidad(leerboolean());
            platos.add(plato);
            ArchivoDatos.guardar(platos, "platos.dat");
            System.out.println("Plato registrado correctamente.");
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        } catch (PlatoDuplicadoException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Ya existe un plato registrado con ese nombre" + "\n" + e.getMessage());
            return;
        }
    }

    // Muestra por consola todos los platos registrados en el menú
    ////////////////////////////////////////////////////
    // Opcion #2: Mostrar platos
    ////////////////////////////////////////////////////
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

    // Busca un plato específico por nombre y muestra su información
    ////////////////////////////////////////////////////
    // Opcion #3: Buscar plato
    ////////////////////////////////////////////////////
    private void buscarPlato() {
        System.out.print("Nombre del plato: ");
        String nombre = sc.nextLine().trim();
        Plato plato = buscarPorNombre(nombre);
        if (plato == null) {
            try {
                throw new ElementoNoEncontradoException("Plato '" + nombre + "' no fue encontrado.");
            } catch (ElementoNoEncontradoException e) {
                MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
            }
        } else {
            System.out.println(plato);
        }
    }

    // Permite modificar los datos o eliminar un plato existente del menú
    ////////////////////////////////////////////////////
    // Opcion #4: Modificar plato
    ////////////////////////////////////////////////////
    private void modificarPlato() {
        try {
            System.out.print("Nombre del plato: ");
            String nombre = sc.nextLine().trim();
            Plato plato = buscarPorNombre(nombre);
            if (plato == null) {
                try {
                    throw new ElementoNoEncontradoException("Plato '" + nombre + "' no fue encontrado.");
                } catch (ElementoNoEncontradoException e) {
                    MensajesDeExcepciones.mostrarAdvertencia(e.getMessage());
                    return;
                }
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
                    case 1 -> {
                        System.out.print("Nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();
                        if (buscarPorNombre(nuevoNombre) != null) {
                            System.out.println("Ya existe un plato con ese nombre.");
                        } else {
                            plato.setNombre(nuevoNombre);
                            ArchivoDatos.guardar(platos, "platos.dat");
                            System.out.println("Nombre actualizado.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Nuevo precio: ");
                        plato.setPrecio(leerDouble());
                        ArchivoDatos.guardar(platos, "platos.dat");
                        System.out.println("Precio actualizado.");
                    }
                    case 3 -> {
                        System.out.print("Nuevo estado (Disponible/Agotado): ");
                        plato.setDisponibilidad(leerboolean());
                        ArchivoDatos.guardar(platos, "platos.dat");
                        System.out.println("Disponibilidad actualizada.");
                    }
                    case 4 -> {
                        platos.remove(plato);
                        ArchivoDatos.guardar(platos, "platos.dat");
                        System.out.println("Plato eliminado.");
                        op = 5;
                    }
                    case 5 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } while (op != 5);
        } catch (IllegalArgumentException e) {
            MensajesDeExcepciones.mostrarAdvertencia("Debe ingresar correctamente los datos" + "\n" + e.getMessage());
            return;
        }
    }
}