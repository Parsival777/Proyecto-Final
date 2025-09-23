package src;

import java.util.Scanner;

public class GestionEmpleados {
    private static NodoArbolEmpleados raiz = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionEmpleados() {
        System.out.println("\n=== GESTIÓN DE EMPLEADOS (ÁRBOL BINARIO) ===");
        System.out.println("1. Agregar empleado");
        System.out.println("2. Eliminar empleado");
        System.out.println("3. Mostrar empleados por departamento (inorden)");
        System.out.println("4. Buscar empleado");
        System.out.println("5. Calcular nómina total recursiva");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionEmpleados(scanner.nextLine().trim());
    }

    private static void procesarOpcionEmpleados(String input) {
        if (input.equals("0")) {
            System.out.println("Volviendo al menú principal...");
            return;
        }

        if (input.equals("1")) {
            agregarEmpleado();
            menuGestionEmpleados();
            return;
        }

        if (input.equals("2")) {
            eliminarEmpleado();
            menuGestionEmpleados();
            return;
        }

        if (input.equals("3")) {
            mostrarEmpleadosInorden();
            menuGestionEmpleados();
            return;
        }

        if (input.equals("4")) {
            buscarEmpleado();
            menuGestionEmpleados();
            return;
        }

        if (input.equals("5")) {
            calcularNominaTotal();
            menuGestionEmpleados();
            return;
        }

        System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 5.");
        menuGestionEmpleados();
    }

    private static void agregarEmpleado() {
        System.out.print("ID del empleado: ");
        int id = obtenerEnteroPositivoRecursivo("ID del empleado: ", scanner.nextLine().trim(), 0);

        System.out.print("Nombre del empleado: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre del empleado: ", scanner.nextLine().trim());

        System.out.println("Seleccione el departamento:");
        System.out.println("1. Barista");
        System.out.println("2. Limpieza");
        System.out.println("3. Cocina");
        System.out.print("Opción: ");
        
        int opcionDepto = obtenerEnteroEnRangoRecursivo(1, 3, "Opción: ", scanner.nextLine().trim(), 0);
        String departamento = obtenerDepartamentoPorOpcion(opcionDepto);

        System.out.print("Salario mensual: ");
        double salario = obtenerDoublePositivoRecursivo("Salario mensual: ", scanner.nextLine().trim(), 0);

        raiz = insertarRecursivo(raiz, new Empleado(id, nombre, departamento, salario));
        System.out.println("Empleado agregado al sistema.");
    }

    private static String obtenerDepartamentoPorOpcion(int opcion) {
        switch (opcion) {
            case 1: return "Barista";
            case 2: return "Limpieza";
            case 3: return "Cocina";
            default: return "General";
        }
    }

    private static NodoArbolEmpleados insertarRecursivo(NodoArbolEmpleados nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoArbolEmpleados(empleado);
        }

        if (empleado.id < nodo.empleado.id) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, empleado);
            return nodo;
        }

        if (empleado.id > nodo.empleado.id) {
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

    private static NodoArbolEmpleados eliminarRecursivo(NodoArbolEmpleados nodo, int id) {
        if (nodo == null) {
            return null;
        }

        if (id < nodo.empleado.id) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, id);
            return nodo;
        }

        if (id > nodo.empleado.id) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, id);
            return nodo;
        }

        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }

        if (nodo.derecho == null) {
            return nodo.izquierdo;
        }

        nodo.empleado = encontrarMinimo(nodo.derecho);
        nodo.derecho = eliminarRecursivo(nodo.derecho, nodo.empleado.id);
        return nodo;
    }

    private static Empleado encontrarMinimo(NodoArbolEmpleados nodo) {
        if (nodo.izquierdo == null) {
            return nodo.empleado;
        }
        return encontrarMinimo(nodo.izquierdo);
    }

    private static void mostrarEmpleadosInorden() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        System.out.println("Empleados (ordenados por ID):");
        inordenRecursivo(raiz, 1);
    }

    private static void inordenRecursivo(NodoArbolEmpleados nodo, int nivel) {
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
        if (resultado != null) {
            System.out.println("Empleado encontrado: " + resultado);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    private static Empleado buscarRecursivo(NodoArbolEmpleados nodo, int id) {
        if (nodo == null) {
            return null;
        }

        if (id == nodo.empleado.id) {
            return nodo.empleado;
        }

        if (id < nodo.empleado.id) {
            return buscarRecursivo(nodo.izquierdo, id);
        }

        return buscarRecursivo(nodo.derecho, id);
    }

    private static void calcularNominaTotal() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        double total = calcularNominaRecursivo(raiz, 0.0);
        System.out.println("Nómina total mensual: $" + String.format("%.2f", total));
    }

    private static double calcularNominaRecursivo(NodoArbolEmpleados nodo, double acumulado) {
        if (nodo == null) {
            return acumulado;
        }

        double conIzquierdo = calcularNominaRecursivo(nodo.izquierdo, acumulado);
        double conActual = conIzquierdo + nodo.empleado.salario;
        return calcularNominaRecursivo(nodo.derecho, conActual);
    }

    private static String obtenerEntradaNoVaciaRecursivo(String mensaje, String input) {
        if (input.isEmpty()) {
            System.out.print("Este campo no puede estar vacío. " + mensaje);
            return obtenerEntradaNoVaciaRecursivo(mensaje, scanner.nextLine().trim());
        }
        return input;
    }

    private static int obtenerEnteroEnRangoRecursivo(int min, int max, String mensaje, String input, int intentos) {
        if (intentos > 10) return min;

        boolean esNumero = verificarEsNumero(input, 0, true);

        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), intentos + 1);
        }

        int valor = convertirStringAInt(input, 0, 0);

        if (valor >= min && valor <= max) {
            return valor;
        }

        System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ". " + mensaje);
        return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), 0);
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

        if (despuesPunto) {
            double nuevoResultado = resultado + (digito / divisor);
            return convertirStringADouble(str, index + 1, nuevoResultado, divisor * 10, true);
        }

        double nuevoResultado = resultado * 10 + digito;
        return convertirStringADouble(str, index + 1, nuevoResultado, divisor, false);
    }

    static class NodoArbolEmpleados {
        Empleado empleado;
        NodoArbolEmpleados izquierdo;
        NodoArbolEmpleados derecho;

        public NodoArbolEmpleados(Empleado empleado) {
            this.empleado = empleado;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    static class Empleado {
        int id;
        String nombre;
        String departamento;
        double salario;

        public Empleado(int id, String nombre, String departamento, double salario) {
            this.id = id;
            this.nombre = nombre;
            this.departamento = departamento;
            this.salario = salario;
        }

        @Override
        public String toString() {
            return "Empleado{" + "id=" + id + ", nombre='" + nombre + '\'' + 
                   ", departamento='" + departamento + '\'' + 
                   ", salario=$" + String.format("%.2f", salario) + '}';
        }
    }
}