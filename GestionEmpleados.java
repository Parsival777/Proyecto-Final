import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class GestionEmpleados {
    private static NodoArbolEmpleados raiz = null;
    private static Scanner scanner = new Scanner(System.in);

    private static void cargarEmpleadosDesdeArchivo() {
        List<Empleado> empleados = GestionArchivosEmpleados.cargarEmpleados();
        for (Empleado empleado : empleados) {
            raiz = insertarRecursivo(raiz, empleado);
        }
    }

    public static void menuGestionEmpleados() {
        
        if (raiz == null) {
            cargarEmpleadosDesdeArchivo();
        }

        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE EMPLEADOS ===");
            System.out.println("1. Agregar empleado");
            System.out.println("2. Eliminar empleado");
            System.out.println("3. Mostrar empleados por departamento (inorden)");
            System.out.println("4. Buscar empleado");
            System.out.println("5. Calcular nómina total recursiva");
            System.out.println("6. Eliminar base de datos de empleados");
            System.out.println("0. Volver al menú principal");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 6);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                case 1:
                    agregarEmpleado();
                    break;
                case 2:
                    eliminarEmpleado();
                    break;
                case 3:
                    mostrarEmpleadosInorden();
                    break;
                case 4:
                    buscarEmpleado();
                    break;
                case 5:
                    calcularNominaTotal();
                    break;
                case 6:
                    eliminarBaseDatosEmpleados();
                    break;
            }
        } while (opcion != 0);
    }

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
            if (valor > 0) {
                return valor;
            } else {
                System.out.println("Error: El valor debe ser positivo.");
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

    private static String seleccionarDepartamento() {
        System.out.println("\nSeleccione el departamento:");
        System.out.println("1. Limpieza");
        System.out.println("2. Cocina");
        System.out.println("3. Barista");

        int opcion = obtenerEnteroEnRango("Opción: ", 1, 3);

        switch (opcion) {
            case 1:
                return "Limpieza";
            case 2:
                return "Cocina";
            case 3:
                return "Barista";
            default:
                return "General";
        }
    }

    private static void agregarEmpleado() {
        int id = GestionArchivosEmpleados.obtenerUltimoID() + 1;
        String nombre = obtenerStringNoVacio("Nombre del empleado: ");
        String departamento = seleccionarDepartamento();
        double salario = obtenerDoublePositivo("Salario mensual: ");

        Empleado nuevoEmpleado = new Empleado(id, nombre, departamento, salario);
        raiz = insertarRecursivo(raiz, nuevoEmpleado);

        
        GestionArchivosEmpleados.guardarEmpleado(nuevoEmpleado);

        System.out.println("✓ Empleado agregado correctamente. ID: " + id);
    }

    private static void eliminarEmpleado() {
        int id = obtenerEnteroValido("ID del empleado a eliminar: ");
        raiz = eliminarRecursivo(raiz, id);
        System.out.println("Empleado eliminado si existía.");

        
        actualizarArchivoDesdeArbol();
    }

    private static void actualizarArchivoDesdeArbol() {
        List<Empleado> empleados = new ArrayList<>();
        recolectarEmpleadosRecursivo(raiz, empleados);
        GestionArchivosEmpleados.guardarListaEmpleados(empleados);
    }

    private static void recolectarEmpleadosRecursivo(NodoArbolEmpleados nodo, List<Empleado> empleados) {
        if (nodo != null) {
            recolectarEmpleadosRecursivo(nodo.izquierdo, empleados);
            empleados.add(nodo.empleado);
            recolectarEmpleadosRecursivo(nodo.derecho, empleados);
        }
    }

    private static void eliminarBaseDatosEmpleados() {
        System.out.println("\n⚠️  ADVERTENCIA: Esta acción eliminará TODOS los empleados permanentemente.");
        System.out.print("¿Está seguro? (Escriba 'ELIMINAR' para confirmar): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equals("ELIMINAR")) {
            boolean eliminado = GestionArchivosEmpleados.eliminarBaseDatosEmpleados();
            if (eliminado) {
                raiz = null; 
                System.out.println("✓ Base de datos de empleados eliminada correctamente.");
            } else {
                System.out.println("No se encontró la base de datos de empleados o ya estaba vacía.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private static void mostrarEmpleadosInorden() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        System.out.println("\n=== LISTA DE EMPLEADOS (ORDENADOS POR ID) ===");
        System.out.println("╔══════╦══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  ID  ║                          INFORMACIÓN DEL EMPLEADO                    ║");
        System.out.println("╠══════╬══════════════════════════════════════════════════════════════════════╣");

        inordenRecursivo(raiz);

        System.out.println("╚══════╩══════════════════════════════════════════════════════════════════════╝");

        int totalEmpleados = contarEmpleadosRecursivo(raiz, 0);
        double nominaTotal = calcularNominaRecursivo(raiz, 0.0);
        System.out.printf("\nTotal de empleados: %d | Nómina total mensual: $%,.2f\n", totalEmpleados, nominaTotal);
    }

    private static void inordenRecursivo(NodoArbolEmpleados nodo) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo);
            System.out.printf("║ %4d ║ %-68s ║\n",
                    nodo.empleado.id,
                    String.format("%s           | %s        | Salario: $%,.2f",
                            nodo.empleado.nombre,
                            nodo.empleado.departamento,
                            nodo.empleado.salario));
            inordenRecursivo(nodo.derecho);
        }
    }

    private static int contarEmpleadosRecursivo(NodoArbolEmpleados nodo, int contador) {
        if (nodo == null) return contador;

        int conIzquierdo = contarEmpleadosRecursivo(nodo.izquierdo, contador);
        int conActual = conIzquierdo + 1;
        return contarEmpleadosRecursivo(nodo.derecho, conActual);
    }

    private static void buscarEmpleado() {
        int id = obtenerEnteroValido("ID del empleado a buscar: ");

        Empleado resultado = buscarRecursivo(raiz, id);
        if (resultado != null) {
            System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         EMPLEADO ENCONTRADO                          ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ ID: %-70d ║\n", resultado.id);
            System.out.printf("║ Nombre: %-65s ║\n", resultado.nombre);
            System.out.printf("║ Departamento: %-60s ║\n", resultado.departamento);
            System.out.printf("║ Salario: $%-64.2f ║\n", resultado.salario);
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    private static void calcularNominaTotal() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        double total = calcularNominaRecursivo(raiz, 0.0);
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         NÓMINA TOTAL MENSUAL                         ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total: $%,-61.2f║\n", total);
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static double calcularNominaRecursivo(NodoArbolEmpleados nodo, double acumulado) {
        if (nodo == null) return acumulado;

        double conIzquierdo = calcularNominaRecursivo(nodo.izquierdo, acumulado);
        double conActual = conIzquierdo + nodo.empleado.salario;
        return calcularNominaRecursivo(nodo.derecho, conActual);
    }

    
    public static List<Empleado> obtenerEmpleadosPorDepartamento(String departamento) {
        List<Empleado> empleadosDepartamento = new ArrayList<>();
        recolectarEmpleadosPorDepartamentoRecursivo(raiz, departamento, empleadosDepartamento);
        return empleadosDepartamento;
    }

    private static void recolectarEmpleadosPorDepartamentoRecursivo(NodoArbolEmpleados nodo, String departamento, List<Empleado> empleados) {
        if (nodo != null) {
            recolectarEmpleadosPorDepartamentoRecursivo(nodo.izquierdo, departamento, empleados);
            if (nodo.empleado.departamento.equalsIgnoreCase(departamento)) {
                empleados.add(nodo.empleado);
            }
            recolectarEmpleadosPorDepartamentoRecursivo(nodo.derecho, departamento, empleados);
        }
    }

    
    private static NodoArbolEmpleados insertarRecursivo(NodoArbolEmpleados nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoArbolEmpleados(empleado);
        }

        if (empleado.id < nodo.empleado.id) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, empleado);
        } else if (empleado.id > nodo.empleado.id) {
            nodo.derecho = insertarRecursivo(nodo.derecho, empleado);
        }

        return nodo;
    }

    private static NodoArbolEmpleados eliminarRecursivo(NodoArbolEmpleados nodo, int id) {
        if (nodo == null) return null;

        if (id < nodo.empleado.id) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, id);
        } else if (id > nodo.empleado.id) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, id);
        } else {
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;

            nodo.empleado = encontrarMinimo(nodo.derecho);
            nodo.derecho = eliminarRecursivo(nodo.derecho, nodo.empleado.id);
        }

        return nodo;
    }

    private static Empleado encontrarMinimo(NodoArbolEmpleados nodo) {
        if (nodo.izquierdo == null) return nodo.empleado;
        return encontrarMinimo(nodo.izquierdo);
    }

    private static Empleado buscarRecursivo(NodoArbolEmpleados nodo, int id) {
        if (nodo == null) return null;

        if (id == nodo.empleado.id) {
            return nodo.empleado;
        } else if (id < nodo.empleado.id) {
            return buscarRecursivo(nodo.izquierdo, id);
        } else {
            return buscarRecursivo(nodo.derecho, id);
        }
    }

    
    static class NodoArbolEmpleados {
        Empleado empleado;
        NodoArbolEmpleados izquierdo;
        NodoArbolEmpleados derecho;

        public NodoArbolEmpleados(Empleado empleado) {
            this.empleado = empleado;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    public static class Empleado {
        public int id;
        public String nombre;
        public String departamento;
        public double salario;

        public Empleado(int id, String nombre, String departamento, double salario) {
            this.id = id;
            this.nombre = nombre;
            this.departamento = departamento;
            this.salario = salario;
        }

        @Override
        public String toString() {
            return String.format("ID: %d | Nombre: %s | Depto: %s | Salario: $%.2f",
                    id, nombre, departamento, salario);
        }
    }

    
    public static void agregarEmpleadoDemo(Empleado empleado) {
        raiz = insertarRecursivo(raiz, empleado);
        GestionArchivosEmpleados.guardarEmpleado(empleado);
    }
}

