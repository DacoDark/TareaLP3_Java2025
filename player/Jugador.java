package player;

import entorno.Zonas;
import objetos.*;
import entorno.Zona;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa al jugador personaje principal del juego
 */
public class Jugador implements AccesoProfundidad {
    private final Oxigeno tanqueOxigeno;
    private final List<Item> inventario;
    private Zona zonaActual;
    private int profundidadActual;
    private boolean tienePlanos;
    private NaveExploradora nave;
    private RobotExcavador robot;
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
        this.moduloInstalado = true;
        this.inventario = new ArrayList<>();
        this.zonaActual = Zonas.naveEstrellada;
    }

    //*******************************************
    //*     Estado y propiedades del jugador    *
    //*******************************************

    /**
     * Imprime por pantalla el estado del jugador y de su robot en caso de tener uno.
     */
    public void verEstadoJugador(){
        System.out.println("\n===   Estado Jugador  ===");
        System.out.println("Profundidad: " + profundidadActual + " m");
        System.out.println("Oxígeno: " + tanqueOxigeno.getOxigenoRestante() + "/" + tanqueOxigeno.getCapacidadMaxima());
        System.out.println("Mejora Tanque: " + (mejoraTanque ? "Sí" : "No"));
        System.out.println("Traje Térmico: " + (trajeTermico ? "Sí" : "No"));
        System.out.println("Modulo Instalado: " + (moduloInstalado ? "Sí" : "No"));
        System.out.println("Planos: " + (tienePlanos ? "Sí" : "No"));
        System.out.println("Zona: " + zonaActual.getNombre());
        verInventario();

        if (robot != null){
            System.out.println("\n=== Estado del Robot Excavador ===");
            System.out.printf("Nivel: %d%n", robot.getNivel());
            System.out.printf("Energía: %d / %d%n", robot.getEnergia(), robot.getEnergiaMax());
            System.out.printf("Durabilidad: %d / %d%n", robot.getDurabilidad(), robot.getDurabilidadMax());
            System.out.printf("Carga: %d / %d%n", robot.getCargaActual(), robot.getCapacidad_carga());
            System.out.println("Estado: " + (robot.isAveriado() ? "Dañado" : "Operativo"));
        } else {
            System.out.println("\nNo tienes un robot excavador asignado.");
        }
    }

    /**
     * Función que verifica si tiene las mejoras para poder acceder a zonas más profundas.
     * Para pasar de 500 m en la zona profunda, necesita la mejora del módulo. (no es necesario, pero recomendable mejorar la capacidad del tanque)
     * Para acceder a la zona Volcanica necesita la mejora del tanque y el traje termico(decisión propia que le da sentido al juego).
     * @param profundidad_minima tipo: int; descripción: Profundidad a la que se quiere acceder
     * @return tipo: boolean; descripción: Retorna el valor de verdad si cumple las condiciones necesarias de la zona. (lógica invertida para simplificar la validación)
     */
    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        if(profundidad_minima >= 1000){
            // Volcánica o más abajo
            return !this.mejoraTanque || !this.trajeTermico;
        } else if (profundidad_minima >= 500) {
            // Zona Profunda
            return !this.moduloInstalado;
        } else {
            //Arrecife y Nave estrellada
            return false;
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
    public void agregarItem(ItemTipo tipo, int cantidad) {
        if (tipo == null || cantidad <= 0) return;

        // buscar si ya existe
        for (Item i : inventario) {
            if (i.getTipo() == tipo) {
                i.setCantidad(i.getCantidad() + cantidad);
                //System.out.println("[DEBUG] agregado" + tipo + "(ya existía) -> total: " + i.getCantidad());
                return;
            }
        }

        // si no existe, crear uno nuevo
        Item nuevo = new Item(tipo, cantidad);
        inventario.add(nuevo);
        //System.out.println("[DEBUG] agregado nuevo " + tipo + " x" + cantidad);
    }

    /**
     * Función para consumir objetos del inventario.
     * @param tipo tipo: ItemTipo; descripción: Tipo del item que se quiere contar.
     */
    public void consumirItem(ItemTipo tipo,int cantidad){
        for (int i = 0; i < inventario.size(); i++) {
            Item item = inventario.get(i);
            if (item.getTipo() == tipo){
                int nueva_cantidad = item.getCantidad() - cantidad;
                if (nueva_cantidad <= 0) {
                    inventario.remove(i);
                    break;
                } else item.setCantidad(nueva_cantidad);
                //System.out.println("[DEBUG] eliminado " + tipo + " x" + cantidad);
            }
        }
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
            System.out.println("Inventario vacío");
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
        //System.out.println("[DEBUG] vaciarInventario ->" + verInventario());
    }

    //***********************************
    //* Getter y Setters del jugador    *
    //***********************************

    /**
     * Getter del Tanque de oxigeno
     * @return tipo: Oxígeno; descripción: Devuelve el oxígeno que tendrá el personaje
     */
    public Oxigeno getTanqueOxigeno() {
        return tanqueOxigeno;
    }

    /**
     * Getter de la Profundidad actual
     * @return tipo: int; descripción: Devuelve el número de la profundidad en la que se encuentra el personaje
     */
    public int getProfundidadActual() {
        return profundidadActual;
    }

    /**
     * Setter de la profundidad actual
     * @param profundidadActual tipo: int; descripción: profundidad actual del personaje en la cual se va a registrar.
     */
    public void setProfundidadActual(int profundidadActual) { this.profundidadActual = profundidadActual; }

    /**
     * Función que pregunta por si tiene el módulo instalado y actualiza la bandera de progreso.
     * @return tipo: boolean; descripción: Valor de verdad sobre sí se ha instalado el módulo en la nave.
     */
    public boolean tieneModuloProfundidad(){
        if (nave.getModuloProfundidad().isActivo()){
            setModuloInstalado(true);
        }
        return moduloInstalado;
    }

    /**
     * Setter del Modulo
     * @param valor tipo: boolean; descripción: Valor de verdad sobre el módulo instalado, setea la bandera de progreso.
     */
    public void setModuloInstalado(boolean valor) {
        this.moduloInstalado = valor;
    }

    /**
     * Función que pregunta si el jugador posee la mejora del tanque instalada.
     * @return tipo: boolean; descripción: Valor de verdad sobre si el jugador cuenta con la mejora del tanque.
     */
    public boolean isMejoraTanque() {
        return mejoraTanque;
    }

    /**
     * Función que asigna el estado de mejora del tanque.
     * @param mejoraTanque tipo: boolean; descripción: Valor que define si el tanque ha sido mejorado o no.
     */
    public void setMejoraTanque(boolean mejoraTanque) {
        this.mejoraTanque = mejoraTanque;
    }

    /**
     * Función que pregunta si el jugador tiene el traje térmico equipado.
     * @return tipo: boolean; descripción: Valor de verdad sobre si el jugador posee el traje térmico.
     */
    public boolean isTrajeTermico() {
        return trajeTermico;
    }

    /**
     * Función que activa la bandera de progreso correspondiente al traje térmico.
     * Desbloquea las exploraciones ilimitadas en la Nave Estrellada y otras zonas de calor extremo.
     */
    public void setTrajeTermico() {
        this.trajeTermico = true;
    }

    /**
     * Función que pregunta si el jugador ha obtenido los planos de reparación de la nave estrellada.
     * @return tipo: boolean; descripción: Valor de verdad sobre si el jugador posee los planos.
     */
    public boolean isTienePlanos() {
        return tienePlanos;
    }

    /**
     * Función que actualiza la bandera de progreso al adquirir los planos de reparación.
     * Permite al jugador reparar la nave estrellada al volver a ella.
     */
    public void setTienePlanos() {
        this.tienePlanos = true;
    }

    /**
     * Función que retorna la lista completa de ítems en el inventario del jugador.
     * @return tipo: List<Item>; descripción: Lista con los objetos actualmente poseídos por el jugador.
     */
    public List<Item> getInventario() {
        return inventario;
    }

    /**
     * Función que obtiene la zona actual en la que se encuentra el jugador.
     * @return tipo: Zona; descripción: Zona actual donde está el jugador (Arrecife, Profunda, Volcánica o Nave Estrellada).
     */
    public Zona getZonaActual() {
        return zonaActual;
    }

    /**
     * Función que actualiza la zona actual del jugador.
     * @param zonaActual tipo: Zona; descripción: Nueva zona asignada al jugador.
     */
    public void setZonaActual(Zona zonaActual) {
        this.zonaActual = zonaActual;
    }

    /**
     * Función que asocia una instancia de la Nave Exploradora al jugador.
     * @param nave tipo: NaveExploradora; descripción: Referencia a la nave actualmente controlada por el jugador.
     */
    public void setNave(NaveExploradora nave) {
        this.nave = nave;
    }

    /**
     * Función que obtiene la nave asociada al jugador.
     * @return tipo: NaveExploradora; descripción: Referencia de la nave exploradora controlada por el jugador.
     */
    public NaveExploradora getNave() {
        return this.nave;
    }

    /**
     * Función que obtiene el robot excavador asociado al jugador.
     * @return tipo: RobotExcavador; descripción: Referencia al robot excavador del jugador (puede ser null si no ha sido construido).
     */
    public RobotExcavador getRobot() {
        return this.robot;
    }

    /**
     * Función que asigna el robot excavador al jugador.
     * @param robot tipo: RobotExcavador; descripción: Objeto que representa el robot excavador controlado por el jugador.
     */
    public void setRobot(RobotExcavador robot) {
        this.robot = robot;
    }

    /**
     * Función que pregunta si el jugador ha completado el juego (reparación de la nave estrellada).
     * @return tipo: boolean; descripción: Valor de verdad sobre sí se ha completado la misión principal del juego.
     */
    public boolean isJuegoCompletado() {
        return juegoCompletado;
    }

    /**
     * Función que actualiza el estado de finalización del juego.
     * @param juegoCompletado tipo: boolean; descripción: Valor que indica si el jugador ha completado el objetivo final.
     */
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
         int profundidadActual = this.profundidadActual;

         //Verificar si la nueva profundidad está dentro del rango definido por la zona
         if (profundidad_nueva < zona.getProfundidadMin() || profundidad_nueva > zonaActual.getProfundidadMax()){
             Zona nuevaZona = determinarZonaPorProfundidad(profundidad_nueva);
             if (nuevaZona != null){
                 System.out.println("Has entrado en una nueva zona: "+nuevaZona.getNombre() + "!");
                 this.zonaActual = nuevaZona;
                 nuevaZona.entrar(this);
             } else {
                 System.out.println("No existe ninguna zona en esa profundidad");
                 return;
             }
         }

         if (profundidad_nueva < 0){
            System.out.println("No puedes subir más, ya estás en la superficie");
            return;
         }

         //Validar si el jugador o la nave soportan esa profundidad
         if((this.puedeAcceder(profundidad_nueva)) || nave.puedeAcceder(profundidad_nueva)){
             System.out.println("No puedes descender a " + profundidad_nueva + " m. La presión es demasiado alta");
             System.out.println("tu traje no soportan esa presión, mejora el tanque en la nave para poder explorar más profundo.");

             return;
         }

         int delta_profundidad = profundidad_nueva - profundidadActual;
         int costo = FormulaO2.cMover(this, zona,delta_profundidad);

         //Si la presión es infinita, no puede moverse
         if (costo == Integer.MAX_VALUE){
             System.out.println("La presión es demasiado alta, para descender sin mejorar el tanque");
             return;
         }

         //Actualizar profundidad y aplicar costo
         this.profundidadActual = profundidad_nueva;
         tanqueOxigeno.consumirO2(costo);
         System.out.println("Movimiento a Profundidad: " + profundidad_nueva + " m, costo de O2: " + costo);
         //Verificar si se quedó sin Oxígeno - Manejar Muerte de Jugador
         if (this.getTanqueOxigeno().getOxigenoRestante() <= 0){
             System.out.println("Te has quedado sin oxígeno durante la inmersión...");
             System.out.println("Pierdes todo tu inventario y reapareces en la nave.");

             //Vaciar inventario
             this.vaciarInventario();

             //Reaparecer en nave anclada
             this.profundidadActual = nave.getProfundidadAnclaje();

             //Determinar zona según profundidad del anclaje
             Zona zonaActual = determinarZonaPorProfundidad(profundidadActual);
             if (zonaActual != null){
                 this.setZonaActual(zonaActual);
                 zonaActual.entrar(this);
             }

             //Recargar Oxígeno
             this.getTanqueOxigeno().recargarCompleto();
             System.out.println("Has reaparecido en la nave anclada a " + profundidadActual + " m. Oxígeno recargado.");
         }
     }

    /**
     * Función para detectar la zona en la que se encuentra en profundidad para poder ser actualizada en caso de cambio.
     * @param profundidad_nueva tipo: int; descripción: profundidad en la que se encuentra el personaje
     * @return tipo: Zona; descripción: Zona en la que se encuentra el personaje según la profundidad.
     * (Se considera Arrecife entre [1,199]) para evitar conflicto.
     */
     public Zona determinarZonaPorProfundidad(int profundidad_nueva){
         if (profundidad_nueva == 0) return Zonas.naveEstrellada;
         if (profundidad_nueva > 0 && profundidad_nueva <= 199) return entorno.Zonas.arrecife;
         if (profundidad_nueva >= 200 && profundidad_nueva <= 999) return entorno.Zonas.profunda;
         if (profundidad_nueva >= 1000 && profundidad_nueva <= 1500) return entorno.Zonas.volcanica;
         return null;
     }
}
