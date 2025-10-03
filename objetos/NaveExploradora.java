package objetos;

/**
 * Nave principal del Jugador
 */
public class NaveExploradora extends Vehiculo implements AccesoProfundidad {
    private int profundidadSoportada;
    private int profundidadAnclaje;
    private ModuloProfundidad modulo; //Proveniente de la clase anidada

    public NaveExploradora() {
        this.profundidadSoportada  = 500;       // límite base
        this.profundidadAnclaje = 0;            // Superficie por defecto
        this.modulo = new ModuloProfundidad();
    }

    /**
     * Anclar la nave en una determinada profundidad por el jugador
     * @param profundidad_nueva int
     */
    public void anclarNaveExploradora(int profundidad_nueva){
        if (profundidad_nueva <= getProfundidadMaximaPermitida()){
            this.profundidadAnclaje = profundidad_nueva;
            System.out.println("Nave Exploradora anclada a " + profundidad_nueva+ " m.");
        } else {
            System.out.println("La nave no puede descender tan profundo (máx " + getProfundidadMaximaPermitida() + "m).");
        }
    }

    public int getProfundidadAnclaje(){
        return profundidadAnclaje;
    }

    @Override
    public boolean puedeAcceder(int profundidad_minima) {
        return getProfundidadMaximaPermitida() >= profundidad_minima;
    }

    private int getProfundidadMaximaPermitida(){
        return modulo.isActivo() ? (profundidadSoportada + modulo.getProfundidadExtra()) : profundidadSoportada;
    }

    @Override
    public void transferirObjetos(){
        System.out.println("Transferencia de objetos aún no implementada");
    }

    /**
     * Clase anidada para el Módulo de profundidad
     */
    public class ModuloProfundidad{
        private boolean activo;
        private int profundidad_extra;

        public ModuloProfundidad(){
            this.activo = false;
            this.profundidad_extra = 0;
        }

        public void aumentarProfundidad(int profundidadExtra) {
            if (!activo) {
                this.profundidad_extra = profundidadExtra;
                this.activo = true;
                System.out.println("Módulo de profundidad activado: +" + profundidadExtra + " m.");
            }
        }

        public boolean isActivo(){
            return activo;
        }

        public int getProfundidadExtra(){
            return profundidad_extra;
        }
    }

    /**
     * Permite obtener la referencia al módulo para instalar/configurar desde fuera
     * @return referencia al objeto modulo
     */
    public ModuloProfundidad getModuloProfundidad(){
        return modulo;
    }
}
