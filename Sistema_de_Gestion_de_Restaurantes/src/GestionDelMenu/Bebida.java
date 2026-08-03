package GestionDelMenu;

public class Bebida extends Plato {

    // Retorna la categoría correspondiente a bebidas
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.BEBIDA;
    }
}
