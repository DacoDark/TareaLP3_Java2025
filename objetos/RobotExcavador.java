package objetos;

import entorno.NaveEstrellada;
import player.Jugador;

/**
 * Representa al robot excavador
 */
public class RobotExcavador extends Vehiculo{
    private int capacidad_carga;

    public void excavarRecursos(Jugador jugador){}

    public void descargarEnNave(NaveExploradora nave){}

    public void reparar(){}

    @Override
    public boolean puedeAcceder(int profundidad_minima){
        return true;
    }

    @Override
    public void transferirObjetos(Jugador jugador, ItemTipo tipo, int cantidad) {}

    @Override
    public void agregarABodega(ItemTipo tipo, int cantidad) {}

    @Override
    public void verBodega() {}

    @Override
    public void retirarDeBodega(Jugador jugador, ItemTipo tipo, int cantidad) {}
}
