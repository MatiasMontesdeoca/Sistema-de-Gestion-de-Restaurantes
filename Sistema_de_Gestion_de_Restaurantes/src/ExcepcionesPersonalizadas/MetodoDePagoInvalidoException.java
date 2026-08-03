package ExcepcionesPersonalizadas;

public class MetodoDePagoInvalidoException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public MetodoDePagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
