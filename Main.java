import java.util.Scanner;
import player.Jugador;
import player.Oxigeno;
import entorno.ZonaArrecife;
import objetos.ItemTipo;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Crear oxígeno y jugador
        Oxigeno oxigeno = new Oxigeno(60);
        Jugador jugador = new Jugador(oxigeno);

        // Instanciar zona de prueba
        ZonaArrecife arrecife = new ZonaArrecife();
        arrecife.entrar(jugador);

        System.out.println("=== Exploración Subacuática ===");
        boolean jugando = true;

        while (jugando) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Ver estado jugador");
            System.out.println("2. Moverse en profundidad");
            System.out.println("3. Recolectar recurso");
            System.out.println("4. Explorar");
            System.out.println("5. Salir");


            System.out.print("Opción: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    jugador.verEstadoJugador();
                    break;

                case 2:
                    System.out.print("Ingrese nueva profundidad (m): ");
                    int nuevaProf = sc.nextInt();
                    jugador.profundidadActualizar(nuevaProf, arrecife);
                    break;

                case 3:
                    System.out.println("Elige recurso: 1=CUARZO, 2=SILICIO, 3=COBRE");
                    int r = sc.nextInt();
                    ItemTipo tipo;
                    switch (r) {
                        case 1: tipo = ItemTipo.CUARZO; break;
                        case 2: tipo = ItemTipo.SILICIO; break;
                        case 3: tipo = ItemTipo.COBRE; break;
                        default: tipo = ItemTipo.CUARZO;
                    }
                    arrecife.recolectarTipoRecurso(jugador, tipo);
                    break;

                case 4: arrecife.explorarZona(jugador);
                    break;

                case 5:
                    jugando = false;
                    System.out.println("Saliendo del juego...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

            if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0) {
                System.out.println("\n¡Te has quedado sin oxígeno! Fin del juego.");
                jugando = false;
            }
        }

        sc.close();
    }
}
