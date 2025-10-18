package objetos;

import player.Jugador;
import entorno.Zona;

import java.util.*;

/**
 * Representa al robot excavador que controla el jugador.
 * Puede recolectar recursos automáticamente, almacenarlos en su bodega y descargarlos en la NaveExploradora
 */
public class RobotExcavador extends Vehiculo{
    private int capacidad_carga;
    private int cargaActual;
    private int energia;
    private int energiaMax;
    private int durabilidad;
    private int durabilidadMax;
    private boolean activo;
    private boolean averiado;
    private int nivel;
    private final Random rand;

    /**
     * Constructor de la clase
     */
    public RobotExcavador() {
        this.energiaMax = 100;
        this.energia = energiaMax;
        this.durabilidadMax = 100;
        this.durabilidad = durabilidadMax;
        this.capacidad_carga = 1000; //¿Estará bien el número encuentro que es mucho?
        this.cargaActual = 0;
        this.nivel = 1;
        this.activo = true;
        this.averiado = false;
        this.rand = new Random();
        this.bodega = new ArrayList<>();
    }

    //*******************
    //*     Acciones    *
    //*******************

    /**
     * Función para sacar recursos automáticamente, siempre que no esté averiado o tenga energía.
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego.
     */
    public void excavarRecursos(Jugador jugador){
        if (averiado) {
            System.out.println("El robot está averiado y no puede excavar. Debes repararlo primero.");
            return;
        }

        if (!activo) {
            System.out.println("El robot está desactivado. Actívalo antes de usarlo.");
            return;
        }

        if (energia <= 0) {
            System.out.println("El robot no tiene energía suficiente para excavar.");
            activo = false;
            return;
        }

        Zona zonaActual = jugador.getZonaActual();
        System.out.println("El robot comienza a excavar en " + zonaActual.getNombre() + "...");

        // Determinar recursos disponibles según la zona
        EnumSet<ItemTipo> recursosZona = zonaActual.getRecursos();

        if (recursosZona == null || recursosZona.isEmpty()) {
            System.out.println("No hay recursos excavables en esta zona.");
            return;
        }

        // Elegir aleatoriamente un recurso y cantidad
        ArrayList<ItemTipo> lista = new ArrayList<>(recursosZona);
        while (energia > 10 || !averiado || cargaActual < capacidad_carga) {

            ItemTipo recursoEncontrado = lista.get(rand.nextInt(lista.size()));
            int cantidad = rand.nextInt(3) + 1; // entre 1 y 3 unidades
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Verificar capacidad disponible
            if (cargaActual + cantidad > capacidad_carga) {
                System.out.println("Capacidad máxima alcanzada. Descarga el robot antes de seguir excavando.");
                averiado = true;
                return;
            }

            // Agregar recurso a la bodega del robot
            agregarABodega(recursoEncontrado, cantidad);
            cargaActual += cantidad;
            System.out.println("El robot encontró: "+cantidad+ " de " + recursoEncontrado);

            // Gastar energía
            energia -= 5;
            if (energia < 0) energia = 0;

            // Pequeña probabilidad de avería
            if (rand.nextDouble() < 0.05) {
                averiado = true;
                System.out.println("El robot sufrió daños durante la excavación. Debe ser reparado.");
                break;
            }
        }
        System.out.println("Excavación completa. Carga actual: " + cargaActual + "/" + capacidad_carga);
    }


        /**
         * Función para dejar todos los items que tiene el robot en la bodega de la nave.
         * @param nave tipo: NaveExploradora; descripción: Dirección de la nave en la cual se dejara el inventario.
         */
    public void descargarEnNave(NaveExploradora nave){
        if (bodega.isEmpty()){
            System.out.println("El robot no tiene recursos para descargar.");
        }
        System.out.println("Descargando recursos del robot en la bodega");
        for (Item i : bodega){
            nave.agregarABodega(i.getTipo(), i.getCantidad());
        }
        bodega.clear();
        cargaActual = 0;
        System.out.println("Descarga completa de recursos. La bodega del robot quedo vacía");
    }

    /**
     * Función que repara el Robot Excavador, una vez que está averiado. (Se puede averiar por superar la carga o durabilidad en 0)
     */
    public void reparar(){
        if (!averiado){
            System.out.println("El robot no necesita reparación.");
            return;
        }

        durabilidad = durabilidadMax;
        averiado = false;
        activo = true;
        System.out.println("El robot a sido reparado");
    }

    /**
     * Función que verifica si puede acceder a alguna zona. Como sigue al jugador la implementación de esta función no es necesaria.
     * @param profundidad_minima tipo: int; descripción: Profundidad a la que se está ingresando(no tiene implementación).
     * @return tipo: boolean; descripción: No tiene implementación
     */
    @Override
    public boolean puedeAcceder(int profundidad_minima){
        //Depende de la nave/jugador
        return false;
    }

    /**
     * Función que transfiere items al inventario del jugador.
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se va a transferir
     * @param cantidad tipo: int; descripción: Cantidad del item que se va a transferir
     */
    @Override
    public void transferirObjetos(Jugador jugador, ItemTipo tipo, int cantidad) {
        if (contarEnBodega(tipo) < cantidad){
            System.out.println("El robot no tiene esa cantidad de "+tipo);
        }
        retirarDeBodega(jugador, tipo, cantidad);
        jugador.agregarItem(tipo, cantidad);
        System.out.println("Transferidos "+cantidad+" de "+tipo + " al jugador");
    }

    /**
     * Función que agrega items a bodega dado un tipo y una cantidad específica. (Esta función es llamado por DescargarEnNave())
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se agrega a la bodega del Robot
     * @param cantidad tipo: int; descripción: Cantidad del item que se agrega a la bodega del Robot
     */
    @Override
    public void agregarABodega(ItemTipo tipo, int cantidad) {
        for (Item i : bodega){
            if (i.getTipo() == tipo){
                i.setCantidad(i.getCantidad() + cantidad);
                return;
            }
        }
        bodega.add(new Item(tipo, cantidad));
    }

    /**
     * Función que lista los items que tiene actualmente el Robot en su bodega, además de información como la carga total.
     */
    @Override
    public void verBodega() {
        System.out.println("\n=== Bodega del Robot Excavador ===");
        if (bodega.isEmpty()){
            System.out.println("Vacía. Aún no recolectas recursos aquí");
        }
        for (Item i : bodega){
            System.out.printf("- %s: %d%n", i.getTipo(), i.getCantidad());
        }
        System.out.println("Carga total: " + cargaActual + "/" + capacidad_carga);
    }

    /**
     * Función que quita un Item de la bodega reduciendo inventario, y si se saca completo lo elimina.
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego.
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se va a extraer
     * @param cantidad tipo: int; descripción: Cantidad del item que se va a extraer
     */
    @Override
    public void retirarDeBodega(Jugador jugador, ItemTipo tipo, int cantidad) {
        Iterator<Item> it = bodega.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getTipo() == tipo) {
                int nuevaCantidad = item.getCantidad() - cantidad;
                if (nuevaCantidad <= 0) {
                    // eliminación segura
                    it.remove();
                } else {
                    item.setCantidad(nuevaCantidad);
                }
            }
        }
        System.out.println("No se encontró " + tipo + " en la bodega o no hay suficiente cantidad.");
    }

    //***********************
    //*     Auxiliares      *
    //***********************

    /**
     * Función para contar y poder listar de manera correcta los Items
     * @param tipo tipo: ItemTipo; descripción: Tipo del Item que se quiere contar.
     * @return tipo: int; descripción: Número de veces que se repite el item en el inventario.
     */
    private int contarEnBodega(ItemTipo tipo){
        for (Item i : bodega){
            if (i.getTipo() == tipo) return i.getCantidad();
        }
        return 0;
    }

    //*******************
    //*     Mejoras     *
    //*******************

    /**
     * Función que mejora las características del Robot, Nivel+1, Energía +25%, CapacidadMáx +25%, Durabilidad +25%
     */
    public void mejorar(){
        if (nivel >= 3){
            System.out.println("El robot ya está en su nivel máximo");
        }
        nivel++;
        energiaMax += 25;
        durabilidadMax += 25;
        durabilidad = durabilidadMax;
        capacidad_carga += 250;
        System.out.println("Robot mejorado a nivel " + nivel + ". Capacidad de carga mejorada un 25%: " + capacidad_carga + "Energía mejorada un 25%:" + energiaMax + "durabilidad mejorada un 25%:" + durabilidad);
    }

    public void recargar(){
        energia = energiaMax;
        activo = true;
        System.out.println("Recargaste a tu robot, puede volver a salir a excavar.");
    }

    //*******************
    //*   Getters       *
    //*******************

    /**
     * Getters de la energía
     * @return tipo: int; descripción: Cantidad actual de la energía del robot
     */
    public int getEnergia() { return energia; }

    /**
     * Getter de la energía máxima
     * @return tipo: int; descripción: Cantidad máxima de la energía del robot.
     */
    public int getEnergiaMax() { return energiaMax; }

    /**
     * Getter de la durabilidad
     * @return tipo: int; descripción: Cantidad de durabilidad del robot.
     */
    public int getDurabilidad() { return durabilidad; }

    /**
     * Getter de la durabilidad máxima
     * @return tipo: int; descripción: Cantidad de la durabilidad máxima del robot.
     */
    public int getDurabilidadMax() { return durabilidadMax; }

    /**
     * Getter de la capacidad de carga
     * @return tipo: int; descripción: Cantidad de la capacidad de carga del robot.
     */
    public int getCapacidad_carga() { return capacidad_carga; }

    /**
     * Getter de la carga actual
     * @return tipo: int; descripción: Cantidad de la carga actual del robot.
     */
    public int getCargaActual() { return cargaActual; }

    /**
     * Getter de la condición de averiado
     * @return tipo: boolean; descripción: Valor de verdad del estado del robot.
     */
    public boolean isAveriado() { return averiado; }

    /**
     * Getter del nivel
     * @return tipo: int; descripción: Cantidad del nivel actual del robot.
     */
    public int getNivel() { return nivel; }
}
