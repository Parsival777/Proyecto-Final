package src;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Cargar el menú desde CSV al iniciar el programa
        MenuAlimentos.cargarMenuDesdeCSV();

        int opcion;
        do {
            System.out.println("\n== SISTEMA DE GESTIÓN DE CAFETERÍA ==");
            System.out.println("1. Gestión de Mesas");
            System.out.println("2. Gestión de Productos del Menú");
            System.out.println("3. Gestión de Tareas (Cola de Prioridades)");
            System.out.println("4. Gestión de Empleados (Árbol Binario)");
            System.out.println("5. Estadísticas de Ventas (Recursividad)");
            System.out.println("6. Gestión de Inventario");
            System.out.println("7. Análisis de Datos de Ventas (Divide y Vencerás)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    gestionarMesas();
                    break;
                case 2:
                    gestionarMenuAlimentos();
                    break;
                case 3:
                    gestionarTareas();
                    break;
                case 4:
                    gestionarEmpleados();
                    break;
                case 5:
                    gestionarEstadisticasVentas();
                    break;
                case 6:
                    gestionarInventario();
                    break;
                case 7:
                    gestionarAnalisisVentas();
                    break;
                case 8:
                    System.out.println("¡Gracias por usar el sistema! ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 8);

        scanner.close();
    }

    private static void gestionarMenuAlimentos() {
        int opcion;
        do {
            System.out.println("\n== GESTIÓN DE PRODUCTOS DEL MENÚ ==");
            System.out.println("1. Mostrar menú completo (Interfaz Gráfica)");
            System.out.println("2. Buscar producto por ID");
            System.out.println("3. Buscar producto por nombre");
            System.out.println("4. Realizar pedido en mesa");
            System.out.println("8. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    mostrarMenuCompletoGUI();
                    break;
                case 2:
                    buscarProductoPorID();
                    break;
                case 3:
                    buscarProductoPorNombre();
                    break;
                case 4:
                    realizarPedidoMesa();
                    break;
                case 8:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 8);
    }

    private static void mostrarMenuCompletoGUI() {
        System.out.println("Abriendo interfaz gráfica del menú...");
        MenuInt.mostrarMenuGUI();
    }

    private static void buscarProductoPorID() {
        System.out.print("Ingrese el ID del producto a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MenuAlimentos.ProductoMenu producto = MenuAlimentos.buscarProductoPorID(id);

        if (producto != null) {
            System.out.println("✓ Producto encontrado:");
            System.out.println(producto);
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }

    private static void buscarProductoPorNombre() {
        System.out.print("Ingrese el nombre del producto a buscar: ");
        String nombre = scanner.nextLine();

        int cantidadProductos = MenuAlimentos.obtenerCantidadProductos();
        boolean encontrado = false;

        System.out.println("✓ Resultados de búsqueda:");
        for (int i = 0; i < cantidadProductos; i++) {
            MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(i + 1);
            if (producto != null && producto.nombre.toLowerCase().contains(nombre.toLowerCase())) {
                System.out.println(producto);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println(" No se encontraron productos con: '" + nombre + "'");
        }
    }

    private static void realizarPedidoMesa() {
        System.out.print("Ingrese el número de mesa: ");
        int numeroMesa = scanner.nextInt();
        scanner.nextLine();

        MenuAlimentos.Ticket ticket = new MenuAlimentos.Ticket(numeroMesa);

        System.out.println("Realizando pedido para mesa " + numeroMesa);
        MenuAlimentos.mostrarMenu();

        boolean continuar = true;
        while (continuar) {
            System.out.print("Seleccione el número del producto (0 para terminar): ");
            int opcionProducto = scanner.nextInt();
            scanner.nextLine();

            if (opcionProducto == 0) {
                continuar = false;
                continue;
            }

            MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(opcionProducto);
            if (producto == null) {
                System.out.println("Producto no válido");
                continue;
            }

            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Comentarios (opcional): ");
            String comentarios = scanner.nextLine();

            MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentarios, cantidad);
            ticket.agregarPedido(pedido);

            System.out.println("✓ Producto agregado al pedido");
        }

        // Mostrar ticket final
        ticket.mostrarTicket();
    }

    // CORREGIR ESTOS MÉTODOS:
    private static void gestionarMesas() {
        GestionMesas.menuGestionMesas();
    }

    private static void gestionarTareas() {
        GestionTareas.menuGestionTareas();
    }

    private static void gestionarEmpleados() {
        GestionEmpleados.menuGestionEmpleados();
    }

    private static void gestionarEstadisticasVentas() {
        EstadisticasVentas.menuEstadisticas();
    }

    private static void gestionarInventario() {
        GestionInventario.menuGestionInventario();
    }

    private static void gestionarAnalisisVentas() {
        AnalisisVentas.menuAnalisisVentas();
    }
}
