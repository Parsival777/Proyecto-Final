import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DemoCafeteria {
    private static Random random = new Random();

    public static void ejecutarDemo() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                   DEMOSTRACIÓN DEL SISTEMA DE CAFETERÍA");
        System.out.println("=".repeat(80));

        // Asegurarse de que el menú esté cargado
        MenuAlimentos.cargarMenuDesdeCSV();

        // 1. Mostrar el menú usando la clase real
        System.out.println("\n1. MENU DE CAFETERÍA POWER CAFE");
        System.out.println("-".repeat(50));
        MenuAlimentos.mostrarMenu();

        // 2. Crear empleados y agregarlos al sistema de gestión
        System.out.println("\n2. EMPLEADOS REGISTRADOS");
        System.out.println("-".repeat(50));
        crearEmpleados();

        // 3. Crear tareas
        System.out.println("\n3. TAREAS PROGRAMADAS");
        System.out.println("-".repeat(50));
        crearTareas();

        // 4. Generar tickets usando la clase Ticket real
        System.out.println("\n4. TICKETS GENERADOS");
        System.out.println("-".repeat(50));
        generarTickets();
    }

    private static void crearEmpleados() {
        String[] nombres = {"María González", "Carlos Rodríguez", "Ana Martínez", "Pedro López",
                "Laura Hernández", "Jorge Díaz", "Sofía Pérez", "Miguel Castro"};
        String[] departamentos = {"Cocina", "Limpieza", "Barista"}; // Solo estos departamentos

        // Crear 4 empleados y agregarlos al sistema de gestión
        for (int i = 0; i < 4; i++) {
            String nombre = nombres[random.nextInt(nombres.length)];
            String departamento = departamentos[random.nextInt(departamentos.length)];
            double salario = 10000 + (random.nextInt(6000) / 100.0) * 100; // $10,000 - $16,000

            // Crear empleado con ID único (1000 + i para evitar conflictos)
            int id = 1000 + i;
            GestionEmpleados.Empleado nuevoEmpleado = new GestionEmpleados.Empleado(id, nombre, departamento, salario);

            // Agregar al árbol de empleados del sistema (solo si no existe)
            GestionEmpleados.agregarEmpleadoDemo(nuevoEmpleado);

            System.out.printf("• %s - %s - $%,.2f (ID: %d)\n", nombre, departamento, salario, id);
        }
        System.out.println("✓ Empleados agregados al sistema de gestión");
    }

    private static void crearTareas() {
        String[] tareas = {
                "Limpieza general del local", "Preparar ingredientes del día",
                "Atender capacitación de baristas", "Revisar inventario",
                "Preparar promociones del día", "Atención a clientes especiales",
                "Revisar equipos de cocina", "Organizar almacén"
        };

        // Crear 5 tareas (sin prioridades)
        for (int i = 0; i < 5; i++) {
            String tarea = tareas[random.nextInt(tareas.length)];
            int tiempo = 30 + random.nextInt(180); // 30-210 minutos

            System.out.printf("• %s - %d min\n", tarea, tiempo);
        }
    }

    private static void generarTickets() {
        // Comentarios lógicos según el tipo de producto
        String[] comentariosBebidasCalientes = {
                "Extra caliente", "Sin azúcar", "Poca espuma", "Con leche deslactosada",
                "Doble shot", "Con canela", "Templado"
        };

        String[] comentariosBebidasFrias = {
                "Con hielo extra", "Poco hielo", "Sin hielo", "Endulzado ligero",
                "Con limón extra", "Batido fuerte", "Natural"
        };

        String[] comentariosComida = {
                "Bien cocido", "Poco cocido", "Sin sal", "Aderezo aparte",
                "Con extra queso", "Sin picante", "Vegetariano", "Sin gluten"
        };

        String[] comentariosPostres = {
                "Para compartir", "Con extra crema", "Sin azúcar", "Caliente",
                "Frío", "Con salsa de chocolate", "Sin nueces"
        };

        // Generar 3 tickets
        for (int i = 0; i < 3; i++) {
            int numeroMesa = 1 + random.nextInt(10); // Mesa 1-10
            generarTicket(numeroMesa, comentariosBebidasCalientes, comentariosBebidasFrias,
                    comentariosComida, comentariosPostres);
            System.out.println();
        }
    }

    private static void generarTicket(int numeroMesa, String[] comentariosCalientes,
                                      String[] comentariosFrios, String[] comentariosComida,
                                      String[] comentariosPostres) {
        // Crear un ticket real usando la clase del sistema
        MenuAlimentos.Ticket ticket = new MenuAlimentos.Ticket(numeroMesa);

        // Agregar 3-5 productos al ticket
        int cantidadProductos = 3 + random.nextInt(3); // 3-5 productos
        int totalProductos = MenuAlimentos.obtenerCantidadProductos();

        for (int i = 0; i < cantidadProductos; i++) {
            int idProducto = 1 + random.nextInt(totalProductos);
            MenuAlimentos.ProductoMenu producto = MenuAlimentos.buscarProductoPorID(idProducto);

            if (producto != null) {
                int cantidad = 1 + random.nextInt(3); // 1-3 unidades
                String comentario = obtenerComentarioAleatorio(producto, comentariosCalientes,
                        comentariosFrios, comentariosComida, comentariosPostres);

                MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentario, cantidad);
                ticket.agregarPedido(pedido);
            }
        }

        // Mostrar el ticket usando el método real
        ticket.mostrarTicket();

        // GUARDAR EL TICKET EN ARCHIVO (NUEVO)
        guardarTicketDemoArchivo(ticket);
    }

    // MÉTODO NUEVO PARA GUARDAR TICKETS DE LA DEMO
    private static void guardarTicketDemoArchivo(MenuAlimentos.Ticket ticket) {
        // Formato de fecha para archivo: 26-SEP-2025 (sin / para evitar problemas)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        String fecha = LocalDate.now().format(formatter).toUpperCase();
        String nombreArchivo = "Tickets_" + fecha + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            // Formato de hora para mostrar: 26/SEP/2025 14:30
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm", Locale.ENGLISH);
            String horario = java.time.LocalDateTime.now().format(horaFormatter).toUpperCase();

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

                // Determinar estado y si contribuye al total
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
            System.out.println("Error al guardar ticket de demo: " + e.getMessage());
            // Intentar crear el directorio si no existe
            java.io.File archivo = new java.io.File(nombreArchivo);
            try {
                archivo.getParentFile().mkdirs(); // Crear directorios padres si no existen
                System.out.println("Directorios creados, intentando guardar nuevamente...");
                guardarTicketDemoArchivo(ticket); // Reintentar
            } catch (Exception ex) {
                System.out.println("Error crítico al guardar ticket de demo: " + ex.getMessage());
            }
        }
    }

    private static String obtenerComentarioAleatorio(MenuAlimentos.ProductoMenu producto,
                                                     String[] comentariosCalientes,
                                                     String[] comentariosFrios,
                                                     String[] comentariosComida,
                                                     String[] comentariosPostres) {
        String nombre = producto.nombre.toLowerCase();

        if (nombre.contains("café") || nombre.contains("té") || nombre.contains("capuccino") ||
                nombre.contains("latte") || nombre.contains("chocolate caliente")) {
            return comentariosCalientes[random.nextInt(comentariosCalientes.length)];
        } else if (nombre.contains("frappé") || nombre.contains("smoothie") ||
                nombre.contains("limonada") || nombre.contains("refresco")) {
            return comentariosFrios[random.nextInt(comentariosFrios.length)];
        } else if (nombre.contains("sandwich") || nombre.contains("bagel") ||
                nombre.contains("ensalada") || nombre.contains("sopa")) {
            return comentariosComida[random.nextInt(comentariosComida.length)];
        } else if (nombre.contains("pastel") || nombre.contains("galleta") ||
                nombre.contains("muffin") || nombre.contains("postre")) {
            return comentariosPostres[random.nextInt(comentariosPostres.length)];
        }

        return ""; // Sin comentario
    }
}
