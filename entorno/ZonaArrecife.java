package entorno;

import player.Jugador;
import player.FormulaO2;
import objetos.ItemTipo;
import java.util.Random;

/**
 * Zona del Arrecife
 */
public class ZonaArrecife extends Zona {
    private int piezasTanque = 3;
    private Random rand = new Random();

    public ZonaArrecife() {
        this.nombre = "Zona Arrecife";
        this.profundidadMin = 0;
        this.profundidadMax = 199;
        //Falta implementar objeto por zona
    }
    @Override
    public void entrar(Jugador jugador) {
        if (!jugador.puedeAcceder(profundidadMin)){
            System.out.println("No puedes entrar a la Zona Arrecife");
            return;
        }
        System.out.println("Entrando en Zona Arrecife (0-199m)");
    }

    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo tipo) {
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        double presion = FormulaO2.presion("ZonaArrecife",d, jugador.isMejoraTanque());
        int costo = FormulaO2.cRecolectar(d, presion);

        jugador.getTanqueOxigeno().consumirO2(costo);

        //Cambia con la zona
        int cantidad = cantidadLoot(d,1,3);
        jugador.agregarItem(tipo,cantidad);

        System.out.println("Recolectaste " + cantidad +" de " + tipo + " (costo O2: " + costo + ")");
    }

    @Override
    public void explorarZona(Jugador jugador) {
        double d = normalizarProfundidad(jugador.getProfundidadActual());
        double press = FormulaO2.presion("ZonaArrecife",d,jugador.isMejoraTanque());
        int costo = FormulaO2.cExplorar(d, press);

        jugador.getTanqueOxigeno().consumirO2(costo);
        if (piezasTanque>0 && rand.nextDouble() < 0.3) {
            jugador.agregarItem(ItemTipo.PIEZA_TANQUE,1);
            piezasTanque--;
            System.out.println("¡Encontraste una PIEZA_TANQUE! (Costo O2: " + costo + ")");
        } else {
            ItemTipo[] recursos = {ItemTipo.CUARZO,ItemTipo.SILICIO,ItemTipo.COBRE};
            ItemTipo recurso = recursos[rand.nextInt(recursos.length)];
            int cantidad = cantidadLoot(d,1,3);
            jugador.agregarItem(recurso,cantidad);
            System.out.println("Exploraste y obtuviste " + cantidad + " de " + recurso + " (Costo O2: " + costo + ")");
        }
    }

    public int getProfundidadMin() {
        return profundidadMin;
    }

    /**
     * La cantidad de loot por acción de recolectar aumenta con la profundidad y sigue la siguiente formula:
     * n(d) = max(1, floor(numero_min + (numero_max - numero_min)*d))
     * @param d tipo: double; descripción: profundidad
     * @param numero_min tipo: int; descripción: cantidad minima que puede encontrar
     * @param numero_max tipo: int; descripción: cantidad máxima que puede encontrar
     * @return Tipo:int; descripción: Cantidad encontrada.
     */
    private int cantidadLoot(double d, int numero_min,int numero_max) {
        return Math.max(1,(int)Math.floor(numero_min + (numero_max-numero_min) * d));
    }
}
