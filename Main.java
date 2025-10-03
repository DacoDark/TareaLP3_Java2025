import java.util.Scanner;
import player.Jugador;
import player.Oxigeno;
import entorno.*;
import objetos.NaveExploradora;
import objetos.NaveExploradora;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Instancias principales
        Oxigeno oxigeno = new Oxigeno(60);
        Jugador jugador = new Jugador(oxigeno);
        NaveExploradora nave = new NaveExploradora();

        //Zonas persistentes
        ZonaArrecife arrecife = new ZonaArrecife();
        ZonaProfunda profunda = new ZonaProfunda();
        ZonaVolcanica volcanica = new ZonaVolcanica();
        NaveEstrellada naveEstrellada = new NaveEstrellada();

        Zona zonaActual = arrecife; //Empieza el jugador en el arrecife
        boolean enNave = true;      //Jugador inicia dentro de la nave exploradora

        System.out.println("=== Exploración Subacuática ===");
        boolean jugando = true;

        while (jugando) {
            System.out.println("\n=== Zona actual: " + zonaActual.getClass().getSimpleName() + " ===");
            System.out.println("Jugador está " + (enNave ? "dentro de la nave" : "en el agua"));

            if (enNave) {
                // -Menú dentro de la nave-
                System.out.println("\n--- Menú Nave ---");
                System.out.println("1. Ver estado jugador");
                System.out.println("2. Subir/descender nave");
                System.out.println("3. Gestionar inventario");
                System.out.println("4. Crear objetos");
                System.out.println("5. Elegir destino / salir a zona");
                System.out.println("6. Salir del juego");

                System.out.print("Opción: ");
                int opcion = sc.nextInt();

                switch (opcion) {
                    case 1 -> jugador.verEstadoJugador();
                    case 2 -> {
                        System.out.print("Nueva profundidad de anclaje: ");
                        int profundidad = sc.nextInt();
                        nave.anclarNaveExploradora(profundidad);
                        System.out.println("Nave anclada a " + profundidad + " m");
                    }
                    case 3 -> System.out.println("(gestión de inventario aún no implementada)");
                    case 4 -> System.out.println("(crafteo aún no implementado)");
                    case 5 -> {
                        System.out.println("¿Qué deseas hacer?");
                        System.out.println("1. Salir al agua en zona actual");
                        System.out.println("2. Viajar a otra zona");
                        int subopcion = sc.nextInt();
                        if (subopcion == 1) {
                            enNave = false; // salir al agua
                            jugador.profundidadActualizar(nave.getProfundidadAnclaje(), zonaActual);
                            System.out.println("Has salido de la nave en profundidad " + nave.getProfundidadAnclaje()+" m.");
                        } else {
                            System.out.println("1. Arrecife, 2. Profunda, 3. Volcánica, 4. Nave Estrellada");
                            int destino = sc.nextInt();
                            switch (destino) {
                                case 1 -> {
                                    if(nave.puedeAcceder(arrecife.getProfundidadMin()) || jugador.puedeAcceder(arrecife.getProfundidadMin())) {
                                        zonaActual = arrecife;
                                    } else {
                                        System.out.println("No se puede acceder al Arrecife");
                                    }
                                }
                                case 2 -> {
                                    if(nave.puedeAcceder(profunda.getProfundidadMin()) || jugador.puedeAcceder(profunda.getProfundidadMin())) {
                                        zonaActual = profunda;
                                    } else {
                                        System.out.println("No se puede acceder a la Zona profunda.");
                                    }
                                }
                                case 3 -> {
                                    if(nave.puedeAcceder(volcanica.getProfundidadMin()) || jugador.puedeAcceder(volcanica.getProfundidadMin())) {
                                        zonaActual = volcanica;
                                    } else {
                                        System.out.println("No se puede acceder a la Zona Volcánica.");
                                    }
                                }
                                case 4 -> {
                                    //Zona funciona distinto a las demás - bimodal - esto hay que cambiarlo después
                                    if(nave.puedeAcceder(naveEstrellada.getProfundidadMin()) || jugador.puedeAcceder(naveEstrellada.getProfundidadMin())) {
                                        zonaActual = naveEstrellada;
                                    } else {
                                        System.out.println("No se puede acceder a la Nave Estrellada.");
                                    }
                                }
                                default -> System.out.println("Opción inválida.");
                            }
                            System.out.println("Has viajado a: " + zonaActual.getClass().getSimpleName());
                        }
                    }
                    case 6 -> {
                        jugando = false;
                        System.out.println("Saliendo del juego...");
                    }
                }
                //Recarga automática al estar en la nave
                jugador.getTanqueOxigeno().recargarCompleto();
            } else {
                System.out.println("\n--- Menú Agua ---");
                System.out.println("1. Ver estado jugador");
                System.out.println("2. Subir/descender (a nado)");
                System.out.println("3. Recolectar recurso");
                System.out.println("4. Explorar");
                System.out.println("5. Volver a la nave");

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
                        zonaActual.recolectarTipoRecurso(jugador, null); //Luego implementamos la selección de recursos por zona

                    case 4:
                        zonaActual.explorarZona(jugador);
                        break;

                    case 5:
                        enNave = true;
                        System.out.println("Regresaste a la nave.");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

                // Condición de derrota: O2 = 0
                if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0) {
                    System.out.println("\n¡Te has quedado sin oxígeno! Pierdes inventario y reapareces en la nave.");
                    //Eliminar inventario
                    enNave = true;
                    jugador.getTanqueOxigeno().recargarCompleto();
                }
            }
        }
        sc.close();
    }
}
