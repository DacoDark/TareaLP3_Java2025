package player;

/**
 * Contiene las fórmulas para calcular consumo de O2
 */
public class FormulaO2 {

    /**
     * Costo de explorar
     * @param d double
     * @param presion double
     * @return cantidad de oxígeno consumido por explorar
     */
    public static int cExplorar(double d, double presion) {
        return (int) Math.ceil(12 + (10*d) + presion);
    }

    /**
     * Costo de recolectar
     * @param d double
     * @param presion  double
     * @return cantidad de oxigeno consumido por recolectar
     */
    public static int cRecolectar(double d, double presion) {
        return (int) Math.ceil(10 + ( 6*d ) + presion);
    }

    /**
     * Costo de Mover
     * @param d double
     * @param delta_profundidad int
     * @return
     */
    public static int cMover(double d, int delta_profundidad) {
        return (int) Math.ceil((3 + (3*d)) * (delta_profundidad/ 50.0));
    }

    /**
     * Calcula presión según zona y estado del tanque
     * @param zona String
     * @param d double
     * @param mejoraTanque boolean
     * @return Cantidad de presión asignada según zona y estado de tanque.
     */
    public static double presion(String zona, double d, boolean mejoraTanque){
        if (mejoraTanque) return 0;
        return switch (zona) {
            case "Zona Profunda" -> 10 + 6 * d;
            case "Zona Volcanica" -> Double.POSITIVE_INFINITY;
            default -> 0;
        };
    }
}
