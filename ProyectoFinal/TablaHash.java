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
        
        procesarOpcionTablaHash(scanner.nextLine().trim());
    }
    
    private static void procesarOpcionTablaHash(String input) {
        procesarOpcionRecursiva(input);
    }
    
    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));
        
        procesarOpcion(input, "1", TablaHash::agregarProducto);
        procesarOpcion(input, "2", TablaHash::eliminarProducto);
        procesarOpcion(input, "3", TablaHash::buscarProducto);
        procesarOpcion(input, "4", TablaHash::mostrarProductos);
        
        procesarCasoDefault(input);
    }
    
    private static void procesarCasoBase(String input, String target, Runnable action) {
        if (input.equals(target)) {
            action.run();
        }
    }
    
    private static void procesarOpcion(String input, String opcion, Runnable funcion) {
        if (input.equals(opcion)) {
            funcion.run();
            menuTablaHash();
        }
    }
    
    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);
        
        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuTablaHash();
        }
    }
    
    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }
    
    private static void agregarProducto() {
        System.out.print("ID del producto: ");
        int id = obtenerEnteroPositivoRecursivo("ID del producto: ", scanner.nextLine().trim(), 0);
        
        System.out.print("Nombre del producto: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre del producto: ", scanner.nextLine().trim());
        
        System.out.print("Precio del producto: ");
        double precio = obtenerDoublePositivoRecursivo("Precio del producto: ", scanner.nextLine().trim(), 0);
        
        int indice = funcionHash(id);
        Producto nuevoProducto = new Producto(id, nombre, precio);
        
        manejarInsercionProducto(indice, nuevoProducto, 1);
    }
    
    private static void manejarInsercionProducto(int indice, Producto producto, int intento) {
        if (tabla[indice] == null) {
            tabla[indice] = producto;
            System.out.println("Producto agregado en posición " + indice);
            return;
        }
        
        manejarColisionInsercion(indice, producto, intento);
    }
    
    private static void manejarColisionInsercion(int indice, Producto producto, int intento) {
        if (intento >= TAMANO_TABLA) {
            System.out.println("No se pudo agregar el producto. Tabla llena.");
            return;
        }
        
        int nuevaPosicion = (indice + intento) % TAMANO_TABLA;
        if (tabla[nuevaPosicion] == null) {
            tabla[nuevaPosicion] = producto;
            System.out.println("Producto agregado en posición " + nuevaPosicion + " (colisión resuelta)");
            return;
        }
        
        manejarColisionInsercion(indice, producto, intento + 1);
    }
    
    private static void eliminarProducto() {
        System.out.print("ID del producto a eliminar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del producto a eliminar: ", scanner.nextLine().trim(), 0);
        
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        manejarEliminacionProducto(indice);
    }
    
    private static void manejarEliminacionProducto(int indice) {
        if (indice != -1) {
            tabla[indice] = null;
            System.out.println("Producto eliminado de la posición " + indice);
            return;
        }
        
        System.out.println("Producto no encontrado.");
    }
    
    private static void buscarProducto() {
        System.out.print("ID del producto a buscar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del producto a buscar: ", scanner.nextLine().trim(), 0);
        
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        manejarResultadoBusqueda(indice);
    }
    
    private static void manejarResultadoBusqueda(int indice) {
        if (indice != -1) {
            System.out.println("Producto encontrado: " + tabla[indice]);
            return;
        }
        
        System.out.println("Producto no encontrado.");
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
        
        mostrarPosicionRecursiva(indice);
        mostrarRecursivo(indice + 1);
    }
    
    private static void mostrarPosicionRecursiva(int indice) {
        if (tabla[indice] != null) {
            System.out.println("Posición " + indice + ": " + tabla[indice]);
            return;
        }
        
        System.out.println("Posición " + indice + ": Vacía");
    }
    
    private static int funcionHash(int id) {
        return id % TAMANO_TABLA;
    }
    
    private static String obtenerEntradaNoVaciaRecursivo(String mensaje, String input) {
        if (input.isEmpty()) {
            System.out.print("Este campo no puede estar vacío. " + mensaje);
            return obtenerEntradaNoVaciaRecursivo(mensaje, scanner.nextLine().trim());
        }
        return input;
    }
    
    private static int obtenerEnteroPositivoRecursivo(String mensaje, String input, int intentos) {
        if (intentos > 10) return 1;
        
        boolean esNumero = verificarEsNumero(input, 0, true);
        
        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroPositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }
        
        int valor = convertirStringAInt(input, 0, 0);
        
        if (valor <= 0) {
            System.out.print("Por favor, ingrese un número positivo. " + mensaje);
            return obtenerEnteroPositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }
        
        return valor;
    }
    
    private static double obtenerDoublePositivoRecursivo(String mensaje, String input, int intentos) {
        if (intentos > 10) return 1.0;
        
        boolean esNumero = verificarEsNumeroDecimal(input, 0, true, false);
        
        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerDoublePositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }
        
        double valor = convertirStringADouble(input, 0, 0.0, 1.0, false);
        
        if (valor <= 0) {
            System.out.print("Por favor, ingrese un número positivo. " + mensaje);
            return obtenerDoublePositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }
        
        return valor;
    }
    
    private static boolean verificarEsNumero(String str, int index, boolean esNumero) {
        if (index >= str.length()) return esNumero;
        if (!esNumero) return false;
        
        char c = str.charAt(index);
        boolean esDigito = (c >= '0' && c <= '9') || (index == 0 && c == '-');
        
        return verificarEsNumero(str, index + 1, esNumero && esDigito);
    }
    
    private static boolean verificarEsNumeroDecimal(String str, int index, boolean esNumero, boolean tienePunto) {
        if (index >= str.length()) return esNumero;
        if (!esNumero) return false;
        
        char c = str.charAt(index);
        boolean esDigito = (c >= '0' && c <= '9');
        boolean esPunto = (c == '.' && !tienePunto && index > 0);
        boolean esSigno = (c == '-' && index == 0);
        
        boolean nuevoTienePunto = tienePunto || esPunto;
        
        return verificarEsNumeroDecimal(str, index + 1, 
                                      esNumero && (esDigito || esPunto || esSigno), 
                                      nuevoTienePunto);
    }
    
    private static int convertirStringAInt(String str, int index, int resultado) {
        if (index >= str.length()) return resultado;
        
        char c = str.charAt(index);
        if (c == '-') {
            return -convertirStringAInt(str, index + 1, 0);
        }
        
        int digito = c - '0';
        int nuevoResultado = resultado * 10 + digito;
        
        return convertirStringAInt(str, index + 1, nuevoResultado);
    }
    
    private static double convertirStringADouble(String str, int index, double resultado, double divisor, boolean despuesPunto) {
        if (index >= str.length()) return resultado;
        
        char c = str.charAt(index);
        if (c == '-') {
            return -convertirStringADouble(str, index + 1, 0, 1, false);
        }
        
        if (c == '.') {
            return convertirStringADouble(str, index + 1, resultado, 10, true);
        }
        
        int digito = c - '0';
        
        return manejarDigitoDouble(str, index, resultado, divisor, despuesPunto, digito);
    }
    
    private static double manejarDigitoDouble(String str, int index, double resultado, double divisor, boolean despuesPunto, int digito) {
        if (despuesPunto) {
            double nuevoResultado = resultado + (digito / divisor);
            return convertirStringADouble(str, index + 1, nuevoResultado, divisor * 10, true);
        }
        
        double nuevoResultado = resultado * 10 + digito;
        return convertirStringADouble(str, index + 1, nuevoResultado, divisor, false);
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