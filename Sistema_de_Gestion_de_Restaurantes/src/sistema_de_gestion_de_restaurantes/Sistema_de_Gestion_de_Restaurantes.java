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

    // Método principal de entrada que carga los datos serializados e inicia el menú general del sistema
    public static void main(String[] args) {
        ArrayList<Cliente> clientes = ArchivoDatos.cargar("clientes.dat");
        ArrayList<Mesero> meseros = ArchivoDatos.cargar("meseros.dat");
        ArrayList<Mesa> mesas = ArchivoDatos.cargar("mesas.dat");
        ArrayList<Plato> platos = ArchivoDatos.cargar("platos.dat");
        ArrayList<Pedido> pedidos = ArchivoDatos.cargar("pedidos.dat");
        ArrayList<Factura> facturas = ArchivoDatos.cargar("facturas.dat");
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        ArrayList<String> reportesGenerados = ArchivoDatos.cargar("reportes.dat");
        MenuGestionDeClientes menuClientes = new MenuGestionDeClientes(clientes, mesas);
        MenuGestionDeMeseros menuMeseros = new MenuGestionDeMeseros(meseros, mesas);
        MenuGestionDeMesasYReservas menuMesas = new MenuGestionDeMesasYReservas(mesas, meseros);
        MenuGestionDelMenu menuMenu = new MenuGestionDelMenu(platos);
        MenuGestionDePedidos menuPedidos = new MenuGestionDePedidos(pedidos, clientes, meseros, mesas, platos);
        MenuFacturacionYPagos menuFacturacion = new MenuFacturacionYPagos(pedidos, facturas, mesas);
        MenuReportes menuReportes = new MenuReportes(facturas, pedidos, detalles, reportesGenerados);
        Scanner teclado = new Scanner(System.in);
        int opcion;
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
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                MensajesDeExcepciones.mostrarError("Debe ingresar un digito entero valido\n" + e.getMessage());
                opcion = -1;
            }
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
        teclado.close();
    }
}