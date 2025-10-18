package player;

import entorno.*;
/**
 * Contiene las fórmulas para calcular consumo de O2
 */
public class FormulaO2 {

    /**
     * Función que calcula el gasto de oxígeno al explorar en profundidad, considera presión base para la zona profunda.
     * Primero normaliza la profundidad actual del jugador con respecto a los límites de la zona dejándolo entre[0,1]
     * Luego, considera la presión que depende de la zona, sí la presión es Inf(caso volcanica), se setea como max_value para evitar errores.
     * Finalmente, sigue la forma: [12 + 10*d + pres(d)]
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param zona tipo: Zona; descripción: Zona en la que se mueve el jugador
     * @return tipo: int; descripción: Cantidad de oxígeno gastado según la formula entregada
     */
    public static int cExplorar(Jugador jugador, Zona zona) {
        double d = calcularProfundidadNormalizada(jugador, zona);
        double pres = calcularPresion(jugador, zona, d);

        if (Double.isInfinite(pres)){
            return Integer.MAX_VALUE;
        }
        double costo = 12 + 10 * d + pres;
        //System.out.printf("[DEBUG EXPLORAR] d=%.3f pres=%.3f costo=%.3f%n", d, pres, costo);
        return (int) Math.ceil(costo);
    }

    /**
     * Función que calcula el gasto de oxígeno al Recolectar items en profundidad, considera presión base para la zona profunda.
     * Primero normaliza la profundidad actual del jugador con respecto a los límites de la zona dejándolo entre[0,1]
     * Luego, considera la presión que depende de la zona, sí la presión es Inf(caso volcanica), se setea como max_value para evitar errores.
     * Finalmente, sigue la forma: [10 + 6*d + pres(d)]
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param zona tipo: Zona; descripción: Zona en la que se mueve el jugador
     * @return tipo: int; descripción: Cantidad de oxígeno gastado según la formula entregada
     */
    public static int cRecolectar(Jugador jugador, Zona zona) {
        double d = calcularProfundidadNormalizada(jugador, zona);
        double presion = calcularPresion(jugador,zona,d);

        if (Double.isInfinite(presion)){
            return Integer.MAX_VALUE;
        }
        double costo = 10 + 6 * d + presion;
        //System.out.printf("[DEBUG RECOLECTAR] d=%.3f pres=%.3f costo=%.3f%n", d, presion, costo);
        return (int) Math.ceil(costo);
    }

    /**
     * Función que calcula el gasto de oxígeno al moverse en profundidad, (no se considera la presión en el movimiento según la formula)
     * Primero normaliza la profundidad actual del jugador con respecto a los límites de la zona dejándolo entre[0,1]
     * Luego sigue la forma: [3 + 3*d * |Δz|/50]
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param zona tipo: Zona; descripción: Zona en la que se mueve el jugador
     * @return tipo: int; descripción: Cantidad de oxígeno gastado según la formula entregada
     */
    public static int cMover(Jugador jugador, Zona zona, int deltaZ) {
        // Profundidad actual
        double z = jugador.getProfundidadActual();
        // Normalizamos
        double d = (z - zona.getProfundidadMin()) / Math.max(1.0, zona.getProfundidadMax() - zona.getProfundidadMin());
        d = Math.min(Math.max(d, 0), 1); // asegurar rango [0,1]

        double costo = 3 + 3 * d * (Math.abs(deltaZ) / 50.0);
        System.out.printf("[DEBUG] z=%.1f, Δz=%d, d=%.3f, costo=%.3f%n", z, deltaZ, d, costo);

        return (int) Math.ceil(costo);
    }

    /**
     * Normaliza la profundidad en la que se encuentra el jugador
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param zona tipo: Zona; descripción: Zona en la que se mueve el jugador
     * @return tipo: double; descripción: Cantidad en tamaño double del valor normalizado de la profundidad
     */
    private static double calcularProfundidadNormalizada(Jugador jugador, Zona zona) {
        double z = jugador.getProfundidadActual();
        double d = (z - zona.getProfundidadMin()) / Math.max(1.0, zona.getProfundidadMax() - zona.getProfundidadMin());
        d = Math.min(Math.max(d, 0), 1);
        return d;
    }

    /**
     * Función que calcula la presión según la zona.
     * Sigue la forma:
     * [pres(d)= 0] ; Arrecife o NaveEstrellada
     * [pres(d)= 10+6*d] ; Zona Profunda
     * [pres(d)= inf] ; Zona Volcanica
     * @param jugador tipo: Jugador; descripción: Personaje que juega el juego
     * @param zona tipo: Zona; descripción: Zona en la que se encuentra el personaje
     * @param d tipo: double; descripción: Profundidad normalizada entre [0,1]
     * @return tipo: double; descripción: Retorna el valor de la profundidad el cual se usa en la fórmula de gasto de oxígeno.
     */
    private static double calcularPresion(Jugador jugador,Zona zona, double d) {
        //Se elimina la presion si tiene la mejora del tanque
        if (jugador.isMejoraTanque()) return 0.0;

        //Arrecife o Nave estrellada, no tiene presión
        if (zona instanceof ZonaArrecife || zona instanceof NaveEstrellada) {
            return 0.0;
        }

        if (zona instanceof ZonaProfunda profunda) {
            return profunda.getPresion() + 6 * d;
        }

        if (zona instanceof ZonaVolcanica) {
            return Double.POSITIVE_INFINITY;
        }

        return 0.0;
    }
}
