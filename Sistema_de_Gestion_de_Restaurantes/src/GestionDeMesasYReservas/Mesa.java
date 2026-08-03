package GestionDeMesasYReservas;

import GestionDeClientes.Cliente;
import java.io.Serializable;
import java.util.Objects;

public class Mesa implements Serializable {

    private int numero;
    private int capacidad;
    private EstadoMesa estado;
    private Reserva reservaActiva;
    private int personasOcupando;
    private Cliente clienteActual;

    // Constructor que inicializa el estado de la mesa como LIBRE
    public Mesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    // Obtiene el número identificador de la mesa
    public int getNumero() {
        return numero;
    }

    // Establece y valida el número de la mesa
    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El numero de mesa debe ser mayor a cero.");
        }
        this.numero = numero;
    }

    // Obtiene la capacidad máxima de personas de la mesa
    public int getCapacidad() {
        return capacidad;
    }

    // Establece la capacidad máxima comprobando la cantidad de ocupantes actuales
    public void setCapacidad(int capacidad) {
        if (this.estado == EstadoMesa.OCUPADA && capacidad < this.personasOcupando) {
            throw new IllegalArgumentException("No se puede reducir la capacidad a " + capacidad + " porque actualmente hay " + this.personasOcupando + " personas en la mesa.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        this.capacidad = capacidad;
    }

    // Obtiene el estado actual de la mesa (LIBRE, OCUPADA, RESERVADA)
    public EstadoMesa getEstado() {
        return estado;
    }

    // Establece el estado actual de la mesa
    public void setEstado(EstadoMesa estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    // Obtiene la reserva activa vinculada a la mesa
    public Reserva getReservaActiva() {
        return reservaActiva;
    }

    // Asigna la reserva activa y cambia el estado a RESERVADA
    public void setReservaActiva(Reserva reservaActiva) {
        this.reservaActiva = reservaActiva;
        if (reservaActiva != null) {
            this.estado = EstadoMesa.RESERVADA;
        }
    }

    // Obtiene la cantidad de personas actualmente sentadas en la mesa
    public int getPersonasOcupando() {
        return personasOcupando;
    }

    // Establece la cantidad de personas actualmente sentadas
    public void setPersonasOcupando(int personasOcupando) {
        this.personasOcupando = personasOcupando;
    }

    // Obtiene el cliente que ocupa actualmente la mesa
    public Cliente getClienteActual() {
        return clienteActual;
    }

    // Asigna el cliente que ocupará la mesa
    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    // Comprueba disponibilidad (reiniciando reserva activa) y retorna si está LIBRE
    public boolean estaDisponible() {
        this.reservaActiva = null;
        return estado == EstadoMesa.LIBRE;
    }

    // Comprueba si la mesa se encuentra en estado RESERVADA
    public boolean estaReservada() {
        return estado == EstadoMesa.RESERVADA;
    }

    // Comprueba si la mesa se encuentra en estado OCUPADA
    public boolean estaOcupada() {
        return estado == EstadoMesa.OCUPADA;
    }

    // Devuelve la representación en cadena de texto de la mesa
    @Override
    public String toString() {
        return "Mesa #" + numero +
               ", Capacidad: " + capacidad +
               ", Estado: " + estado;
    }

    // Genera el código hash según el número de la mesa
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    // Compara la igualdad con otra mesa por su número identificador
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mesa)) return false;
        Mesa other = (Mesa) obj;
        return numero == other.numero;
    }
}