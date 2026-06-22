package GestionDeMeseros;

import java.util.ArrayList;
import java.util.Objects;
import GestionDeMesasYReservas.Mesa;
import GestionDeClientes.Persona;

public class Mesero extends Persona {

    // Historial de mesas que el mesero ha atendido en el tiempo
    private ArrayList<Mesa> historialMesas = new ArrayList<>();

    // Mesas actualmente asignadas al mesero
    private ArrayList<Mesa> mesasAsignadas;

    // Total de personas atendidas por el mesero
    private int personasAtendidas;

    // Constructor: inicializa listas y valores base
    public Mesero() {
        this.mesasAsignadas = new ArrayList<>();
        this.personasAtendidas = 0;
    }

    // Obtiene el historial de mesas atendidas
    public ArrayList<Mesa> getHistorialMesas() {
        return historialMesas;
    }

    // Obtiene las mesas actualmente asignadas al mesero
    public ArrayList<Mesa> getMesasAsignadas() {
        return mesasAsignadas;
    }

    // Asigna una mesa al mesero
    public void asignarMesa(Mesa mesa) {

        // Validación: mesa no puede ser nula
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }

        // Evita asignar la misma mesa dos veces
        if (mesasAsignadas.contains(mesa)) {
            throw new IllegalStateException("La mesa ya está asignada a este mesero.");
        }

        // Limita a máximo 4 mesas por mesero
        if (!puedeAsignarseNuevaMesa()) {
            throw new IllegalStateException("El mesero ya alcanzó el máximo de mesas asignadas (4).");
        }

        // Agrega la mesa a la lista de asignadas
        mesasAsignadas.add(mesa);
    }

    // Retira una mesa del mesero
    public void retirarMesa(Mesa mesa) {

        // Validación: mesa no puede ser nula
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }

        // Elimina la mesa si existe, si no lanza error
        if (!mesasAsignadas.remove(mesa)) {
            throw new IllegalStateException("La mesa no estaba asignada a este mesero.");
        }
    }

    // Obtiene la cantidad de mesas asignadas actualmente
    public int getCantidadMesasAsignadas() {
        return mesasAsignadas.size();
    }

    // Obtiene el total de personas atendidas
    public int getPersonasAtendidas() {
        return personasAtendidas;
    }

    // Incrementa el número de personas atendidas
    public void incrementarPersonasAtendidas(int cantidad) {

        // Validación: debe ser mayor a 0
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        this.personasAtendidas += cantidad;
    }

    // Permite establecer manualmente el total de personas atendidas
    public void setPersonasAtendidas(int personasAtendidas) {

        // Validación: no puede ser negativo
        if (personasAtendidas < 0) {
            throw new IllegalArgumentException("No puede ser negativo.");
        }

        this.personasAtendidas = personasAtendidas;
    }

    // Verifica si el mesero puede recibir más mesas (máximo 4)
    public boolean puedeAsignarseNuevaMesa() {
        return mesasAsignadas.size() < 4;
    }

    // Representación en texto del mesero
    @Override
    public String toString() {
        return super.toString() +
                ", Mesas Asignadas: " + mesasAsignadas.size() +
                ", Personas Atendidas: " + personasAtendidas;
    }

    // Hash basado en persona y cantidad de personas atendidas
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), personasAtendidas);
    }

    // Dos meseros son iguales si coinciden como Persona y personas atendidas
    @Override
    public boolean equals(Object obj) {

        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Mesero other = (Mesero) obj;
        return personasAtendidas == other.personasAtendidas;
    }
}