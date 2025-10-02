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

    public ItemTipo getTipo() {
        return tipo;
    }
    public void setTipo(ItemTipo tipo) {
        this.tipo = tipo;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    @Override
    public String toString() {
        return tipo + " x" + cantidad;
    }
}
