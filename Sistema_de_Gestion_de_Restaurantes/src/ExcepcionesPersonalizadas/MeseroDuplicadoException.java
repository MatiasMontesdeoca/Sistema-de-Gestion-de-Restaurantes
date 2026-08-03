package ExcepcionesPersonalizadas;

public class MeseroDuplicadoException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public MeseroDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
