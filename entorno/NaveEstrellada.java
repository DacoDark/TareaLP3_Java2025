package entorno;

import player.Jugador;
import objetos.ItemTipo;
import java.util.Random;

/**
 * Zona Nave Estrellada (Bimodal 0m)
 */
public class NaveEstrellada extends Zona {
    private boolean moduloEncontrado;
    private boolean planosEncontrados = false;
    private boolean exploracionLimitada = false;
    private Random rand = new Random();

    public NaveEstrellada() {
        this.nombre = "Nave Estrellada";
        this.profundidadMin = 0;
        this.profundidadMax = 0;
        this.moduloEncontrado = false;
    }

    public int getProfundidadMin() {
        return profundidadMin;
    }

    @Override
    public void entrar(Jugador jugador) {
        System.out.println("Te encuentras en la Nave Estrellada, está el llamas 🔥, en cualquier momento puede explotar!.");
        if (jugador.isTienePlanos()) {
            System.out.println("🔥 El incendio de la nave puede ser apagado con los recursos adecuados...");
            System.out.println("Usas los planos y comienzas las reparaciones...");
            //repararNave(jugador); Implenentar cuando vea los objetos
        } else {
            System.out.println("Aún no tienes los planos necesarios para reparar la nave.");
            System.out.println("Puedes intentar explorar el interior para buscar piezas o el módulo de profundidad, pero hazlo rápido, sin un traje térmico te calcinaras vivo...");
        }
    }

    @Override
    public void recolectarTipoRecurso(Jugador jugador, ItemTipo itemTipo) {
        if (!jugador.isTrajeTermico()) {
            System.out.println("El calor extremo no te permite recolectar nada... ☠");
            exploracionLimitada = true;
            return;
        }

        if (itemTipo == ItemTipo.CABLES || itemTipo == ItemTipo.PIEZAS_METAL) {
            int cantidad = rand.nextInt(2) + 1;
            jugador.agregarItem(itemTipo, cantidad);
            System.out.println("Recolectaste " + cantidad + " de " + itemTipo);
        } else {
            System.out.println("Solo se pueden recolectar CABLES y PIEZAS_METAL en esta zona.");
        }
    }

    @Override
    public void explorarZona(Jugador jugador) {
        if (jugador.isTienePlanos()) {
            System.out.println("Ya has encontrado los planos. Puedes concentrarte en reparar la nave.");
            return;
        }

        if (!jugador.isTrajeTermico()) {
            if (exploracionLimitada) {
                System.out.println("El calor te abruma... no puedes seguir explorando sin un traje térmico.");
                return;
            }
            exploracionLimitada = true;
        }

        System.out.println("Explorando el interior de la nave estrellada...");
        // No hay consumo de O2 en esta zona

        // 25% de encontrar el MÓDULO DE PROFUNDIDAD
        if (!moduloEncontrado && rand.nextDouble() < 0.25) {
            jugador.agregarItem(ItemTipo.MODULO_PROFUNDIDAD, 1);
            moduloEncontrado = true;
            System.out.println("⚙️  ¡Encontraste el MÓDULO DE PROFUNDIDAD! Tu nave ahora podrá descender más allá de 1000 m.");
        } else {
            // Loot genérico si tiene traje térmico
            if (jugador.isTrajeTermico()) {
                ItemTipo[] posibles = {ItemTipo.CABLES, ItemTipo.PIEZAS_METAL};
                ItemTipo recurso = posibles[rand.nextInt(posibles.length)];
                int cantidad = rand.nextInt(2) + 1;
                jugador.agregarItem(recurso, cantidad);
                System.out.println("Has encontrado " + cantidad + " de " + recurso);
            } else {
                System.out.println("No encontraste nada útil antes de que el calor se vuelva insoportable...");
            }
        }
    }

    /**
     * Función para reparar la Nave.
     * @param jugador
     */
    private void repararNave(Jugador jugador) {}
}
