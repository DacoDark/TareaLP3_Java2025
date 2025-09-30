package entorno;

import objetos.ItemTipo;
import player.Jugador;

import java.util.EnumSet;

/**
 * Clase abstracta que representa una zona del juego
 */
public abstract class Zona {
    protected String nombre;
    protected int profundidadMin;
    protected int profundidadMax;
    protected EnumSet<ItemTipo> recursos;

    public abstract void entrar(Jugador jugador);

    public abstract void explorarZona(Jugador jugador);

    public abstract void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo);
}
