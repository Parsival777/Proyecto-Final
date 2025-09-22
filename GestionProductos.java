import java.util.Scanner;

public class GestionProductos {
    private static NodoArbol raiz = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionProductos() {
        System.out.println("\n=== GESTIÓN DE PRODUCTOS DEL MENÚ ===");
        System.out.println("1. Agregar producto al menú");
        System.out.println("2. Eliminar producto del menú");
        System.out.println("3. Mostrar productos (inorden)");
        System.out.println("4. Buscar producto");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionProductos(scanner.nextLine().trim());
    }

    private static void procesarOpcionProductos(String input) {
        procesarOpcionRecursivo(input);
    }

    private static void procesarOpcionRecursivo(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));

        procesarOpcion(input, "1", GestionProductos::agregarProducto);
        procesarOpcion(input, "2", GestionProductos::eliminarProducto);
        procesarOpcion(input, "3", GestionProductos::mostrarProductosInorden);
        procesarOpcion(input, "4", GestionProductos::buscarProducto);

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
            menuGestionProductos();
        }
    }

    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);

        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuGestionProductos();
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

        System.out.print("Categoría: ");
        String categoria = obtenerEntradaNoVaciaRecursivo("Categoría: ", scanner.nextLine().trim());

        raiz = insertarRecursivo(raiz, new Producto(id, nombre, categoria));
        System.out.println("Producto agregado al menú.");
    }

    private static NodoArbol insertarRecursivo(NodoArbol nodo, Producto producto) {
        if (nodo == null) {
            return new NodoArbol(producto);
        }

        return insertarPorComparacion(nodo, producto, Integer.compare(producto.id, nodo.producto.id));
    }

    private static NodoArbol insertarPorComparacion(NodoArbol nodo, Producto producto, int comparacion) {
        return manejarComparacionInsercion(nodo, producto, comparacion);
    }

    private static NodoArbol manejarComparacionInsercion(NodoArbol nodo, Producto producto, int comparacion) {
        if (comparacion < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, producto);
            return nodo;
        }

        return manejarComparacionMayorOIgual(nodo, producto, comparacion);
    }

    private static NodoArbol manejarComparacionMayorOIgual(NodoArbol nodo, Producto producto, int comparacion) {
        if (comparacion > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, producto);
            return nodo;
        }

        System.out.println("Ya existe un producto con este ID.");
        return nodo;
    }

    private static void eliminarProducto() {
        System.out.print("ID del producto a eliminar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del producto a eliminar: ", scanner.nextLine().trim(), 0);

        raiz = eliminarRecursivo(raiz, id);
        System.out.println("Producto eliminado si existía.");
    }

    private static NodoArbol eliminarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }

        return eliminarPorComparacion(nodo, id, Integer.compare(id, nodo.producto.id));
    }

    private static NodoArbol eliminarPorComparacion(NodoArbol nodo, int id, int comparacion) {
        return manejarComparacionEliminacion(nodo, id, comparacion);
    }

    private static NodoArbol manejarComparacionEliminacion(NodoArbol nodo, int id, int comparacion) {
        if (comparacion < 0) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, id);
            return nodo;
        }

        return manejarComparacionMayorEliminacion(nodo, id, comparacion);
    }

    private static NodoArbol manejarComparacionMayorEliminacion(NodoArbol nodo, int id, int comparacion) {
        if (comparacion > 0) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, id);
            return nodo;
        }

        return eliminarNodoEncontrado(nodo);
    }

    private static NodoArbol eliminarNodoEncontrado(NodoArbol nodo) {
        return manejarHijosEliminacion(nodo);
    }

    private static NodoArbol manejarHijosEliminacion(NodoArbol nodo) {
        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }

        return manejarHijoDerechoEliminacion(nodo);
    }

    private static NodoArbol manejarHijoDerechoEliminacion(NodoArbol nodo) {
        if (nodo.derecho == null) {
            return nodo.izquierdo;
        }

        nodo.producto = encontrarMinimo(nodo.derecho);
        nodo.derecho = eliminarRecursivo(nodo.derecho, nodo.producto.id);
        return nodo;
    }

    private static Producto encontrarMinimo(NodoArbol nodo) {
        if (nodo.izquierdo == null) {
            return nodo.producto;
        }
        return encontrarMinimo(nodo.izquierdo);
    }

    private static void mostrarProductosInorden() {
        if (raiz == null) {
            System.out.println("El menú está vacío.");
            return;
        }

        System.out.println("Productos (ordenados por ID):");
        inordenRecursivo(raiz, 1);
    }

    private static void inordenRecursivo(NodoArbol nodo, int nivel) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo, nivel + 1);
            System.out.println("Nivel " + nivel + ": " + nodo.producto);
            inordenRecursivo(nodo.derecho, nivel + 1);
        }
    }

    private static void buscarProducto() {
        System.out.print("ID del producto a buscar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del producto a buscar: ", scanner.nextLine().trim(), 0);

        Producto resultado = buscarRecursivo(raiz, id);
        manejarResultadoBusqueda(resultado);
    }

    private static void manejarResultadoBusqueda(Producto resultado) {
        manejarResultadoEncontrado(resultado);
    }

    private static void manejarResultadoEncontrado(Producto resultado) {
        if (resultado != null) {
            System.out.println("Producto encontrado: " + resultado);
            return;
        }
        manejarResultadoNoEncontrado();
    }

    private static void manejarResultadoNoEncontrado() {
        System.out.println("Producto no encontrado.");
    }

    private static Producto buscarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }

        return buscarPorComparacion(nodo, id, Integer.compare(id, nodo.producto.id));
    }

    private static Producto buscarPorComparacion(NodoArbol nodo, int id, int comparacion) {
        return manejarComparacionBusqueda(nodo, id, comparacion);
    }

    private static Producto manejarComparacionBusqueda(NodoArbol nodo, int id, int comparacion) {
        if (comparacion == 0) {
            return nodo.producto;
        }

        return manejarComparacionDiferenteBusqueda(nodo, id, comparacion);
    }

    private static Producto manejarComparacionDiferenteBusqueda(NodoArbol nodo, int id, int comparacion) {
        if (comparacion < 0) {
            return buscarRecursivo(nodo.izquierdo, id);
        }

        return buscarRecursivo(nodo.derecho, id);
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

    private static boolean verificarEsNumero(String str, int index, boolean esNumero) {
        if (index >= str.length()) return esNumero;
        if (!esNumero) return false;

        char c = str.charAt(index);
        boolean esDigito = (c >= '0' && c <= '9') || (index == 0 && c == '-');

        return verificarEsNumero(str, index + 1, esNumero && esDigito);
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

    static class NodoArbol {
        Producto producto;
        NodoArbol izquierdo;
        NodoArbol derecho;

        public NodoArbol(Producto producto) {
            this.producto = producto;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    static class Producto {
        int id;
        String nombre;
        String categoria;

        public Producto(int id, String nombre, String categoria) {
            this.id = id;
            this.nombre = nombre;
            this.categoria = categoria;
        }

        @Override
        public String toString() {
            return "Producto{" + "id=" + id + ", nombre='" + nombre + '\'' + ", categoria='" + categoria + '\'' + '}';
        }
    }
}
