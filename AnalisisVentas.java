
import java.util.Scanner;

public class AnalisisVentas {
    private static int[] datosVentas = new int[100];
    private static int contador = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void menuAnalisisVentas() {
        System.out.println("\n=== ANÁLISIS DE DATOS DE VENTAS (DIVIDE Y VENCERÁS) ===");
        System.out.println("1. Agregar dato de venta diaria");
        System.out.println("2. Ordenar datos de ventas (Quicksort recursivo)");
        System.out.println("3. Buscar dato de venta (Búsqueda binaria recursiva)");
        System.out.println("4. Mostrar datos de ventas");
        System.out.println("5. Distribuir tareas por departamento (Divide y Vencerás)");
        System.out.println("6. Calcular eficiencia por turnos (MergeSort recursivo)");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionAnalisis(scanner.nextLine().trim());
    }

    private static void procesarOpcionAnalisis(String input) {
        procesarOpcionRecursiva(input);
    }

    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));

        procesarOpcion(input, "1", AnalisisVentas::agregarDatoVenta);
        procesarOpcion(input, "2", AnalisisVentas::ordenarDatosVentas);
        procesarOpcion(input, "3", AnalisisVentas::buscarDatoVenta);
        procesarOpcion(input, "4", AnalisisVentas::mostrarDatosVentas);
        procesarOpcion(input, "5", AnalisisVentas::distribuirTareasDivideVenceras); // NUEVO
        procesarOpcion(input, "6", AnalisisVentas::ordenarMergeSort); // NUEVO

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
            menuAnalisisVentas();
        }
    }

    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4", "5", "6"}, 0);

        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 6.");
            menuAnalisisVentas();
        }
    }

    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }

    // ========== MÉTODOS EXISTENTES (se mantienen igual) ==========
    private static void agregarDatoVenta() {
        manejarCapacidadDatos();
    }

    private static void manejarCapacidadDatos() {
        if (contador >= datosVentas.length) {
            System.out.println("No se pueden agregar más datos de ventas.");
            return;
        }

        System.out.print("Ingrese la cantidad de ventas del día: ");
        int numero = obtenerEnteroRecursivo("Ingrese la cantidad de ventas del día: ", scanner.nextLine().trim(), 0);

        datosVentas[contador++] = numero;
        System.out.println("Dato de venta agregado: " + numero);
    }

    private static void ordenarDatosVentas() {
        manejarDatosVacios(() -> {
            quicksortRecursivo(0, contador - 1);
            System.out.println("Datos de ventas ordenados correctamente.");
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

        int pivote = datosVentas[fin];

        if (datosVentas[j] <= pivote) {
            intercambiar(i + 1, j);
            return particionRecursiva(inicio, fin, i + 1, j + 1);
        }

        return particionRecursiva(inicio, fin, i, j + 1);
    }

    private static void intercambiar(int i, int j) {
        int temp = datosVentas[i];
        datosVentas[i] = datosVentas[j];
        datosVentas[j] = temp;
    }

    private static void buscarDatoVenta() {
        manejarDatosVacios(() -> {
            System.out.print("Ingrese la cantidad de ventas a buscar: ");
            int objetivo = obtenerEnteroRecursivo("Ingrese la cantidad de ventas a buscar: ", scanner.nextLine().trim(), 0);

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
            System.out.println("Dato de venta encontrado en la posición: " + resultado);
            return;
        }

        System.out.println("Dato de venta no encontrado.");
    }

    private static boolean estaOrdenado(int index) {
        if (index >= contador - 1) {
            return true;
        }

        if (datosVentas[index] > datosVentas[index + 1]) {
            return false;
        }

        return estaOrdenado(index + 1);
    }

    private static int busquedaBinariaRecursiva(int objetivo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }

        int medio = inicio + (fin - inicio) / 2;

        if (datosVentas[medio] == objetivo) {
            return medio;
        }

        if (datosVentas[medio] > objetivo) {
            return busquedaBinariaRecursiva(objetivo, inicio, medio - 1);
        }

        return busquedaBinariaRecursiva(objetivo, medio + 1, fin);
    }

    private static void mostrarDatosVentas() {
        manejarDatosVacios(() -> {
            System.out.println("Datos de ventas almacenados:");
            mostrarRecursivo(0);
        });
    }

    private static void mostrarRecursivo(int index) {
        if (index >= contador) {
            return;
        }

        System.out.print(datosVentas[index] + " ");
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
            System.out.println("No hay datos de ventas para operar.");
            return;
        }
        action.run();
    }

    // ========== NUEVOS MÉTODOS DIVIDE Y VENCERÁS ==========

    // 5. Distribuir tareas por departamento (Divide y Vencerás)
    private static void distribuirTareasDivideVenceras() {
        System.out.print("Ingrese la cantidad total de tareas a distribuir: ");
        int totalTareas = obtenerEnteroPositivoRecursivo("Ingrese la cantidad total de tareas: ", scanner.nextLine().trim(), 0);
        
        System.out.print("Ingrese la cantidad de departamentos: ");
        int numDepartamentos = obtenerEnteroPositivoRecursivo("Ingrese la cantidad de departamentos: ", scanner.nextLine().trim(), 0);

        int[] tareasPorDepto = new int[numDepartamentos];
        distribuirTareasRecursivo(totalTareas, numDepartamentos, tareasPorDepto, 0);

        System.out.println("Distribución de tareas por departamento:");
        mostrarDistribucionRecursiva(tareasPorDepto, 0);
    }

    private static void distribuirTareasRecursivo(int tareasRestantes, int deptosRestantes, int[] distribucion, int index) {
        if (deptosRestantes == 0 || tareasRestantes == 0) {
            return;
        }

        // Divide el problema: asigna una parte proporcional a cada departamento
        int tareasParaEsteDepto = (int) Math.ceil((double) tareasRestantes / deptosRestantes);
        distribucion[index] = tareasParaEsteDepto;

        // Vencer: resolver para los departamentos restantes
        distribuirTareasRecursivo(tareasRestantes - tareasParaEsteDepto, deptosRestantes - 1, distribucion, index + 1);
    }

    // 6. MergeSort recursivo para ordenar eficiencia por turnos
    private static void ordenarMergeSort() {
        manejarDatosVacios(() -> {
            int[] copiaDatos = new int[contador];
            System.arraycopy(datosVentas, 0, copiaDatos, 0, contador);
            
            mergeSortRecursivo(copiaDatos, 0, contador - 1);
            
            System.out.println("Datos ordenados con MergeSort:");
            mostrarArrayRecursivo(copiaDatos, 0);
        });
    }

    private static void mergeSortRecursivo(int[] array, int inicio, int fin) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            
            // Divide: ordenar las dos mitades
            mergeSortRecursivo(array, inicio, medio);
            mergeSortRecursivo(array, medio + 1, fin);
            
            // Vencer: combinar las mitades ordenadas
            mergeRecursivo(array, inicio, medio, fin);
        }
    }

    private static void mergeRecursivo(int[] array, int inicio, int medio, int fin) {
        int[] temp = new int[fin - inicio + 1];
        mergeRecursivoAux(array, temp, inicio, medio, medio + 1, fin, 0);
        
        // Copiar el array temporal al original
        copiarArrayRecursivo(temp, array, inicio, 0);
    }

    private static void mergeRecursivoAux(int[] array, int[] temp, int leftStart, int leftEnd, int rightStart, int rightEnd, int tempIndex) {
        if (leftStart > leftEnd && rightStart > rightEnd) {
            return;
        }

        if (leftStart > leftEnd) {
            temp[tempIndex] = array[rightStart];
            mergeRecursivoAux(array, temp, leftStart, leftEnd, rightStart + 1, rightEnd, tempIndex + 1);
        } else if (rightStart > rightEnd) {
            temp[tempIndex] = array[leftStart];
            mergeRecursivoAux(array, temp, leftStart + 1, leftEnd, rightStart, rightEnd, tempIndex + 1);
        } else if (array[leftStart] <= array[rightStart]) {
            temp[tempIndex] = array[leftStart];
            mergeRecursivoAux(array, temp, leftStart + 1, leftEnd, rightStart, rightEnd, tempIndex + 1);
        } else {
            temp[tempIndex] = array[rightStart];
            mergeRecursivoAux(array, temp, leftStart, leftEnd, rightStart + 1, rightEnd, tempIndex + 1);
        }
    }

    private static void copiarArrayRecursivo(int[] origen, int[] destino, int destStart, int index) {
        if (index >= origen.length) {
            return;
        }
        destino[destStart + index] = origen[index];
        copiarArrayRecursivo(origen, destino, destStart, index + 1);
    }

    private static void mostrarArrayRecursivo(int[] array, int index) {
        if (index >= array.length) {
            System.out.println();
            return;
        }

        System.out.print(array[index] + " ");
        if ((index + 1) % 10 == 0) {
            System.out.println();
        }
        mostrarArrayRecursivo(array, index + 1);
    }

    private static void mostrarDistribucionRecursiva(int[] distribucion, int index) {
        if (index >= distribucion.length) {
            return;
        }

        System.out.println("Departamento " + (index + 1) + ": " + distribucion[index] + " tareas");
        mostrarDistribucionRecursiva(distribucion, index + 1);
    }

    // ========== MÉTODOS AUXILIARES EXISTENTES ==========
    public static void registrarVentaDiaria(int venta) {
        if (contador >= datosVentas.length) {
            System.out.println("No se pueden agregar más datos de ventas.");
            return;
        }
        datosVentas[contador++] = venta;
        System.out.println("Venta diaria registrada: $" + venta);
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
}
