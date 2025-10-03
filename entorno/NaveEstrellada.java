package entorno;

import objetos.ItemTipo;
import player.Jugador;

public class NaveEstrellada extends Zona {
    private boolean moduloEncontrado;

    public NaveEstrellada() {
        this.nombre = "Nave Estrellada";
        this.profundidadMin = 0;
        this.profundidadMax = 0;
        this.moduloEncontrado = false;
    }

    public int getProfundidadMin() {
        return profundidadMin;
    }

    @Override
    public void entrar(Jugador jugador) {}

    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo itemTipo) {}

    @Override
    public void explorarZona(Jugador jugador) {}
}
