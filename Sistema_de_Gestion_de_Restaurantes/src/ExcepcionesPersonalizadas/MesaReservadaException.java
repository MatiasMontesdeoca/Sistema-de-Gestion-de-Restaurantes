package ExcepcionesPersonalizadas;

public class MesaReservadaException extends RuntimeException {

    // Constructor que asigna el mensaje personalizado de error
    public MesaReservadaException(String mensaje) {
        super(mensaje);
    }
}
