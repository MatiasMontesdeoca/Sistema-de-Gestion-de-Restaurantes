package GestionDeMeseros;

import java.util.ArrayList;
import java.util.Objects;
import GestionDeMesasYReservas.Mesa;
import GestionDeClientes.Persona;

public class Mesero extends Persona{
    //Atributos
    private ArrayList<Mesa> historialMesas = new ArrayList<>();
    private ArrayList<Mesa> mesasAsignadas;
    private int personasAtendidas;

    //Constructor
    public Mesero() {
        this.mesasAsignadas = new ArrayList<>();
        this.personasAtendidas = 0;
    }
    
    //Get de historialMesas 
    public ArrayList<Mesa> getHistorialMesas(){
        return historialMesas;
    }

    //Get de mesasAsignadas
    public ArrayList<Mesa> getMesasAsignadas() {
        return mesasAsignadas;
    }

    //Métodos
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

    public void retirarMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }

        if (!mesasAsignadas.remove(mesa)) {
            throw new IllegalStateException("La mesa no estaba asignada a este mesero.");
        }
    }

    public int getCantidadMesasAsignadas() {
        return mesasAsignadas.size();
    }

    public int getPersonasAtendidas() {
        return personasAtendidas;
    }

    public void incrementarPersonasAtendidas(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.personasAtendidas += cantidad;
    }

    public void setPersonasAtendidas(int personasAtendidas) {
        if (personasAtendidas < 0) {
            throw new IllegalArgumentException("No puede ser negativo.");
        }
        this.personasAtendidas = personasAtendidas;
    }

    public boolean puedeAsignarseNuevaMesa() {
        return mesasAsignadas.size() < 4;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Mesas Asignadas: " + mesasAsignadas.size() +
                ", Personas Atendidas: " + personasAtendidas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), personasAtendidas);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Mesero other = (Mesero) obj;
        return personasAtendidas == other.personasAtendidas;
    }
}
