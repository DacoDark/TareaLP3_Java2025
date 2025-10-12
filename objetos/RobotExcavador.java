package objetos;

import player.Jugador;
import entorno.Zona;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        this.capacidad_carga = 1000; //¿Estará bien el número?
        this.cargaActual = 0;
        this.nivel = 1;
        this.activo = false;
        this.averiado = false;
        this.rand = new Random();
        this.bodega = new ArrayList<>();
    }

    //*******************
    //*     Acciones    *
    //*******************

    public void excavarRecursos(Jugador jugador){
        if (averiado){
            System.out.println("El robot está averiado. Requiere reparación");
        }
        if (energia < 10){
            System.out.println("Energía insuficiente. Recarga el robot en la nave");
        }
        if (cargaActual >= capacidad_carga){
            System.out.println("Capacidad de carga completa. Descarga el robot en la nave");
        }

        activo = true;
        Zona zona = jugador.getZonaActual();
        System.out.println("🤖 El robot comienza a excavar en " + zona.getNombre() + "...");

        // Convertir el EnumSet en una lista para selección aleatoria
        List<ItemTipo> listaRecursos = new ArrayList<>(zona.getRecursos());
        if (listaRecursos.isEmpty()) {
            System.out.println("No hay recursos disponibles en esta zona.");
            activo = false;
            return;
        }

        int acciones = nivel; //Más nivel -> más acciones automáticas
        for (int i = 0; i < acciones; i++){
            if (energia < 10 || cargaActual >= capacidad_carga) break;

            energia -= 10;
            durabilidad -= rand.nextInt(5)+2;

            if (durabilidad <= 0){
                averiado = true;
                activo = false;
                System.out.println("El robot se ha dañado durante la excavación");
                break;
            }

            //Recursos aleatorios de la zona actual
            ItemTipo tipo = listaRecursos.get(rand.nextInt(listaRecursos.size()));
            int cantidad = rand.nextInt(2)+1;

            if (cargaActual + cantidad > capacidad_carga){
                cantidad = capacidad_carga - cargaActual;

                agregarABodega(tipo, cantidad);
                cargaActual = cantidad;

                System.out.println("Recolectó " + cantidad + " de " + tipo + " (Energía restante: " + energia + ")");
            }

            activo = false;
            if (!averiado){
                System.out.println("Excavación completa. Carga actual: " + cargaActual + "/" + capacidad_carga);
            }
        }
    }

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

    public void reparar(){
        if (!averiado){
            System.out.println("El robot no necesita reparación.");
        }

        durabilidad = durabilidadMax;
        averiado = false;
        System.out.println("El robot a sido reparado");
    }

    @Override
    public boolean puedeAcceder(int profundidad_minima){
        //Depende de la nave/jugador
        return true;
    }

    @Override
    public void transferirObjetos(Jugador jugador, ItemTipo tipo, int cantidad) {
        if (contarEnBodega(tipo) < cantidad){
            System.out.println("El robot no tiene esa cantidad de "+tipo);
        }
        eliminarDeBodega(tipo, cantidad);
        jugador.agregarItem(tipo, cantidad);
        System.out.println("Transferidos "+cantidad+" de "+tipo + " al jugador");
    }

    @Override
    public void agregarABodega(ItemTipo tipo, int cantidad) {
        for (Item i : bodega){
            if (i.getTipo() == tipo){
                i.setCantidad(i.getCantidad() + cantidad);
            }
        }
        bodega.add(new Item(tipo, cantidad));
    }

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

    @Override
    public void retirarDeBodega(Jugador jugador, ItemTipo tipo, int cantidad) {
        if (contarEnBodega(tipo) < cantidad){
            System.out.println("El robot no tiene esa cantidad de "+tipo);
        }
        eliminarDeBodega(tipo,cantidad);
        jugador.agregarItem(tipo, cantidad);
        cargaActual -= cantidad;
        System.out.println(" Retiraste " + cantidad + " de " + tipo + " del robot.");
    }

    //***********************
    //*     Auxiliares      *
    //***********************

    private int contarEnBodega(ItemTipo tipo){
        for (Item i : bodega){
            if (i.getTipo() == tipo) return i.getCantidad();
        }
        return 0;
    }

    private void eliminarDeBodega(ItemTipo tipo, int cantidad){
        for (int i = 0; i < bodega.size(); i++){
            Item item = bodega.get(i);
            if (item.getTipo() == tipo) {
                item.setCantidad(item.getCantidad() - cantidad);
                if (item.getCantidad() <= 0) {
                    bodega.remove(i);
                }
            }
        }
    }

    //*******************
    //*     Mejoras     *
    //*******************

    public void mejorar(){
        if (nivel >= 3){
            System.out.println("El robot ya está en su nivel máximo");
        }
        nivel++;
        energiaMax += 25;
        durabilidadMax += 25;
        durabilidad = durabilidadMax;
        capacidad_carga += 25;
        System.out.println("⚙️ Robot mejorado a nivel " + nivel + ". Capacidad de carga: " + capacidad_carga + ".");
    }

    //*******************
    //*   Getters       *
    //*******************

    public int getEnergia() { return energia; }
    public int getEnergiaMax() { return energiaMax; }
    public int getDurabilidad() { return durabilidad; }
    public int getDurabilidadMax() { return durabilidadMax; }
    public int getCapacidad_carga() { return capacidad_carga; }
    public int getCargaActual() { return cargaActual; }
    public boolean isAveriado() { return averiado; }
    public int getNivel() { return nivel; }
}
