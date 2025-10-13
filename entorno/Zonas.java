package entorno;

/**
 * Clase que contiene las zonas, así se logran crear una sola vez y mantener la consistencia.
 */
public class Zonas{
    public static final ZonaArrecife arrecife = new ZonaArrecife();
    public static final ZonaProfunda profunda = new ZonaProfunda();
    public static final ZonaVolcanica volcanica = new ZonaVolcanica();
    public static final NaveEstrellada naveEstrellada = new NaveEstrellada();
}
