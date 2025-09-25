import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuAlimentos {
    private static ProductoMenu[] menu;
    private static final String[] RUTAS_CSV_MENU = {
            "data/menu de cafeteria 1.0.csv",
            "src/data/menu de cafeteria 1.0.csv",
            "menu de cafeteria 1.0.csv"
    };

    public static void cargarMenuDesdeCSV() {
        File archivo = encontrarArchivo(RUTAS_CSV_MENU, "menú");
        if (archivo == null) {
            System.out.println("No se pudo encontrar el archivo CSV del menú.");
            menu = new ProductoMenu[0];
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            int contadorLineas = 0;

            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty() && !linea.startsWith("ID") && !linea.startsWith(",,")) {
                    contadorLineas++;
                }
            }
            br.close();

            if (contadorLineas == 0) {
                menu = new ProductoMenu[0];
                return;
            }

            menu = new ProductoMenu[contadorLineas];

            br = new BufferedReader(new FileReader(archivo));
            int index = 0;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith(",,"))
                    continue;

                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    try {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        double precio = Double.parseDouble(datos[2].trim());
                        String categoria = determinarCategoria(id);

                        menu[index++] = new ProductoMenu(id, nombre, categoria, precio);
                    } catch (NumberFormatException e) {
                        System.out.println("Error en línea: " + linea);
                    }
                }
            }
            br.close();
            System.out.println("Menú cargado correctamente: " + menu.length + " productos");

        } catch (IOException e) {
            System.out.println("Error al cargar el menú desde CSV: " + e.getMessage());
            menu = new ProductoMenu[0];
        }
    }

    private static File encontrarArchivo(String[] rutas, String tipo) {
        // Buscar en múltiples ubicaciones posibles
        String[] ubicaciones = {
                "", // Directorio actual
                System.getProperty("user.dir"), // Directorio de trabajo
                new File(System.getProperty("user.dir")).getParent(), // Directorio padre
                System.getProperty("user.dir") + "/src", // Subdirectorio src
                System.getProperty("user.dir") + "/data", // Subdirectorio data
                System.getProperty("user.dir") + "/src/data" // src/data
        };

        for (String ubicacion : ubicaciones) {
            for (String ruta : rutas) {
                try {
                    File archivo;
                    if (ubicacion.isEmpty()) {
                        archivo = new File(ruta);
                    } else {
                        archivo = new File(ubicacion, ruta);
                    }

                    System.out.println("Buscando: " + archivo.getAbsolutePath());

                    if (archivo.exists() && archivo.isFile()) {
                        System.out.println("✓ Archivo de " + tipo + " encontrado: " + archivo.getAbsolutePath());
                        return archivo;
                    }
                } catch (Exception e) {
                    // Continuar con la siguiente ruta
                }
            }
        }

        System.out.println("✗ No se encontró el archivo CSV de " + tipo);
        System.out.println("Directorios buscados:");
        for (String ubicacion : ubicaciones) {
            System.out.println("  - " + (ubicacion.isEmpty() ? "Directorio actual" : ubicacion));
        }
        return null;
    }

    private static String determinarCategoria(int id) {
        if (id >= 101 && id <= 130)
            return "Paninis y Antipasti";
        if (id >= 201 && id <= 225)
            return "Bebidas Frías";
        if (id >= 301 && id <= 325)
            return "Bebidas Calientes";
        if (id >= 401 && id <= 420)
            return "Postres";

        // Para los IDs del 1-25 que tienes en tu ejemplo, asignar categorías
        // específicas
        if (id >= 1 && id <= 12) {
            if (id <= 6)
                return "Bebidas Calientes";
            else
                return "Bebidas Frías";
        }
        if (id >= 13 && id <= 19)
            return "Paninis y Antipasti";
        if (id >= 20 && id <= 25)
            return "Postres";

        return "General";
    }

    public static void mostrarMenu() {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }

        if (menu.length == 0) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println(
                "║                               MENÚ DE CAFETERÍA POWER CAFE                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");

        // Agrupar productos por categoría
        Map<String, List<ProductoMenu>> productosPorCategoria = new LinkedHashMap<>();

        // Inicializar categorías en el orden deseado
        String[] categorias = { "Bebidas Calientes", "Bebidas Frías", "Paninis y Antipasti", "Postres", "General" };
        for (String categoria : categorias) {
            productosPorCategoria.put(categoria, new ArrayList<>());
        }

        // Agrupar productos
        for (ProductoMenu producto : menu) {
            if (productosPorCategoria.containsKey(producto.categoria)) {
                productosPorCategoria.get(producto.categoria).add(producto);
            } else {
                productosPorCategoria.get("General").add(producto);
            }
        }

        // Mostrar productos por categoría
        boolean primeraCategoria = true;
        for (Map.Entry<String, List<ProductoMenu>> entry : productosPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            List<ProductoMenu> productos = entry.getValue();

            if (!productos.isEmpty()) {
                // Encabezado de categoría
                if (!primeraCategoria) {
                    System.out.println(
                            "╠════════════════════════════════════════════════════════════════════════════════╣");
                }
                System.out.printf("║ %-78s ║\n", " " + categoria.toUpperCase() + " ");
                System.out
                        .println("╠════════╦═══════════════════════════════════════════════════════╦═══════════════╣");
                System.out
                        .println("║   ID   ║                  PRODUCTO                             ║     PRECIO    ║");
                System.out
                        .println("╠════════╬═══════════════════════════════════════════════════════╬═══════════════╣");

                // Productos de la categoría
                for (ProductoMenu producto : productos) {
                    String nombreProducto = producto.nombre;
                    // Limitar la longitud del nombre para que quepa en la tabla
                    if (nombreProducto.length() > 40) {
                        nombreProducto = nombreProducto.substring(0, 37) + "...";
                    }

                    System.out.printf("║  %4d  ║ %-53s ║   $%9.1f  ║\n",
                            producto.id, nombreProducto, producto.precio);
                }
                primeraCategoria = false;
            }
        }

        System.out.println("╚════════╩═══════════════════════════════════════════════════════╩═══════════════╝");
        System.out.println("Total de productos disponibles: " + menu.length + "\n");
    }

    public static ProductoMenu obtenerProducto(int opcion) {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }

        if (menu.length == 0) {
            return null;
        }

        if (opcion >= 1 && opcion <= menu.length) {
            return menu[opcion - 1];
        }
        return null;
    }

    public static int obtenerCantidadProductos() {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }
        return menu.length;
    }

    public static ProductoMenu buscarProductoPorID(int id) {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }

        if (menu.length == 0) {
            return null;
        }

        for (ProductoMenu producto : menu) {
            if (producto.id == id) {
                return producto;
            }
        }
        return null;
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
            return String.format("%d. %s - $%.2f MXN", id, nombre, precio);
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
            return String.format("%dx %s - $%.2f MXN | Comentarios: %s | Hora: %s",
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
            String horario = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║                   TICKET MESA " + String.format("%-23s", numeroMesa) + "║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║ Fecha: " + String.format("%-45s", horario) + " ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.println("║   Cant. Producto                           Precio    ║");
            System.out.println("╠══════════════════════════════════════════════════════╣");

            for (PedidoMesa pedido : pedidos) {
                String nombreProducto = pedido.producto.nombre;
                if (nombreProducto.length() > 30) {
                    nombreProducto = nombreProducto.substring(0, 30) + "...";
                }

                String lineaProducto = String.format("║   %-5d %-33s $%7.2f   ║",
                        pedido.cantidad, nombreProducto, pedido.getSubtotal());
                System.out.println(lineaProducto);

                if (pedido.comentarios != null && !pedido.comentarios.trim().isEmpty()
                        && !pedido.comentarios.equals("nan")) {
                    String comentario = pedido.comentarios;
                    if (comentario.length() > 42) {
                        comentario = comentario.substring(0, 42) + "...";
                    }
                    System.out.println("║        Comentarios: " + String.format("%-33s", comentario) + "║");
                }
                System.out.println("║                                                      ║");
            }

            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.printf("║        TOTAL:                             $%10.2f║%n", calcularTotal());
            System.out.println("╚══════════════════════════════════════════════════════╝");
        }

        public int getCantidadPedidos() {
            int total = 0;
            for (PedidoMesa pedido : pedidos) {
                total += pedido.cantidad;
            }
            return total;
        }

        public int getNumeroMesa() {
            return numeroMesa;
        }

        public Queue<PedidoMesa> getPedidos() {
            return pedidos;
        }
    }
}