package player;

import entorno.*;
/**
 * Contiene las fórmulas para calcular consumo de O2
 */
public class FormulaO2 {


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

    public static int cMover(Jugador jugador, Zona zona, int deltaZ) {
        // Profundidad actual
        double z = jugador.getProfundidadActual();
        // Normalizamos
        double d = (z - zona.getProfundidadMin()) / Math.max(1.0, zona.getProfundidadMax() - zona.getProfundidadMin());
        d = Math.min(Math.max(d, 0), 1); // asegurar rango [0,1]

        double costo = 3 + 3 * d * (Math.abs(deltaZ) / 50.0);
        //System.out.printf("[DEBUG] z=%.1f, Δz=%d, d=%.3f, costo=%.3f%n", z, deltaZ, d, costo);

        return (int) Math.ceil(costo);
    }
    private static double calcularProfundidadNormalizada(Jugador jugador, Zona zona) {
        double z = jugador.getProfundidadActual();
        double d = (z - zona.getProfundidadMin()) / Math.max(1.0, zona.getProfundidadMax() - zona.getProfundidadMin());
        d = Math.min(Math.max(d, 0), 1);
        return d;
    }

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
