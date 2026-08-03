package GestionDelMenu;

public class Entrada extends Plato {

    // Retorna la categoría correspondiente a platos de entrada
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.ENTRADA;
    }
}
