 package GestionDeClientes;

public abstract class Persona {

    // Atributos comunes para cualquier persona del sistema
    protected String cedula;  // Identificación única (10 dígitos)
    protected String nombre;  // Nombre completo (nombre y apellido)

    // Obtiene la cédula de la persona
    public String getCedula() {
        return cedula;
    }

    // Establece la cédula con validación
    public void setcedula(String cedula) {
        // Validación de null o vacío
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo o vacio.");
        }
        
        cedula = cedula.trim();

        // Validación de formato: exactamente 10 dígitos numéricos
        if (!cedula.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                "El numero de cedula ingresado debe contener 10 digitos numericos"
            );
        }

        this.cedula = cedula;
    }

    // Obtiene el nombre de la persona
    public String getNombre() {
        return nombre;
    }

    // Establece el nombre con validación
    public void setNombre(String nombre) {
        // Validación de null o vacío
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        
        nombre = nombre.trim();

        // Expresión regular: mínimo dos palabras (nombre + apellido)
        // permite letras con acentos y ñ
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+(\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";

        if (!nombre.matches(regex)) {
            throw new IllegalArgumentException(
                "Debe ingresar nombre y apellido (solo letras, minimo dos palabras)."
            );
        }

        this.nombre = nombre;
    }

    // Representación en texto del objeto Persona
    @Override
    public String toString() {
        return "cedula: " + cedula + ", Nombre: " + nombre;
    }

    // Hash basado en la cédula (identificador único)
    @Override
    public int hashCode() {
        return cedula != null ? cedula.hashCode() : 0;
    }

    // Dos personas son iguales si tienen la misma cédula y mismo tipo de clase
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Persona other = (Persona) obj;
        return cedula != null && cedula.equals(other.cedula);
    }
}