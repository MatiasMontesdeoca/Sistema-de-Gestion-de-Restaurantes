package ExcepcionesPersonalizadas;

public class NoHayMesasRegistradasException extends RuntimeException {

    // Constructor por defecto con mensaje estándar
    public NoHayMesasRegistradasException() {
        super("No hay mesas registradas en el sistema. Debe registrar al menos una mesa antes de realizar esta operacion.");
    }

    // Constructor con mensaje personalizado
    public NoHayMesasRegistradasException(String mensaje) {
        super(mensaje);
    }
}
