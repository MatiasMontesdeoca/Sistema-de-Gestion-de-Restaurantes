package GestionDelMenu;

public class Bebida extends Plato{
    //Override de la categoria de plato con BEBIDA
      @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.BEBIDA;
    }
}
