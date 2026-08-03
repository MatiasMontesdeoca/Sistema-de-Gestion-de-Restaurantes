package ExcepcionesPersonalizadas;

public class ElementoNoEncontradoException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
