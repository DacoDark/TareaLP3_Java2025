package objetos;

import entorno.Zona;
import entorno.Zonas;
import player.Jugador;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;

/**
 * Nave principal del Jugador
 * Principal transporte del jugador entre zonas y poder guardar sus objetos
 * Aquí se pueden construir cosas.
 */
public class NaveExploradora extends Vehiculo implements AccesoProfundidad {
    private final int profundidadSoportada;
    private int profundidadAnclaje;
    private final ModuloProfundidad modulo; //Proveniente de la clase anidada

    public NaveExploradora() {
        this.profundidadSoportada  = 500;       // límite base
        this.profundidadAnclaje = 0;            // Superficie por defecto
        this.modulo = new ModuloProfundidad();
        this.bodega = new ArrayList<>();
    }

    //*******************
    //*     Anclaje     *
    //*******************
    /**
     * Anclar la nave en una determinada profundidad por el jugador
     * @param profundidad_nueva tipo: int; descripción: profundidad a la que se va a anclar.
     */
    public void anclarNaveExploradora(int profundidad_nueva){
        if (profundidad_nueva <= getProfundidadMaximaPermitida()){
            this.profundidadAnclaje = profundidad_nueva;
            System.out.println("Nave Exploradora anclada a " + profundidad_nueva+ " m.");
        } else {
            System.out.println("La nave no puede descender tan profundo (máx " + getProfundidadMaximaPermitida() + "m).");
        }
    }

    public int getProfundidadAnclaje(){
        return profundidadAnclaje;
    }

    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        return getProfundidadMaximaPermitida() < profundidad_minima;
    }

    private int getProfundidadMaximaPermitida(){
        return modulo.isActivo() ? (profundidadSoportada + modulo.getProfundidadExtra()) : profundidadSoportada;
    }

    public Zona getZonaAnclajeActual() {
        if (profundidadAnclaje == 0) return Zonas.naveEstrellada;
        else if (profundidadAnclaje <= 199) return Zonas.arrecife;
        else if (profundidadAnclaje <= 999) return Zonas.profunda;
        else if (profundidadAnclaje <= 1500) return Zonas.volcanica;
        else return Zonas.naveEstrellada;
    }


    //*******************
    //*     Bodega      *
    //*******************

    @Override
    public void transferirObjetos(Jugador jugador, ItemTipo tipo, int cantidad){
        if (jugador.contarItem(tipo) < cantidad){
            System.out.println("No tienes esa cantidad en el inventario.");
        }
        agregarABodega(tipo,cantidad);
        jugador.consumirItem(tipo,cantidad);
        System.out.println("Guardaste " + cantidad + " de " + tipo + " en la bodega.");
    }
    @Override
    public void agregarABodega(ItemTipo tipo, int cantidad) {
        for(Item i : bodega){
            if(i.getTipo() == tipo){
                i.setCantidad(i.getCantidad()+cantidad);
            }
        }
        bodega.add(new Item(tipo,cantidad));
    }

    @Override
    public void retirarDeBodega(Jugador jugador, ItemTipo tipo, int cantidad) {
        for (Item i : bodega){
            if(i.getTipo() == tipo){
                if (i.getCantidad() >= cantidad){
                    i.setCantidad(i.getCantidad() - cantidad);
                    jugador.agregarItem(tipo,cantidad);
                    if (i.getCantidad() == 0) bodega.remove(i);
                    System.out.println("Retiraste " + cantidad + " de " + tipo + " de la bodega.");
                } else {
                    System.out.println("No tienes esa cantidad en la bodega.");
                }
            } else {
                System.out.println("No tienes de este recurso en la bodega.");
            }
        }
    }

    @Override
    public void verBodega() {
        System.out.println("\n=== Bodega de la NaveExploradora ===");
        if (bodega.isEmpty()){
            System.out.println("Vacía. Puedes guardar tus recursos aquí");
        }
        for (Item i : bodega){
            System.out.printf("- %s: %d%n", i.getTipo(), i.getCantidad());
        }
    }

    public List<Item> getBodega() {
        return bodega;
    }

    //*******************
    //*     Crafteo     *
    //*******************

    /**
     * Función que despliega el menú de creación de objetos, en está parte se maneja todo lo que tenga que ver con upgrades del personaje.
     * @param jugador tipo: Jugador; descripción: personaje que juega el juego.
     */
    public void menuCrafteo(Jugador jugador){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Menu Crafteo ===");
        System.out.println("Usa los materiales almacenados en la bodega.");
        verBodega();
        System.out.println("\n1. Fabricar traje térmico");
        System.out.println("2. Mejorar tanque de oxígeno");
        System.out.println("3. Mejorar Capacidad de oxígeno");
        System.out.println("4. Instalar módulo de profundidad");
        System.out.println("5. Menú Robot Excavador");
        System.out.println("6. Salir");
        System.out.println("\nElegir una opción: ");
        int opcion = sc.nextInt();
        switch (opcion){
            case 1 -> fabricarTrajeTermico(jugador);
            case 2 -> mejorarTanque(jugador);
            case 3 -> mejorarTanqueOxigeno(jugador);
            case 4 -> instalarModuloProfundidad(jugador);
            case 5 -> {
                System.out.println("1. Crear Robot Excavador");
                System.out.println("2. Excavar recursos automáticamente");
                System.out.println("3. Descargar recursos en nave");
                System.out.println("4. Reparar robot");
                System.out.println("5. Mejorar robot");
                System.out.println("6. Salir ");
                System.out.println("\nElegir una opción: ");
                int opRobot = sc.nextInt();

                switch (opRobot) {
                    case 1  -> ensamblarRobotExcavador(jugador);
                    case 2 -> {
                        if (jugador.getNave() == null){
                            jugador.getRobot().excavarRecursos(jugador);
                        } else {
                            System.out.println("Aún no tienes un robot excavador.");
                        }

                    }
                    case 3 -> {
                        if (jugador.getNave() == null){
                            jugador.getRobot().descargarEnNave(jugador.getNave());
                        } else {
                            System.out.println("Aún no tienes un robot excavador.");
                        }
                    }
                    case 4 -> {
                        if (tieneMateriales(ItemTipo.CABLES,4,ItemTipo.PIEZAS_METAL,3,ItemTipo.MAGNETITA,5)){
                            jugador.consumirItem(ItemTipo.CABLES,4);
                            jugador.consumirItem(ItemTipo.PIEZAS_METAL,3);
                            jugador.consumirItem(ItemTipo.MAGNETITA,5);
                            jugador.getRobot().reparar();
                        } else {
                            System.out.println("No tienes los materiales necesarios (4 cables, 3 Piezas de metal, 5 Magnetita)");
                        }
                    }
                    case 5 -> {
                        if (tieneMateriales(ItemTipo.TITANIO,10,ItemTipo.CUARZO,20)){
                            jugador.consumirItem(ItemTipo.TITANIO,10);
                            jugador.consumirItem(ItemTipo.CUARZO,20);
                            jugador.getRobot().mejorar();
                        } else {
                            System.out.println("No tienes los materiales necesarios (10 Titanio, 20 cuarzo)");
                        }

                    }
                }
            }
            default -> System.out.println("Opción no válida");
        }
    }

    /**
     * Función que permite fabricar el traje termico dado los ingredientes necesarios
     * @param jugador tipo: Jugador; descripción: personaje que juega el juego.
     */
    private void fabricarTrajeTermico(Jugador jugador){
        if (jugador.isTrajeTermico()){
            System.out.println("Ya tienes un traje térmico");
        }
        if (tieneMateriales(ItemTipo.SILICIO, 10, ItemTipo.ORO,3, ItemTipo.CUARZO,5)){
            consumirDeBodega(ItemTipo.SILICIO, 10);
            consumirDeBodega(ItemTipo.ORO, 3);
            consumirDeBodega(ItemTipo.CUARZO,5);
            jugador.setTrajeTermico();
            System.out.println("Traje termico fabricado con éxito");
        } else {
            System.out.println("Materiales insuficientes para crear el traje termico necesitas: \n(10 Silicio, 3 Oro, 5 Cuarzo)");
        }
    }

    /**
     * Función que permite fabricar la mejora del tanque del jugador.
     * @param jugador tipo: Jugador; descripción: personaje que juega el juego.
     */
    private void mejorarTanque(Jugador jugador){
        if (jugador.isMejoraTanque()){
            System.out.println("Ya tienes un mejora tanque");
        }
        if (tieneMateriales(ItemTipo.PIEZA_TANQUE,3)){
            consumirDeBodega(ItemTipo.PIEZA_TANQUE,3);
            jugador.setMejoraTanque(true);
            jugador.getTanqueOxigeno().aumentarOxigeno(60); //Base(60) mejora del 100% otros 60.
            System.out.println("Tanque de oxígeno mejorado, Ahora no sufrirás los efectos de la presión, Capacidad aumentada en un 100%.");
        } else {
            System.out.println("Materiales insuficientes para mejorar el tanque necesitas: \n(3 Pieza_Tanque)");
        }
    }

    /**
     * Función que permite mejorar la capacidad del Tanque de Oxígeno, para tener expediciones más largas.
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego.
     */
    private void mejorarTanqueOxigeno(Jugador jugador){
        if (jugador.isMejoraTanque()){
            System.out.println("Necesitas mejorar el tanque primero");
        }
        if (tieneMateriales(ItemTipo.PLATA,10,ItemTipo.CUARZO,15)){
            consumirDeBodega(ItemTipo.PLATA,10);
            consumirDeBodega(ItemTipo.CUARZO,15);
            jugador.getTanqueOxigeno().aumentarOxigeno(30);
            System.out.println("Tanque de oxígeno mejorado, Capacidad aumentada en un 50%.");
        } else {
            System.out.println("Materiales insuficientes para mejorar la capacidad de oxígeno necesitas: \n(10 Plata,15 Cuarzo)");
        }
    }

    /**
     * Función que permite hacer la instalación del módulo de profundidad, para poder superar los 500 m
     * @param jugador tipo: Jugador; descripción: personaje que juega el juego
     */
    private void instalarModuloProfundidad(Jugador jugador){
        if (jugador.tieneModuloProfundidad()){
            System.out.println("Ya tienes el modulo de profundidad instalado");
        }
        if (tieneMateriales(ItemTipo.MODULO_PROFUNDIDAD,1)){
            consumirDeBodega(ItemTipo.MODULO_PROFUNDIDAD, 1);
            modulo.aumentarProfundidad(1000); //500 m permitidos, se le agrega 1000 para alcanzar los 1500 m.
        } else {
            System.out.println("Aún no has encontrado el módulo para instalarlo");
        }
    }

    /**
     * Función que permite fabricar al robot excavador y se le asigna al personaje.
     * @param jugador tipo: Jugador; descripción: personaje que juega el juego.
     */
    private void ensamblarRobotExcavador(Jugador jugador){
        if (tieneMateriales(ItemTipo.COBRE,15,ItemTipo.MAGNETITA,10,ItemTipo.DIAMANTE,5,ItemTipo.ACERO,20)){
            consumirDeBodega(ItemTipo.COBRE,15);
            consumirDeBodega(ItemTipo.MAGNETITA,10);
            consumirDeBodega(ItemTipo.DIAMANTE,5);
            consumirDeBodega(ItemTipo.ACERO,20);
            RobotExcavador robotExcavador = new RobotExcavador();
            jugador.setRobot(robotExcavador);
        } else {
            System.out.println("Materiales insuficientes para fabricar el robot excavador necesitas: \n(15 Cobre, 10 Magnetita, 5 Diamante, 20 Acero)");
        }
    }

    //*******************************
    //*     Auxiliares de Bodega    *
    //*******************************

    /**
     * Función para identificar los materiales que se necesitan y se tienen en el inventario para construir/mejorar cosas.
     * @param args tipo: args; descripción: Cantidad de argumentos que se pueden colocar, esto dependen de las zonas.
     * @return tipo: boolean; descripción: Retorna valor booleano si es que el jugador tiene las cantidades mencionadas en su inventario.
     */
    private boolean tieneMateriales(Object... args){
        for (int i = 0; i < args.length; i += 2){
            ItemTipo tipo = (ItemTipo) args[i];
            int cantidad = (int)args[i+1];
            if (contarEnBodega(tipo) < cantidad){
                return false;
            }
        }
        return true;
    }

    /**
     * Función para eliminar elementos de la bodega
     * @param tipo tipo: ItemTipo; descripción: tipo del item que se va a eliminar de la bodega
     * @param cantidad tipo: int; descripción: número entero de la cantidad del tipo a eliminar de la bodega.
     */
    private void consumirDeBodega(ItemTipo tipo, int cantidad) {
        Iterator<Item> it = bodega.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getTipo() == tipo) {
                int nuevaCantidad = item.getCantidad() - cantidad;
                if (nuevaCantidad <= 0) {
                    it.remove();  // eliminación segura
                } else {
                    item.setCantidad(nuevaCantidad);
                }
                return;
            }
        }
        System.out.println("No se encontró " + tipo + " en la bodega o no hay suficiente cantidad.");
    }

    /**
     * Función para contar los items que se tienen guardados en bodega
     * @param tipo tipo: ItemTipo; descripción: Tipo del item del cual se quiere saber la cantidad
     * @return tipo: int; descripción: número entero de la cantidad que se encuentra en bodega.
     */
    private int contarEnBodega(ItemTipo tipo){
        for (Item i : bodega){
            if (i.getTipo() == tipo)
                return i.getCantidad();
        }
        return 0;
    }



    /**
     * Clase anidada para el Módulo de profundidad
     */
    public static class ModuloProfundidad{
        private boolean activo;
        private int profundidad_extra;

        //***************************************************
        //*     Método Constructor de la clase anidada      *
        //***************************************************
        public ModuloProfundidad(){
            this.activo = false;
            this.profundidad_extra = 0;
        }

        /**
         * Función que determina que una vez instalado el módulo, se pueda extender la profundidad de la nave.
         * @param profundidadExtra tipo: int; descripción: Cantidad de profundidad la cual se va a agregar a la nave
         */
        public void aumentarProfundidad(int profundidadExtra) {
            if (!activo) {
                this.profundidad_extra = profundidadExtra;
                this.activo = true;
                System.out.println("Módulo de profundidad activado: +" + profundidadExtra + " m.");
            }
        }

        /**
         * Función que retorna el valor de verdad, si está instalado el módulo o no.
         * @return tipo: boolean; descripción: Valor de verdad que significa si está instalado el módulo de profundidad en la nave
         */
        public boolean isActivo(){
            return activo;
        }

        /**
         * Función para obtener la profundidad máxima de una zona
         * @return tipo: int; descripción: Cantidad entera de la profundidad extra.
         */
        public int getProfundidadExtra(){
            return profundidad_extra;
        }
    }

    /**
     * Permite obtener la referencia al módulo para instalar/configurar desde fuera
     * @return referencia al objeto modulo
     */
    public ModuloProfundidad getModuloProfundidad(){
        return modulo;
    }
}
