package sistema_de_gestion_de_restaurantes;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import GestionDeReportes.MenuReportes;
import GestionDeFacturasYPagos.MenuFacturacionYPagos;
import GestionDeFacturasYPagos.Factura;
import GestionDePedidos.Pedido;
import GestionDePedidos.MenuGestionDePedidos;
import GestionDePedidos.DetallePedido;
import GestionDelMenu.Plato;
import GestionDelMenu.MenuGestionDelMenu;
import GestionDeMesasYReservas.Mesa;
import GestionDeMesasYReservas.MenuGestionDeMesasYReservas;
import GestionDeMeseros.MenuGestionDeMeseros;
import GestionDeMeseros.Mesero;
import GestionDeClientes.MenuGestionDeClientes;
import GestionDeClientes.Cliente;
import Serializacion.ArchivoDatos;
import java.util.ArrayList;
import java.util.Scanner;

public class Sistema_de_Gestion_de_Restaurantes {

    public static void main(String[] args) {

        // Listas globales compartidas por todo el sistema
        // Aquí se almacenan todos los datos del restaurante en memoria
        ArrayList<Cliente> clientes = ArchivoDatos.cargar("clientes.dat");
        ArrayList<Mesero> meseros = ArchivoDatos.cargar("meseros.dat");
        ArrayList<Mesa> mesas = ArchivoDatos.cargar("mesas.dat");
        ArrayList<Plato> platos = ArchivoDatos.cargar("platos.dat");
        ArrayList<Pedido> pedidos = ArchivoDatos.cargar("pedidos.dat");
        ArrayList<Factura> facturas = ArchivoDatos.cargar("facturas.dat");
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        ArrayList<String> reportesGenerados = ArchivoDatos.cargar("reportes.dat");

        // ===================== MENÚS DEL SISTEMA =====================

        // Módulo de gestión de clientes (registro, búsqueda, etc.)
        MenuGestionDeClientes menuClientes =
                new MenuGestionDeClientes(clientes, mesas);

        // Módulo de gestión de meseros (asignación de mesas, control de carga)
        MenuGestionDeMeseros menuMeseros =
                new MenuGestionDeMeseros(meseros, mesas);

        // Módulo de mesas y reservas
        MenuGestionDeMesasYReservas menuMesas =
                new MenuGestionDeMesasYReservas(mesas, meseros);

        // Módulo de gestión del menú (platos del restaurante)
        MenuGestionDelMenu menuMenu =
                new MenuGestionDelMenu(platos);

        // Módulo de pedidos (crear, editar, gestionar estados)
        MenuGestionDePedidos menuPedidos =
                new MenuGestionDePedidos(
                        pedidos,
                        clientes,
                        meseros,
                        mesas,
                        platos);

        // Módulo de facturación y pagos
        MenuFacturacionYPagos menuFacturacion =
                new MenuFacturacionYPagos(pedidos, facturas, mesas);

        // Módulo de reportes (ventas, platos, mesas, etc.)
        MenuReportes menuReportes =
                new MenuReportes(
                        facturas,
                        pedidos,
                        detalles,
                        reportesGenerados);

        // Scanner principal para leer opciones del menú principal
        Scanner teclado = new Scanner(System.in);

        int opcion;

        // Bucle principal del sistema (menú general)
        do {

            System.out.println("\n======================================");
            System.out.println(" SISTEMA DE GESTION DE RESTAURANTES");
            System.out.println("======================================");
            System.out.println("1. Gestion de Clientes");
            System.out.println("2. Gestion de Meseros");
            System.out.println("3. Gestion de Mesas y Reservas");
            System.out.println("4. Gestion del Menu");
            System.out.println("5. Gestion de Pedidos");
            System.out.println("6. Facturacion y Pagos");
            System.out.println("7. Reportes");
            System.out.println("8. Salir del sistema");
            System.out.print("Seleccione una opcion: ");

            // Lectura segura de la opción del usuario
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido" + "\n" + e.getMessage());
                opcion = -1; // Si hay error, fuerza opción inválida
            }

            // Navegación entre módulos del sistema
            switch (opcion) {

                case 1 -> menuClientes.iniciarMenu();

                case 2 -> menuMeseros.iniciarMenu();

                case 3 -> menuMesas.iniciarMenu();

                case 4 -> menuMenu.iniciarMenu();

                case 5 -> menuPedidos.iniciarMenu();

                case 6 -> menuFacturacion.iniciarMenu();

                case 7 -> menuReportes.iniciarMenu();

                case 8 -> System.out.println("Gracias por utilizar el sistema.");

                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 8);

        // Cierre del scanner para liberar recursos
        teclado.close();
    }
}