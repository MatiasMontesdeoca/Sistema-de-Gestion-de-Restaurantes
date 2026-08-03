package GestionDeClientes;

import java.util.Objects;
import java.io.Serializable;

public class Cliente extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    private String telefono;
    private String correoElectronico;
    private int visitasMes;
    private int descuentosDisponibles;

    // Constructor por defecto
    public Cliente() {
    }

    // Obtiene el teléfono del cliente
    public String getTelefono() {
        return telefono;
    }

    // Establece y valida el teléfono del cliente (10 dígitos)
    public void setTelefono(String telefono) {     
        if (telefono == null || telefono.isEmpty()) {
            throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
        }
        telefono = telefono.trim();
        if (!telefono.matches("\\d{10}")) {
            throw new IllegalArgumentException("El numero de telefono ingresado debe contener 10 digitos numericos");
        }
        this.telefono = telefono;
    }

    // Obtiene el correo electrónico del cliente
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    // Establece y valida el formato del correo electrónico
    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío.");
        }
        String correo = correoElectronico.trim();
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!correo.matches(regex)) {
            throw new IllegalArgumentException("Formato de correo electronico invalido.");
        }
        this.correoElectronico = correo;
    }

    // Obtiene el número de visitas acumuladas en el mes
    public int getVisitasMes() {
        return visitasMes;
    }

    // Establece el número de visitas acumuladas en el mes
    public void setVisitasMes(int visitasMes) {
        if (visitasMes < 0) {
            throw new IllegalArgumentException("Las visitas no pueden ser negativas.");
        }
        this.visitasMes = visitasMes;
    }

    // Incrementa en una unidad la cantidad de visitas del cliente
    public void incrementarVisitas() {
        this.visitasMes++;
    }

    // Obtiene la cantidad de descuentos de fidelidad disponibles
    public int getDescuentosDisponibles() {
        return descuentosDisponibles;
    }

    // Establece la cantidad de descuentos disponibles
    public void setDescuentosDisponibles(int descuentosDisponibles) {
        if (descuentosDisponibles < 0) {
            throw new IllegalArgumentException("Los descuentos no pueden ser negativos.");
        }
        this.descuentosDisponibles = descuentosDisponibles;
    }

    // Descuenta una unidad de los descuentos de fidelidad disponibles
    public void consumirDescuentoFidelidad() {
        if (descuentosDisponibles <= 0) {
            throw new IllegalStateException("No hay descuentos disponibles.");
        }
        this.descuentosDisponibles--;
    }

    // Comprueba si el cliente tiene al menos un descuento disponible
    public boolean tieneDescuentoDisponible() {
        return descuentosDisponibles > 0;
    }

    // Devuelve la representación en cadena de texto del cliente
    @Override
    public String toString() {
        return super.toString() +
                ", Telefono: " + telefono +
                ", Correo: " + correoElectronico +
                ", Visitas Mes: " + visitasMes +
                ", Descuentos: " + descuentosDisponibles;
    }

    // Genera el código hash según datos del cliente
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), telefono, correoElectronico);
    }

    // Compara igualdad con otro cliente mediante teléfono y correo
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Cliente other = (Cliente) obj;
        return Objects.equals(telefono, other.telefono)
                && Objects.equals(correoElectronico, other.correoElectronico);
    }
}