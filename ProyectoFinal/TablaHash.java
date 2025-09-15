import java.util.Scanner;

public class TablaHash {
    private static final int TAMANO_TABLA = 10;
    private static Producto[] tabla = new Producto[TAMANO_TABLA];
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menuTablaHash() {
        System.out.println("\n=== GESTIÓN DE TABLAS HASH ===");
        System.out.println("1. Agregar producto");
        System.out.println("2. Eliminar producto");
        System.out.println("3. Buscar producto");
        System.out.println("4. Mostrar todos los productos");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionTablaHash();
    }
    
    private static void procesarOpcionTablaHash() {
        String input = scanner.nextLine().trim();
        
        switch (input) {
            case "1":
                agregarProducto();
                menuTablaHash();
                break;
            case "2":
                eliminarProducto();
                menuTablaHash();
                break;
            case "3":
                buscarProducto();
                menuTablaHash();
                break;
            case "4":
                mostrarProductos();
                menuTablaHash();
                break;
            case "0":
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
                menuTablaHash();
        }
    }
    
    private static void agregarProducto() {
        System.out.print("ID del producto: ");
        int id = obtenerEnteroPositivo("ID del producto: ");
        
        System.out.print("Nombre del producto: ");
        String nombre = obtenerEntradaNoVacia("Nombre del producto: ");
        
        System.out.print("Precio del producto: ");
        double precio = obtenerDoublePositivo("Precio del producto: ");
        
        int indice = funcionHash(id);
        Producto nuevoProducto = new Producto(id, nombre, precio);
        
        if (tabla[indice] == null) {
            tabla[indice] = nuevoProducto;
            System.out.println("Producto agregado en posición " + indice);
        } else {
            int nuevaPosicion = resolverColision(indice, 1);
            if (nuevaPosicion != -1) {
                tabla[nuevaPosicion] = nuevoProducto;
                System.out.println("Producto agregado en posición " + nuevaPosicion + " (colisión resuelta)");
            } else {
                System.out.println("No se pudo agregar el producto. Tabla llena.");
            }
        }
    }
    
    private static int resolverColision(int indice, int intento) {
        if (intento >= TAMANO_TABLA) {
            return -1;
        }
        
        int nuevaPosicion = (indice + intento) % TAMANO_TABLA;
        if (tabla[nuevaPosicion] == null) {
            return nuevaPosicion;
        }
        
        return resolverColision(indice, intento + 1);
    }
    
    private static void eliminarProducto() {
        System.out.print("ID del producto a eliminar: ");
        int id = obtenerEnteroPositivo("ID del producto a eliminar: ");
        
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            tabla[indice] = null;
            System.out.println("Producto eliminado de la posición " + indice);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
    
    private static void buscarProducto() {
        System.out.print("ID del producto a buscar: ");
        int id = obtenerEnteroPositivo("ID del producto a buscar: ");
        
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            System.out.println("Producto encontrado: " + tabla[indice]);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
    
    private static int buscarIndiceRecursivo(int id, int indice, int intento) {
        if (intento >= TAMANO_TABLA) {
            return -1;
        }
        
        int posicion = (indice + intento) % TAMANO_TABLA;
        if (tabla[posicion] != null && tabla[posicion].id == id) {
            return posicion;
        }
        
        return buscarIndiceRecursivo(id, indice, intento + 1);
    }
    
    private static void mostrarProductos() {
        System.out.println("Contenido de la tabla hash:");
        mostrarRecursivo(0);
    }
    
    private static void mostrarRecursivo(int indice) {
        if (indice >= TAMANO_TABLA) {
            return;
        }
        
        if (tabla[indice] != null) {
            System.out.println("Posición " + indice + ": " + tabla[indice]);
        } else {
            System.out.println("Posición " + indice + ": Vacía");
        }
        
        mostrarRecursivo(indice + 1);
    }
    
    private static int funcionHash(int id) {
        return id % TAMANO_TABLA;
    }
    
    
    private static String obtenerEntradaNoVacia(String mensaje) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.print("Este campo no puede estar vacío. " + mensaje);
            return obtenerEntradaNoVacia(mensaje);
        }
        return input;
    }
    
    private static int obtenerEnteroPositivo(String mensaje) {
        String input = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(input);
            if (valor > 0) {
                return valor;
            } else {
                System.out.print("Por favor, ingrese un número positivo. " + mensaje);
                return obtenerEnteroPositivo(mensaje);
            }
        } catch (NumberFormatException e) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroPositivo(mensaje);
        }
    }
    
    private static double obtenerDoublePositivo(String mensaje) {
        String input = scanner.nextLine().trim();
        try {
            double valor = Double.parseDouble(input);
            if (valor > 0) {
                return valor;
            } else {
                System.out.print("Por favor, ingrese un número positivo. " + mensaje);
                return obtenerDoublePositivo(mensaje);
            }
        } catch (NumberFormatException e) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerDoublePositivo(mensaje);
        }
    }
    
    static class Producto {
        int id;
        String nombre;
        double precio;
        
        public Producto(int id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
        
        @Override
        public String toString() {
            return "Producto{" + "id=" + id + ", nombre='" + nombre + '\'' + ", precio=" + precio + '}';
        }
    }
}