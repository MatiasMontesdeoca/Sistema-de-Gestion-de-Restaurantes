package ExcepcionesPersonalizadas;

public class NoHayMesasRegistradasException extends RuntimeException {

    public NoHayMesasRegistradasException() {
        super("No hay mesas registradas en el sistema. Debe registrar al menos una mesa antes de realizar esta operacion.");
    }

    public NoHayMesasRegistradasException(String mensaje) {
        super(mensaje);
    }
}
