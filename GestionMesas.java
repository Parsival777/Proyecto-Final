import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GestionMesas {
    private static Map<Integer, Mesa> mesas = new HashMap<>();
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

    private static String obtenerStringOpcional(String mensaje) {
        System.out.print(mensaje);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? "Sin comentarios" : input;
    }

    public static void menuGestionMesas() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE MESAS ===");

            if (!mesas.isEmpty()) {
                System.out.println("Mesas activas:");
                for (Integer numero : mesas.keySet()) {
                    System.out.println(numero + ". Mesa " + numero);
                }
            }

            System.out.println("A. Agregar nueva mesa");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("0")) {
                return;
            } else if (input.equals("A")) {
                agregarMesa();
            } else {
                try {
                    int numeroMesa = Integer.parseInt(input);
                    if (mesas.containsKey(numeroMesa)) {
                        menuMesaEspecifica(mesas.get(numeroMesa));
                    } else {
                        System.out.println("Error: La mesa " + numeroMesa + " no existe.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Opción no válida. Use 'A' para agregar, '0' para volver, o un número de mesa existente.");
                }
            }
        } while (true);
    }

    private static void agregarMesa() {
        int numeroMesa = obtenerEnteroPositivo("Ingrese el número de la mesa: ");

        if (mesas.containsKey(numeroMesa)) {
            System.out.println("La mesa " + numeroMesa + " ya existe.");
            return;
        }

        Mesa nuevaMesa = new Mesa(numeroMesa);
        mesas.put(numeroMesa, nuevaMesa);
        System.out.println("✓ Mesa " + numeroMesa + " agregada correctamente.");

        menuMesaEspecifica(nuevaMesa);
    }

    private static void menuMesaEspecifica(Mesa mesa) {
        int opcion;
        do {
            System.out.println("\n=== MESA " + mesa.getNumero() + " ===");
            System.out.println("1. Agregar pedido");
            System.out.println("2. Procesar pedido (FIFO)");
            System.out.println("3. Mostrar todos los pedidos de la mesa");
            System.out.println("4. Limpiar mesa y generar ticket");
            System.out.println("0. Volver al menú de gestión de mesas");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 4);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú de gestión de mesas...");
                    break;
                case 1:
                    agregarPedidoMesa(mesa);
                    break;
                case 2:
                    procesarPedidosMesa(mesa);
                    break;
                case 3:
                    mostrarPedidosMesa(mesa);
                    break;
                case 4:
                    limpiarMesaEspecifica(mesa);
                    opcion = 0; // Forzar salida después de limpiar
                    break;
            }
        } while (opcion != 0);
    }

    private static void agregarPedidoMesa(Mesa mesa) {
        MenuAlimentos.mostrarMenu();
        int opcion = obtenerEnteroValido("Seleccione el número del producto: ");

        MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(opcion);
        if (producto == null) {
            System.out.println("Producto no válido.");
            return;
        }

        int cantidad = obtenerEnteroEnRango("Cantidad: ", 1, 100);
        String comentarios = obtenerStringOpcional("Comentarios (opcional, presione Enter para omitir): ");

        MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentarios, cantidad);
        mesa.agregarPedido(pedido);
        System.out.println("✓ Pedido agregado a la mesa " + mesa.getNumero());
    }

    private static void procesarPedidosMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos pendientes.");
            return;
        }

        MenuAlimentos.PedidoMesa pedido = mesa.procesarPedido();
        if (pedido != null) {
            System.out.println("✓ Pedido procesado: " + pedido);
            System.out.println("Pedidos pendientes: " + mesa.obtenerCantidadPedidosPendientes());
            System.out.println("Pedidos procesados: " + mesa.obtenerCantidadPedidosProcesados());
        }
    }

    private static void mostrarPedidosMesa(Mesa mesa) {
        System.out.println("\n=== PEDIDOS DE LA MESA " + mesa.getNumero() + " ===");

        System.out.println("\n--- PEDIDOS PENDIENTES ---");
        if (mesa.estaVacia()) {
            System.out.println("No hay pedidos pendientes.");
        } else {
            mesa.mostrarPedidosPendientes();
        }

        System.out.println("\n--- PEDIDOS PROCESADOS ---");
        if (mesa.estaVaciaProcesados()) {
            System.out.println("No hay pedidos procesados.");
        } else {
            mesa.mostrarPedidosProcesados();
        }

        System.out.println("\n--- RESUMEN ---");
        System.out.println("Total pendientes: " + mesa.obtenerCantidadPedidosPendientes());
        System.out.println("Total procesados: " + mesa.obtenerCantidadPedidosProcesados());
        System.out.println("Total general: " + (mesa.obtenerCantidadPedidosPendientes() + mesa.obtenerCantidadPedidosProcesados()));
    }

    private static void limpiarMesaEspecifica(Mesa mesa) {
        if (!mesa.estaVacia() || !mesa.estaVaciaProcesados()) {
            MenuAlimentos.Ticket ticket = mesa.generarTicket();
            ticket.mostrarTicket();
            guardarTicketArchivo(ticket);
            System.out.println("✓ Ticket guardado. Las ventas se analizarán automáticamente.");
        }

        mesas.remove(mesa.getNumero());
        System.out.println("✓ Mesa " + mesa.getNumero() + " limpiada y eliminada correctamente.");
    }

    private static void guardarTicketArchivo(MenuAlimentos.Ticket ticket) {
        String fecha = LocalDate.now().toString();
        String nombreArchivo = "Tickets_" + fecha + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String horario = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            pw.println("╔══════════════════════════════════════════════════════╗");
            pw.println("║                   TICKET MESA " + String.format("%-25s", ticket.getNumeroMesa()) + "║");
            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.println("║ Fecha: " + String.format("%-47s", horario) + "║");
            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.println("║   Cant. Producto                           Precio   ║");
            pw.println("╠══════════════════════════════════════════════════════╣");

            for (MenuAlimentos.PedidoMesa pedido : ticket.getPedidos()) {
                String nombreProducto = pedido.producto.nombre;
                if (nombreProducto.length() > 30) {
                    nombreProducto = nombreProducto.substring(0, 30) + "...";
                }

                String lineaProducto = String.format("║   %-5d %-35s $%7.2f   ║",
                        pedido.cantidad, nombreProducto, pedido.getSubtotal());
                pw.println(lineaProducto);

                if (pedido.comentarios != null && !pedido.comentarios.trim().isEmpty() && !pedido.comentarios.equals("Sin comentarios")) {
                    String comentario = pedido.comentarios;
                    if (comentario.length() > 42) {
                        comentario = comentario.substring(0, 42) + "...";
                    }
                    pw.println("║        Comentarios: " + String.format("%-37s", comentario) + "║");
                }
                pw.println("║                                                    ║");
            }

            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.printf ("║        TOTAL:                             $%10.2f   ║%n", ticket.calcularTotal());
            pw.println("╚══════════════════════════════════════════════════════╝");
            pw.println();

        } catch (IOException e) {
            System.out.println("Error al guardar ticket: " + e.getMessage());
        }
    }

    static class Mesa {
        private int numero;
        private Queue<MenuAlimentos.PedidoMesa> pedidosPendientes;
        private Queue<MenuAlimentos.PedidoMesa> pedidosProcesados;

        public Mesa(int numero) {
            this.numero = numero;
            this.pedidosPendientes = new java.util.LinkedList<>();
            this.pedidosProcesados = new java.util.LinkedList<>();
        }

        public int getNumero() {
            return numero;
        }

        public void agregarPedido(MenuAlimentos.PedidoMesa pedido) {
            pedidosPendientes.add(pedido);
        }

        public MenuAlimentos.PedidoMesa procesarPedido() {
            if (pedidosPendientes.isEmpty()) {
                return null;
            }
            MenuAlimentos.PedidoMesa pedido = pedidosPendientes.poll();
            pedidosProcesados.add(pedido);
            return pedido;
        }

        public boolean estaVacia() {
            return pedidosPendientes.isEmpty();
        }

        public boolean estaVaciaProcesados() {
            return pedidosProcesados.isEmpty();
        }

        public int obtenerCantidadPedidosPendientes() {
            return pedidosPendientes.size();
        }

        public int obtenerCantidadPedidosProcesados() {
            return pedidosProcesados.size();
        }

        public void mostrarPedidosPendientes() {
            int index = 1;
            for (MenuAlimentos.PedidoMesa pedido : pedidosPendientes) {
                System.out.println(index++ + ". [PENDIENTE] " + pedido);
            }
        }

        public void mostrarPedidosProcesados() {
            int index = 1;
            for (MenuAlimentos.PedidoMesa pedido : pedidosProcesados) {
                System.out.println(index++ + ". [PROCESADO] " + pedido);
            }
        }

        public MenuAlimentos.Ticket generarTicket() {
            MenuAlimentos.Ticket ticket = new MenuAlimentos.Ticket(numero);

            for (MenuAlimentos.PedidoMesa pedido : pedidosPendientes) {
                ticket.agregarPedido(pedido);
            }

            for (MenuAlimentos.PedidoMesa pedido : pedidosProcesados) {
                ticket.agregarPedido(pedido);
            }

            return ticket;
        }
    }
}
