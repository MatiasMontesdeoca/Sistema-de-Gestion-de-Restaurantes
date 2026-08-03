package GestionDelMenu;

public class Postre extends Plato {

    // Retorna la categoría correspondiente a postres
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.POSTRE;
    }
}
