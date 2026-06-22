package GestionDelMenu;

public class Entrada extends Plato{
    //Override de la categoria de un plto con ENTRADA
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.ENTRADA;
    }
}
