package player;

/**
 * Clase de Oxígeno
 */
public class Oxigeno {
    private int oxigenoRestante;
    private int capacidadMaxima;

    /**
     * Constructor de Clase
     * @param capacidadMaxima tipo: int; descripción: Capacidad Base de oxígeno.
     */
    public Oxigeno(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.oxigenoRestante = capacidadMaxima;
    }

    /**
     * Función que consumir oxígeno del jugador
     * @param unidades tipo: int; descripción: cantidad que se le resta al oxígeno disponible.
     */
    public void consumirO2(int unidades){
        oxigenoRestante -= unidades;
        if (oxigenoRestante < 0) {
            oxigenoRestante = 0;
        }
    }

    /**
     * Función que recarga el oxígeno al máximo, se usa al volver a la nave
     */
    public void recargarCompleto(){
        oxigenoRestante = capacidadMaxima;
    }

    //*******************
    //*     Getters     *
    //*******************

    /**
     * Getter de la Capacidad máxima
     * @return tipo: int; descripción: Cantidad de la capacidad máxima de oxígeno.
     */
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * Getter del oxígeno restante
     * @return tipo: int; descripción: Cantidad del oxígeno que le queda al jugador.
     */
    public int getOxigenoRestante() {
        return oxigenoRestante;
    }

    //*******************
    //*     Métodos     *
    //*******************

    /**
     * Función para mejorar la capacidad máxima del oxígeno en caso de mejora
     * @param extra tipo: int; descripción: Cantidad que se mejora la capacidad máxima de oxígeno
     */
    public void aumentarOxigeno(int extra) {
        capacidadMaxima += extra;
        oxigenoRestante = capacidadMaxima;
    }
}
