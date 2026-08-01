package ExcepcionesPersonalizadas;

public class MetodoDePagoInvalidoException extends RuntimeException{
    public MetodoDePagoInvalidoException(String mensaje){
        super(mensaje);
    }
    
}
