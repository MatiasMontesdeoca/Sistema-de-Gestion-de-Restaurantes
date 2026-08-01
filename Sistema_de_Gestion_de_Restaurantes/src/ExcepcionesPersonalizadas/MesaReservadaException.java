package ExcepcionesPersonalizadas;

public class MesaReservadaException extends RuntimeException{
    public MesaReservadaException(String mensaje){
        super(mensaje);
    }
    
}
