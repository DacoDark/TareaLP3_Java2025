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
    protected int presion;

    public abstract void entrar(Jugador jugador);

    public abstract void explorarZona(Jugador jugador);

    public abstract void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo);

    /**
     * Normaliza la profundidad actual de la zona
     * @param profundidad int
     * @return double en [0,1]
     */
    public double normalizarProfundidad(double profundidad) {
        return (profundidad-profundidadMin) / Math.max(1,(profundidadMax-profundidadMin));
    }

    public int getProfundidadMin(){
        return profundidadMin;
    }

    public int getProfundidadMax(){
        return profundidadMax;
    }

    public String getNombre() {
        return nombre;
    }

    public EnumSet<ItemTipo> getRecursos(){
        return recursos;
    }
}
