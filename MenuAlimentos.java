import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

public class MenuAlimentos {
    private static ProductoMenu[] menu = {
            new ProductoMenu(1, "Café Americano", "Bebidas", 2.50),
            new ProductoMenu(2, "Café Latte", "Bebidas", 3.00),
            new ProductoMenu(3, "Capuchino", "Bebidas", 3.50),
            new ProductoMenu(4, "Té Verde", "Bebidas", 2.00),
            new ProductoMenu(5, "Sandwich de Pollo", "Comida", 5.50),
            new ProductoMenu(6, "Ensalada César", "Comida", 4.50),
            new ProductoMenu(7, "Bagel con Queso Crema", "Comida", 3.00),
            new ProductoMenu(8, "Pastel de Chocolate", "Postres", 4.00),
            new ProductoMenu(9, "Galletas", "Postres", 2.50),
            new ProductoMenu(10, "Jugo de Naranja", "Bebidas", 3.00)
    };

    public static void mostrarMenu() {
        System.out.println("\n=== MENÚ DE ALIMENTOS ===");
        for (int i = 0; i < menu.length; i++) {
            System.out.println((i + 1) + ". " + menu[i]);
        }
    }

    public static ProductoMenu obtenerProducto(int opcion) {
        if (opcion >= 1 && opcion <= menu.length) {
            return menu[opcion - 1];
        }
        return null;
    }

    public static int obtenerCantidadProductos() {
        return menu.length;
    }

    static class ProductoMenu {
        int id;
        String nombre;
        String categoria;
        double precio;

        public ProductoMenu(int id, String nombre, String categoria, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.categoria = categoria;
            this.precio = precio;
        }

        @Override
        public String toString() {
            return String.format("%s - $%.2f (%s)", nombre, precio, categoria);
        }
    }

    static class PedidoMesa {
        ProductoMenu producto;
        String comentarios;
        String horaPedido;
        int cantidad;

        public PedidoMesa(ProductoMenu producto, String comentarios, int cantidad) {
            this.producto = producto;
            this.comentarios = comentarios;
            this.cantidad = cantidad;
            this.horaPedido = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mma - dd/MM/yyyy"));
        }

        public double getSubtotal() {
            return producto.precio * cantidad;
        }

        @Override
        public String toString() {
            return String.format("%dx %s - $%.2f | Comentarios: %s | Hora: %s",
                    cantidad, producto.nombre, getSubtotal(), comentarios, horaPedido);
        }
    }

    static class Ticket {
        private Queue<PedidoMesa> pedidos;
        private int numeroMesa;

        public Ticket(int numeroMesa) {
            this.numeroMesa = numeroMesa;
            this.pedidos = new LinkedList<>();
        }

        public void agregarPedido(PedidoMesa pedido) {
            pedidos.add(pedido);
        }

        public double calcularTotal() {
            double total = 0;
            for (PedidoMesa pedido : pedidos) {
                total += pedido.getSubtotal();
            }
            return total;
        }

        public void mostrarTicket() {
            System.out.println("\n=== TICKET MESA " + numeroMesa + " ===");
            int index = 1;
            for (PedidoMesa pedido : pedidos) {
                System.out.println(index++ + ". " + pedido);
            }
            System.out.printf("TOTAL: $%.2f\n", calcularTotal());
        }

        public int getCantidadPedidos() {
            int total = 0;
            for (PedidoMesa pedido : pedidos) {
                total += pedido.cantidad;
            }
            return total;
        }
    }
}