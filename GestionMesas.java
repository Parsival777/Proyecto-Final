import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class GestionMesas {
    private static Map<Integer, Mesa> mesas = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionMesas() {
        System.out.println("\n=== GESTIÓN DE MESAS ===");
        System.out.println("1. Agregar Mesa");
        System.out.println("2. Seleccionar Mesa");
        System.out.println("3. Limpiar Mesa");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionMesas(scanner.nextLine().trim());
    }

    private static void procesarOpcionMesas(String input) {
        switch (input) {
            case "0":
                System.out.println("Volviendo al menú principal...");
                break;
            case "1":
                agregarMesa();
                break;
            case "2":
                seleccionarMesa();
                break;
            case "3":
                limpiarMesa();
                menuGestionMesas();
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 3.");
                menuGestionMesas();
        }
    }

    private static void agregarMesa() {
        System.out.print("Ingrese el número de la mesa: ");
        int numeroMesa = obtenerEnteroPositivoRecursivo("Ingrese el número de la mesa: ", scanner.nextLine().trim(), 0);

        // Verificar si la mesa ya existe
        if (mesas.containsKey(numeroMesa)) {
            System.out.println("La mesa " + numeroMesa + " ya existe. ¿Desea seleccionarla? (s/n)");
            String respuesta = scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("s") || respuesta.equals("si")) {
                menuMesaEspecifica(mesas.get(numeroMesa));
            } else {
                System.out.println("Operación cancelada.");
                menuGestionMesas();
            }
            return;
        }

        Mesa nuevaMesa = new Mesa(numeroMesa);
        mesas.put(numeroMesa, nuevaMesa);
        System.out.println("Mesa " + numeroMesa + " agregada correctamente.");

        // Mostrar directamente el menú de la mesa recién agregada
        menuMesaEspecifica(nuevaMesa);
    }

    private static void seleccionarMesa() {
        if (mesas.isEmpty()) {
            System.out.println("No hay mesas disponibles. Por favor, agregue una mesa primero.");
            menuGestionMesas();
            return;
        }

        System.out.println("Mesas disponibles:");
        for (Integer numero : mesas.keySet()) {
            System.out.println("Mesa " + numero);
        }

        System.out.print("Seleccione el número de mesa: ");
        int numeroMesa = obtenerEnteroPositivoRecursivo("Seleccione el número de mesa: ", scanner.nextLine().trim(), 0);

        if (mesas.containsKey(numeroMesa)) {
            menuMesaEspecifica(mesas.get(numeroMesa));
        } else {
            System.out.println("Mesa no encontrada.");
            menuGestionMesas();
        }
    }

    private static void menuMesaEspecifica(Mesa mesa) {
        System.out.println("\n=== MESA " + mesa.getNumero() + " ===");
        System.out.println("1. Agregar pedido");
        System.out.println("2. Procesar pedido (mayor prioridad)");
        System.out.println("3. Mostrar todos los pedidos de la mesa");
        System.out.println("4. Limpiar mesa");
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
                procesarPedidoMesa(mesa);
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
        System.out.print("Nombre del pedido: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre del pedido: ", scanner.nextLine().trim());

        System.out.print("Prioridad (1-5, donde 1 es la más alta): ");
        int prioridad = obtenerEnteroEnRangoRecursivo(1, 5, "Prioridad (1-5, donde 1 es la más alta): ", scanner.nextLine().trim(), 0);

        Pedido pedido = new Pedido(nombre, prioridad);
        mesa.agregarPedido(pedido);
        System.out.println("Pedido agregado a la mesa " + mesa.getNumero() + ": " + pedido);
    }

    private static void procesarPedidoMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos.");
            return;
        }

        Pedido pedido = mesa.procesarPedido();
        System.out.println("Pedido procesado en la mesa " + mesa.getNumero() + ": " + pedido);
    }

    private static void mostrarPedidosMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos.");
            return;
        }

        System.out.println("Pedidos de la mesa " + mesa.getNumero() + ":");
        mesa.mostrarPedidosRecursivo();
    }

    private static void limpiarMesa() {
        if (mesas.isEmpty()) {
            System.out.println("No hay mesas para limpiar.");
            return;
        }

        System.out.println("Mesas disponibles:");
        for (Integer numero : mesas.keySet()) {
            System.out.println("Mesa " + numero);
        }

        System.out.print("Seleccione el número de mesa a limpiar: ");
        int numeroMesa = obtenerEnteroPositivoRecursivo("Seleccione el número de mesa a limpiar: ", scanner.nextLine().trim(), 0);

        if (mesas.containsKey(numeroMesa)) {
            limpiarMesaEspecifica(mesas.get(numeroMesa));
        } else {
            System.out.println("Mesa no encontrada.");
        }
    }

    private static void limpiarMesaEspecifica(Mesa mesa) {
        // Registrar ventas antes de limpiar
        if (!mesa.estaVacia()) {
            System.out.println("Registrando ventas de la mesa " + mesa.getNumero() + "...");

            // Calcular total de ventas (simplificado - en una implementación real se sumarían los precios)
            int totalVentas = mesa.obtenerCantidadPedidos();

            // Registrar en estadísticas de ventas
            EstadisticasVentas.registrarVentasMesa(mesa.getNumero(), totalVentas);

            System.out.println("Ventas registradas correctamente.");
        }

        // Eliminar la mesa
        mesas.remove(mesa.getNumero());
        System.out.println("Mesa " + mesa.getNumero() + " limpiada y eliminada correctamente.");

        // Volver al menú de gestión de mesas
        menuGestionMesas();
    }

    // Métodos auxiliares para validación de entrada
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

        return verificarRangoRecursivo(min, max, mensaje, valor);
    }

    private static int verificarRangoRecursivo(int min, int max, String mensaje, int valor) {
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
        private PriorityQueue<Pedido> pedidos;

        public Mesa(int numero) {
            this.numero = numero;
            this.pedidos = new PriorityQueue<>((p1, p2) -> {
                return Integer.compare(p1.prioridad, p2.prioridad);
            });
        }

        public int getNumero() {
            return numero;
        }

        public void agregarPedido(Pedido pedido) {
            pedidos.add(pedido);
        }

        public Pedido procesarPedido() {
            return pedidos.poll();
        }

        public boolean estaVacia() {
            return pedidos.isEmpty();
        }

        public int obtenerCantidadPedidos() {
            return pedidos.size();
        }

        public void mostrarPedidosRecursivo() {
            Pedido[] pedidosArray = pedidos.toArray(new Pedido[0]);
            mostrarPedidosRecursivo(pedidosArray, 0);
        }

        private void mostrarPedidosRecursivo(Pedido[] pedidos, int index) {
            if (index >= pedidos.length) {
                return;
            }

            System.out.println((index + 1) + ". " + pedidos[index]);
            mostrarPedidosRecursivo(pedidos, index + 1);
        }
    }

    static class Pedido {
        String nombre;
        int prioridad;

        public Pedido(String nombre, int prioridad) {
            this.nombre = nombre;
            this.prioridad = prioridad;
        }

        @Override
        public String toString() {
            return "Pedido{" + "nombre='" + nombre + '\'' + ", prioridad=" + obtenerNivelPrioridad(prioridad) + " (" + prioridad + ")" + '}';
        }

        private String obtenerNivelPrioridad(int prioridad) {
            return obtenerNivelPrioridadRecursivo(prioridad, 1);
        }

        private String obtenerNivelPrioridadRecursivo(int prioridad, int nivel) {
            if (nivel > 5) return "Desconocida";

            if (prioridad == nivel) {
                switch (nivel) {
                    case 1: return "Para Llevar - Urgente";
                    case 2: return "Mesa VIP";
                    case 3: return "Mesa Regular";
                    case 4: return "Pedido Online";
                    case 5: return "Pedido Programado";
                }
            }

            return obtenerNivelPrioridadRecursivo(prioridad, nivel + 1);
        }
    }
}