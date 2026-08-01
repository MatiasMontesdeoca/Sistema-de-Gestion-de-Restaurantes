package GestionDeClientes;

import java.util.Objects;

public class Cliente extends Persona {

    // Atributos
    private String telefono;
    private String correoElectronico;
    private int visitasMes;
    private int descuentosDisponibles;

    // Constructor vacío
    public Cliente() {
    }

    // Obtiene el teléfono del cliente
    public String getTelefono() {
        return telefono;
    }

    // Establece el teléfono del cliente con validación
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

    // Establece el correo electrónico con validación de formato
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

    // Obtiene el número de visitas del mes
    public int getVisitasMes() {
        return visitasMes;
    }

    // Establece el número de visitas del mes
    public void setVisitasMes(int visitasMes) {
        if (visitasMes < 0) {
            throw new IllegalArgumentException("Las visitas no pueden ser negativas.");
        }
        this.visitasMes = visitasMes;
    }

    // Incrementa en 1 las visitas del cliente
    public void incrementarVisitas() {
        this.visitasMes++;
    }

    // Obtiene los descuentos disponibles
    public int getDescuentosDisponibles() {
        return descuentosDisponibles;
    }

    // Establece los descuentos disponibles
    public void setDescuentosDisponibles(int descuentosDisponibles) {
        if (descuentosDisponibles < 0) {
            throw new IllegalArgumentException("Los descuentos no pueden ser negativos.");
        }
        this.descuentosDisponibles = descuentosDisponibles;
    }

    // Consume un descuento de fidelidad si está disponible
    public void consumirDescuentoFidelidad() {
        if (descuentosDisponibles <= 0) {
            throw new IllegalStateException("No hay descuentos disponibles.");
        }
        this.descuentosDisponibles--;
    }

    // Verifica si el cliente tiene descuentos disponibles
    public boolean tieneDescuentoDisponible() {
        return descuentosDisponibles > 0;
    }

    // Representación en texto del objeto
    @Override
    public String toString() {
        return super.toString() +
                ", Telefono: " + telefono +
                ", Correo: " + correoElectronico +
                ", Visitas Mes: " + visitasMes +
                ", Descuentos: " + descuentosDisponibles;
    }

    // Hash basado en teléfono y correo
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), telefono, correoElectronico);
    }

    // Comparación entre clientes
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Cliente other = (Cliente) obj;
        return Objects.equals(telefono, other.telefono)
                && Objects.equals(correoElectronico, other.correoElectronico);
    }
}