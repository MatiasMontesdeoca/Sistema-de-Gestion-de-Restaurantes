package GestionDeMesasYReservas;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class Reserva implements Serializable {

    private String nombreCliente;
    private LocalTime horaLlegada;

    // Constructor por defecto
    public Reserva() {
    }

    // Obtiene el nombre del cliente asociado a la reserva
    public String getNombreCliente() {
        return nombreCliente;
    }

    // Establece y valida el nombre del cliente de la reserva
    public void setNombreCliente(String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede ser nulo o vacio.");
        }
        this.nombreCliente = nombreCliente.trim();
    }

    // Obtiene la hora programada de llegada
    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    // Establece y valida que la hora de llegada esté dentro del horario laboral (08:00 - 21:00)
    public void setHoraLlegada(LocalTime horaLlegada) {
        if (horaLlegada == null) {
            throw new IllegalArgumentException("La hora de llegada no puede ser nula.");
        }
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(21, 0);
        if (horaLlegada.isBefore(apertura) || horaLlegada.isAfter(cierre)) {
            throw new IllegalArgumentException("La hora de llegada no puede ser fuera del horario laboral");
        }
        this.horaLlegada = horaLlegada;
    }

    // Devuelve la representación en cadena de texto de la reserva
    @Override
    public String toString() {
        return "Cliente: " + nombreCliente +
                ", Hora llegada: " + horaLlegada;
    }

    // Genera el código hash en base al nombre y la hora de llegada
    @Override
    public int hashCode() {
        return Objects.hash(nombreCliente, horaLlegada);
    }

    // Compara igualdad con otra reserva mediante el cliente y la hora
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Reserva)) return false;
        Reserva other = (Reserva) obj;
        return Objects.equals(nombreCliente, other.nombreCliente)
                && Objects.equals(horaLlegada, other.horaLlegada);
    }
}