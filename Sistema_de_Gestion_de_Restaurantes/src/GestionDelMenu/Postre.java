package GestionDelMenu;

import GestionDelMenu.Plato;
import GestionDelMenu.CategoriaPlato;

public class Postre extends Plato{
    @Override
    public CategoriaPlato getCategoria() {
        return CategoriaPlato.POSTRE;
    }
}
