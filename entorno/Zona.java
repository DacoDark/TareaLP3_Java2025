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

    //*******************************
    //*     Métodos abstractos      *
    //*******************************
    public abstract void entrar(Jugador jugador);
    public abstract void explorarZona(Jugador jugador);
    public abstract void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo);

    /**
     * Normaliza la profundidad actual de la zona
     * @param profundidad tipo: int; descripción: profundad que se va a normalizar entre [0,1]
     * @return double en [0,1]
     */
    public double normalizarProfundidad(double profundidad) {
        return (profundidad-profundidadMin) / Math.max(1,(profundidadMax-profundidadMin));
    }

    //*******************************
    //*     Getters de la clase     *
    //*******************************

    /**
     * Función get de la profundidad minima de la zona
     * @return tipo: int; descripción: Retorna el valor de la profundidad minima de la zona
     */
    public int getProfundidadMin(){
        return profundidadMin;
    }

    /**
     * Función get de la profundidad máxima de la zona
     * @return tipo: int; descripción: Retorna el valor de la profundidad minima de la zona
     */
    public int getProfundidadMax(){
        return profundidadMax;
    }

    /**
     * Función que obtiene el nombre de la zona
     * @return tipo: String; descripción: nombre de la zona que se quiere obtener
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Función que obtiene los recursos disponibles de una zona
     * @return tipo: EnumSet<ItemTipo> descripción: Enum de los items que están disponible en la zona
     */
    public EnumSet<ItemTipo> getRecursos(){
        return recursos;
    }
}
