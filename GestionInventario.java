import java.io.*;
import java.util.Scanner;

public class GestionInventario {
    private static final int TAMANO_TABLA = 50;
    private static Ingrediente[] tabla = new Ingrediente[TAMANO_TABLA];
    private static Scanner scanner = new Scanner(System.in);
    private static final String[] RUTAS_CSV_INVENTARIO = {
        "data/Invetario de cafeteria 1.csv",
        "src/data/Invetario de cafeteria 1.csv",
        "Invetario de cafeteria 1.csv"
    };

    // Cargar inventario automáticamente al iniciar la clase
    static {
        cargarInventarioDesdeCSV();
    }

    // Métodos de validación robustos
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

    private static int obtenerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = obtenerEnteroValido(mensaje);
            if (valor > 0) {
                return valor;
            } else {
                System.out.println("Error: El valor debe ser positivo.");
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

    private static double obtenerDoublePositivo(String mensaje) {
        while (true) {
            double valor = obtenerDoubleValido(mensaje);
            if (valor >= 0) {
                return valor;
            } else {
                System.out.println("Error: El valor no puede ser negativo.");
            }
        }
    }

    private static String obtenerStringNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Error: Este campo no puede estar vacío.");
            }
        }
    }

    public static void menuGestionInventario() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE INVENTARIO ===");
            System.out.println("1. Agregar ingrediente al inventario");
            System.out.println("2. Eliminar ingrediente del inventario");
            System.out.println("3. Buscar ingrediente");
            System.out.println("4. Mostrar todo el inventario");
            System.out.println("5. Actualizar cantidad de ingrediente");
            System.out.println("6. Verificar stock de ingrediente");
            System.out.println("7. Guardar inventario a CSV");
            System.out.println("8. Cargar inventario desde CSV");
            System.out.println("9. Estadísticas del inventario");
            System.out.println("0. Volver al menú principal");
            
            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 9);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                case 1:
                    agregarIngrediente();
                    break;
                case 2:
                    eliminarIngrediente();
                    break;
                case 3:
                    buscarIngrediente();
                    break;
                case 4:
                    mostrarInventario();
                    break;
                case 5:
                    actualizarCantidadIngrediente();
                    break;
                case 6:
                    verificarStockIngrediente();
                    break;
                case 7:
                    guardarInventarioACSV();
                    break;
                case 8:
                    cargarInventarioDesdeCSV();
                    break;
                case 9:
                    mostrarEstadisticasInventario();
                    break;
            }
        } while (opcion != 0);
    }

    // Método para cargar inventario desde CSV (automático al inicio)
    public static void cargarInventarioDesdeCSV() {
        File archivo = encontrarArchivo(RUTAS_CSV_INVENTARIO, "inventario");
        if (archivo == null) {
            System.out.println("✗ No se pudo cargar el inventario - archivo no encontrado");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            // Limpiar tabla actual
            for (int i = 0; i < TAMANO_TABLA; i++) {
                tabla[i] = null;
            }

            br.readLine(); // Saltar encabezado

            int contador = 0;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(",");
                if (datos.length >= 4) {
                    try {
                        int id = Integer.parseInt(datos[0].trim());
                        String nombre = datos[1].trim();
                        double cantidad = Double.parseDouble(datos[2].trim());
                        String unidad = datos[3].trim();

                        Ingrediente ingrediente = new Ingrediente(id, nombre, cantidad, unidad);
                        int indice = funcionHash(id);

                        // Usar sondeo lineal para manejar colisiones
                        int intento = 0;
                        while (intento < TAMANO_TABLA) {
                            int posicion = (indice + intento) % TAMANO_TABLA;
                            if (tabla[posicion] == null) {
                                tabla[posicion] = ingrediente;
                                contador++;
                                break;
                            }
                            intento++;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Error en línea: " + linea);
                    }
                }
            }
            br.close();
            System.out.println("✓ Inventario cargado correctamente: " + contador + " ingredientes");

        } catch (IOException e) {
            System.out.println("Error al cargar inventario desde CSV: " + e.getMessage());
        }
    }

    private static File encontrarArchivo(String[] rutas, String tipo) {
        // Buscar en múltiples ubicaciones posibles
        String[] ubicaciones = {
            "", // Directorio actual
            System.getProperty("user.dir"), // Directorio de trabajo
            new File(System.getProperty("user.dir")).getParent(), // Directorio padre
            System.getProperty("user.dir") + "/src", // Subdirectorio src
            System.getProperty("user.dir") + "/data", // Subdirectorio data
            System.getProperty("user.dir") + "/src/data" // src/data
        };

        for (String ubicacion : ubicaciones) {
            for (String ruta : rutas) {
                try {
                    File archivo;
                    if (ubicacion.isEmpty()) {
                        archivo = new File(ruta);
                    } else {
                        archivo = new File(ubicacion, ruta);
                    }

                    if (archivo.exists() && archivo.isFile()) {
                        System.out.println("✓ Archivo de " + tipo + " encontrado: " + archivo.getAbsolutePath());
                        return archivo;
                    }
                } catch (Exception e) {
                    // Continuar con la siguiente ruta
                }
            }
        }

        System.out.println("✗ No se encontró el archivo CSV de " + tipo);
        return null;
    }

    public static void guardarInventarioACSV() {
        File archivo = encontrarArchivo(RUTAS_CSV_INVENTARIO, "inventario");
        if (archivo == null) {
            System.out.println("No se puede guardar - archivo de destino no encontrado");
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(archivo));
            pw.println("ID,Nombre del Producto,Stock,Unidad");

            int contador = 0;
            for (int i = 0; i < TAMANO_TABLA; i++) {
                if (tabla[i] != null) {
                    Ingrediente ing = tabla[i];
                    pw.printf("%d,%s,%.1f,%s\n", ing.id, ing.nombre, ing.cantidad, ing.unidad);
                    contador++;
                }
            }
            pw.close();
            System.out.println("✓ Inventario guardado correctamente: " + contador + " ingredientes");

        } catch (IOException e) {
            System.out.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    private static void agregarIngrediente() {
        int id = obtenerEnteroPositivo("ID del ingrediente: ");
        
        // Verificar si el ID ya existe
        if (buscarIndiceRecursivo(id, funcionHash(id), 0) != -1) {
            System.out.println("Error: Ya existe un ingrediente con ID " + id);
            return;
        }

        String nombre = obtenerStringNoVacio("Nombre del ingrediente: ");
        double cantidad = obtenerDoublePositivo("Cantidad en inventario: ");
        String unidad = obtenerStringNoVacio("Unidad de medida: ");

        int indice = funcionHash(id);
        Ingrediente nuevoIngrediente = new Ingrediente(id, nombre, cantidad, unidad);

        // Usar sondeo lineal para manejar colisiones
        int intento = 0;
        while (intento < TAMANO_TABLA) {
            int posicion = (indice + intento) % TAMANO_TABLA;
            if (tabla[posicion] == null) {
                tabla[posicion] = nuevoIngrediente;
                System.out.println("✓ Ingrediente agregado correctamente en posición " + posicion);
                return;
            }
            intento++;
        }

        System.out.println("Error: No se pudo agregar el ingrediente. Inventario lleno.");
    }

    private static void eliminarIngrediente() {
        int id = obtenerEnteroPositivo("ID del ingrediente a eliminar: ");

        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            String nombreEliminado = tabla[indice].nombre;
            tabla[indice] = null;
            System.out.println("✓ Ingrediente '" + nombreEliminado + "' eliminado correctamente.");
        } else {
            System.out.println("Error: No se encontró un ingrediente con ID " + id);
        }
    }

    private static void buscarIngrediente() {
        int id = obtenerEnteroPositivo("ID del ingrediente a buscar: ");

        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            System.out.println("✓ Ingrediente encontrado:");
            System.out.println(tabla[indice]);
        } else {
            System.out.println("Error: No se encontró un ingrediente con ID " + id);
        }
    }

    private static void mostrarInventario() {
        int totalIngredientes = contarIngredientesRecursivo(0, 0);

        if (totalIngredientes == 0) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n=== INVENTARIO COMPLETO ===");
        System.out.println("Total de ingredientes: " + totalIngredientes);
        System.out.println("Capacidad de la tabla: " + TAMANO_TABLA);
        System.out.println("Espacio disponible: " + (TAMANO_TABLA - totalIngredientes));
        System.out.println("----------------------------------------");

        mostrarIngredientesRecursivo(0, 1);
    }

    private static int contarIngredientesRecursivo(int indice, int contador) {
        if (indice >= TAMANO_TABLA) {
            return contador;
        }

        if (tabla[indice] != null) {
            return contarIngredientesRecursivo(indice + 1, contador + 1);
        }

        return contarIngredientesRecursivo(indice + 1, contador);
    }

    private static void mostrarIngredientesRecursivo(int indice, int numero) {
        if (indice >= TAMANO_TABLA) {
            return;
        }

        if (tabla[indice] != null) {
            System.out.printf("%2d. [Posición %2d] %s\n", numero, indice, tabla[indice]);
            mostrarIngredientesRecursivo(indice + 1, numero + 1);
        } else {
            mostrarIngredientesRecursivo(indice + 1, numero);
        }
    }

    private static void actualizarCantidadIngrediente() {
        int id = obtenerEnteroPositivo("ID del ingrediente a actualizar: ");

        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            System.out.println("Ingrediente actual: " + tabla[indice]);
            double nuevaCantidad = obtenerDoublePositivo("Nueva cantidad: ");
            
            double cantidadAnterior = tabla[indice].cantidad;
            tabla[indice].cantidad = nuevaCantidad;
            
            System.out.printf("✓ Cantidad actualizada: %.1f → %.1f %s\n", 
                cantidadAnterior, nuevaCantidad, tabla[indice].unidad);
        } else {
            System.out.println("Error: No se encontró un ingrediente con ID " + id);
        }
    }

    private static void verificarStockIngrediente() {
        int id = obtenerEnteroPositivo("ID del ingrediente a verificar: ");

        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            Ingrediente ingrediente = tabla[indice];
            System.out.println("Stock actual: " + ingrediente);
            
            double cantidadRequerida = obtenerDoublePositivo("Cantidad requerida: ");
            
            if (ingrediente.cantidad >= cantidadRequerida) {
                System.out.println("✓ Stock suficiente. Disponible: " + ingrediente.cantidad + " " + ingrediente.unidad);
            } else {
                double faltante = cantidadRequerida - ingrediente.cantidad;
                System.out.printf("✗ Stock insuficiente. Faltan: %.1f %s\n", faltante, ingrediente.unidad);
            }
        } else {
            System.out.println("Error: No se encontró un ingrediente con ID " + id);
        }
    }

    private static void mostrarEstadisticasInventario() {
        int totalIngredientes = contarIngredientesRecursivo(0, 0);
        
        if (totalIngredientes == 0) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n=== ESTADÍSTICAS DEL INVENTARIO ===");
        System.out.println("Total de ingredientes: " + totalIngredientes);
        System.out.println("Capacidad total: " + TAMANO_TABLA);
        System.out.printf("Porcentaje de uso: %.1f%%\n", (totalIngredientes * 100.0 / TAMANO_TABLA));
        
        // Calcular estadísticas de cantidades
        double[] stats = calcularEstadisticasRecursivo(0, 0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE);
        System.out.printf("Cantidad total en inventario: %.1f\n", stats[0]);
        System.out.printf("Cantidad promedio: %.1f\n", stats[0] / totalIngredientes);
        System.out.printf("Cantidad máxima: %.1f\n", stats[1]);
        System.out.printf("Cantidad mínima: %.1f\n", stats[2]);
        
        // Mostrar ingredientes con stock bajo (menos de 10 unidades)
        System.out.println("\n--- INGREDIENTES CON STOCK BAJO (< 10) ---");
        int bajos = mostrarStockBajoRecursivo(0, 0);
        if (bajos == 0) {
            System.out.println("No hay ingredientes con stock bajo.");
        } else {
            System.out.println("Total de ingredientes con stock bajo: " + bajos);
        }
    }

    private static double[] calcularEstadisticasRecursivo(int indice, int contador, double suma, double max, double min) {
        if (indice >= TAMANO_TABLA) {
            return new double[]{suma, max, min};
        }

        if (tabla[indice] != null) {
            double cantidad = tabla[indice].cantidad;
            double nuevaSuma = suma + cantidad;
            double nuevoMax = Math.max(max, cantidad);
            double nuevoMin = Math.min(min, cantidad);
            
            return calcularEstadisticasRecursivo(indice + 1, contador + 1, nuevaSuma, nuevoMax, nuevoMin);
        }

        return calcularEstadisticasRecursivo(indice + 1, contador, suma, max, min);
    }

    private static int mostrarStockBajoRecursivo(int indice, int contador) {
        if (indice >= TAMANO_TABLA) {
            return contador;
        }

        if (tabla[indice] != null && tabla[indice].cantidad < 10) {
            System.out.println("• " + tabla[indice].nombre + ": " + tabla[indice].cantidad + " " + tabla[indice].unidad);
            return mostrarStockBajoRecursivo(indice + 1, contador + 1);
        }

        return mostrarStockBajoRecursivo(indice + 1, contador);
    }

    // Métodos de búsqueda recursiva
    private static int buscarIndiceRecursivo(int id, int indice, int intento) {
        if (intento >= TAMANO_TABLA) {
            return -1;
        }

        int posicion = (indice + intento) % TAMANO_TABLA;
        
        if (tabla[posicion] != null && tabla[posicion].id == id) {
            return posicion;
        }

        if (tabla[posicion] == null) {
            return -1;
        }

        return buscarIndiceRecursivo(id, indice, intento + 1);
    }

    private static int funcionHash(int id) {
        return id % TAMANO_TABLA;
    }

    // Métodos públicos para integración con otros módulos
    public static Ingrediente obtenerIngredientePorID(int id) {
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            return tabla[indice];
        }
        return null;
    }

    public static void actualizarCantidadIngrediente(int id, double nuevaCantidad) {
        int indice = buscarIndiceRecursivo(id, funcionHash(id), 0);
        if (indice != -1) {
            tabla[indice].cantidad = nuevaCantidad;
        }
    }

    public static boolean verificarStock(int id, double cantidadRequerida) {
        Ingrediente ingrediente = obtenerIngredientePorID(id);
        if (ingrediente != null) {
            return ingrediente.cantidad >= cantidadRequerida;
        }
        return false;
    }

    public static int getTotalIngredientes() {
        return contarIngredientesRecursivo(0, 0);
    }

    public static boolean estaVacio() {
        return contarIngredientesRecursivo(0, 0) == 0;
    }

    // Clase interna para representar ingredientes
    static class Ingrediente {
        int id;
        String nombre;
        double cantidad;
        String unidad;

        public Ingrediente(int id, String nombre, double cantidad, String unidad) {
            this.id = id;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.unidad = unidad;
        }

        @Override
        public String toString() {
            return String.format("ID: %d | %-20s | %6.1f %-5s", id, nombre, cantidad, unidad);
        }
    }
}
