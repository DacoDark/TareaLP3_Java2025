import java.util.Scanner;
import player.Jugador;
import player.Oxigeno;
import entorno.ZonaArrecife;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Crear oxígeno y jugador
        Oxigeno oxigeno = new Oxigeno(60);
        Jugador jugador = new Jugador(oxigeno);

        // Instanciar zona de prueba
        ZonaArrecife arrecife = new ZonaArrecife();

        System.out.println("=== Exploración Subacuática ===");
        boolean jugando = true;

        while (jugando) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Ver estado jugador");
            System.out.println("2. Moverse en profundidad");
            System.out.println("3. Salir");

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
