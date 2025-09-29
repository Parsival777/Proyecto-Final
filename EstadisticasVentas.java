import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EstadisticasVentas {
    private static Scanner scanner = new Scanner(System.in);

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
            System.out.println("\n=== ANÁLISIS DE VENTAS ===");
            System.out.println("1. Mostrar resumen de ventas del día");
            System.out.println("2. Calcular venta total del día");
            System.out.println("3. Ordenar datos de ventas (Quicksort recursivo)");
            System.out.println("4. Buscar dato de venta (Búsqueda binaria)");
            System.out.println("5. Análisis de tendencias (Divide y Vencerás)");
            System.out.println("0. Volver al menú principal");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 5);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                case 1:
                    mostrarResumenVentas();
                    break;
                case 2:
                    calcularVentaTotalDia();
                    break;
                case 3:
                    ordenarDatosVentas();
                    break;
                case 4:
                    buscarDatoVenta();
                    break;
                case 5:
                    analizarTendencias();
                    break;
            }
        } while (opcion != 0);
    }

    private static List<File> obtenerArchivosTickets() {
        List<File> archivos = new ArrayList<>();
        File directorio = new File(".");
        File[] archivosDir = directorio.listFiles();

        if (archivosDir != null) {
            for (File archivo : archivosDir) {

                if (archivo.isFile() && archivo.getName().startsWith("Tickets_") && archivo.getName().endsWith(".txt")) {
                    archivos.add(archivo);
                    System.out.println("✓ Archivo encontrado: " + archivo.getName());
                }
            }
        }

        if (archivos.isEmpty()) {
            System.out.println("No se encontraron archivos de tickets.");
        } else {
            System.out.println("Total de archivos encontrados: " + archivos.size());
        }

        return archivos;
    }

    private static List<TicketData> leerTicketsDelDia() {
        List<TicketData> tickets = new ArrayList<>();
        List<File> archivos = obtenerArchivosTickets();

        if (archivos.isEmpty()) {
            return tickets;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        String fechaHoy = LocalDate.now().format(formatter).toUpperCase();
        String nombreArchivoHoy = "Tickets_" + fechaHoy + ".txt";

        File archivoHoy = null;
        for (File archivo : archivos) {
            if (archivo.getName().equals(nombreArchivoHoy)) {
                archivoHoy = archivo;
                break;
            }
        }

        if (archivoHoy == null) {
            System.out.println("No se encontró el archivo de tickets de hoy: " + nombreArchivoHoy);

            archivoHoy = archivos.get(archivos.size() - 1);
            System.out.println("Usando archivo más reciente: " + archivoHoy.getName());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoHoy))) {
            String linea;
            int numeroMesa = -1;
            double total = 0.0;
            int cantidadPedidos = 0;
            String fecha = "";
            boolean leyendoProductos = false;
            List<String> lineasProductos = new ArrayList<>();

            while ((linea = br.readLine()) != null) {
                if (linea.contains("TICKET MESA")) {

                    if (numeroMesa != -1) {
                        tickets.add(new TicketData(numeroMesa, fecha, cantidadPedidos, total));
                        cantidadPedidos = 0;
                        total = 0.0;
                    }

                    String[] partes = linea.split("MESA");
                    if (partes.length > 1) {
                        try {
                            String numeroStr = partes[1].trim().split(" ")[0];
                            numeroMesa = Integer.parseInt(numeroStr.replaceAll("[^0-9]", ""));
                        } catch (NumberFormatException e) {
                            numeroMesa = -1;
                        }
                    }
                } else if (linea.contains("Fecha:")) {
                    fecha = linea.substring(linea.indexOf("Fecha:") + 6).trim();
                } else if (linea.contains("Cant. Producto")) {
                    leyendoProductos = true;
                    lineasProductos.clear();
                } else if (linea.contains("TOTAL:")) {
                    leyendoProductos = false;

                    String totalStr = linea.replaceAll("[^0-9.]", "").trim();
                    if (!totalStr.isEmpty()) {
                        try {
                            total = Double.parseDouble(totalStr);
                        } catch (NumberFormatException e) {
                            total = 0.0;
                        }
                    }


                    for (String lineaProducto : lineasProductos) {
                        if (lineaProducto.contains("$") && !lineaProducto.contains("Comentarios:") && !lineaProducto.contains("Estado:")) {

                            if (!lineaProducto.contains("CANCELADO")) {

                                try {
                                    String cantidadStr = lineaProducto.split("║")[1].trim().split("\\s+")[0];
                                    int cantidad = Integer.parseInt(cantidadStr);
                                    cantidadPedidos += cantidad;
                                } catch (Exception e) {

                                    cantidadPedidos++;
                                }
                            }
                        }
                    }


                    if (numeroMesa != -1 && total > 0) {
                        tickets.add(new TicketData(numeroMesa, fecha, cantidadPedidos, total));
                    }


                    numeroMesa = -1;
                    cantidadPedidos = 0;
                    total = 0.0;
                } else if (leyendoProductos && linea.contains("║") && !linea.contains("════") &&
                        !linea.contains("Estado:") && !linea.contains("Comentarios:")) {
                    lineasProductos.add(linea);
                }
            }


            if (numeroMesa != -1 && total > 0) {
                tickets.add(new TicketData(numeroMesa, fecha, cantidadPedidos, total));
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + archivoHoy.getName() + " - " + e.getMessage());
        }

        System.out.println("Tickets procesados: " + tickets.size());
        return tickets;
    }

    private static void mostrarResumenVentas() {
        List<TicketData> tickets = leerTicketsDelDia();

        if (tickets.isEmpty()) {
            System.out.println("No hay tickets generados hoy.");
            return;
        }

        System.out.println("\n=== RESUMEN DE VENTAS DEL DÍA ===");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      VENTAS DEL DÍA                          ║");
        System.out.println("╠══════════╦══════════════╦════════════════╦═══════════════════╣");
        System.out.println("║   MESA   ║   PEDIDOS    ║     TOTAL      ║      FECHA        ║");
        System.out.println("╠══════════╬══════════════╬════════════════╬═══════════════════╣");

        int totalTickets = tickets.size();
        int totalPedidos = 0;
        double totalVentas = 0.0;

        for (TicketData ticket : tickets) {
            System.out.printf("║ %8d ║ %12d ║ $%12.2f  ║ %17s ║\n",
                    ticket.numeroMesa, ticket.cantidadPedidos, ticket.total,
                    ticket.fecha.length() > 16 ? ticket.fecha.substring(0, 16) : ticket.fecha);
            totalPedidos += ticket.cantidadPedidos;
            totalVentas += ticket.total;
        }

        System.out.println("╠══════════╬══════════════╬════════════════╬═══════════════════╣");
        System.out.printf("║ %8s ║ %12d ║ $%11.2f   ║ %16s  ║\n",
                "TOTAL", totalPedidos, totalVentas, totalTickets + " tickets");
        System.out.println("╚══════════╩══════════════╩════════════════╩═══════════════════╝");
    }

    private static void calcularVentaTotalDia() {
        List<TicketData> tickets = leerTicketsDelDia();

        if (tickets.isEmpty()) {
            System.out.println("No hay tickets generados hoy.");
            return;
        }

        double total = calcularTotalRecursivo(tickets, 0, 0.0);
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    VENTA TOTAL DEL DÍA                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf("║                    Total: $%5.2f                           ║\n", total);
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static double calcularTotalRecursivo(List<TicketData> tickets, int index, double acumulado) {
        if (index >= tickets.size()) {
            return acumulado;
        }
        return calcularTotalRecursivo(tickets, index + 1, acumulado + tickets.get(index).total);
    }

    private static void ordenarDatosVentas() {
        List<TicketData> tickets = leerTicketsDelDia();

        if (tickets.isEmpty()) {
            System.out.println("No hay tickets para ordenar.");
            return;
        }

        TicketData[] ticketsArray = tickets.toArray(new TicketData[0]);
        quicksortRecursivo(ticketsArray, 0, ticketsArray.length - 1);

        System.out.println("\n=== VENTAS ORDENADAS (MENOR A MAYOR) ===");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   VENTAS ORDENADAS                           ║");
        System.out.println("╠══════╦══════════╦══════════════╦════════════════╦════════════╣");
        System.out.println("║ POS  ║   MESA   ║   PEDIDOS    ║     TOTAL      ║   FECHA    ║");
        System.out.println("╠══════╬══════════╬══════════════╬════════════════╬════════════╣");

        mostrarTicketsRecursivo(ticketsArray, 0);

        System.out.println("╚══════╩══════════╩══════════════╩════════════════╩════════════╝");
    }

    private static void quicksortRecursivo(TicketData[] array, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionarRecursivo(array, inicio, fin);
            quicksortRecursivo(array, inicio, indicePivote - 1);
            quicksortRecursivo(array, indicePivote + 1, fin);
        }
    }

    private static int particionarRecursivo(TicketData[] array, int inicio, int fin) {
        double pivote = array[fin].total;
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (array[j].total <= pivote) {
                i++;
                intercambiar(array, i, j);
            }
        }
        intercambiar(array, i + 1, fin);
        return i + 1;
    }

    private static void intercambiar(TicketData[] array, int i, int j) {
        TicketData temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void mostrarTicketsRecursivo(TicketData[] tickets, int index) {
        if (index >= tickets.length) {
            return;
        }
        System.out.printf("║ %4d ║ %8d ║ %12d ║ $%11.2f   ║ %10s ║\n",
                index + 1, tickets[index].numeroMesa, tickets[index].cantidadPedidos,
                tickets[index].total, tickets[index].fecha.length() > 10 ?
                        tickets[index].fecha.substring(0, 10) : tickets[index].fecha);
        mostrarTicketsRecursivo(tickets, index + 1);
    }

    private static void buscarDatoVenta() {
    List<TicketData> tickets = leerTicketsDelDia();

    if (tickets.isEmpty()) {
        System.out.println("No hay tickets para buscar.");
        return;
    }

    double objetivo = obtenerDoubleValido("Ingrese el monto a buscar: ");

    TicketData[] ticketsArray = tickets.toArray(new TicketData[0]);
    quicksortRecursivo(ticketsArray, 0, ticketsArray.length - 1);

    int resultado = busquedaBinariaRecursiva(ticketsArray, objetivo, 0, ticketsArray.length - 1);

    System.out.println("╔══════════════════════════════════════════════════════════════╗");
    System.out.println("║                    BÚSQUEDA DE VENTA                         ║");
    System.out.println("╠══════════════════════════════════════════════════════════════╣");
    
    if (resultado != -1) {
        System.out.printf("║ Monto $%.2f encontrado en la mesa %d                        ║\n",
                objetivo, ticketsArray[resultado].numeroMesa);
        System.out.printf("║ Detalles: %d pedidos, fecha: %-20s             ║\n",
                ticketsArray[resultado].cantidadPedidos, 
                ticketsArray[resultado].fecha.length() > 15 ? 
                ticketsArray[resultado].fecha.substring(0, 15) : ticketsArray[resultado].fecha);
    } else {
        System.out.println("║ Monto no encontrado en ningún ticket.                        ║");
    }
    System.out.println("╚══════════════════════════════════════════════════════════════╝");
}

    private static int busquedaBinariaRecursiva(TicketData[] array, double objetivo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }

        int medio = inicio + (fin - inicio) / 2;

        if (Math.abs(array[medio].total - objetivo) < 0.01) {
            return medio;
        }

        if (array[medio].total > objetivo) {
            return busquedaBinariaRecursiva(array, objetivo, inicio, medio - 1);
        }

        return busquedaBinariaRecursiva(array, objetivo, medio + 1, fin);
    }

    private static void analizarTendencias() {
        Map<String, Integer> productosMasPedidos = obtenerProductosMasPedidos();

        if (productosMasPedidos.isEmpty()) {
            System.out.println("No hay datos para analizar tendencias.");
            return;
        }

        System.out.println("\n=== ANÁLISIS DE TENDENCIAS (PRODUCTOS MÁS PEDIDOS) ===");
        List<Map.Entry<String, Integer>> listaProductos = new ArrayList<>(productosMasPedidos.entrySet());


        quicksortProductosRecursivo(listaProductos, 0, listaProductos.size() - 1);


        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 PRODUCTOS MÁS PEDIDOS                        ║");
        System.out.println("╠══════╦══════════════════════════════════════╦════════════════╣");
        System.out.println("║ POS  ║              PRODUCTO                ║    PEDIDOS     ║");
        System.out.println("╠══════╬══════════════════════════════════════╬════════════════╣");

        mostrarTopProductosRecursivo(listaProductos, Math.min(10, listaProductos.size()), 0);

        System.out.println("╚══════╩══════════════════════════════════════╩════════════════╝");
    }

    private static Map<String, Integer> obtenerProductosMasPedidos() {
        Map<String, Integer> productos = new HashMap<>();
        List<File> archivos = obtenerArchivosTickets();

        for (File archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                boolean enSeccionProductos = false;

                while ((linea = br.readLine()) != null) {
                    if (linea.contains("Cant. Producto")) {
                        enSeccionProductos = true;
                        continue;
                    }

                    if (linea.contains("TOTAL:")) {
                        enSeccionProductos = false;
                        continue;
                    }

                    if (enSeccionProductos && linea.contains("║") && !linea.contains("Comentarios:") &&
                            !linea.contains("Estado:") && !linea.contains("════")) {

                        if (!linea.contains("CANCELADO")) {

                            try {
                                String contenido = linea.replaceAll("║", "").trim();
                                String[] partes = contenido.split("\\s+", 3);

                                if (partes.length >= 3) {
                                    int cantidad = Integer.parseInt(partes[0].trim());
                                    String nombreProducto = partes[2].trim().replaceAll("\\$.*", "").trim();

                                    if (!nombreProducto.isEmpty() && !nombreProducto.equals("...")) {
                                        productos.put(nombreProducto, productos.getOrDefault(nombreProducto, 0) + cantidad);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error leyendo archivo: " + archivo.getName());
            }
        }
        return productos;
    }

    private static void quicksortProductosRecursivo(List<Map.Entry<String, Integer>> lista, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionarProductosRecursivo(lista, inicio, fin);
            quicksortProductosRecursivo(lista, inicio, indicePivote - 1);
            quicksortProductosRecursivo(lista, indicePivote + 1, fin);
        }
    }

    private static int particionarProductosRecursivo(List<Map.Entry<String, Integer>> lista, int inicio, int fin) {
        int pivote = lista.get(fin).getValue();
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (lista.get(j).getValue() >= pivote) {
                i++;
                intercambiarProductos(lista, i, j);
            }
        }
        intercambiarProductos(lista, i + 1, fin);
        return i + 1;
    }

    private static void intercambiarProductos(List<Map.Entry<String, Integer>> lista, int i, int j) {
        Map.Entry<String, Integer> temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    private static void mostrarTopProductosRecursivo(List<Map.Entry<String, Integer>> lista, int limite, int index) {
        if (index >= limite || index >= lista.size()) {
            return;
        }

        String nombreProducto = lista.get(index).getKey();
        if (nombreProducto.length() > 30) {
            nombreProducto = nombreProducto.substring(0, 30) + "...";
        }

        System.out.printf("║ %4d ║ %-36s ║ %14d ║\n",
                index + 1, nombreProducto, lista.get(index).getValue());
        mostrarTopProductosRecursivo(lista, limite, index + 1);
    }

    static class TicketData {
        int numeroMesa;
        String fecha;
        int cantidadPedidos;
        double total;

        public TicketData(int numeroMesa, String fecha, int cantidadPedidos, double total) {
            this.numeroMesa = numeroMesa;
            this.fecha = fecha;
            this.cantidadPedidos = cantidadPedidos;
            this.total = total;
        }
    }
}


