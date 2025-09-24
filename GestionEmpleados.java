import java.util.Scanner;

public class GestionEmpleados {
    private static NodoArbolEmpleados raiz = null;
    private static Scanner scanner = new Scanner(System.in);

    // Nueva funcionalidad: Gestión de tareas para empleados
    private static class TareaEmpleado {
        String descripcion;
        int prioridad; // 1-5, donde 1 es más alta
        int duracionEstimada; // en minutos
        String empleadoAsignado;

        public TareaEmpleado(String descripcion, int prioridad, int duracion, String empleado) {
            this.descripcion = descripcion;
            this.prioridad = prioridad;
            this.duracionEstimada = duracion;
            this.empleadoAsignado = empleado;
        }

        @Override
        public String toString() {
            return String.format("Tarea: %s | Prioridad: %d | Duración: %d min | Empleado: %s",
                    descripcion, prioridad, duracionEstimada, empleadoAsignado);
        }
    }

    // Lista simple de tareas
    private static TareaEmpleado[] tareas = new TareaEmpleado[100];
    private static int contadorTareas = 0;

    public static void menuGestionEmpleados() {
        System.out.println("\n=== GESTIÓN DE EMPLEADOS Y TAREAS ===");
        System.out.println("1. Gestión de empleados (Árbol binario)");
        System.out.println("2. Gestión de tareas (Recursividad)");
        System.out.println("3. Distribución de tareas (Divide y Vencerás)");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        String input = scanner.nextLine().trim();
        switch (input) {
            case "0":
                return;
            case "1":
                menuEmpleados();
                break;
            case "2":
                menuTareas();
                break;
            case "3":
                distribuirTareasDivideVenceras();
                break;
            default:
                System.out.println("Opción no válida.");
        }
        menuGestionEmpleados();
    }

    private static void menuEmpleados() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE EMPLEADOS (ÁRBOL BINARIO) ===");
            System.out.println("1. Agregar empleado");
            System.out.println("2. Eliminar empleado");
            System.out.println("3. Mostrar empleados por departamento (inorden)");
            System.out.println("4. Buscar empleado");
            System.out.println("5. Calcular nómina total recursiva");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine().trim());

            switch (opcion) {
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
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void menuTareas() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE TAREAS (RECURSIVIDAD) ===");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Mostrar todas las tareas");
            System.out.println("3. Calcular tiempo total de tareas");
            System.out.println("4. Buscar tarea por empleado");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine().trim());

            switch (opcion) {
                case 1:
                    agregarTarea();
                    break;
                case 2:
                    mostrarTareasRecursivo(0);
                    break;
                case 3:
                    calcularTiempoTotalRecursivo(0, 0);
                    break;
                case 4:
                    buscarTareasPorEmpleado();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void agregarTarea() {
        System.out.print("Descripción de la tarea: ");
        String descripcion = scanner.nextLine().trim();

        if (descripcion.isEmpty()) {
            System.out.println("La descripción no puede estar vacía.");
            return;
        }

        System.out.print("Prioridad (1-5, donde 1 es más alta): ");
        int prioridad = Integer.parseInt(scanner.nextLine().trim());

        if (prioridad < 1 || prioridad > 5) {
            System.out.println("La prioridad debe estar entre 1 y 5.");
            return;
        }

        System.out.print("Duración estimada (minutos): ");
        int duracion = Integer.parseInt(scanner.nextLine().trim());

        if (duracion <= 0) {
            System.out.println("La duración debe ser positiva.");
            return;
        }

        System.out.print("Empleado asignado: ");
        String empleado = scanner.nextLine().trim();

        if (empleado.isEmpty()) {
            System.out.println("El empleado no puede estar vacío.");
            return;
        }

        if (contadorTareas < tareas.length) {
            tareas[contadorTareas++] = new TareaEmpleado(descripcion, prioridad, duracion, empleado);
            System.out.println("✓ Tarea agregada correctamente.");
        } else {
            System.out.println("No se pueden agregar más tareas. Límite alcanzado.");
        }
    }

    private static void mostrarTareasRecursivo(int index) {
        if (index >= contadorTareas) {
            if (contadorTareas == 0) {
                System.out.println("No hay tareas registradas.");
            }
            return;
        }

        System.out.println((index + 1) + ". " + tareas[index]);
        mostrarTareasRecursivo(index + 1);
    }

    private static void calcularTiempoTotalRecursivo(int index, int total) {
        if (index >= contadorTareas) {
            System.out.println("Tiempo total estimado para " + contadorTareas + " tareas: " + total + " minutos");
            return;
        }

        calcularTiempoTotalRecursivo(index + 1, total + tareas[index].duracionEstimada);
    }

    private static void buscarTareasPorEmpleado() {
        System.out.print("Nombre del empleado: ");
        String empleado = scanner.nextLine().trim();

        System.out.println("Tareas asignadas a " + empleado + ":");
        int encontradas = buscarTareasRecursivo(empleado, 0, 0);

        if (encontradas == 0) {
            System.out.println("No se encontraron tareas para este empleado.");
        } else {
            System.out.println("Total encontradas: " + encontradas + " tareas");
        }
    }

    private static int buscarTareasRecursivo(String empleado, int index, int contador) {
        if (index >= contadorTareas) {
            return contador;
        }

        if (tareas[index].empleadoAsignado.equalsIgnoreCase(empleado)) {
            System.out.println("- " + tareas[index].descripcion + " (" + tareas[index].duracionEstimada + " min)");
            return buscarTareasRecursivo(empleado, index + 1, contador + 1);
        }

        return buscarTareasRecursivo(empleado, index + 1, contador);
    }

    // Divide y Vencerás: Distribución equitativa de tareas
    private static void distribuirTareasDivideVenceras() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas para distribuir.");
            return;
        }

        System.out.println("=== DISTRIBUCIÓN DE TAREAS (DIVIDE Y VENCERÁS) ===");
        System.out.println("Total de tareas a distribuir: " + contadorTareas);
        distribuirTareasRecursivo(0, contadorTareas - 1, 1);
    }

    private static void distribuirTareasRecursivo(int inicio, int fin, int nivel) {
        if (inicio >= fin) {
            System.out.printf("Nivel %d: Tarea individual: %s\n", nivel, tareas[inicio].descripcion);
            return;
        }

        int medio = (inicio + fin) / 2;

        System.out.printf("Nivel %d:\n", nivel);
        System.out.printf("  Grupo 1 (tareas %d-%d): %d tareas\n", inicio + 1, medio + 1, medio - inicio + 1);
        System.out.printf("  Grupo 2 (tareas %d-%d): %d tareas\n", medio + 2, fin + 1, fin - medio);

        // Calcular tiempo total de cada grupo
        int tiempoGrupo1 = calcularTiempoGrupo(inicio, medio, 0);
        int tiempoGrupo2 = calcularTiempoGrupo(medio + 1, fin, 0);

        System.out.printf("  Tiempo Grupo 1: %d min | Tiempo Grupo 2: %d min\n", tiempoGrupo1, tiempoGrupo2);

        // Dividir recursivamente
        distribuirTareasRecursivo(inicio, medio, nivel + 1);
        distribuirTareasRecursivo(medio + 1, fin, nivel + 1);
    }

    private static int calcularTiempoGrupo(int inicio, int fin, int acumulado) {
        if (inicio > fin) {
            return acumulado;
        }
        return calcularTiempoGrupo(inicio + 1, fin, acumulado + tareas[inicio].duracionEstimada);
    }

    // ========== MÉTODOS ORIGINALES DE GESTIÓN DE EMPLEADOS ==========

    private static void agregarEmpleado() {
        System.out.print("ID del empleado: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Nombre del empleado: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Departamento: ");
        String departamento = scanner.nextLine().trim();

        System.out.print("Salario mensual: ");
        double salario = Double.parseDouble(scanner.nextLine().trim());

        Empleado nuevoEmpleado = new Empleado(id, nombre, departamento, salario);
        raiz = insertarRecursivo(raiz, nuevoEmpleado);
        System.out.println("✓ Empleado agregado correctamente.");
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

    private static void eliminarEmpleado() {
        System.out.print("ID del empleado a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        raiz = eliminarRecursivo(raiz, id);
        System.out.println("Empleado eliminado si existía.");
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

    private static void mostrarEmpleadosInorden() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        System.out.println("Empleados (ordenados por ID):");
        inordenRecursivo(raiz);
    }

    private static void inordenRecursivo(NodoArbolEmpleados nodo) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo);
            System.out.println(nodo.empleado);
            inordenRecursivo(nodo.derecho);
        }
    }

    private static void buscarEmpleado() {
        System.out.print("ID del empleado a buscar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Empleado resultado = buscarRecursivo(raiz, id);
        if (resultado != null) {
            System.out.println("✓ Empleado encontrado: " + resultado);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    private static Empleado buscarRecursivo(NodoArbolEmpleados nodo, int id) {
        if (nodo == null) return null;

        if (id == nodo.empleado.id) return nodo.empleado;
        if (id < nodo.empleado.id) return buscarRecursivo(nodo.izquierdo, id);
        return buscarRecursivo(nodo.derecho, id);
    }

    private static void calcularNominaTotal() {
        if (raiz == null) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        double total = calcularNominaRecursivo(raiz, 0.0);
        System.out.println("Nómina total mensual: $" + String.format("%.2f", total));
    }

    private static double calcularNominaRecursivo(NodoArbolEmpleados nodo, double acumulado) {
        if (nodo == null) return acumulado;

        double conIzquierdo = calcularNominaRecursivo(nodo.izquierdo, acumulado);
        double conActual = conIzquierdo + nodo.empleado.salario;
        return calcularNominaRecursivo(nodo.derecho, conActual);
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

    static class Empleado {
        int id;
        String nombre;
        String departamento;
        double salario;

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
}