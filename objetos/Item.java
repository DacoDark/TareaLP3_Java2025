package objetos;

/**
 * Representa un ítem en el inventario
 */
public class Item {
    private ItemTipo tipo;
    private int cantidad;

    /**
     * Constructor del objeto Item
     * @param tipo tipo: ItemTipo; descripción: Tipo del objeto que se va a crear.
     * @param cantidad tipo: int; descripción: Cantidad del objeto que se va a crear.
     */
    public Item(ItemTipo tipo, int cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    //*******************************************
    //*     Getters y Setters de la Clase       *
    //*******************************************

    /**
     * Getter del tipo del Item
     * @return tipo: ItemTipo; descripción: Retorna el tipo del item creado.
     */
    public ItemTipo getTipo() {
        return tipo;
    }

    /**
     * Setter del topo del Item
     * @param tipo tipo: ItemTipo; descripción: Cambia el tipo del Item, según el entregado por parámetro.
     */
    public void setTipo(ItemTipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter de la cantidad del item
     * @return tipo: int; descripción: Retorna la cantidad entera del item.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Setter de la cantidad disponible del item.
     * @param cantidad tipo: int; descripción: cantidad del item.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
