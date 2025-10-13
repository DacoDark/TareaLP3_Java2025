package objetos;

import player.Jugador;
import java.util.List;

/**
 * Clase abstracta para vehículos
 */
public abstract class Vehiculo implements AccesoProfundidad {
    protected List<Item> bodega;

    public abstract void transferirObjetos(Jugador jugador, ItemTipo tipo, int cantidad);
    public abstract void agregarABodega(ItemTipo tipo,int cantidad);
    public abstract void retirarDeBodega(Jugador jugador, ItemTipo tipo, int cantidad);
    public abstract void verBodega();

}
