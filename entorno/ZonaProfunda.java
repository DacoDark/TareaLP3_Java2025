package entorno;

import player.Jugador;
import player.FormulaO2;
import objetos.ItemTipo;
import java.util.Random;

/**
 * Zona Profunda
 */
public class ZonaProfunda extends Zona {
    private int presion;
    private Random rand = new Random();

    public ZonaProfunda() {
        this.nombre = "ZonaProfunda";
        this.profundidadMin = 200;
        this.profundidadMax = 999;
    }

    public int getProfundidadMin() {
        return profundidadMin;
    }

    @Override
    public void entrar(Jugador jugador) {
        if (!jugador.puedeAcceder(profundidadMin)) {
            System.out.println("No puedes entrar a la Zona Profunda a nado.");
            return;
        }
        System.out.println("Entrando en la Zona Profunda (200-999m)");
    }

    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo itemTipo) {}

    @Override
    public void explorarZona(Jugador jugador) {}
}

