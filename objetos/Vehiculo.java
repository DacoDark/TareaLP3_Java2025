package objetos;

import player.Jugador;
import objetos.Item;
import java.util.List;

/**
 * Clase abstracta para vehículos
 */
public abstract class Vehiculo implements AccesoProfundidad {
    protected int profundidad_maxima;
    protected List<Item> bodega;

    public abstract void transferirObjetos();
}
