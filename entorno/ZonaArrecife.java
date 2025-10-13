package entorno;

import player.Jugador;
import player.FormulaO2;
import objetos.ItemTipo;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

/**
 * Zona del Arrecife (0-199 m)
 * Recursos: Cuarzo, Silicio, Cobre.
 * Aquí se pueden encontrar las piezas_tanque para mejorar el Tanque de oxígeno
 */
public class ZonaArrecife extends Zona {
    private int piezasTanque = 3;
    private final Random rand;

    /**
     * Constructor de la clase
     */
    public ZonaArrecife() {
        this.nombre = "Zona Arrecife";
        this.profundidadMin = 1;
        this.profundidadMax = 199;
        rand = new Random();
        this.presion = 0;
        this.recursos = EnumSet.of(
                ItemTipo.CUARZO,
                ItemTipo.SILICIO,
                ItemTipo.COBRE
        );
    }

    /**
     * Función que verifica si el jugador puede entrar a la zona.
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego.
     */
    @Override
    public void entrar(Jugador jugador) {
        if (jugador.puedeAcceder(profundidadMin)){
            System.out.println("No puedes entrar a la Zona Arrecife");
            return;
        }
        System.out.println("Entrando en Zona Arrecife (0-199m)");
    }

    /**
     * Función que recolecta objetos dado un tipo específico que se busca
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     * @param tipo tipo: ItemTipo; Descripción: Tipo del item que quiere encontrar.
     */
    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo) {
        if (!recursos.contains(tipo)){
            System.out.println("Ese recurso no se encuentra en esta zona.");
        }

        //Cambia la cantidad de loot con la zona (min:2; max:6)
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        int cantidad = cantidadLootRecolectar(d);
        jugador.agregarItem(tipo,cantidad);
        int costo = FormulaO2.cRecolectar(jugador, this);
        jugador.getTanqueOxigeno().consumirO2(costo);
        System.out.println("Recolectaste " + cantidad +" de " + tipo + " (costo O2: " + costo + ")");
    }

    /**
     * Función que explora la zona por objetos únicos y en caso de no encontrar adquiere objetos aleatorios de la zona
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     */
    @Override
    public void explorarZona(Jugador jugador) {
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        int costo = FormulaO2.cExplorar(jugador, this);
        jugador.getTanqueOxigeno().consumirO2(costo);
        System.out.println("Exploras el colorido arrecife, lleno de vida marina");
        if (piezasTanque>0 && rand.nextDouble() < 0.3) {
            jugador.agregarItem(ItemTipo.PIEZA_TANQUE,1);
            piezasTanque--;
            System.out.println("¡Encontraste una PIEZA_TANQUE! (Costo O2: " + costo + ")");
        } else {
            var listaRecursos = new ArrayList<>(recursos);
            ItemTipo encontrado = listaRecursos.get(rand.nextInt(listaRecursos.size()));
            int cantidad = cantidadLootExploracion(d);
            jugador.agregarItem(encontrado,cantidad);
            System.out.println("Exploraste y obtuviste " + cantidad + " de " + encontrado + " (Costo O2: " + costo + ")");
        }
    }

    // ****************************************
    // *    Getters y Setters de la Clase     *
    // ****************************************
    /**
     * Getter del parámetro profundidad de la zona.
     * @return tipo:int; descripción: Valor de la profundidad mínima de la zona
     */
    public int getProfundidadMin() {
        return profundidadMin;
    }

    // ******************************
    // *    Métodos de la Clase     *
    // ******************************

    /**
     * La cantidad de loot por acción de recolectar aumenta con la profundidad y sigue la siguiente formula:
     * n(d) = max(1, floor(numero_min + (numero_max - numero_min)*d))
     *
     * @param d tipo: double; descripción: profundidad
     * @return Tipo:int; descripción: Cantidad encontrada.
     */
    private int cantidadLootRecolectar(double d) {
        return Math.max(1,(int)Math.floor(1 + (3 - 1) * d));
    }

    /**
     * La cantidad de loot por acción de explorar y no encontrar el objeto único, aumenta con la profundidad y sigue la siguiente fórmula:
     * n(d) = max(1, floor(numero_min*d))
     * @param d tipo: double; descripción: profundidad
     * @return Tipo:int; descripción: Cantidad encontrada.
     */
    private int cantidadLootExploracion(double d) {
        return Math.max(1,(int)Math.floor(1*d));
    }
}
