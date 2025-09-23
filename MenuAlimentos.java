package src;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

public class MenuAlimentos {
    private static ProductoMenu[] menu;
    private static final String[] RUTAS_CSV_MENU = {
        "D:\\Estructura de datos\\ProyectoFinal\\data\\MENU DE CAFETERIA .csv",
        "data/MENU DE CAFETERIA.csv",
        "../data/MENU DE CAFETERIA.csv",
        "./data/MENU DE CAFETERIA.csv",
        "MENU DE CAFETERIA.csv"
    };

    // Método para cargar el menú desde CSV
    public static void cargarMenuDesdeCSV() {
        File archivo = encontrarArchivo(RUTAS_CSV_MENU, "menú");
        if (archivo == null) {
            System.out.println("❌ No se pudo cargar el menú. El archivo CSV no fue encontrado.");
            menu = new ProductoMenu[0]; // Array vacío en lugar de menú predeterminado
            return;
        }
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            int contadorLineas = 0;
            
            // Contar líneas válidas
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty() && !linea.startsWith("ID") && !linea.startsWith(",,")) {
                    contadorLineas++;
                }
            }
            br.close();
            
            if (contadorLineas == 0) {
                System.out.println("❌ El archivo CSV está vacío o no contiene datos válidos.");
                menu = new ProductoMenu[0];
                return;
            }
            
            // Crear array del tamaño correcto
            menu = new ProductoMenu[contadorLineas];
            
            // Leer datos
            br = new BufferedReader(new FileReader(archivo));
            int index = 0;
            br.readLine(); // Saltar encabezado
            
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith(",,")) continue;
                
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    try {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        double precio = Double.parseDouble(datos[2].trim());
                        String categoria = determinarCategoria(id);
                        
                        menu[index++] = new ProductoMenu(id, nombre, categoria, precio);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Error en formato de datos en línea: " + linea);
                    }
                }
            }
            br.close();
            System.out.println("✓ Menú cargado correctamente: " + menu.length + " productos");
            
        } catch (IOException e) {
            System.out.println("❌ Error al cargar el menú desde CSV: " + e.getMessage());
            menu = new ProductoMenu[0]; // Array vacío en lugar de menú predeterminado
        }
    }
    
    private static File encontrarArchivo(String[] rutas, String tipo) {
        System.out.println("Buscando archivo de " + tipo + "...");
        
        for (String ruta : rutas) {
            File archivo = new File(ruta);
            System.out.println("Probando ruta: " + archivo.getAbsolutePath());
            
            if (archivo.exists() && archivo.isFile()) {
                System.out.println("✓ Archivo de " + tipo + " encontrado: " + archivo.getAbsolutePath());
                return archivo;
            }
            
            // Intentar con ruta absoluta desde el directorio de trabajo actual
            File archivoAbsoluto = new File(System.getProperty("user.dir"), ruta);
            System.out.println("Probando ruta absoluta: " + archivoAbsoluto.getAbsolutePath());
            
            if (archivoAbsoluto.exists() && archivoAbsoluto.isFile()) {
                System.out.println("✓ Archivo de " + tipo + " encontrado: " + archivoAbsoluto.getAbsolutePath());
                return archivoAbsoluto;
            }
        }
        
        System.out.println("✗ No se encontró el archivo CSV de " + tipo + " en las rutas esperadas");
        return null;
    }
    
    private static String determinarCategoria(int id) {
        if (id >= 101 && id <= 130) return "Paninis y Antipasti";
        if (id >= 201 && id <= 225) return "Bebidas Frías";
        if (id >= 301 && id <= 325) return "Bebidas Calientes";
        if (id >= 401 && id <= 420) return "Postres";
        return "General";
    }
    
    public static void mostrarMenu() {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }
        
        if (menu.length == 0) {
            System.out.println("❌ No hay productos disponibles en el menú.");
            return;
        }
        
        System.out.println("\n=== MENÚ DE CAFETERÍA ===");
        for (int i = 0; i < menu.length; i++) {
            System.out.println((i + 1) + ". " + menu[i]);
        }
    }

    public static ProductoMenu obtenerProducto(int opcion) {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }
        
        if (menu.length == 0) {
            System.out.println("❌ No hay productos disponibles en el menú.");
            return null;
        }
        
        if (opcion >= 1 && opcion <= menu.length) {
            return menu[opcion - 1];
        }
        System.out.println("❌ Opción inválida. Por favor seleccione un número entre 1 y " + menu.length);
        return null;
    }

    public static int obtenerCantidadProductos() {
        if (menu == null) {
            cargarMenuDesdeCSV();
        }
        return menu.length;
    }

    // Método para buscar producto por ID (para integración con otras clases)
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
            return String.format("%d. %s - $%.2f MXN (%s)", id, nombre, precio, categoria);
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
            System.out.println("\n=== TICKET MESA " + numeroMesa + " ===");
            int index = 1;
            for (PedidoMesa pedido : pedidos) {
                System.out.println(index++ + ". " + pedido);
            }
            System.out.printf("TOTAL: $%.2f MXN\n", calcularTotal());
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
