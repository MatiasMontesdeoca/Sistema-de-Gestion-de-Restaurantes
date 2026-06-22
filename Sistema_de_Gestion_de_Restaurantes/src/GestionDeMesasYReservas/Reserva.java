package GestionDeMesasYReservas;

import java.time.LocalTime;
import java.util.Objects;

public class Reserva {

    // Nombre del cliente que realiza la reserva
    private String nombreCliente;

    // Hora en la que el cliente tiene programada su llegada
    private LocalTime horaLlegada;

    // Constructor vacío
    public Reserva() {
    }

    // Obtiene el nombre del cliente de la reserva
    public String getNombreCliente() {
        return nombreCliente;
    }

    // Establece el nombre del cliente con validación
    public void setNombreCliente(String nombreCliente) {

        // Validación: no puede ser nulo ni vacío
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede ser nulo o vacio.");
        }

        // Limpieza de espacios innecesarios
        this.nombreCliente = nombreCliente.trim();
    }

    // Obtiene la hora de llegada de la reserva
    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    // Establece la hora de llegada con validación
    public void setHoraLlegada(LocalTime horaLlegada) {

        // Validación: no puede ser nula
        if (horaLlegada == null) {
            throw new IllegalArgumentException("La hora de llegada no puede ser nula.");
        }

        this.horaLlegada = horaLlegada;
    }

    // Representación en texto del objeto Reserva
    @Override
    public String toString() {
        return "Cliente: " + nombreCliente +
                ", Hora llegada: " + horaLlegada;
    }

    // Hash basado en nombre del cliente y hora de llegada
    @Override
    public int hashCode() {
        return Objects.hash(nombreCliente, horaLlegada);
    }

    // Dos reservas son iguales si coinciden nombre del cliente y hora
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Reserva)) return false;

        Reserva other = (Reserva) obj;

        return Objects.equals(nombreCliente, other.nombreCliente)
                && Objects.equals(horaLlegada, other.horaLlegada);
    }
}