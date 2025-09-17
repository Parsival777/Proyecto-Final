import java.util.Scanner;

public class OrdenamientoBusqueda {
    private static int[] datos = new int[100];
    private static int contador = 0;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menuOrdenamientoBusqueda() {
        System.out.println("\n=== ORDENAMIENTO Y BÚSQUEDA ===");
        System.out.println("1. Agregar dato numérico");
        System.out.println("2. Ordenar datos (Quicksort recursivo)");
        System.out.println("3. Buscar dato (Búsqueda binaria recursiva)");
        System.out.println("4. Mostrar datos");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionOrdenamiento(scanner.nextLine().trim());
    }
    
    private static void procesarOpcionOrdenamiento(String input) {
        procesarOpcionRecursiva(input);
    }
    
    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));
        
        procesarOpcion(input, "1", OrdenamientoBusqueda::agregarDato);
        procesarOpcion(input, "2", OrdenamientoBusqueda::ordenarDatos);
        procesarOpcion(input, "3", OrdenamientoBusqueda::buscarDato);
        procesarOpcion(input, "4", OrdenamientoBusqueda::mostrarDatos);
        
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
            menuOrdenamientoBusqueda();
        }
    }
    
    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);
        
        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuOrdenamientoBusqueda();
        }
    }
    
    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }
    
    private static void agregarDato() {
        manejarCapacidadDatos();
    }
    
    private static void manejarCapacidadDatos() {
        if (contador >= datos.length) {
            System.out.println("No se pueden agregar más datos.");
            return;
        }
        
        System.out.print("Ingrese un número: ");
        int numero = obtenerEnteroRecursivo("Ingrese un número: ", scanner.nextLine().trim(), 0);
        
        datos[contador++] = numero;
        System.out.println("Número agregado: " + numero);
    }
    
    private static void ordenarDatos() {
        manejarDatosVacios(() -> {
            quicksortRecursivo(0, contador - 1);
            System.out.println("Datos ordenados correctamente.");
        });
    }
    
    private static void quicksortRecursivo(int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionRecursiva(inicio, fin, inicio, inicio);
            quicksortRecursivo(inicio, indicePivote - 1);
            quicksortRecursivo(indicePivote + 1, fin);
        }
    }
    
    private static int particionRecursiva(int inicio, int fin, int i, int j) {
        if (j >= fin) {
            intercambiar(i + 1, fin);
            return i + 1;
        }
        
        int pivote = datos[fin];
        
        if (datos[j] <= pivote) {
            intercambiar(i + 1, j);
            return particionRecursiva(inicio, fin, i + 1, j + 1);
        }
        
        return particionRecursiva(inicio, fin, i, j + 1);
    }
    
    private static void intercambiar(int i, int j) {
        int temp = datos[i];
        datos[i] = datos[j];
        datos[j] = temp;
    }
    
    private static void buscarDato() {
        manejarDatosVacios(() -> {
            System.out.print("Ingrese el número a buscar: ");
            int objetivo = obtenerEnteroRecursivo("Ingrese el número a buscar: ", scanner.nextLine().trim(), 0);
            
            manejarOrdenamientoBusqueda(objetivo);
        });
    }
    
    private static void manejarOrdenamientoBusqueda(int objetivo) {
        if (!estaOrdenado(0)) {
            System.out.println("Los datos no están ordenados. Ordenando primero...");
            quicksortRecursivo(0, contador - 1);
        }
        
        int resultado = busquedaBinariaRecursiva(objetivo, 0, contador - 1);
        manejarResultadoBusqueda(resultado);
    }
    
    private static void manejarResultadoBusqueda(int resultado) {
        if (resultado != -1) {
            System.out.println("Número encontrado en la posición: " + resultado);
            return;
        }
        
        System.out.println("Número no encontrado.");
    }
    
    private static boolean estaOrdenado(int index) {
        if (index >= contador - 1) {
            return true;
        }
        
        if (datos[index] > datos[index + 1]) {
            return false;
        }
        
        return estaOrdenado(index + 1);
    }
    
    private static int busquedaBinariaRecursiva(int objetivo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }
        
        int medio = inicio + (fin - inicio) / 2;
        
        return manejarComparacionBusqueda(objetivo, inicio, fin, medio);
    }
    
    private static int manejarComparacionBusqueda(int objetivo, int inicio, int fin, int medio) {
        if (datos[medio] == objetivo) {
            return medio;
        }
        
        return manejarComparacionMayorMenor(objetivo, inicio, fin, medio);
    }
    
    private static int manejarComparacionMayorMenor(int objetivo, int inicio, int fin, int medio) {
        if (datos[medio] > objetivo) {
            return busquedaBinariaRecursiva(objetivo, inicio, medio - 1);
        }
        
        return busquedaBinariaRecursiva(objetivo, medio + 1, fin);
    }
    
    private static void mostrarDatos() {
        manejarDatosVacios(() -> {
            System.out.println("Datos almacenados:");
            mostrarRecursivo(0);
        });
    }
    
    private static void mostrarRecursivo(int index) {
        if (index >= contador) {
            return;
        }
        
        System.out.print(datos[index] + " ");
        manejarSaltoLinea(index);
        mostrarRecursivo(index + 1);
    }
    
    private static void manejarSaltoLinea(int index) {
        if ((index + 1) % 10 == 0) {
            System.out.println();
        }
    }
    
    private static void manejarDatosVacios(Runnable action) {
        if (contador == 0) {
            System.out.println("No hay datos para operar.");
            return;
        }
        action.run();
    }
    
    private static int obtenerEnteroRecursivo(String mensaje, String input, int intentos) {
        if (intentos > 10) return 0;
        
        boolean esNumero = verificarEsNumero(input, 0, true);
        
        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }
        
        return convertirStringAInt(input, 0, 0);
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
}