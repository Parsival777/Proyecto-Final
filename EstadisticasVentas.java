import java.util.Scanner;

public class EstadisticasVentas {
    private static int[] ventasPorHora = new int[100];
    private static int contadorVentas = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void menuEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DE VENTAS (RECURSIVIDAD) ===");
        System.out.println("1. Registrar venta por hora");
        System.out.println("2. Calcular venta total del día");
        System.out.println("3. Calcular promedio de ventas por hora");
        System.out.println("4. Encontrar hora pico de ventas");
        System.out.println("5. Encontrar hora baja de ventas");
        System.out.println("6. Distribuir personal por horas");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionEstadisticas(scanner.nextLine().trim());
    }

    private static void procesarOpcionEstadisticas(String input) {
        procesarOpcionRecursiva(input);
    }

    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));

        procesarOpcion(input, "1", EstadisticasVentas::agregarVentaHora);
        procesarOpcion(input, "2", EstadisticasVentas::calcularVentaTotal);
        procesarOpcion(input, "3", EstadisticasVentas::calcularVentaPromedio);
        procesarOpcion(input, "4", EstadisticasVentas::encontrarHoraPico);
        procesarOpcion(input, "5", EstadisticasVentas::encontrarHoraBaja);
        procesarOpcion(input, "6", EstadisticasVentas::distribuirPersonal);

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
            menuEstadisticas();
        }
    }

    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4", "5", "6"}, 0);

        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 6.");
            menuEstadisticas();
        }
    }

    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }

    private static void agregarVentaHora() {
        manejarCapacidadVentas();
    }

    private static void manejarCapacidadVentas() {
        if (contadorVentas >= ventasPorHora.length) {
            System.out.println("No se pueden registrar más ventas.");
            return;
        }

        System.out.print("Ventas en la hora (cantidad de pedidos): ");
        int ventas = obtenerEnteroPositivoRecursivo("Ventas en la hora (cantidad de pedidos): ", scanner.nextLine().trim(), 0);

        ventasPorHora[contadorVentas++] = ventas;
        System.out.println("Ventas registradas: " + ventas + " pedidos");
    }

    private static void calcularVentaTotal() {
        manejarVentasVacias(() -> {
            int total = sumaRecursiva(ventasPorHora, contadorVentas - 1);
            System.out.println("Venta total del día: " + total + " pedidos");
        });
    }

    private static int sumaRecursiva(int[] array, int index) {
        if (index < 0) {
            return 0;
        }
        return array[index] + sumaRecursiva(array, index - 1);
    }

    private static void calcularVentaPromedio() {
        manejarVentasVacias(() -> {
            int total = sumaRecursiva(ventasPorHora, contadorVentas - 1);
            double promedio = (double) total / contadorVentas;
            System.out.println("Promedio de ventas por hora: " + String.format("%.2f", promedio) + " pedidos");
        });
    }

    private static void encontrarHoraPico() {
        manejarVentasVacias(() -> {
            int maximo = maximoRecursivo(ventasPorHora, contadorVentas - 1, Integer.MIN_VALUE);
            System.out.println("Hora pico de ventas: " + maximo + " pedidos");
        });
    }

    private static int maximoRecursivo(int[] array, int index, int max) {
        if (index < 0) {
            return max;
        }

        return maximoRecursivo(array, index - 1, array[index] > max ? array[index] : max);
    }

    private static void encontrarHoraBaja() {
        manejarVentasVacias(() -> {
            int minimo = minimoRecursivo(ventasPorHora, contadorVentas - 1, Integer.MAX_VALUE);
            System.out.println("Hora baja de ventas: " + minimo + " pedidos");
        });
    }

    private static int minimoRecursivo(int[] array, int index, int min) {
        if (index < 0) {
            return min;
        }

        return minimoRecursivo(array, index - 1, array[index] < min ? array[index] : min);
    }

    private static void distribuirPersonal() {
        manejarVentasVacias(() -> {
            System.out.print("Ingrese el número de turnos: ");
            int numTurnos = obtenerEnteroPositivoRecursivo("Ingrese el número de turnos: ", scanner.nextLine().trim(), 0);

            int[] turnos = new int[numTurnos];
            distribuirRecursivo(ventasPorHora, contadorVentas - 1, turnos, 0);

            System.out.println("Distribución de personal por turnos:");
            mostrarDistribucionRecursiva(turnos, 0);
        });
    }

    private static void distribuirRecursivo(int[] ventas, int ventaIndex, int[] turnos, int turnoIndex) {
        if (ventaIndex < 0) {
            return;
        }

        int turnoMinimo = encontrarTurnoMinimo(turnos, turnos.length - 1, 0);
        turnos[turnoMinimo] += ventas[ventaIndex];

        distribuirRecursivo(ventas, ventaIndex - 1, turnos, turnoIndex);
    }

    private static int encontrarTurnoMinimo(int[] turnos, int index, int minIndex) {
        if (index < 0) {
            return minIndex;
        }

        int nuevoMinIndex = turnos[index] < turnos[minIndex] ? index : minIndex;
        return encontrarTurnoMinimo(turnos, index - 1, nuevoMinIndex);
    }

    private static void mostrarDistribucionRecursiva(int[] turnos, int index) {
        if (index >= turnos.length) {
            return;
        }

        System.out.println("Turno " + (index + 1) + ": " + turnos[index] + " pedidos asignados");
        mostrarDistribucionRecursiva(turnos, index + 1);
    }

    private static void manejarVentasVacias(Runnable action) {
        if (contadorVentas == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        action.run();
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

    public static void registrarVentasMesa(int numeroMesa, int totalVentas) {
        if (contadorVentas >= ventasPorHora.length) {
            System.out.println("No se pueden registrar más ventas. Límite alcanzado.");
            return;
        }
        ventasPorHora[contadorVentas++] = totalVentas;
        System.out.println("Ventas de la mesa " + numeroMesa + " registradas: " + totalVentas + " pedidos");
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
