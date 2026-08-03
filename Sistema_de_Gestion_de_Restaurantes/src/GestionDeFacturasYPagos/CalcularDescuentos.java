package GestionDeFacturasYPagos;

import GestionDeClientes.Cliente;

public class CalcularDescuentos {

    // Calcula el descuento del 10% por fidelidad si el cliente dispone de descuentos
    public double calcularDescuentoClienteFrecuente(Cliente cliente, double total) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido.");
        }
        if (cliente.tieneDescuentoDisponible()) {
            return total * 0.10;
        }
        return 0;
    }

    // Calcula el monto de descuento según un porcentaje de cupón
    public double calcularDescuentoCupon(double total, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje invalido.");
        }
        return total * (porcentaje / 100.0);
    }

    // Retorna el importe total restar la cantidad de descuento aplicada
    public double aplicarDescuentoFinal(double total, double descuento) {
        if (descuento < 0 || descuento > total) {
            throw new IllegalArgumentException("Descuento invalido.");
        }
        return total - descuento;
    }
}