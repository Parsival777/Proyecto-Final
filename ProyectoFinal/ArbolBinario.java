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
        
        procesarOpcionArbol();
    }
    
    private static void procesarOpcionArbol() {
        String input = scanner.nextLine().trim();
        
        switch (input) {
            case "1":
                agregarEmpleado();
                menuArbolBinario();
                break;
            case "2":
                eliminarEmpleado();
                menuArbolBinario();
                break;
            case "3":
                mostrarEmpleadosInorden();
                menuArbolBinario();
                break;
            case "4":
                buscarEmpleado();
                menuArbolBinario();
                break;
            case "0":
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
                menuArbolBinario();
        }
    }
    
    private static void agregarEmpleado() {
        System.out.print("ID del empleado: ");
        int id = obtenerEnteroPositivo("ID del empleado: ");
        
        System.out.print("Nombre del empleado: ");
        String nombre = obtenerEntradaNoVacia("Nombre del empleado: ");
        
        System.out.print("Departamento: ");
        String departamento = obtenerEntradaNoVacia("Departamento: ");
        
        raiz = insertarRecursivo(raiz, new Empleado(id, nombre, departamento));
        System.out.println("Empleado agregado.");
    }
    
    private static NodoArbol insertarRecursivo(NodoArbol nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoArbol(empleado);
        }
        
        if (empleado.id < nodo.empleado.id) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, empleado);
        } else if (empleado.id > nodo.empleado.id) {
            nodo.derecho = insertarRecursivo(nodo.derecho, empleado);
        } else {
            System.out.println("Ya existe un empleado con este ID.");
        }
        
        return nodo;
    }
    
    private static void eliminarEmpleado() {
        System.out.print("ID del empleado a eliminar: ");
        int id = obtenerEnteroPositivo("ID del empleado a eliminar: ");
        
        raiz = eliminarRecursivo(raiz, id);
        System.out.println("Empleado eliminado si existía.");
    }
    
    private static NodoArbol eliminarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }
        
        if (id < nodo.empleado.id) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, id);
        } else if (id > nodo.empleado.id) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, id);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            
            nodo.empleado = encontrarMinimo(nodo.derecho);
            nodo.derecho = eliminarRecursivo(nodo.derecho, nodo.empleado.id);
        }
        
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
        int id = obtenerEnteroPositivo("ID del empleado a buscar: ");
        
        Empleado resultado = buscarRecursivo(raiz, id);
        if (resultado != null) {
            System.out.println("Empleado encontrado: " + resultado);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }
    
    private static Empleado buscarRecursivo(NodoArbol nodo, int id) {
        if (nodo == null) {
            return null;
        }
        
        if (id == nodo.empleado.id) {
            return nodo.empleado;
        }
        
        if (id < nodo.empleado.id) {
            return buscarRecursivo(nodo.izquierdo, id);
        } else {
            return buscarRecursivo(nodo.derecho, id);
        }
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