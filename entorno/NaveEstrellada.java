package entorno;

import player.Jugador;
import objetos.ItemTipo;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

/**
 * Zona Nave Estrellada (Bimodal 0m)
 */
public class NaveEstrellada extends Zona {
    private boolean moduloEncontrado;
    private boolean exploracionLimitada = false;
    private final Random rand;

    public NaveEstrellada() {
        this.nombre = "Nave Estrellada";
        this.profundidadMin = 0;
        this.profundidadMax = 0;
        this.moduloEncontrado = false;
        this.rand = new Random();
        this.recursos = EnumSet.of(
          ItemTipo.CABLES,
          ItemTipo.PIEZAS_METAL
        );
    }

    @Override
    public void entrar(Jugador jugador) {
        System.out.println("Te encuentras en la Nave Estrellada, está en llamas 🔥🔥🔥 y en cualquier momento puede explotar!.");
        if (jugador.isTienePlanos()) {
            System.out.println("🔥 El incendio de la nave puede ser apagado con los recursos adecuados...");
            System.out.println("Usas los planos y comienzas las reparaciones...");
            repararNave(jugador);
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
                var listaRecursos = new ArrayList<>(recursos);
                ItemTipo encontrado = listaRecursos.get(rand.nextInt(listaRecursos.size()));
                int cantidad = rand.nextInt(2) + 1;
                jugador.agregarItem(encontrado, cantidad);
                System.out.println("Has encontrado " + cantidad + " de " + encontrado);
            } else {
                System.out.println("No encontraste nada útil antes de que el calor se vuelva insoportable...");
            }
        }
    }

    /**
     * Función para reparar la Nave.
     * @param jugador tipo: Jugador; descripción: Jugador que juega el juego.
     */
    private void repararNave(Jugador jugador) {
        if (jugador.isTienePlanos()) {
            System.out.println("Necesito los planos de esta maquina para comenzar a reparar, mejor me pongo en marcha, no hay tiempo que perder.");
        }

        System.out.println("Reparando el interior de la nave estrellada...");

        int reqTitanio = 50;
        int reqAcero = 30;
        int reqUranio = 15;
        int reqSulfuro = 20;

        int Titanio = jugador.contarItem(ItemTipo.TITANIO);
        int Acero = jugador.contarItem(ItemTipo.ACERO);
        int Uranio = jugador.contarItem(ItemTipo.URANIO);
        int Sulfuro = jugador.contarItem(ItemTipo.SULFURO);

        if (Titanio < reqTitanio || Acero < reqAcero || Uranio < reqUranio || Sulfuro < reqSulfuro) {
            System.out.println("No tienes suficientes materiales para reparar la nave, examinas el plano y te das cuenta que necesitas: ");
            System.out.println("Titanio: " + reqTitanio + " Acero: " + reqAcero + "Uranio: " +reqUranio + "Sulfuro: " + reqSulfuro + "Sulfuro: " + reqSulfuro);
            System.out.println("Tienes actualmente: " + Titanio + " de Titanio" + Acero + "de Acero" + Sulfuro + " de Sulfuro" + Uranio + "de Uranio");
        } else {
            jugador.consumirItem(ItemTipo.TITANIO,reqTitanio);
            jugador.consumirItem(ItemTipo.ACERO,reqAcero);
            jugador.consumirItem(ItemTipo.URANIO,reqUranio);
            jugador.consumirItem(ItemTipo.SULFURO,reqSulfuro);

            System.out.println("\n🛠️  Iniciando procedimiento de reparación de la nave...");
            try {
                Thread.sleep(1200);
                System.out.println("Soldando placas metálicas...");
                Thread.sleep(1200);
                System.out.println("Reinstalando cableado principal...");
                Thread.sleep(1200);
                System.out.println("Restableciendo energía auxiliar...");
                Thread.sleep(1000);
                System.out.println("Sistemas de ventilación operativos...");
                Thread.sleep(800);
                System.out.println("Fuego extinguido. Módulos estabilizados...");
                Thread.sleep(1200);
                System.out.println("Secuencia de arranque completada...");
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("\n ¡La nave estrellada ha sido reparada con éxito!");
            System.out.println(" ¡Has completado el juego!");
            jugador.setJuegoCompletado(true);
        }
    }
}