package sistema_de_gestion_de_restaurantes;
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
import java.util.ArrayList;
import java.util.Scanner;
public class Sistema_de_Gestion_de_Restaurantes {

    public static void main(String[] args) {        
       // ===== Listas compartidas =====
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Mesero> meseros = new ArrayList<>();
        ArrayList<Mesa> mesas = new ArrayList<>();
        ArrayList<Plato> platos = new ArrayList<>();
        ArrayList<Pedido> pedidos = new ArrayList<>();
        ArrayList<Factura> facturas = new ArrayList<>();
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            Mesa mesa = new Mesa();
            mesa.setNumero(i);
            mesas.add(mesa);
        }

        // ===== Menús =====
        MenuGestionDeClientes menuClientes =
                new MenuGestionDeClientes(clientes, mesas);

        MenuGestionDeMeseros menuMeseros =
                new MenuGestionDeMeseros(meseros, mesas);

        MenuGestionDeMesasYReservas menuMesas =
                new MenuGestionDeMesasYReservas(mesas, meseros);

        MenuGestionDelMenu menuMenu =
                new MenuGestionDelMenu(platos);

        MenuGestionDePedidos menuPedidos =
                new MenuGestionDePedidos(
                        pedidos,
                        clientes,
                        meseros,
                        mesas,
                        platos);

        MenuFacturacionYPagos menuFacturacion =
                new MenuFacturacionYPagos(pedidos, facturas);

        MenuReportes menuReportes =
                new MenuReportes(
                        facturas,
                        pedidos,
                        detalles);

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
                opcion = -1;
            }

            switch (opcion) {

                case 1:
                    menuClientes.iniciarMenu();
                    break;

                case 2:
                    menuMeseros.iniciarMenu();
                    break;

                case 3:
                    menuMesas.iniciarMenu();
                    break;

                case 4:
                    menuMenu.iniciarMenu();
                    break;

                case 5:
                    menuPedidos.iniciarMenu();
                    break;

                case 6:
                    menuFacturacion.iniciarMenu();
                    break;

                case 7:
                    menuReportes.iniciarMenu();
                    break;

                case 8:
                    System.out.println("Gracias por utilizar el sistema.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 8);

        teclado.close();
    }
}