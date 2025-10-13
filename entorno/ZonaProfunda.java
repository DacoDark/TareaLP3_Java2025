package entorno;

import player.Jugador;
import player.FormulaO2;
import objetos.ItemTipo;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

/**
 * Zona Profunda (200 m-999 m)
 * Recursos: Plata, Oro, Acero, Diamante, Magnetita
 * -Libre acceso hasta la profundidad de 500 m
 * -Con módulo de profundidad acceso total
 * -Sin la mejora de tanque la presión hace efecto, una vez mejorado la presión suma 0 a la fórmula de oxígeno.
 */
public class ZonaProfunda extends Zona {
    private final Random rand;

    /**
     * Constructor de la clase
     */
    public ZonaProfunda() {
        this.nombre = "ZonaProfunda";
        this.profundidadMin = 200;
        this.profundidadMax = 999;
        this.presion = 10;
        rand = new Random();
        this.recursos = EnumSet.of(
                ItemTipo.PLATA,
                ItemTipo.ORO,
                ItemTipo.ACERO,
                ItemTipo.DIAMANTE,
                ItemTipo.MAGNETITA
        );
    }
    /**
     * Función que verifica si el jugador puede entrar a la zona.
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego.
     */
    @Override
    public void entrar(Jugador jugador) {
        System.out.println("Entrando en la Zona Profunda (200-999m) Oscuridad y presión creciente");
    }

    /**
     * Función que recolecta objetos dado un tipo específico que se busca
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     * @param tipo tipo: ItemTipo; Descripción: Tipo del item que quiere encontrar.
     */
    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo) {
        if (jugador.getProfundidadActual() > 500 && jugador.tieneModuloProfundidad()){
            System.out.println("No puedes recolectar más allá de 500 sin el modulo de profundidad.");
        }
        if (!recursos.contains(tipo)) {
            System.out.println("Ese tipo recurso no se encuentra en esta zona.");
        }

        double d = normalizarProfundidad(jugador.getProfundidadActual());
        int cantidad = cantidadLootRecolectar(d);
        jugador.agregarItem(tipo,cantidad);
        int costo = FormulaO2.cRecolectar(jugador, this);
        if (costo == Integer.MAX_VALUE) return;
        jugador.getTanqueOxigeno().consumirO2(costo);
        System.out.println("Recolectaste " + cantidad +" de " + tipo + " (costo O2: " + costo + ")");
    }

    /**
     * Función que explora la zona por objetos únicos y en caso de no encontrar adquiere objetos aleatorios de la zona
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     */
    @Override
    public void explorarZona(Jugador jugador) {
        if (jugador.getProfundidadActual() > 500 && jugador.tieneModuloProfundidad()){
            System.out.println("No puedes explorar bajo 500 m si el modulo de profundidad.");
        }

        double d = normalizarProfundidad(jugador.getProfundidadActual());
        int costo = FormulaO2.cExplorar(jugador, this);
        if (costo == Integer.MAX_VALUE) return;
        jugador.getTanqueOxigeno().consumirO2(costo);

        var listaRecursos = new ArrayList<>(recursos);
        ItemTipo encontrado = listaRecursos.get(rand.nextInt(listaRecursos.size()));
        int cantidad = cantidadLootExploracion(d);

        jugador.agregarItem(encontrado, cantidad);
        System.out.printf(
                "Exploraste grietas abisales y hallaste %d de %s (Costo O2: %d)%n",
                cantidad, encontrado, costo
        );

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
        return Math.max(1,(int)Math.floor(2 + (6 - 2) * d));
    }

    /**
     * La cantidad de loot por acción de explorar y no encontrar el objeto único, aumenta con la profundidad y sigue la siguiente fórmula:
     * n(d) = max(1, floor(numero_min*d))
     * @param d tipo: double; descripción: profundidad
     * @return Tipo:int; descripción: Cantidad encontrada.
     */
    private int cantidadLootExploracion(double d) {
        return Math.max(1,(int)Math.floor(2*d));
    }

    public double getPresion() {
        return presion;
    }
}