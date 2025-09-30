package player;

import objetos.Item;
import objetos.ItemTipo;
import entorno.Zona;
import objetos.AccesoProfundidad;
import objetos.NaveExploradora;

import java.util.List;

/**
 * Representa al jugador
 */
public class Jugador implements AccesoProfundidad {
    private Oxigeno oxigeno;
    private List<Item> inventario;
    private Zona zonaActual;
    private int profundidad_actual;
    private boolean tienePlanos;
    private NaveExploradora nave;
    private boolean trajeTermico;
    private boolean mejoraTanque;

    public void verEstadoJugador(){}

    public void profundidadActualizar(int profundidad_nueva, Zona zona){}

    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        return false;
    }
}
