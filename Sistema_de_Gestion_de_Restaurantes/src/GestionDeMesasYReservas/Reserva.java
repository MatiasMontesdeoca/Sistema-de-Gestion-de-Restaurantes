package GestionDeMesasYReservas;

import java.time.LocalTime;
import java.util.Objects;

public class Reserva {
    //Atributos
    private String nombreCliente;
    private LocalTime horaLlegada;

    //Constructor
    public Reserva() {
    }

    //Get y Set para el nombre del cliente que reserva la mesa
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede ser nulo o vacio.");
        }
        this.nombreCliente = nombreCliente.trim();
    }

    //Get y Set para la hora de llegada del cliente a la reserva
    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        if (horaLlegada == null) {
            throw new IllegalArgumentException("La hora de llegada no puede ser nula.");
        }
        this.horaLlegada = horaLlegada;
    }

    //Overrides
    @Override
    public String toString() {
        return "Cliente: " + nombreCliente +
                ", Hora llegada: " + horaLlegada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreCliente, horaLlegada);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Reserva)) return false;

        Reserva other = (Reserva) obj;
        return Objects.equals(nombreCliente, other.nombreCliente)
                && Objects.equals(horaLlegada, other.horaLlegada);
    }
}
