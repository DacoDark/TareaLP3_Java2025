import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

import player.Jugador;
import player.Oxigeno;
import objetos.Item;
import objetos.NaveExploradora;
import objetos.ItemTipo;
import entorno.Zona;
import entorno.Zonas;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Instancias principales
        Oxigeno oxigeno = new Oxigeno(60);
        Jugador jugador = new Jugador(oxigeno);
        //System.out.println("[DEBUG] instancia jugador hash: " + jugador.hashCode());
        NaveExploradora nave = new NaveExploradora();
        //enlazamos la nave al jugador
        jugador.setNave(nave);

        //El juego comienza aquí
        Zona zonaActual = Zonas.naveEstrellada;
        boolean enNave = true;      //Jugador inicia dentro de la nave exploradora
        boolean jugando = true;



        System.out.println("=== Exploración Subacuática - Inicio ===");
        System.out.println("Comienzas en la Nave Estrellada (0 m)");

        //Mostramos la entrada inicial de la zona
        zonaActual.entrar(jugador);
        System.out.println("Puedes intentar explorar el interior para buscar piezas o el módulo de profundidad, pero hazlo rápido, sin un traje térmico te calcinaras vivo...");

        while (jugando) {
            try {
                System.out.println("\n---------------------------------------");
                System.out.println("Zona Nave anclada: " + jugador.getNave().getZonaAnclajeActual().getNombre()
                        + " | Jugador " + (enNave ? "dentro de la nave" : "en el agua"));
                System.out.println("---------------------------------------");

                if (enNave) {
                    jugador.getTanqueOxigeno().recargarCompleto(); //Recarga 02 en la nave
                    // -Menú dentro de la nave-
                    System.out.println("\n--- Menú: Dentro de la Nave ---");
                    System.out.println("1. Ver estado jugador");
                    System.out.println("2. Anclar nave (fijar profundidad)");
                    System.out.println("3. Ver inventario");
                    System.out.println("4. Ver inventario Bodega");
                    System.out.println("5. Guardar objetos en bodega");
                    System.out.println("6. Retirar objetos en bodega");
                    System.out.println("7. Abrir el menú de crafteo");
                    System.out.println("8. Salir al agua");
                    System.out.println("9. Salir del juego");
                    System.out.print("Opción: ");
                    int opcion = sc.nextInt();

                    switch (opcion) {
                        case 1 -> jugador.verEstadoJugador();
                        case 2 -> {
                            System.out.print("Nueva profundidad de anclaje: ");
                            int profundidad = sc.nextInt();
                            nave.anclarNaveExploradora(profundidad);
                            jugador.setProfundidadActual(profundidad);
                        }
                        case 3 -> jugador.verInventario();
                        case 4 -> nave.verBodega();
                        case 5 -> {
                            System.out.println("¿Qué deseas guardar?");
                            ItemTipo tipo = seleccionarItemTipo(jugador,nave,false);
                            if (tipo != null) {
                                System.out.println("Cantidad a guardad: ");
                                int cantidad = sc.nextInt();
                                nave.transferirObjetos(jugador,tipo,cantidad);
                            }
                        }
                        case 6 -> {
                            System.out.println("¿Qué deseas retirar?");
                            ItemTipo tipo = seleccionarItemTipo(jugador, nave, true);
                            if (tipo != null) {
                                System.out.println("Cantidad a retirada: ");
                                int cantidad = sc.nextInt();
                                nave.transferirObjetos(jugador,tipo,cantidad);
                            }
                        }
                        case 7 -> nave.menuCrafteo(jugador); //Se implementa el crafteo desde la nave
                        case 8 -> {
                            System.out.println("Salir al agua en la zona actual");
                            int profSalida = nave.getProfundidadAnclaje();
                            jugador.profundidadActualizar(profSalida, jugador.getZonaActual());
                            enNave = false;
                            System.out.println("Has salido al agua en " + profSalida + " m.");
                        }
                        case 9 -> {
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
                            jugador.profundidadActualizar(nuevaProf, jugador.getZonaActual());
                            break;

                        case 3:
                            mostrarOpcionesRecolectar(jugador.getZonaActual());
                            System.out.print("Elige recurso: ");
                            int r = sc.nextInt();
                            ItemTipo tipo = mapearOpcionARecurso(jugador.getZonaActual(), r);
                            if (tipo != null) jugador.getZonaActual().recolectarTipoRecurso(jugador, tipo);
                            break;

                        case 4:
                            jugador.getZonaActual().explorarZona(jugador);
                            break;

                        case 5:
                            //Jugador decide volver a la nave
                            enNave = true;
                            //Recarga su oxígeno
                            jugador.getTanqueOxigeno().recargarCompleto();
                            //Su nueva zona es la zona donde está anclada la nave.
                            //Obtenemos la profundidad de la nave
                            int nueva_profundidad = jugador.getNave().getProfundidadAnclaje();
                            //Actualizamos la profundidad del jugador con la profundidad de la nave
                            jugador.profundidadActualizar(nueva_profundidad, jugador.getZonaActual());
                            // System.out.print("[DEBUG] La profundidad del jugador es: "+ jugador.getProfundidadActual() + " y al volver debería ser: " + jugador.getNave().getProfundidadAnclaje());
                            System.out.println("Regresaste a la nave.");
                            break;

                        default:
                            System.out.println("Opción inválida.");
                    }

                    // Condición de derrota: O2 = 0
                    if (jugador.getTanqueOxigeno().getOxigenoRestante() <= 0){
                        System.out.println("Te has quedado sin oxígeno durante la inmersión...");
                        System.out.println("Pierdes todo tu inventario y reapareces en la nave.");
                        enNave = true;
                        //Vaciar inventario
                        jugador.vaciarInventario();

                        //Reaparecer en nave anclada
                        jugador.setProfundidadActual(nave.getProfundidadAnclaje());

                        //Determinar zona según profundidad del anclaje
                        Zona nuevaZona = jugador.getNave().getZonaAnclajeActual();
                        jugador.setZonaActual(nuevaZona);

                        //Recargar Oxígeno
                        jugador.getTanqueOxigeno().recargarCompleto();
                        System.out.println("Has reaparecido en la nave anclada a " + jugador.getNave().getProfundidadAnclaje() + " m. Oxígeno recargado.");
                    }

                    //Condición Victoria
                    if (jugador.isJuegoCompletado()) {
                        System.out.println("🚀 ¡Has completado la misión!");
                        jugando = false;
                    }
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

    private static void mostrarOpcionesRecolectar(Zona zona){
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

    public static ItemTipo seleccionarItemTipo(Jugador jugador, NaveExploradora nave, boolean desdeBodega) {
        System.out.println("\n=== Selección de Ítem ===");
        List<Item> fuente = desdeBodega ? nave.getBodega() : jugador.getInventario();

        if (fuente.isEmpty()) {
            System.out.println("No hay ítems disponibles en " + (desdeBodega ? "la bodega." : "tu inventario."));
            return null;
        }

        int index = 1;
        for (Item it : fuente) {
            if (it.getCantidad() > 0) {
                System.out.printf("%2d. %-20s [x%d]%n", index++, it.getTipo(), it.getCantidad());
            }
        }

        System.out.println("0. Cancelar");
        System.out.print("Selecciona el número del ítem: ");

        int opcion;
        try {
            opcion = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Se cancela la selección.");
            return null;
        }

        if (opcion == 0) return null;
        if (opcion < 1 || opcion >= index) {
            System.out.println("Opción fuera de rango.");
            return null;
        }

        ItemTipo elegido = fuente.get(opcion - 1).getTipo();
        System.out.println("Seleccionado: " + elegido);
        return elegido;
    }
}