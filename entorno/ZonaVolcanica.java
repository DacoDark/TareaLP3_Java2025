package entorno;

import objetos.ItemTipo;
import player.Jugador;

public class ZonaVolcanica extends Zona {
    private boolean planoEncontrado;

    public ZonaVolcanica() {
        this.nombre = "Zona Volcanica";
        this.profundidadMin = 1000;
        this.profundidadMax = 1499;
        //Falta implementar objetos por zona
    }

    public int getProfundidadMin() {
        return profundidadMin;
    }

    @Override
    public void entrar(Jugador jugador) {
        //Validación global
        if (!jugador.puedeAcceder(profundidadMin)){
            System.out.println("No es seguro, faltan requisitos para entrar a la Zona Volcánica, por tu seguridad mejor vuelve.");
            return;
        }
        System.out.println("Entrando en Zona Volcánica (1000-1499m)");
    }

    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo itemTipo) {}

    @Override
    public void explorarZona(Jugador jugador) {}
}

