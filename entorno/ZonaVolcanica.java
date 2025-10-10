package entorno;

import player.Jugador;
import player.FormulaO2;
import objetos.ItemTipo;
import java.util.Random;

/**
 * Zona Volcanica (1000-1500 m)
 * Recursos: Titanio, Sulfuro, Uranio
 * Acceso obligatorio con Traje Termico y Mejora de Tanque
 */
public class ZonaVolcanica extends Zona {
    private boolean planoEncontrado;
    private final Random rand;

    /**
     * Constructor de la clase
     */
    public ZonaVolcanica() {
        this.nombre = "Zona Volcanica";
        this.profundidadMin = 1000;
        this.profundidadMax = 1500;
        rand = new Random();
    }
    /**
     * Función que verifica si el jugador puede entrar a la zona.
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego.
     */
    @Override
    public void entrar(Jugador jugador) {
        //Validación global
        if (!jugador.puedeAcceder(profundidadMin)){
            System.out.println("No es seguro, faltan requisitos para entrar a la Zona Volcánica, por tu seguridad mejor vuelve.");
            return;
        }
        System.out.println("Entrando en Zona Volcánica (1000-1500 m)");
    }

    /**
     * Función que recolecta objetos dado un tipo específico que se busca
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     * @param tipo tipo: ItemTipo; Descripción: Tipo del item que quiere encontrar.
     */
    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo) {
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        double presion = FormulaO2.presion("ZonaVolcanica",d, jugador.isMejoraTanque());
        int costo = FormulaO2.cRecolectar(d, presion);

        jugador.getTanqueOxigeno().consumirO2(costo);

        //Cambia la cantidad de loot con la zona (min:3; max:8)
        int cantidad = cantidadLootRecolectar(d);
        jugador.agregarItem(tipo,cantidad);

        System.out.println("Recolectaste " + cantidad +" de " + tipo + " (costo O2: " + costo + ")");
    }

    /**
     * Función que explora la zona por objetos únicos y en caso de no encontrar adquiere objetos aleatorios de la zona
     * @param jugador tipo: Jugador; Descripción: Personaje que juega el juego
     */
    @Override
    public void explorarZona(Jugador jugador) {
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        double presion = FormulaO2.presion("ZonaVolcanica",d, jugador.isMejoraTanque());
        int costo = FormulaO2.cExplorar(d, presion);

        jugador.getTanqueOxigeno().consumirO2(costo);
        if (!planoEncontrado && rand.nextDouble() < 0.15) {
            setPlanoEncontrado();
            jugador.agregarItem(ItemTipo.PLANO_NAVE,1);
            System.out.println("¡Encontraste El Plano de las Nave! Ahora podrás repararla (Costo O2: " + costo + ")");
        } else {
            ItemTipo[] recursos = {ItemTipo.TITANIO,ItemTipo.SULFURO,ItemTipo.URANIO};
            ItemTipo recurso = recursos[rand.nextInt(recursos.length)];
            int cantidad = cantidadLootExploracion(d);
            jugador.agregarItem(recurso,cantidad);
            System.out.println("Exploraste y obtuviste " + cantidad + " de " + recurso + " (Costo O2: " + costo + ")");

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

    /**
     * Setter del parámetro planoEncontrado, con una probabilidad del 15% se pone verdadero.
     */
    public void setPlanoEncontrado() {
        this.planoEncontrado = true;
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
        return Math.max(1,(int)Math.floor(3 + (8 - 3) * d));
    }

    /**
     * La cantidad de loot por acción de explorar y no encontrar el objeto único, aumenta con la profundidad y sigue la siguiente fórmula:
     * n(d) = max(1, floor(numero_min*d))
     * @param d tipo: double; descripción: profundidad
     * @return Tipo:int; descripción: Cantidad encontrada.
     */
    private int cantidadLootExploracion(double d) {
        return Math.max(1,(int)Math.floor(3*d));
    }
}