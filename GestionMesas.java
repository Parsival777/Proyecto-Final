import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class GestionMesas {
    private static Map<Integer, Mesa> mesas = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionMesas() {
        System.out.println("\n=== GESTIÓN DE MESAS ===");

        // Mostrar mesas existentes como opciones
        if (!mesas.isEmpty()) {
            System.out.println("Mesas activas:");
            for (Integer numero : mesas.keySet()) {
                System.out.println(numero + ". Mesa " + numero);
            }
        }

        System.out.println("A. Agregar nueva mesa");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionMesas(scanner.nextLine().trim().toUpperCase());
    }

    private static void procesarOpcionMesas(String input) {
        if (input.equals("0")) {
            System.out.println("Volviendo al menú principal...");
            return;
        }

        if (input.equals("A")) {
            agregarMesa();
            return;
        }

        // Verificar si es un número de mesa válido
        try {
            int numeroMesa = Integer.parseInt(input);
            if (mesas.containsKey(numeroMesa)) {
                menuMesaEspecifica(mesas.get(numeroMesa));
                return;
            }
        } catch (NumberFormatException e) {
            // No es un número, continuar
        }

        System.out.println("Opción no válida.");
        menuGestionMesas();
    }

    private static void agregarMesa() {
        System.out.print("Ingrese el número de la mesa: ");
        int numeroMesa = obtenerEnteroPositivoRecursivo("Ingrese el número de la mesa: ", scanner.nextLine().trim(), 0);

        if (mesas.containsKey(numeroMesa)) {
            System.out.println("La mesa " + numeroMesa + " ya existe.");
            menuGestionMesas();
            return;
        }

        Mesa nuevaMesa = new Mesa(numeroMesa);
        mesas.put(numeroMesa, nuevaMesa);
        System.out.println("Mesa " + numeroMesa + " agregada correctamente.");

        menuMesaEspecifica(nuevaMesa);
    }

    private static void menuMesaEspecifica(Mesa mesa) {
        System.out.println("\n=== MESA " + mesa.getNumero() + " ===");
        System.out.println("1. Agregar pedido");
        System.out.println("2. Procesar pedido (FIFO)");
        System.out.println("3. Mostrar todos los pedidos de la mesa");
        System.out.println("4. Limpiar mesa y generar ticket");
        System.out.println("0. Volver al menú de gestión de mesas");
        System.out.print("Seleccione una opción: ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "0":
                System.out.println("Volviendo al menú de gestión de mesas...");
                menuGestionMesas();
                break;
            case "1":
                agregarPedidoMesa(mesa);
                menuMesaEspecifica(mesa);
                break;
            case "2":
                procesarPedidosMesa(mesa);
                menuMesaEspecifica(mesa);
                break;
            case "3":
                mostrarPedidosMesa(mesa);
                menuMesaEspecifica(mesa);
                break;
            case "4":
                limpiarMesaEspecifica(mesa);
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
                menuMesaEspecifica(mesa);
        }
    }

    private static void agregarPedidoMesa(Mesa mesa) {
        MenuAlimentos.mostrarMenu();
        System.out.print("Seleccione el número del producto: ");
        int opcion = obtenerEnteroEnRangoRecursivo(1, MenuAlimentos.obtenerCantidadProductos(),
                "Seleccione el número del producto: ", scanner.nextLine().trim(), 0);

        MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(opcion);
        if (producto == null) {
            System.out.println("Producto no válido.");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = obtenerEnteroPositivoRecursivo("Cantidad: ", scanner.nextLine().trim(), 0);

        System.out.print("Comentarios o especificaciones: ");
        String comentarios = scanner.nextLine().trim();

        MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentarios, cantidad);
        mesa.agregarPedido(pedido);
        System.out.println("Pedido agregado a la mesa " + mesa.getNumero());
    }

    private static void procesarPedidosMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos.");
            return;
        }

        System.out.print("¿Cuántos pedidos desea procesar? ");
        int cantidad = obtenerEnteroEnRangoRecursivo(1, mesa.obtenerCantidadPedidos(),
                "¿Cuántos pedidos desea procesar? ", scanner.nextLine().trim(), 0);

        for (int i = 0; i < cantidad; i++) {
            MenuAlimentos.PedidoMesa pedido = mesa.procesarPedido();
            if (pedido != null) {
                System.out.println("Pedido procesado: " + pedido);
            }
        }
    }

    private static void mostrarPedidosMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos.");
            return;
        }

        System.out.println("Pedidos de la mesa " + mesa.getNumero() + ":");
        mesa.mostrarPedidos();
    }

    private static void limpiarMesaEspecifica(Mesa mesa) {
        if (!mesa.estaVacia()) {
            // Generar ticket
            MenuAlimentos.Ticket ticket = mesa.generarTicket();
            ticket.mostrarTicket();

            // Registrar ventas para estadísticas
            int totalPedidos = ticket.getCantidadPedidos();
            double totalVentas = ticket.calcularTotal();

            EstadisticasVentas.registrarVentasMesa(mesa.getNumero(), totalPedidos, totalVentas);
            AnalisisVentas.registrarVentaDiaria((int) totalVentas);

            System.out.println("Ventas registradas correctamente.");
        }

        mesas.remove(mesa.getNumero());
        System.out.println("Mesa " + mesa.getNumero() + " limpiada y eliminada correctamente.");
        menuGestionMesas();
    }

    // Métodos auxiliares para entrada de datos (se mantienen igual)
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

    static class Mesa {
        private int numero;
        private Queue<MenuAlimentos.PedidoMesa> pedidos;

        public Mesa(int numero) {
            this.numero = numero;
            this.pedidos = new java.util.LinkedList<>();
        }

        public int getNumero() {
            return numero;
        }

        public void agregarPedido(MenuAlimentos.PedidoMesa pedido) {
            pedidos.add(pedido);
        }

        public MenuAlimentos.PedidoMesa procesarPedido() {
            return pedidos.poll();
        }

        public boolean estaVacia() {
            return pedidos.isEmpty();
        }

        public int obtenerCantidadPedidos() {
            return pedidos.size();
        }

        public void mostrarPedidos() {
            int index = 1;
            for (MenuAlimentos.PedidoMesa pedido : pedidos) {
                System.out.println(index++ + ". " + pedido);
            }
        }

        public MenuAlimentos.Ticket generarTicket() {
            MenuAlimentos.Ticket ticket = new MenuAlimentos.Ticket(numero);
            for (MenuAlimentos.PedidoMesa pedido : pedidos) {
                ticket.agregarPedido(pedido);
            }
            return ticket;
        }
    }
}