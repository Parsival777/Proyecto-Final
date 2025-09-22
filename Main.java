import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarMenuPrincipal();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE CAFETERÍA ===");
        System.out.println("1. Gestión de Mesas");
        System.out.println("2. Gestión de Productos del Menú");
        System.out.println("3. Estadísticas de Ventas (Recursividad)");
        System.out.println("4. Gestión de Inventario");
        System.out.println("5. Análisis de Datos de Ventas");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        procesarOpcionPrincipal();
    }

    private static void procesarOpcionPrincipal() {
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                GestionMesas.menuGestionMesas();
                mostrarMenuPrincipal();
                break;
            case "2":
                GestionProductos.menuGestionProductos();
                mostrarMenuPrincipal();
                break;
            case "3":
                EstadisticasVentas.menuEstadisticas();
                mostrarMenuPrincipal();
                break;
            case "4":
                GestionInventario.menuGestionInventario();
                mostrarMenuPrincipal();
                break;
            case "5":
                AnalisisVentas.menuAnalisisVentas();
                mostrarMenuPrincipal();
                break;
            case "0":
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 5.");
                mostrarMenuPrincipal();
        }
    }
}