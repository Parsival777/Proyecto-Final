import java.util.Scanner;

public class ArbolBinario {
    private static NodoArbol raiz = null;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menuArbolBinario() {
        System.out.println("\n=== GESTIÓN DE ÁRBOLES BINARIOS ===");
        System.out.println("1. Agregar empleado");
        System.out.println("2. Eliminar empleado");
        System.out.println("3. Mostrar empleados (inorden)");
        System.out.println("4. Buscar empleado");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionArbol(scanner.nextLine().trim());
    }
    
    private static void procesarOpcionArbol(String input) {
        procesarOpcionRecursivo(input);
    }
    
    private static void procesarOpcionRecursivo(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));
        
        procesarOpcion(input, "1", ArbolBinario::agregarEmpleado);
        procesarOpcion(input, "2", ArbolBinario::eliminarEmpleado);
        procesarOpcion(input, "3", ArbolBinario::mostrarEmpleadosInorden);
        procesarOpcion(input, "4", ArbolBinario::buscarEmpleado);
        
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
            menuArbolBinario();
        }
    }
    
    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);
        
        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuArbolBinario();
        }
    }
    
    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }
    
    private static void agregarEmpleado() {
        System.out.print("ID del empleado: ");
        int id = obtenerEnteroPositivoRecursivo("ID del empleado: ", scanner.nextLine().trim(), 0);
        
        System.out.print("Nombre del empleado: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre del empleado: ", scanner.nextLine().trim());
        
        System.out.print("Departamento: ");
        String departamento = obtenerEntradaNoVaciaRecursivo("Departamento: ", scanner.nextLine().trim());
        
        raiz = insertarRecursivo(raiz, new Empleado(id, nombre, departamento));
        System.out.println("Empleado agregado.");
    }
    
    private static NodoArbol insertarRecursivo(NodoArbol nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoArbol(empleado);
        }
        
        return insertarPorComparacion(nodo, empleado, Integer.compare(empleado.id, nodo.empleado.id));
    }
    
    private static NodoArbol insertarPorComparacion(NodoArbol nodo, Empleado empleado, int comparacion) {
        return manejarComparacionInsercion(nodo, empleado, comparacion);
    }
    
    private static NodoArbol manejarComparacionInsercion(NodoArbol nodo, Empleado empleado, int comparacion) {
        if (comparacion < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, empleado);
            return nodo;
        }
        
        return manejarComparacionMayorOIgual(nodo, empleado, comparacion);
    }
    
    private static NodoArbol manejarComparacionMayorOIgual(NodoArbol nodo, Empleado empleado, int comparacion) {
        if (comparacion > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, empleado);
            return nodo;
        }
        
        System.out.println("Ya existe un empleado con este ID.");
        return nodo;
    }
    
    private static void eliminarEmpleado() {
        System.out.print("ID del empleado a eliminar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del empleado a eliminar: ", scanner.nextLine().trim(), 0);
        
        raiz = eliminarRecursivo(raiz, id);
        System.out.println("Empleado eliminado si existía.");
    }
    
    private static NodoArbol eliminarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }
        
        return eliminarPorComparacion(nodo, id, Integer.compare(id, nodo.empleado.id));
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
        
        nodo.empleado = encontrarMinimo(nodo.derecho);
        nodo.derecho = eliminarRecursivo(nodo.derecho, nodo.empleado.id);
        return nodo;
    }
    
    private static Empleado encontrarMinimo(NodoArbol nodo) {
        if (nodo.izquierdo == null) {
            return nodo.empleado;
        }
        return encontrarMinimo(nodo.izquierdo);
    }
    
    private static void mostrarEmpleadosInorden() {
        if (raiz == null) {
            System.out.println("El árbol está vacío.");
            return;
        }
        
        System.out.println("Empleados (ordenados por ID):");
        inordenRecursivo(raiz, 1);
    }
    
    private static void inordenRecursivo(NodoArbol nodo, int nivel) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo, nivel + 1);
            System.out.println("Nivel " + nivel + ": " + nodo.empleado);
            inordenRecursivo(nodo.derecho, nivel + 1);
        }
    }
    
    private static void buscarEmpleado() {
        System.out.print("ID del empleado a buscar: ");
        int id = obtenerEnteroPositivoRecursivo("ID del empleado a buscar: ", scanner.nextLine().trim(), 0);
        
        Empleado resultado = buscarRecursivo(raiz, id);
        manejarResultadoBusqueda(resultado);
    }
    
    private static void manejarResultadoBusqueda(Empleado resultado) {
        manejarResultadoEncontrado(resultado);
    }
    
    private static void manejarResultadoEncontrado(Empleado resultado) {
        if (resultado != null) {
            System.out.println("Empleado encontrado: " + resultado);
            return;
        }
        manejarResultadoNoEncontrado();
    }
    
    private static void manejarResultadoNoEncontrado() {
        System.out.println("Empleado no encontrado.");
    }
    
    private static Empleado buscarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }
        
        return buscarPorComparacion(nodo, id, Integer.compare(id, nodo.empleado.id));
    }
    
    private static Empleado buscarPorComparacion(NodoArbol nodo, int id, int comparacion) {
        return manejarComparacionBusqueda(nodo, id, comparacion);
    }
    
    private static Empleado manejarComparacionBusqueda(NodoArbol nodo, int id, int comparacion) {
        if (comparacion == 0) {
            return nodo.empleado;
        }
        
        return manejarComparacionDiferenteBusqueda(nodo, id, comparacion);
    }
    
    private static Empleado manejarComparacionDiferenteBusqueda(NodoArbol nodo, int id, int comparacion) {
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
        Empleado empleado;
        NodoArbol izquierdo;
        NodoArbol derecho;
        
        public NodoArbol(Empleado empleado) {
            this.empleado = empleado;
            this.izquierdo = null;
            this.derecho = null;
        }
    }
    
    static class Empleado {
        int id;
        String nombre;
        String departamento;
        
        public Empleado(int id, String nombre, String departamento) {
            this.id = id;
            this.nombre = nombre;
            this.departamento = departamento;
        }
        
        @Override
        public String toString() {
            return "Empleado{" + "id=" + id + ", nombre='" + nombre + '\'' + ", departamento='" + departamento + '\'' + '}';
        }
    }
}