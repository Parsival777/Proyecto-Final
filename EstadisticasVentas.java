import java.util.Scanner;

public class EstadisticasVentas {
    private static int[] ventasPorHora = new int[24];
    private static double[] montosPorHora = new double[24];
    private static int totalVentasDia = 0;
    private static double totalMontoDia = 0.0;
    private static Scanner scanner = new Scanner(System.in);

    // Métodos de validación
    private static int obtenerEnteroValido(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número entero válido.");
            }
        }
    }

    private static int obtenerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = obtenerEnteroValido(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            } else {
                System.out.printf("Error: El valor debe estar entre %d y %d.\n", min, max);
            }
        }
    }

    private static double obtenerDoubleValido(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
    }

    public static void menuEstadisticas() {
        int opcion;
        do {
            System.out.println("\n=== ESTADÍSTICAS Y ANÁLISIS DE VENTAS ===");
            System.out.println("1. Mostrar resumen de ventas del día");
            System.out.println("2. Calcular venta total del día (recursivo)");
            System.out.println("3. Calcular promedio de ventas por hora");
            System.out.println("4. Encontrar hora pico de ventas");
            System.out.println("5. Ordenar datos de ventas (Quicksort recursivo)");
            System.out.println("6. Buscar dato de venta (Búsqueda binaria)");
            System.out.println("7. Análisis de tendencias (Divide y Vencerás)");
            System.out.println("0. Volver al menú principal");
            
            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 7);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                case 1:
                    mostrarResumenVentas();
                    break;
                case 2:
                    calcularVentaTotalRecursivo();
                    break;
                case 3:
                    calcularPromedioVentas();
                    break;
                case 4:
                    encontrarHoraPico();
                    break;
                case 5:
                    ordenarDatosVentas();
                    break;
                case 6:
                    buscarDatoVenta();
                    break;
                case 7:
                    analizarTendenciasDivideVenceras();
                    break;
            }
        } while (opcion != 0);
    }

    // Registrar venta desde tickets (se llama automáticamente)
    public static void registrarVenta(int hora, int cantidadPedidos, double montoTotal) {
        if (hora >= 0 && hora < 24) {
            ventasPorHora[hora] += cantidadPedidos;
            montosPorHora[hora] += montoTotal;
            totalVentasDia += cantidadPedidos;
            totalMontoDia += montoTotal;
            System.out.println("✓ Venta registrada: Hora " + hora + ":00 - " + cantidadPedidos + " pedidos - $" + montoTotal);
        }
    }

    private static void mostrarResumenVentas() {
        System.out.println("\n=== RESUMEN DE VENTAS DEL DÍA ===");
        System.out.println("Total de pedidos: " + totalVentasDia);
        System.out.println("Monto total: $" + String.format("%.2f", totalMontoDia));

        System.out.println("\nVentas por hora:");
        boolean hayVentas = false;
        for (int i = 0; i < 24; i++) {
            if (ventasPorHora[i] > 0) {
                System.out.printf("Hora %02d:00 - %d pedidos - $%.2f MXN\n",
                        i, ventasPorHora[i], montosPorHora[i]);
                hayVentas = true;
            }
        }

        if (!hayVentas) {
            System.out.println("No hay ventas registradas hoy.");
        }
    }

    // Recursividad: Calcular venta total
    private static void calcularVentaTotalRecursivo() {
        if (totalVentasDia == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        double total = calcularTotalRecursivo(0, 0.0);
        System.out.println("Venta total calculada recursivamente: $" + String.format("%.2f", total));
    }

    private static double calcularTotalRecursivo(int index, double acumulado) {
        if (index >= 24) {
            return acumulado;
        }
        return calcularTotalRecursivo(index + 1, acumulado + montosPorHora[index]);
    }

    private static void calcularPromedioVentas() {
        int horasConVentas = contarHorasConVentas(0, 0);
        if (horasConVentas == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        double promedio = totalMontoDia / horasConVentas;
        System.out.printf("Promedio de ventas por hora: $%.2f MXN (%d horas con ventas)\n", promedio, horasConVentas);
    }

    private static int contarHorasConVentas(int index, int contador) {
        if (index >= 24) {
            return contador;
        }
        int nuevoContador = (montosPorHora[index] > 0) ? contador + 1 : contador;
        return contarHorasConVentas(index + 1, nuevoContador);
    }

    private static void encontrarHoraPico() {
        if (totalVentasDia == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        int horaPico = encontrarMaximoRecursivo(0, 0, 0);
        System.out.printf("Hora pico: %02d:00 - %d pedidos - $%.2f MXN\n",
                horaPico, ventasPorHora[horaPico], montosPorHora[horaPico]);
    }

    private static int encontrarMaximoRecursivo(int index, int maxIndex, double maxValor) {
        if (index >= 24) {
            return maxIndex;
        }

        if (montosPorHora[index] > maxValor) {
            return encontrarMaximoRecursivo(index + 1, index, montosPorHora[index]);
        }
        return encontrarMaximoRecursivo(index + 1, maxIndex, maxValor);
    }

    // Divide y Vencerás: Quicksort para ordenar ventas
    private static void ordenarDatosVentas() {
        if (totalVentasDia == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        double[] montosOrdenados = montosPorHora.clone();
        quicksortRecursivo(montosOrdenados, 0, 23);

        System.out.println("Ventas ordenadas de menor a mayor:");
        mostrarArrayRecursivo(montosOrdenados, 0);
    }

    private static void quicksortRecursivo(double[] array, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionarRecursivo(array, inicio, fin);
            quicksortRecursivo(array, inicio, indicePivote - 1);
            quicksortRecursivo(array, indicePivote + 1, fin);
        }
    }

    private static int particionarRecursivo(double[] array, int inicio, int fin) {
        double pivote = array[fin];
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (array[j] <= pivote) {
                i++;
                intercambiar(array, i, j);
            }
        }
        intercambiar(array, i + 1, fin);
        return i + 1;
    }

    private static void intercambiar(double[] array, int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Búsqueda binaria recursiva
    private static void buscarDatoVenta() {
        if (totalVentasDia == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        double objetivo = obtenerDoubleValido("Ingrese el monto a buscar: ");

        double[] copiaOrdenada = montosPorHora.clone();
        quicksortRecursivo(copiaOrdenada, 0, 23);

        int resultado = busquedaBinariaRecursiva(copiaOrdenada, objetivo, 0, 23);
        if (resultado != -1) {
            System.out.println("Monto encontrado en la hora: " + resultado + ":00");
        } else {
            System.out.println("Monto no encontrado.");
        }
    }

    private static int busquedaBinariaRecursiva(double[] array, double objetivo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }

        int medio = inicio + (fin - inicio) / 2;

        if (Math.abs(array[medio] - objetivo) < 0.01) {
            return medio;
        }

        if (array[medio] > objetivo) {
            return busquedaBinariaRecursiva(array, objetivo, inicio, medio - 1);
        }

        return busquedaBinariaRecursiva(array, objetivo, medio + 1, fin);
    }

    // Divide y Vencerás: Análisis de tendencias
    private static void analizarTendenciasDivideVenceras() {
        if (totalVentasDia == 0) {
            System.out.println("No hay datos para analizar.");
            return;
        }

        System.out.println("=== ANÁLISIS DE TENDENCIAS (DIVIDE Y VENCERÁS) ===");
        analizarSegmentoRecursivo(0, 23, 1);
    }

    private static void analizarSegmentoRecursivo(int inicio, int fin, int nivel) {
        if (inicio >= fin) {
            return;
        }

        int medio = (inicio + fin) / 2;

        // Analizar primera mitad
        double sumaPrimera = sumarSegmento(inicio, medio, 0.0);
        // Analizar segunda mitad
        double sumaSegunda = sumarSegmento(medio + 1, fin, 0.0);

        System.out.printf("Nivel %d: Horas %02d-%02d: $%.2f | Horas %02d-%02d: $%.2f\n",
                nivel, inicio, medio, sumaPrimera, medio + 1, fin, sumaSegunda);

        // Dividir y conquistar recursivamente
        analizarSegmentoRecursivo(inicio, medio, nivel + 1);
        analizarSegmentoRecursivo(medio + 1, fin, nivel + 1);
    }

    private static double sumarSegmento(int inicio, int fin, double acumulado) {
        if (inicio > fin) {
            return acumulado;
        }
        return sumarSegmento(inicio + 1, fin, acumulado + montosPorHora[inicio]);
    }

    private static void mostrarArrayRecursivo(double[] array, int index) {
        if (index >= array.length) {
            System.out.println();
            return;
        }

        if (array[index] > 0) {
            System.out.printf("Hora %02d: $%.2f\n", index, array[index]);
        }

        mostrarArrayRecursivo(array, index + 1);
    }

    // Método para reiniciar estadísticas diarias
    public static void reiniciarEstadisticas() {
        for (int i = 0; i < 24; i++) {
            ventasPorHora[i] = 0;
            montosPorHora[i] = 0.0;
        }
        totalVentasDia = 0;
        totalMontoDia = 0.0;
        System.out.println("Estadísticas reiniciadas para nuevo día.");
    }
}
