package GestionDeMesasYReservas;

import GestionDeClientes.Cliente;
import java.io.Serializable;
import java.util.Objects;

public class Mesa implements Serializable{

    // Identificador numérico de la mesa
    private int numero;

    // Capacidad máxima de personas que puede tener la mesa
    private int capacidad;

    // Estado actual de la mesa (LIBRE, OCUPADA, RESERVADA)
    private EstadoMesa estado;

    // Reserva activa asociada a la mesa (si existe)
    private Reserva reservaActiva;

    // Número de personas actualmente ocupando la mesa
    private int personasOcupando;

    // Cliente actualmente sentado en la mesa
    private Cliente clienteActual;

    // Constructor: inicializa la mesa como LIBRE al crearla
    public Mesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    // Obtiene el número de la mesa
    public int getNumero() {
        return numero;
    }

    // Establece el número de la mesa
    public void setNumero(int numero) {

        if (numero <= 0) {
            throw new IllegalArgumentException("El numero de mesa debe ser mayor a cero.");
        }

        this.numero = numero;
    }

    // Obtiene la capacidad de la mesa
    public int getCapacidad() {
        return capacidad;
    }

    // Establece la capacidad de la mesa con validación
    public void setCapacidad(int capacidad) {
        if (this.estado == EstadoMesa.OCUPADA && capacidad < this.personasOcupando) {
        throw new IllegalArgumentException(
            "No se puede reducir la capacidad a " + capacidad + 
            " porque actualmente hay " + this.personasOcupando + " personas en la mesa."
        );
    }

    if (capacidad <= 0) {
        throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
    }

    this.capacidad = capacidad;
}
    
    // Obtiene el estado actual de la mesa
    public EstadoMesa getEstado() {
        return estado;
    }

    // Establece el estado de la mesa (LIBRE, OCUPADA, RESERVADA)
    public void setEstado(EstadoMesa estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    // Obtiene la reserva activa de la mesa
    public Reserva getReservaActiva() {
        return reservaActiva;
    }

    // Asigna una reserva a la mesa y cambia su estado a RESERVADA
    public void setReservaActiva(Reserva reservaActiva) {
        this.reservaActiva = reservaActiva;

        if (reservaActiva != null) {
            this.estado = EstadoMesa.RESERVADA;
        }
    }

    // Obtiene cuántas personas están ocupando la mesa
    public int getPersonasOcupando() {
        return personasOcupando;
    }

    // Define cuántas personas están en la mesa
    public void setPersonasOcupando(int personasOcupando) {
        this.personasOcupando = personasOcupando;
    }

    // Obtiene el cliente actualmente sentado en la mesa
    public Cliente getClienteActual() {
        return clienteActual;
    }

    // Asigna el cliente actual a la mesa
    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    // Verifica si la mesa está disponible (LIBRE)
    public boolean estaDisponible() {
        this.reservaActiva = null; // elimina reserva activa al consultar disponibilidad
        return estado == EstadoMesa.LIBRE;
    }

    // Verifica si la mesa está reservada
    public boolean estaReservada() {
        return estado == EstadoMesa.RESERVADA;
    }

    // Verifica si la mesa está ocupada
    public boolean estaOcupada() {
        return estado == EstadoMesa.OCUPADA;
    }

    // Representación en texto de la mesa
    @Override
    public String toString() {
        return "Mesa #" + numero +
               ", Capacidad: " + capacidad +
               ", Estado: " + estado;
    }

    // Hash basado en el número de mesa (identificador único)
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    // Dos mesas son iguales si tienen el mismo número
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mesa)) return false;

        Mesa other = (Mesa) obj;
        return numero == other.numero;
    }
}