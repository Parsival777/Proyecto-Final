import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DemoCafeteria {
    private static Random random = new Random();

    public static void ejecutarDemo() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                   DEMOSTRACIÓN DEL SISTEMA DE CAFETERÍA");
        System.out.println("=".repeat(80));

        MenuAlimentos.cargarMenuDesdeCSV();

        System.out.println("\n1. MENU DE CAFETERÍA POWER CAFE");
        System.out.println("-".repeat(50));
        MenuAlimentos.mostrarMenu();

        System.out.println("\n2. EMPLEADOS REGISTRADOS");
        System.out.println("-".repeat(50));
        crearEmpleados();

        System.out.println("\n3. TAREAS PROGRAMADAS");
        System.out.println("-".repeat(50));
        crearTareas();

        System.out.println("\n4. TICKETS Y MESAS");
        System.out.println("-".repeat(50));
        generarTicketsYMesas();
    }

    private static void crearEmpleados() {
        String[] nombres = {"Marcos Gutiérrez", "Julieta Cárdenas", "Andrés Cervantes", "Patricia Morales",
                "Laura Hernández", "Antonio Delgado", "Beatriz Herrera", "Camilo Vázquez",
                "Daniela Romero", "Ricardo Silva", "Elena Castro", "Fernando Mendoza"};
        String[] departamentos = {"Limpieza", "Cocina", "Barista"};

        // Cargar empleados existentes primero
        List<GestionEmpleados.Empleado> empleadosExistentes = GestionArchivosEmpleados.cargarEmpleados();

        if (!empleadosExistentes.isEmpty()) {
            System.out.println("Empleados existentes en el sistema:");
            for (GestionEmpleados.Empleado emp : empleadosExistentes) {
                System.out.printf("• %s - %s - $%,.2f (ID: %d)\n",
                        emp.nombre, emp.departamento, emp.salario, emp.id);
            }

            // Cargar empleados existentes al árbol en memoria
            for (GestionEmpleados.Empleado emp : empleadosExistentes) {
                GestionEmpleados.agregarEmpleadoDemo(emp);
            }
        } else {
            System.out.println("No hay empleados registrados. Agregando 10 empleados demo...");

            // Generar empleados demo y guardarlos directamente en el archivo
            List<GestionEmpleados.Empleado> empleadosDemo = new java.util.ArrayList<>();
            int ultimoID = GestionArchivosEmpleados.obtenerUltimoID();

            for (int i = 0; i < 10; i++) {
                String nombre = nombres[random.nextInt(nombres.length)];
                String departamento = departamentos[random.nextInt(departamentos.length)];
                double salario = 10000 + (random.nextInt(6000) / 100.0) * 100;

                int id = ultimoID + i + 1;
                GestionEmpleados.Empleado nuevoEmpleado =
                        new GestionEmpleados.Empleado(id, nombre, departamento, salario);

                empleadosDemo.add(nuevoEmpleado);
                // Agregar al árbol en memoria también
                GestionEmpleados.agregarEmpleadoDemo(nuevoEmpleado);

                System.out.printf("• %s - %s - $%,.2f (ID: %d)\n",
                        nombre, departamento, salario, id);
            }

            // Guardar todos los empleados demo en el archivo
            GestionArchivosEmpleados.guardarListaEmpleados(empleadosDemo);
            System.out.println("✓ 10 empleados demo agregados al sistema y guardados en CSV");
        }

        // Verificar que los empleados están cargados en memoria
        System.out.println("\n✓ Empleados cargados en memoria para asignación de tareas");
    }

    private static void crearTareas() {
        String[] tareasLimpieza = {
                "Limpieza general del local", "Limpiar baños", "Barrer y trapear pisos",
                "Limpiar mesas y sillas", "Organizar almacén", "Limpiar ventanas"
        };

        String[] tareasCocina = {
                "Preparar ingredientes del día", "Revisar inventario de cocina",
                "Preparar alimentos del menú", "Limpiar equipos de cocina",
                "Revisar fechas de caducidad", "Organizar refrigeradores"
        };

        String[] tareasBarista = {
                "Preparar café y bebidas", "Limpiar máquina de café",
                "Revisar inventario de insumos", "Atender capacitación",
                "Preparar promociones del día", "Organizar área de barra"
        };

        System.out.println("\n=== TAREAS EN COLA DE PRIORIDAD ===");
        // Crear tareas para la cola de prioridades (sin procesar)
        for (int i = 0; i < 14; i++) {
            String departamento;
            String[] tareasArray;

            int deptoRandom = random.nextInt(3);
            switch (deptoRandom) {
                case 0:
                    departamento = "Limpieza";
                    tareasArray = tareasLimpieza;
                    break;
                case 1:
                    departamento = "Cocina";
                    tareasArray = tareasCocina;
                    break;
                default:
                    departamento = "Barista";
                    tareasArray = tareasBarista;
                    break;
            }

            String descripcion = tareasArray[random.nextInt(tareasArray.length)];
            int prioridad = 1 + random.nextInt(5); // Prioridad 1-5
            int tiempo = 15 + random.nextInt(120); // 15-135 minutos

            // Obtener empleados del departamento para asignar
            List<GestionEmpleados.Empleado> empleados = GestionEmpleados.obtenerEmpleadosPorDepartamento(departamento);
            String empleadoAsignado;

            if (empleados.isEmpty()) {
                System.out.println("⚠️  No hay empleados en " + departamento + ", asignando manualmente...");
                empleadoAsignado = "Empleado por asignar";
            } else {
                // Seleccionar un empleado aleatorio del departamento
                GestionEmpleados.Empleado empleadoSeleccionado = empleados.get(random.nextInt(empleados.size()));
                empleadoAsignado = empleadoSeleccionado.nombre;
                System.out.println("✓ Asignando a: " + empleadoSeleccionado.nombre + " (" + departamento + ")");
            }

            // Usar la clase Tarea unificada
            GestionTareas.Tarea tarea = new GestionTareas.Tarea(descripcion, prioridad, tiempo, departamento, empleadoAsignado);
            GestionTareas.agregarTareaDemo(tarea);

            System.out.printf("• [P%d] %s - %d min - %s\n",
                    prioridad, descripcion, tiempo, departamento);
        }

        System.out.println("\n✓ Tareas demo creadas y listas para procesar");
        System.out.println("Tareas en cola de prioridad: " + GestionTareas.getColaTareas().size());
    }

    private static void generarTicketsYMesas() {
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

        System.out.println("\n=== GENERANDO 3 TICKETS COMPLETOS (MESAS CERRADAS) ===");

        // 3 MESAS CERRADAS (con tickets generados)
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- Mesa " + i + " (Cerrada con Ticket) ---");
            generarMesaCerrada(i, comentariosBebidasCalientes, comentariosBebidasFrias,
                    comentariosComida, comentariosPostres);
        }

        System.out.println("\n=== CREANDO 2 MESAS ABIERTAS (CON PEDIDOS PENDIENTES) ===");

        // 2 MESAS ABIERTAS (con pedidos pendientes)
        for (int i = 4; i <= 5; i++) {
            System.out.println("\n--- Mesa " + i + " (Abierta con Pedidos Pendientes) ---");
            generarMesaAbierta(i, comentariosBebidasCalientes, comentariosBebidasFrias,
                    comentariosComida, comentariosPostres);
        }

        System.out.println("\n✓ RESUMEN DE LA DEMO:");
        System.out.println("• 3 mesas cerradas con tickets generados");
        System.out.println("• 2 mesas abiertas con pedidos pendientes");
        System.out.println("• Total mesas activas: " + GestionMesas.mesas.size());
        System.out.println("✓ Demo completada - Las mesas están listas para gestión normal");
    }

    private static void generarMesaCerrada(int numeroMesa, String[] comentariosCalientes,
                                           String[] comentariosFrios, String[] comentariosComida,
                                           String[] comentariosPostres) {

        // CREAR MESA
        int numComensales = 1 + random.nextInt(4);
        GestionMesas.Mesa mesa = new GestionMesas.Mesa(numeroMesa, numComensales);
        GestionMesas.mesas.put(numeroMesa, mesa);
        System.out.println("✓ Mesa " + numeroMesa + " creada con " + numComensales + " comensales");

        // AGREGAR PEDIDOS
        int cantidadProductos = 2 + random.nextInt(3);
        agregarPedidosAMesa(mesa, cantidadProductos, comentariosCalientes, comentariosFrios,
                comentariosComida, comentariosPostres);

        // PROCESAR TODOS LOS PEDIDOS (simular que ya se atendieron)
        System.out.println("Procesando pedidos...");
        int pedidosProcesados = 0;
        while (!mesa.estaVacia()) {
            mesa.procesarPedido();
            pedidosProcesados++;
        }
        System.out.println("✓ " + pedidosProcesados + " pedidos procesados");

        // GENERAR Y GUARDAR TICKET
        MenuAlimentos.Ticket ticket = mesa.generarTicket();
        ticket.mostrarTicket();
        guardarTicketDemoArchivo(ticket);

        // CERRAR LA MESA (eliminarla del sistema)
        GestionMesas.mesas.remove(numeroMesa);
        System.out.println("✓ Mesa " + numeroMesa + " cerrada y ticket guardado");
    }

    private static void generarMesaAbierta(int numeroMesa, String[] comentariosCalientes,
                                           String[] comentariosFrios, String[] comentariosComida,
                                           String[] comentariosPostres) {

        // CREAR MESA
        int numComensales = 1 + random.nextInt(4);
        GestionMesas.Mesa mesa = new GestionMesas.Mesa(numeroMesa, numComensales);
        GestionMesas.mesas.put(numeroMesa, mesa);
        System.out.println("✓ Mesa " + numeroMesa + " creada con " + numComensales + " comensales");

        // AGREGAR PEDIDOS PENDIENTES (no procesar)
        int cantidadProductos = 2 + random.nextInt(3);
        agregarPedidosAMesa(mesa, cantidadProductos, comentariosCalientes, comentariosFrios,
                comentariosComida, comentariosPostres);

        System.out.println("✓ " + cantidadProductos + " pedidos agregados (pendientes de procesar)");
        System.out.println("✓ Mesa " + numeroMesa + " dejada abierta con " +
                mesa.obtenerCantidadPedidosPendientes() + " pedidos pendientes");
    }

    private static void agregarPedidosAMesa(GestionMesas.Mesa mesa, int cantidadProductos,
                                            String[] comentariosCalientes, String[] comentariosFrios,
                                            String[] comentariosComida, String[] comentariosPostres) {

        int totalProductos = MenuAlimentos.obtenerCantidadProductos();

        for (int i = 0; i < cantidadProductos; i++) {
            int idProducto = 1 + random.nextInt(totalProductos);
            MenuAlimentos.ProductoMenu producto = MenuAlimentos.buscarProductoPorID(idProducto);

            if (producto != null) {
                int cantidad = 1 + random.nextInt(2);
                String comentario = obtenerComentarioAleatorio(producto, comentariosCalientes,
                        comentariosFrios, comentariosComida, comentariosPostres);

                MenuAlimentos.PedidoMesa pedido = new MenuAlimentos.PedidoMesa(producto, comentario, cantidad);
                mesa.agregarPedido(pedido);

                System.out.println("  • " + cantidad + "x " + producto.nombre + " - $" +
                        String.format("%.2f", pedido.getSubtotal()));
            }
        }
    }

    private static void guardarTicketDemoArchivo(MenuAlimentos.Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        String fecha = LocalDate.now().format(formatter).toUpperCase();
        String nombreArchivo = "Tickets_" + fecha + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

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

        return "";
    }
}
