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
    private Oxigeno tanqueOxigeno;
    private List<Item> inventario;
    private Zona zonaActual;
    private int profundidadActual;
    private boolean tienePlanos;
    private NaveExploradora nave;
    private boolean trajeTermico;
    private boolean mejoraTanque;

    public Jugador(Oxigeno oxigeno){
        this.tanqueOxigeno = oxigeno;
        this.profundidadActual = 0;
        this.tienePlanos = false;
        this.mejoraTanque = false;
        this.trajeTermico = false;
    }

    /**
     * Ver el estado del Jugador
     */
    public void verEstadoJugador(){
        System.out.println("Profundidad: " + profundidadActual + " m");
        System.out.println("Zona: " + zonaActual);
        System.out.println("Oxígeno: " + tanqueOxigeno.getOxigenoRestante() + "/" + tanqueOxigeno.getCapacidadMaxima());
        System.out.println("Inventario: " + inventario);
        System.out.println("Mejora tanque: " + mejoraTanque);
        System.out.println("TrajeTermico: " + trajeTermico);
    }


    public void profundidadActualizar(int profundidad_nueva, Zona zona){
        int delta = Math.abs(profundidad_nueva - this.profundidadActual);
        double d = zona.normalizarProfundidad(profundidad_nueva);
        int costo = FormulaO2.cMover(d, delta);


        tanqueOxigeno.consumirO2(costo);
        this.profundidadActual = profundidad_nueva;

        System.out.println("Movimiento a Profundidad: " + profundidad_nueva + " m, costo de O2: " + costo);
    }

    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        return this.profundidadActual >= profundidad_minima;
    }

    public Oxigeno getTanqueOxigeno() {
        return tanqueOxigeno;
    }

    public boolean isMejoraTanque() {
        return mejoraTanque;
    }

    public void setMejoraTanque(boolean mejoraTanque) {
        this.mejoraTanque = mejoraTanque;
    }
}
