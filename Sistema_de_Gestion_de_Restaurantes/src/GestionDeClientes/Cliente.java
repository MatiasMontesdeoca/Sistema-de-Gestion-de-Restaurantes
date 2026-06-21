package GestionDeClientes;
import java.util.Objects;

public class Cliente extends Persona{
    //Atributos
    private String telefono;
    private String correoElectronico;
    private int visitasMes;
    private int descuentosDisponibles;

    //Constructor
    public Cliente() {
    }

    //Get y Set de Telefono
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        telefono = telefono.trim();
        
        if (telefono == null || telefono.isEmpty()) {
            throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
        }
        if (!telefono.matches("\\d{10}")){
            throw new IllegalArgumentException("El numero de telefono ingresado debe contener 10 digitos numericos");
        }
        this.telefono = telefono;
    }
    
    //Get y Set de correoElectronico
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío.");
        }
        
        String correo = correoElectronico.trim();
        
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        
        if (!correo.matches(regex)) {
        throw new IllegalArgumentException("Formato de correo electronico invalido.");
        }
        this.correoElectronico = correoElectronico.trim();
    }

    //Get y Set de visitasMes
    public int getVisitasMes() {
        return visitasMes;
    }

    public void setVisitasMes(int visitasMes) {
        if (visitasMes < 0) {
            throw new IllegalArgumentException("Las visitas no pueden ser negativas.");
        }
        this.visitasMes = visitasMes;
    }

    public void incrementarVisitas() {
        this.visitasMes++;
    }

    //Get y Set de descuentosDisponibles 
    public int getDescuentosDisponibles() {
        return descuentosDisponibles;
    }

    public void setDescuentosDisponibles(int descuentosDisponibles) {
        if (descuentosDisponibles < 0) {
            throw new IllegalArgumentException("Los descuentos no pueden ser negativos.");
        }
        this.descuentosDisponibles = descuentosDisponibles;
    }

    //Métodos
    public void consumirDescuentoFidelidad() {
        if (descuentosDisponibles <= 0) {
            throw new IllegalStateException("No hay descuentos disponibles.");
        }
        this.descuentosDisponibles--;
    }

    public boolean tieneDescuentoDisponible() {
        return descuentosDisponibles > 0;
    }

    
    //Overrides
    @Override
    public String toString() {
        return super.toString() +
                ", Telefono: " + telefono +
                ", Correo: " + correoElectronico +
                ", Visitas Mes: " + visitasMes +
                ", Descuentos: " + descuentosDisponibles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), telefono, correoElectronico);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;

        Cliente other = (Cliente) obj;
        return Objects.equals(telefono, other.telefono)
                && Objects.equals(correoElectronico, other.correoElectronico);
    }
}
