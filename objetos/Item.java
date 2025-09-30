package objetos;

/**
 * Representa un ítem en el inventario
 */
public class Item {
    private ItemTipo tipo;
    private int cantidad;

    /**
     * Constructor del objeto Item
     * @param tipo ItemTipo
     * @param cantidad int
     */
    public Item(ItemTipo tipo, int cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
    }
}
