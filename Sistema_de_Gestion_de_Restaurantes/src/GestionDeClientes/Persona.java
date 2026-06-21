package GestionDeClientes;

public abstract class Persona {
    //Atributos
    protected String cedula;
    protected String nombre;

    //Constructor
    public Persona() {
    }

    //Get y Set de cedula
    public String getCedula() {
        return cedula;
    }

    public void setcedula(String cedula) {
        cedula = cedula.trim();
        
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo o vacio.");
        }
        
        if (!cedula.matches("\\d{10}")){
            throw new IllegalArgumentException("El numero de cedula ingresado debe contener 10 digitos numericos");}
            
        this.cedula = cedula;
    }
    
    //Get y Set nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre.trim();
        
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+(\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";
        
        if (!nombre.matches(regex)) {
        throw new IllegalArgumentException(
            "Debe ingresar nombre y apellido (solo letras, minimo dos palabras)."
        );}
        
        this.nombre = nombre;
    }

    //Overrides
    @Override
    public String toString() {
        return "cedula: " + cedula + ", Nombre: " + nombre;
    }

    @Override
    public int hashCode() {
        return cedula != null ? cedula.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Persona other = (Persona) obj;
        return cedula != null && cedula.equals(other.cedula);
    }
}
