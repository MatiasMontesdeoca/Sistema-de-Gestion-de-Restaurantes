package ExcepcionesPersonalizadas;

public class ClienteDuplicadoException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
