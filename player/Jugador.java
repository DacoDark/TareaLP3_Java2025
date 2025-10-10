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
    private boolean moduloInstalado;
    private boolean juegoCompletado = false;

    /**
     * Constructor de la Clase
     * @param oxigeno tipo: oxígeno; Descripción: Capacidad del personaje para respirar bajo el agua
     */
    public Jugador(Oxigeno oxigeno){
        this.tanqueOxigeno = oxigeno;
        this.profundidadActual = 0;
        this.tienePlanos = false;
        this.mejoraTanque = false;
        this.trajeTermico = false;
        this.moduloInstalado = false;
        this.inventario = new ArrayList<>();
    }

    //*******************************************
    //*     Estado y propiedades del jugador    *
    //*******************************************

    /**
     * Imprime por pantalla el estado del jugador
     */
    public void verEstadoJugador(){
        System.out.println("\n===   Estado Jugador  ===");
        System.out.println("Profundidad: " + profundidadActual + " m");
        System.out.println("Oxígeno: " + tanqueOxigeno.getOxigenoRestante() + "/" + tanqueOxigeno.getCapacidadMaxima());
        System.out.println("Mejora Tanque: " + (mejoraTanque ? "Sí" : "No"));
        System.out.println("Traje Térmico: " + (trajeTermico ? "Sí" : "No"));
        System.out.println("Modulo Instalado: " + (moduloInstalado ? "Sí" : "No"));
        System.out.println("Planos: " + (tienePlanos ? "Sí" : "No"));
        System.out.println("Zona: " + zonaActual);
        verInventario();
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
            //Arrecife y Nave estrellada
            return true;
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

    //***********************
    //*     Inventario      *
    //***********************

    /**
     * Agrega un item al inventario.
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se agregará al inventario
     * @param cantidad tipo: int; descripción: Cantidad del item que se agregará al inventario
     */
    public void agregarItem(ItemTipo tipo, int cantidad){
        if (cantidad <= 0) return;

        for (Item i : inventario) {
            if (i.getTipo() == tipo){
                i.setCantidad(i.getCantidad()+cantidad);
                return;
            }
            inventario.add(new Item(tipo,cantidad));
        }
    }

    /**
     * Función para consumir objetos del inventario.
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se quiere contar.
     * @return tipo: boolean; descripción: Retorna verdadero consumio todos los items y falso si aún le queda en el inventario
     */
    public boolean consumirItem(ItemTipo tipo,int cantidad){
        for (Item i : inventario) {
            if (i.getTipo() == tipo) {
                if (i.getCantidad() >= cantidad) {
                    i.setCantidad(i.getCantidad() - cantidad);
                    if (i.getCantidad() == 0) inventario.remove(i);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Cuenta cuántas unidades de un tipo tiene el jugador
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se quiere contar.
     * @return tipo: int; descripción: cantidad entera del tipo de item que tiene el jugador
     */
    public int contarItem(ItemTipo tipo) {
        for (Item i : inventario) {
            if (i.getTipo() == tipo) {
                return i.getCantidad();
            }
        }
        return 0;
    }

    /**
     * Muestra el inventario que posee el jugador por pantalla.
     */
    public void verInventario(){
        System.out.println("\n==== Inventario del Jugador ===");
        if (inventario.isEmpty()){
            System.out.println("Inventario vacio");
        }
        for (Item i : inventario) {
            System.out.printf("- %s: %d%n", i.getTipo(), i.getCantidad());
        }
    }

    /**
     * Cuando el jugador muere en una expedición se pierde el inventario
     */
    public void vaciarInventario(){
        inventario.clear();
    }

    //***********************************
    //* Getter y Setters del jugador    *
    //***********************************
    public Oxigeno getTanqueOxigeno() {
        return tanqueOxigeno;
    }

    public int getProfundidadActual() {
        return profundidadActual;
    }

    public boolean tieneModuloProfundidad(){
        if (nave.getModuloProfundidad().isActivo()){
            setModuloInstalado(true);
        }
        return moduloInstalado;
    }
    public void setModuloInstalado(boolean valor) {
        this.moduloInstalado = valor;
    }
    public boolean isMejoraTanque() {
        return mejoraTanque;
    }
    public void setMejoraTanque(boolean mejoraTanque) {
        this.mejoraTanque = mejoraTanque;
    }
    public boolean isTrajeTermico() {
        return trajeTermico;
    }
    public void setTrajeTermico (){
        this.trajeTermico = true;
    }
    public boolean isTienePlanos() {
        return tienePlanos;
    }
    public void setTienePlanos(){
        this.tienePlanos = true;
    }
    public List<Item> getInventario() {
        return inventario;
    }
    public boolean isJuegoCompletado() {
        return juegoCompletado;
    }
    public void setJuegoCompletado(boolean juegoCompletado) {
        this.juegoCompletado = juegoCompletado;
    }
     // ********************
     // * Otros métodos    *
     // ********************

    /**
     * Función para que el jugador pueda moverse a nado dentro de la zona, no se permite cambiar de zona a nado.
     * @param profundidad_nueva tipo: int; descripción: Cantidad en metros que se está moviendo el personaje
     * @param zona tipo: Zona; descripción: Zona actual del personaje.
     */
     public void profundidadActualizar(int profundidad_nueva, Zona zona){
        if (profundidad_nueva < zona.getProfundidadMin() || profundidad_nueva > zona.getProfundidadMax()){
            System.out.println("Esa profundidad está fuera del rango permitido de la zona.");
        }
         int delta = Math.abs(profundidad_nueva - this.profundidadActual);
         double d = zona.normalizarProfundidad(profundidad_nueva);
         int costo = FormulaO2.cMover(d, delta);

         tanqueOxigeno.consumirO2(costo);
         this.profundidadActual = profundidad_nueva;

         System.out.println("Movimiento a Profundidad: " + profundidad_nueva + " m, costo de O2: " + costo);
     }
}
