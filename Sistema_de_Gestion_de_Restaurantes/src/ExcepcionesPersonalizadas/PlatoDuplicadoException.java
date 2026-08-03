package ExcepcionesPersonalizadas;

public class PlatoDuplicadoException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public PlatoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
