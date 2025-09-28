import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.io.File;

public class GestionMesas {
    private static Map<Integer, Mesa> mesas = new HashMap<>();
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


        int numComensales = obtenerEnteroEnRango("Ingrese el número de comensales: ", 1, 20);
        Mesa nuevaMesa = new Mesa(numeroMesa, numComensales);
        mesas.put(numeroMesa, nuevaMesa);
        System.out.println("✓ Mesa " + numeroMesa + " agregada correctamente. Comensales: " + numComensales);

        menuMesaEspecifica(nuevaMesa);
    }

    private static void menuMesaEspecifica(Mesa mesa) {
        int opcion;
        do {
            System.out.println("\n=== MESA " + mesa.getNumero() + " ===");
            System.out.println("Comensales: " + mesa.getNumeroComensales());
            System.out.println("1. Agregar pedido");
            System.out.println("2. Procesar pedido (FIFO)");
            System.out.println("3. Mostrar todos los pedidos de la mesa");
            System.out.println("4. Cambiar número de comensales");
            System.out.println("5. Limpiar mesa y generar ticket");
            System.out.println("0. Volver al menú de gestión de mesas");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 5);

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
                    cambiarNumeroComensales(mesa);
                    break;
                case 5:
                    limpiarMesaEspecifica(mesa);
                    if (mesa.estaLimpiada()) {
                        opcion = 0;
                    }
                    break;
            }
        } while (opcion != 0);
    }

    private static void cambiarNumeroComensales(Mesa mesa) {
        int nuevoNumero = obtenerEnteroEnRango("Ingrese el nuevo número de comensales: ", 1, 50);
        mesa.setNumeroComensales(nuevoNumero);
        System.out.println("✓ Número de comensales actualizado: " + nuevoNumero);
    }

    private static void agregarPedidoMesa(Mesa mesa) {
        boolean continuar = true;
        int comensalActual = 1;

        while (continuar && comensalActual <= mesa.getNumeroComensales()) {
            System.out.println("\n--- Pedido para comensal " + comensalActual + " de " + mesa.getNumeroComensales() + " ---");

            boolean pedidoCompletado = false;
            while (!pedidoCompletado) {
                MenuAlimentos.mostrarMenu();
                System.out.println("0. Saltar al siguiente comensal");
                System.out.println("00. Terminar pedidos (guardar y salir)");
                System.out.print("Seleccione una opción: ");

                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    pedidoCompletado = true;
                    comensalActual++;
                    System.out.println("✓ Saltando al siguiente comensal...");
                } else if (input.equals("00")) {
                    pedidoCompletado = true;
                    continuar = false;
                    System.out.println("✓ Pedidos guardados.");
                } else {
                    try {
                        int opcion = Integer.parseInt(input);
                        MenuAlimentos.ProductoMenu producto = MenuAlimentos.obtenerProducto(opcion);

                        if (producto == null) {
                            System.out.println("Producto no válido.");
                            continue;
                        }

                        int cantidad = obtenerEnteroEnRango("Cantidad: ", 1, 100);
                        String comentarios = obtenerStringOpcional("Comentarios (opcional, presione Enter para omitir): ");

                        MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentarios, cantidad);
                        mesa.agregarPedido(pedido);
                        System.out.println("✓ Pedido agregado para comensal " + comensalActual);


                        System.out.print("¿Agregar otro producto para este comensal? (1: Sí, 2: No): ");
                        int otro = obtenerEnteroEnRango("", 1, 2);
                        if (otro == 2) {
                            pedidoCompletado = true;
                            comensalActual++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Opción no válida.");
                    }
                }


                if (comensalActual > mesa.getNumeroComensales()) {
                    continuar = false;
                    System.out.println("✓ Pedidos completados para todos los comensales.");
                }
            }
        }
    }

    private static void procesarPedidosMesa(Mesa mesa) {
        if (mesa.estaVacia()) {
            System.out.println("La mesa " + mesa.getNumero() + " no tiene pedidos pendientes.");
            return;
        }

        System.out.println("Procesando siguiente pedido...");
        MenuAlimentos.PedidoMesa pedido = mesa.procesarPedido();
        if (pedido != null) {
            System.out.println("✓ Pedido procesado: " + pedido.producto.nombre);
            System.out.println("Pedidos pendientes: " + mesa.obtenerCantidadPedidosPendientes());
            System.out.println("Pedidos procesados: " + mesa.obtenerCantidadPedidosProcesados());
        }
    }

    private static void mostrarPedidosMesa(Mesa mesa) {
        System.out.println("\n=== PEDIDOS DE LA MESA " + mesa.getNumero() + " ===");
        System.out.println("Comensales: " + mesa.getNumeroComensales());

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

        System.out.println("\n--- PEDIDOS CANCELADOS ---");
        if (mesa.estaVaciaCancelados()) {
            System.out.println("No hay pedidos cancelados.");
        } else {
            mesa.mostrarPedidosCancelados();
        }

        System.out.println("\n--- RESUMEN ---");
        System.out.println("Total pendientes: " + mesa.obtenerCantidadPedidosPendientes());
        System.out.println("Total procesados: " + mesa.obtenerCantidadPedidosProcesados());
        System.out.println("Total cancelados: " + mesa.obtenerCantidadPedidosCancelados());
        System.out.println("Total general: " + (mesa.obtenerCantidadPedidosPendientes() +
                mesa.obtenerCantidadPedidosProcesados() +
                mesa.obtenerCantidadPedidosCancelados()));
    }

    private static void limpiarMesaEspecifica(Mesa mesa) {

        if (!mesa.estaVacia()) {
            System.out.println("\n Hay " + mesa.obtenerCantidadPedidosPendientes() + " pedidos pendientes por procesar.");
            System.out.println("1. Regresar al menú de la mesa");
            System.out.println("2. Procesar todos los pedidos pendientes automáticamente");
            System.out.println("3. Cancelar pedidos pendientes y generar ticket");

            int opcion = obtenerEnteroEnRango("Seleccione una opción: ", 1, 3);

            switch (opcion) {
                case 1:
                    System.out.println("Regresando al menú de la mesa...");
                    return;
                case 2:

                    int pendientes = mesa.obtenerCantidadPedidosPendientes();
                    while (!mesa.estaVacia()) {
                        mesa.procesarPedido();
                    }
                    System.out.println("✓ " + pendientes + " pedidos procesados automáticamente.");
                    break;
                case 3:

                    int cancelados = mesa.obtenerCantidadPedidosPendientes();
                    mesa.cancelarPedidosPendientes();
                    System.out.println("✓ " + cancelados + " pedidos marcados como cancelados.");
                    break;
            }
        }


        if (!mesa.estaVaciaProcesados() || !mesa.estaVaciaCancelados()) {
            MenuAlimentos.Ticket ticket = mesa.generarTicket();
            ticket.mostrarTicket();
            guardarTicketArchivo(ticket);
            System.out.println("✓ Ticket guardado. Las ventas se analizarán automáticamente.");
        } else {
            System.out.println("No hay pedidos para generar ticket.");
        }

        mesas.remove(mesa.getNumero());
        mesa.marcarComoLimpiada();
        System.out.println("✓ Mesa " + mesa.getNumero() + " limpiada y eliminada correctamente.");
    }

    private static void guardarTicketArchivo(MenuAlimentos.Ticket ticket) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        String fecha = LocalDate.now().format(formatter).toUpperCase();
        String nombreArchivo = "Tickets_" + fecha + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {


            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm", Locale.ENGLISH);
            String horario = LocalDateTime.now().format(horaFormatter).toUpperCase();

            pw.println("╔══════════════════════════════════════════════════════╗");
            pw.println("║                   TICKET MESA " + String.format("%-25s", ticket.getNumeroMesa()) + "║");
            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.println("║ Fecha: " + String.format("%-47s", horario) + "║");
            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.println("║   Cant. Producto                           Precio   ║");
            pw.println("╠══════════════════════════════════════════════════════╣");

            double totalProcesados = 0.0;
            double totalCancelados = 0.0;

            for (MenuAlimentos.PedidoMesa pedido : ticket.getPedidos()) {
                String nombreProducto = pedido.producto.nombre;
                if (nombreProducto.length() > 30) {
                    nombreProducto = nombreProducto.substring(0, 30) + "...";
                }


                boolean esCancelado = pedido.comentarios != null && pedido.comentarios.contains("CANCELADO");
                String estado = esCancelado ? "✗ CANCELADO" : "✓ PROCESADO";
                double subtotal = pedido.getSubtotal();

                if (esCancelado) {
                    totalCancelados += subtotal;
                } else {
                    totalProcesados += subtotal;
                }

                String lineaProducto = String.format("║   %-5d %-35s $%7.2f   ║",
                        pedido.cantidad, nombreProducto, subtotal);
                pw.println(lineaProducto);
                pw.println("║        Estado: " + String.format("%-37s", estado) + "║");

                if (pedido.comentarios != null && !pedido.comentarios.trim().isEmpty() &&
                        !pedido.comentarios.equals("Sin comentarios") && !pedido.comentarios.contains("CANCELADO")) {
                    String comentario = pedido.comentarios;
                    if (comentario.length() > 42) {
                        comentario = comentario.substring(0, 42) + "...";
                    }
                    pw.println("║        Comentarios: " + String.format("%-37s", comentario) + "║");
                }
                pw.println("║                                                    ║");
            }

            pw.println("╠══════════════════════════════════════════════════════╣");
            pw.printf ("║        SUBTOTAL PROCESADOS:              $%10.2f   ║%n", totalProcesados);
            if (totalCancelados > 0) {
                pw.printf ("║        PEDIDOS CANCELADOS:               $%10.2f   ║%n", totalCancelados);
            }
            pw.printf ("║        TOTAL:                             $%10.2f   ║%n", totalProcesados);
            pw.println("╚══════════════════════════════════════════════════════╝");
            pw.println();

            System.out.println("✓ Ticket guardado en: " + nombreArchivo);

        } catch (IOException e) {
            System.out.println("Error al guardar ticket: " + e.getMessage());

            File archivo = new File(nombreArchivo);
            try {
                archivo.getParentFile().mkdirs();
                System.out.println("Directorios creados, intentando guardar nuevamente...");
                guardarTicketArchivo(ticket);
            } catch (Exception ex) {
                System.out.println("Error crítico al guardar ticket: " + ex.getMessage());
            }
        }
    }

    static class Mesa {
        private int numero;
        private int numeroComensales;
        private Queue<MenuAlimentos.PedidoMesa> pedidosPendientes;
        private Queue<MenuAlimentos.PedidoMesa> pedidosProcesados;
        private Queue<MenuAlimentos.PedidoMesa> pedidosCancelados;
        private boolean limpiada;

        public Mesa(int numero, int numeroComensales) {
            this.numero = numero;
            this.numeroComensales = numeroComensales;
            this.pedidosPendientes = new java.util.LinkedList<>();
            this.pedidosProcesados = new java.util.LinkedList<>();
            this.pedidosCancelados = new java.util.LinkedList<>();
            this.limpiada = false;
        }

        public Mesa(int numero) {
            this(numero, 0);
        }

        public int getNumero() {
            return numero;
        }

        public int getNumeroComensales() {
            return numeroComensales;
        }

        public void setNumeroComensales(int numeroComensales) {
            this.numeroComensales = numeroComensales;
        }

        public boolean estaLimpiada() {
            return limpiada;
        }

        public void marcarComoLimpiada() {
            this.limpiada = true;
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

        public void cancelarPedidosPendientes() {
            while (!pedidosPendientes.isEmpty()) {
                MenuAlimentos.PedidoMesa pedido = pedidosPendientes.poll();

                pedido.comentarios = (pedido.comentarios.equals("Sin comentarios") ? "" : pedido.comentarios + " ") + "CANCELADO";
                pedidosCancelados.add(pedido);
            }
        }

        public boolean estaVacia() {
            return pedidosPendientes.isEmpty();
        }

        public boolean estaVaciaProcesados() {
            return pedidosProcesados.isEmpty();
        }

        public boolean estaVaciaCancelados() {
            return pedidosCancelados.isEmpty();
        }

        public int obtenerCantidadPedidosPendientes() {
            return pedidosPendientes.size();
        }

        public int obtenerCantidadPedidosProcesados() {
            return pedidosProcesados.size();
        }

        public int obtenerCantidadPedidosCancelados() {
            return pedidosCancelados.size();
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

        public void mostrarPedidosCancelados() {
            int index = 1;
            for (MenuAlimentos.PedidoMesa pedido : pedidosCancelados) {
                System.out.println(index++ + ". [CANCELADO] " + pedido);
            }
        }

        public MenuAlimentos.Ticket generarTicket() {
            MenuAlimentos.Ticket ticket = new MenuAlimentos.Ticket(numero);


            for (MenuAlimentos.PedidoMesa pedido : pedidosProcesados) {
                ticket.agregarPedido(pedido);
            }


            for (MenuAlimentos.PedidoMesa pedido : pedidosCancelados) {
                ticket.agregarPedido(pedido);
            }

            return ticket;
        }
    }
}
