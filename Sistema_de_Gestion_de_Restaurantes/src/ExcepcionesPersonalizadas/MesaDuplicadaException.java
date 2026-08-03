package ExcepcionesPersonalizadas;

public class MesaDuplicadaException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public MesaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
