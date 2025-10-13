package player;

public class Oxigeno {
    private int oxigenoRestante;
    private int capacidadMaxima;

    public Oxigeno(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.oxigenoRestante = capacidadMaxima;
    }

    /**
     * Consumir oxígeno
     * @param unidades int
     */
    public void consumirO2(int unidades){
        oxigenoRestante -= unidades;
        if (oxigenoRestante < 0) {
            oxigenoRestante = 0;
        }
    }

    /**
     * Recarga oxígeno al máximo
     */
    public void recargarCompleto(){
        oxigenoRestante = capacidadMaxima;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getOxigenoRestante() {
        return oxigenoRestante;
    }

    public void aumentarOxigeno(int extra) {
        capacidadMaxima += extra;
        oxigenoRestante = capacidadMaxima;
    }
}
