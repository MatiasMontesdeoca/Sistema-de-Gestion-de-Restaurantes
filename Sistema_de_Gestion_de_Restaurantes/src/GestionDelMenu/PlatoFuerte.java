package GestionDelMenu;

public class PlatoFuerte extends Plato{
    //Override de la categoria de un plato con PLATO_FUERTE
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.PLATO_FUERTE;
    }
}
