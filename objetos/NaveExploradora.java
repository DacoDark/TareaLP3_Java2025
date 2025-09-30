package objetos;

import player.Jugador;

/**
 * Nave principal del Jugador
 */

public class NaveExploradora extends Vehiculo implements AccesoProfundidad {
    private int profundidadSoportada;

    /**
     * Anclar la nave en una determinada profundidad por el jugador
     * @param profundidad_nueva int
     */
    public void anclarNaveExploradora(int profundidad_nueva){}

    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        return false;
    }

    /**
     * Clase anidada para el Módulo de profundidad
     */
    public class ModuloProfundidad{
        private boolean activo;
        private int profundidad_extra;

        public void aumentarProfundidad(){}
    }

    @Override
    public void transferirObjetos(){}
}
