package GestionDeMesasYReservas;

import GestionDeClientes.Cliente;
import java.util.Objects;

public class Mesa {
    //Atributos
    private int numero;
    private int capacidad;
    private EstadoMesa estado;
    private Reserva reservaActiva;
    private int personasOcupando;
    private Cliente clienteActual;
    
    //Constructor para dejar las mesas en estado libre al iniciar el programa
    public Mesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    //Get y Set de numero de la mesa
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {

        if (numero <= 0) {
            throw new IllegalArgumentException("El numero de mesa debe ser mayor a cero.");
        }

        this.numero = numero;

        // Capacidades predeterminadas únicamente para las mesas iniciales
        switch (numero) {

            case 1:
            case 2:
            case 3:
                capacidad = 2;
                break;

            case 4:
            case 5:
            case 6:
                capacidad = 4;
                break;

            case 7:
            case 8:
                capacidad = 6;
                break;

            case 9:
                capacidad = 8;
                break;

            case 10:
                capacidad = 10;
                break;

            default:
                // Las mesas nuevas no tienen capacidad asignada, eso se las asigna al crearlas en el MenuGestionDeMesasYReservas.
                capacidad = 0;
        }
    }

    //Get y Set de capacidad de la mesa
    public int getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(int capacidad){
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }

        this.capacidad = capacidad;
    }

    //Get y set del estado de la mesa (Libre, Ocupada o Reservada)
    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    //Get y Set para reservaActiva de una mesa
    public Reserva getReservaActiva() {
        return reservaActiva;
    }

    public void setReservaActiva(Reserva reservaActiva) {
        this.reservaActiva = reservaActiva;

        if (reservaActiva != null) {
            this.estado = EstadoMesa.RESERVADA;
        }
    }
    
    //Get y Set para personasOcupando la mesa
    public int getPersonasOcupando() {
    return personasOcupando;
    }

    public void setPersonasOcupando(int personasOcupando) {
        this.personasOcupando = personasOcupando;
    }
    
    //Get y Set del clienteActual sentado en la mesa
    public Cliente getClienteActual() {
    return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
    this.clienteActual = clienteActual;
    }

    //Método para librerar la mesa
    public boolean estaDisponible() {
        this.reservaActiva = null;
        return estado == EstadoMesa.LIBRE;
    }

    //Método para reservar la mesa
    public boolean estaReservada() {
        return estado == EstadoMesa.RESERVADA;
    }

    //Método para ocupar la mesa
    public boolean estaOcupada() {
        return estado == EstadoMesa.OCUPADA;
    }

    //Overrides
    @Override
    public String toString() {
        return "Mesa #" + numero +
               ", Capacidad: " + capacidad +
               ", Estado: " + estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mesa)) return false;

        Mesa other = (Mesa) obj;
        return numero == other.numero;
    }

}
