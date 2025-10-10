package player;

import objetos.Item;
import objetos.ItemTipo;
import entorno.Zona;
import objetos.AccesoProfundidad;
import objetos.NaveExploradora;

import java.util.ArrayList;
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
    private boolean juegoCompletado = false;

    public Jugador(Oxigeno oxigeno){
        this.tanqueOxigeno = oxigeno;
        this.profundidadActual = 0;
        this.tienePlanos = false;
        this.mejoraTanque = false;
        this.trajeTermico = false;
        this.inventario = new ArrayList<>();
    }

    /**
     * Ver el estado del Jugador
     */
    public void verEstadoJugador(){
        System.out.println("Profundidad: " + profundidadActual + " m");
        System.out.println("Zona: " + zonaActual);
        System.out.println("Oxígeno: " + tanqueOxigeno.getOxigenoRestante() + "/" + tanqueOxigeno.getCapacidadMaxima());
        System.out.println("Mejora tanque: " + mejoraTanque);
        System.out.println("TrajeTermico: " + trajeTermico);
        if (inventario.isEmpty()) {
            System.out.println("Inventario: (vacío)");
        }else {
            System.out.println("Inventario:");
            for (Item it : inventario) {
                System.out.println(" - " + it);
            }
        }
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
        if(profundidad_minima >= 1000){
            // Volcánica o más abajo
            return this.mejoraTanque && this.trajeTermico;
        } else if (profundidad_minima >= 500) {
            // Zona Profunda
            return this.mejoraTanque;
        } else {
            //Arrecife
            return true;
        }
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

    public void setTienePlanos(){
        this.tienePlanos = true;
    }
    public boolean isTienePlanos() {
        return tienePlanos;
    }

    public int getProfundidadActual() {
        return profundidadActual;
    }
    public void setTrajeTermico (){
        this.trajeTermico = true;
    }
    public boolean isTrajeTermico() {
        return trajeTermico;
    }
    public boolean tieneModuloProfundidad(){
        return nave.getModuloProfundidad().isActivo();
    }

    public void agregarItem(ItemTipo tipo, int cantidad){
        for (Item i : inventario) {
            if (i.getTipo() == tipo){
                i.setCantidad(i.getCantidad()+cantidad);
                return;
            }
            inventario.add(new Item(tipo,cantidad));
        }
    }

    public void crearMejoraTanque(){
        int piezas = contarItem(ItemTipo.PIEZA_TANQUE);
        if (piezas >= 3 && !mejoraTanque){
            consumirItem(ItemTipo.PIEZA_TANQUE, 3);
            this.mejoraTanque = true;
            getTanqueOxigeno().aumentarOxigeno(tanqueOxigeno.getOxigenoRestante()); //Mejora la capacidad al doble
            System.out.println("¡Has creado la Mejora tanque! Capacidad de O2 duplicada");
        } else if (mejoraTanque){
            System.out.println("Ya tienes la mejora de tanque.");
        } else {
            System.out.println("No tienes suficientes PIEZA_TANQUE (necesitas 3)");
        }
    }

    public int contarItem(ItemTipo tipo) {
        int total = 0;
        for (Item item : inventario) {
            if (item.getTipo() == tipo) {
                total += item.getCantidad();
            }
        }
        return total;
    }

    private void consumirItem(ItemTipo tipo,int cantidad){
        for (Item i : inventario) {
            if (i.getTipo() == tipo) {
                i.setCantidad(i.getCantidad()-cantidad);
                if (i.getCantidad() <= 0){
                    inventario.remove(i);
                }
                return;
            }
        }
    }

    public boolean isJuegoCompletado() {
        return juegoCompletado;
    }
    public void setJuegoCompletado(boolean juegoCompletado) {
        this.juegoCompletado = juegoCompletado;
    }
}
