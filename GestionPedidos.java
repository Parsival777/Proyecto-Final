import java.util.PriorityQueue;
import java.util.Scanner;

public class GestionPedidos {
    private static PriorityQueue<Pedido> colaPedidos = new PriorityQueue<>((p1, p2) -> {
        return Integer.compare(p1.prioridad, p2.prioridad);
    });

    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionPedidos() {
        System.out.println("\n=== GESTIÓN DE PEDIDOS ===");
        System.out.println("1. Agregar pedido");
        System.out.println("2. Procesar pedido (mayor prioridad)");
        System.out.println("3. Mostrar todos los pedidos");
        System.out.println("4. Buscar pedido por cliente");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionPedidos(scanner.nextLine().trim());
    }

    private static void procesarOpcionPedidos(String input) {
        procesarOpcionRecursiva(input);
    }

    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));

        procesarOpcion(input, "1", GestionPedidos::agregarPedido);
        procesarOpcion(input, "2", GestionPedidos::procesarPedido);
        procesarOpcion(input, "3", GestionPedidos::mostrarPedidosRecursivo);
        procesarOpcion(input, "4", GestionPedidos::buscarPedido);

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
            menuGestionPedidos();
        }
    }

    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);

        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuGestionPedidos();
        }
    }

    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }

    private static void agregarPedido() {
        System.out.print("Nombre del cliente: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre del cliente: ", scanner.nextLine().trim());

        System.out.print("Prioridad (1-5, donde 1 es la más alta): ");
        int prioridad = obtenerEnteroEnRangoRecursivo(1, 5, "Prioridad (1-5, donde 1 es la más alta): ", scanner.nextLine().trim(), 0);

        Pedido pedido = new Pedido(nombre, prioridad);
        colaPedidos.add(pedido);
        System.out.println("Pedido agregado: " + pedido);
    }

    private static void procesarPedido() {
        manejarColaVacia(() -> {
            Pedido pedido = colaPedidos.poll();
            System.out.println("Pedido procesado: " + pedido);
        });
    }

    private static void mostrarPedidosRecursivo() {
        manejarColaVacia(() -> {
            Pedido[] pedidos = colaPedidos.toArray(new Pedido[0]);
            System.out.println("Pedidos en la cola:");
            mostrarPedidosRecursivo(pedidos, 0);
        });
    }

    private static void mostrarPedidosRecursivo(Pedido[] pedidos, int index) {
        if (index >= pedidos.length) {
            return;
        }

        System.out.println((index + 1) + ". " + pedidos[index]);
        mostrarPedidosRecursivo(pedidos, index + 1);
    }

    private static void buscarPedido() {
        System.out.print("Ingrese el nombre del cliente a buscar: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Ingrese el nombre del cliente a buscar: ", scanner.nextLine().trim());

        Pedido[] pedidos = colaPedidos.toArray(new Pedido[0]);
        boolean encontrado = buscarPedidoRecursivo(pedidos, nombre, 0);

        manejarResultadoBusqueda(encontrado);
    }

    private static boolean buscarPedidoRecursivo(Pedido[] pedidos, String nombre, int index) {
        if (index >= pedidos.length) {
            return false;
        }

        if (pedidos[index].cliente.equalsIgnoreCase(nombre)) {
            System.out.println("Pedido encontrado: " + pedidos[index]);
            return true;
        }

        return buscarPedidoRecursivo(pedidos, nombre, index + 1);
    }

    private static void manejarResultadoBusqueda(boolean encontrado) {
        if (!encontrado) {
            System.out.println("Pedido no encontrado.");
        }
    }

    private static void manejarColaVacia(Runnable action) {
        if (colaPedidos.isEmpty()) {
            System.out.println("La cola de pedidos está vacía.");
            return;
        }
        action.run();
    }

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

        System.out.print("Por favor, ingrese un número entre " + min + " и " + max + ". " + mensaje);
        return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), 0);
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

    static class Pedido {
        String cliente;
        int prioridad;

        public Pedido(String cliente, int prioridad) {
            this.cliente = cliente;
            this.prioridad = prioridad;
        }

        @Override
        public String toString() {
            return "Pedido{" + "cliente='" + cliente + '\'' + ", prioridad=" + obtenerNivelPrioridad(prioridad) + " (" + prioridad + ")" + '}';
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