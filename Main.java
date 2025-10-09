import java.util.InputMismatchException;
import java.util.Scanner;

import entorno.NaveEstrellada;
import player.Jugador;
import player.Oxigeno;
import objetos.NaveExploradora;
import objetos.ItemTipo;
import entorno.Zona;
import entorno.Zonas;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Instancias principales
        Oxigeno oxigeno = new Oxigeno(60);
        Jugador jugador = new Jugador(oxigeno);
        NaveExploradora nave = new NaveExploradora();

        //El juego comienza aquí
        Zona zonaActual = Zonas.naveEstrellada;
        boolean enNave = true;      //Jugador inicia dentro de la nave exploradora
        boolean jugando = true;

        System.out.println("=== Exploración Subacuática - Inicio ===");
        System.out.println("Comienzas en la Nave Estrellada (0 m)");

        //Mostramos la entrada inicial de la zona
        zonaActual.entrar(jugador);

        while (jugando) {
            try {
                System.out.println("\n---------------------------------------");
                System.out.println("Zona actual: " + zonaActual.getClass().getSimpleName()
                        + " | Jugador " + (enNave ? "dentro de la nave" : "en el agua"));
                System.out.println("---------------------------------------");

                if (enNave) {
                    jugador.getTanqueOxigeno().recargarCompleto(); //Recarga 02 en la nave
                    // -Menú dentro de la nave-
                    System.out.println("\n--- Menú: Dentro de la Nave ---");
                    System.out.println("1. Ver estado jugador");
                    System.out.println("2. Anclar nave (fijar profundidad)");
                    System.out.println("3. Ver inventario");
                    System.out.println("4. Mejorar equipamiento");
                    System.out.println("5. Viajar / salir al agua");
                    System.out.println("6. Salir del juego");
                    System.out.print("Opción: ");
                    int opcion = sc.nextInt();

                    switch (opcion) {
                        case 1 -> jugador.verEstadoJugador();
                        case 2 -> {
                            System.out.print("Nueva profundidad de anclaje: ");
                            int profundidad = sc.nextInt();
                            nave.anclarNaveExploradora(profundidad);
                        }
                        case 3 -> System.out.println("(gestión de inventario aún no implementada)");
                        case 4 -> { //Implementar las funciones con los items que piden
                            System.out.println("\nOperaciones rápidas:");
                            System.out.println("1. Crear mejora tanque (3 PIEZA_TANQUE)");
                            System.out.println("2. Instalar MÓDULO_PROFUNDIDAD (si lo posees)");
                            System.out.println("3. Crear Traje Termico");
                            System.out.println("4. Mejorar Robot Excavador");
                            System.out.println("5. Reparar nave");
                            System.out.print("Elige: ");
                            int opt = sc.nextInt();

                            switch (opt) {
                                case 1 -> jugador.crearMejoraTanque();
                                case 2 -> {
                                    if (jugador.contarItem(ItemTipo.MODULO_PROFUNDIDAD) > 0) {
                                        nave.getModuloProfundidad().aumentarProfundidad(1000);
                                        System.out.println("✅ Módulo de profundidad instalado (profundidad máxima +1000 m)");
                                    } else {
                                        System.out.println("❌ No tienes MÓDULO_PROFUNDIDAD.");
                                    }
                                }
                                case 3 -> {
                                    //Implementar la creación del Traje termico aquí
                                }
                                case 4 -> {
                                    //Implementar la creación del Robot aquí
                                }
                                case 5 -> {
                                    //Implementar la creación del plano aquí
                                    jugador.setJuegoCompletado(true);
                                }
                                default -> System.out.println("Opción inválida.");
                            }
                        }

                        case 5 -> {
                            System.out.println("1. Salir al agua en la zona actual");
                            System.out.println("2. Viajar a otra zona");
                            int subop = sc.nextInt();

                            if (subop == 1) {
                                int profSalida = nave.getProfundidadAnclaje();
                                jugador.profundidadActualizar(profSalida, zonaActual);
                                enNave = false;
                                System.out.println("Has salido al agua en " + profSalida + " m.");
                            } else {
                                System.out.println("Elige destino:");
                                System.out.println("1. Arrecife (0–500)");
                                System.out.println("2. Profunda (500–1000)");
                                System.out.println("3. Volcánica (1000–1500)");
                                System.out.println("4. Nave Estrellada (0 m)");
                                int destino = sc.nextInt();

                                Zona candidata = switch (destino) {
                                    case 1 -> Zonas.arrecife;
                                    case 2 -> Zonas.profunda;
                                    case 3 -> Zonas.volcanica;
                                    case 4 -> Zonas.naveEstrellada;
                                    default -> zonaActual;
                                };

                                boolean puede = nave.puedeAcceder(candidata.getProfundidadMin())
                                        || jugador.puedeAcceder(candidata.getProfundidadMin());

                                if (puede) {
                                    zonaActual = candidata;
                                    zonaActual.entrar(jugador);

                                    if (jugador.isJuegoCompletado()) {
                                        System.out.println("🚀 ¡Has completado la misión!");
                                        jugando = false;
                                    }
                                } else {
                                    System.out.println("La nave no puede acceder a esa profundidad.");
                                }
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
                            jugador.profundidadActualizar(nuevaProf, zonaActual);
                            break;

                        case 3:
                            mostrarOpcionesRecolecta(zonaActual);
                            System.out.print("Elige recurso: ");
                            int r = sc.nextInt();
                            ItemTipo tipo = mapearOpcionARecurso(zonaActual, r);
                            if (tipo != null) zonaActual.recolectarTipoRecurso(jugador, tipo);
                            break;

                        case 4:
                            zonaActual.explorarZona(jugador);
                            break;

                        case 5:
                            enNave = true;
                            jugador.getTanqueOxigeno().recargarCompleto();
                            System.out.println("Regresaste a la nave.");
                            break;

                        default:
                            System.out.println("Opción inválida.");
                    }

                    // Condición de derrota: O2 = 0
                    if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0) {
                        System.out.println("\n¡Te has quedado sin oxígeno! Pierdes inventario y reapareces en la nave.");
                        //Eliminar inventario
                        //jugador.vaciarInventario();
                        enNave = true;
                        jugador.getTanqueOxigeno().recargarCompleto();
                    }
                }
                // Verificación de victoria final (reparación completada en NaveEstrellada)
                if (zonaActual instanceof NaveEstrellada && jugador.isJuegoCompletado()) {
                    System.out.println("\n ¡Has reparado la nave y completado el juego!");
                    jugando = false;
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Entrada inválida.");
            }
        }
        sc.close();
        System.out.println("Fin del juego.");
    }

    /**
     * Métodos auxiliares para recursos según zonas
     */

    private static void mostrarOpcionesRecolecta(Zona zona){
        String cls = zona.getClass().getSimpleName();
        System.out.println("Recursos disponibles: ");
        switch (cls) {
            case "ZonaArrecife" -> System.out.println("1=CUARZO, 2=SILICIO, 3=COBRE");
            case "ZonaProfunda" -> System.out.println("1=PLATA, 2=ORO, 3=ACERO, 4=DIAMANTE, 5=MAGNETITA");
            case "ZonaVolcanica" -> System.out.println("1=TITANIO, 2=SULFURO, 3=URANIO");
            case "NaveEstrellada" -> System.out.println("1=CABLES, 2=PIEZAS_METAL");
            default -> System.out.println("Zona sin recursos definidos.");
        }
    }

    private static ItemTipo mapearOpcionARecurso(Zona zona,int opcion){
        return switch ( zona.getClass().getSimpleName() ){
            case "ZonaArrecife" -> switch (opcion){
                case 1 -> ItemTipo.CUARZO;
                case 2 -> ItemTipo.SILICIO;
                case 3 -> ItemTipo.COBRE;
                default -> null;
            };
            case "ZonaProfunda" -> switch (opcion){
              case 1 -> ItemTipo.PLATA;
              case 2 -> ItemTipo.ORO;
              case 3 -> ItemTipo.ACERO;
              case 4 -> ItemTipo.DIAMANTE;
              case 5 -> ItemTipo.MAGNETITA;
              default -> null;
            };
            case "ZonaVolcanica" -> switch (opcion){
              case 1 -> ItemTipo.TITANIO;
              case 2 -> ItemTipo.SULFURO;
              case 3 -> ItemTipo.URANIO;
              default -> null;
            };
            case "NaveEstrellada" -> switch (opcion){
              case 1 -> ItemTipo.CABLES;
              case 2 -> ItemTipo.PIEZAS_METAL;
              default -> null;
            };
            default -> null;
        };
    }
}