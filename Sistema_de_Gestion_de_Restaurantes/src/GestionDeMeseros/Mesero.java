package GestionDeMeseros;

import java.util.ArrayList;
import java.util.Objects;
import GestionDeMesasYReservas.Mesa;
import GestionDeClientes.Persona;

public class Mesero extends Persona {

    private ArrayList<Mesa> historialMesas = new ArrayList<>();
    private ArrayList<Mesa> mesasAsignadas;
    private int personasAtendidas;

    // Constructor que inicializa las listas de mesas y contadores del mesero
    public Mesero() {
        this.mesasAsignadas = new ArrayList<>();
        this.personasAtendidas = 0;
    }

    // Obtiene el historial de mesas atendidas por el mesero
    public ArrayList<Mesa> getHistorialMesas() {
        return historialMesas;
    }

    // Obtiene el listado de mesas asignadas actualmente
    public ArrayList<Mesa> getMesasAsignadas() {
        return mesasAsignadas;
    }

    // Asigna una mesa al mesero verificando que no supere el máximo de 4 mesas
    public void asignarMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }
        if (mesasAsignadas.contains(mesa)) {
            throw new IllegalStateException("La mesa ya está asignada a este mesero.");
        }
        if (!puedeAsignarseNuevaMesa()) {
            throw new IllegalStateException("El mesero ya alcanzó el máximo de mesas asignadas (4).");
        }
        mesasAsignadas.add(mesa);
    }

    // Retira una mesa asignada al mesero
    public void retirarMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }
        if (!mesasAsignadas.remove(mesa)) {
            throw new IllegalStateException("La mesa no estaba asignada a este mesero.");
        }
    }

    // Obtiene la cantidad de mesas actualmente asignadas
    public int getCantidadMesasAsignadas() {
        return mesasAsignadas.size();
    }

    // Obtiene el número total de personas atendidas por el mesero
    public int getPersonasAtendidas() {
        return personasAtendidas;
    }

    // Incrementa la cantidad acumulada de personas atendidas
    public void incrementarPersonasAtendidas(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.personasAtendidas += cantidad;
    }

    // Establece manualmente la cantidad de personas atendidas
    public void setPersonasAtendidas(int personasAtendidas) {
        if (personasAtendidas < 0) {
            throw new IllegalArgumentException("No puede ser negativo.");
        }
        this.personasAtendidas = personasAtendidas;
    }

    // Comprueba si el mesero tiene capacidad para recibir más mesas (máximo 4)
    public boolean puedeAsignarseNuevaMesa() {
        return mesasAsignadas.size() < 4;
    }

    // Devuelve la representación en cadena de texto del mesero
    @Override
    public String toString() {
        return super.toString() +
                ", Mesas Asignadas: " + mesasAsignadas.size() +
                ", Personas Atendidas: " + personasAtendidas;
    }

    // Genera el código hash del mesero
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), personasAtendidas);
    }

    // Compara igualdad con otro mesero
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Mesero other = (Mesero) obj;
        return personasAtendidas == other.personasAtendidas;
    }
}