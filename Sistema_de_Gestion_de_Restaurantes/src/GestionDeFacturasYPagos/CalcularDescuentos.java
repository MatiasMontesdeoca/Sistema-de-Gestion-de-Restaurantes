package GestionDeFacturasYPagos;

import GestionDeClientes.Cliente;

public class CalcularDescuentos {
    //Método para calcular el descuento por cliente frecuente (10%)
    public double calcularDescuentoClienteFrecuente(Cliente cliente, double total) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido.");
        }

        if (cliente.tieneDescuentoDisponible()) {
            return total * 0.10;
        }

        return 0;
    }

    //Método para calcular el descuento por un cupón
    public double calcularDescuentoCupon(double total, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje invalido.");
        }
        return total * (porcentaje / 100.0);
    }

    //Método para calcular el total despues de aplicar el descuento
    public double aplicarDescuentoFinal(double total, double descuento) {
        if (descuento < 0 || descuento > total) {
            throw new IllegalArgumentException("Descuento invalido.");
        }
        return total - descuento;
    }
}
