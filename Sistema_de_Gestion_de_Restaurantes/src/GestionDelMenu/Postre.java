package GestionDelMenu;

public class Postre extends Plato{
    //Override de la categoria de un plato con POSTRE
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.POSTRE;
    }
}
