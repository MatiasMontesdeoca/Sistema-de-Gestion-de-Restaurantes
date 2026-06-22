package GestionDeFacturasYPagos;

import GestionDeClientes.Cliente;

public class CalcularDescuentos {

    // Calcula el descuento de cliente frecuente (10%) si el cliente tiene descuentos disponibles
    public double calcularDescuentoClienteFrecuente(Cliente cliente, double total) {

        // Validación del cliente
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido.");
        }

        // Si el cliente tiene descuento disponible, aplica 10% del total
        if (cliente.tieneDescuentoDisponible()) {
            return total * 0.10;
        }

        // Si no tiene descuento, no se aplica nada
        return 0;
    }

    // Calcula el descuento basado en un cupón (porcentaje ingresado)
    public double calcularDescuentoCupon(double total, double porcentaje) {

        // Validación del porcentaje (debe estar entre 0 y 100)
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje invalido.");
        }

        // Conversión del porcentaje a valor decimal
        return total * (porcentaje / 100.0);
    }

    // Aplica el descuento al total y devuelve el valor final a pagar
    public double aplicarDescuentoFinal(double total, double descuento) {

        // Validación para evitar descuentos negativos o mayores al total
        if (descuento < 0 || descuento > total) {
            throw new IllegalArgumentException("Descuento invalido.");
        }

        // Retorna el total menos el descuento
        return total - descuento;
    }
}