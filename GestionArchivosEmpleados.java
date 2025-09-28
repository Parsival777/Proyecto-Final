import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionArchivosEmpleados {
    private static final String ARCHIVO_EMPLEADOS = "empleados.csv";

    public static void guardarEmpleado(GestionEmpleados.Empleado empleado) {
        try (FileWriter fw = new FileWriter(ARCHIVO_EMPLEADOS, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("%d,%s,%s,%.2f\n",
                    empleado.id, empleado.nombre, empleado.departamento, empleado.salario);

        } catch (IOException e) {
            System.out.println("Error al guardar empleado: " + e.getMessage());
        }
    }

    public static List<GestionEmpleados.Empleado> cargarEmpleados() {
        List<GestionEmpleados.Empleado> empleados = new ArrayList<>();
        File archivo = new File(ARCHIVO_EMPLEADOS);

        if (!archivo.exists()) {
            return empleados;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    String departamento = datos[2];
                    double salario = Double.parseDouble(datos[3]);

                    GestionEmpleados.Empleado empleado =
                            new GestionEmpleados.Empleado(id, nombre, departamento, salario);
                    empleados.add(empleado);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar empleados: " + e.getMessage());
        }

        return empleados;
    }

    public static int obtenerUltimoID() {
        List<GestionEmpleados.Empleado> empleados = cargarEmpleados();
        if (empleados.isEmpty()) {
            return 1000; // ID inicial
        }

        // Encontrar el ID más alto
        int maxID = empleados.get(0).id;
        for (GestionEmpleados.Empleado emp : empleados) {
            if (emp.id > maxID) {
                maxID = emp.id;
            }
        }
        return maxID;
    }

    public static boolean eliminarBaseDatosEmpleados() {
        File archivo = new File(ARCHIVO_EMPLEADOS);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }

    public static void guardarListaEmpleados(List<GestionEmpleados.Empleado> empleados) {
        try (FileWriter fw = new FileWriter(ARCHIVO_EMPLEADOS);
             PrintWriter pw = new PrintWriter(fw)) {

            for (GestionEmpleados.Empleado empleado : empleados) {
                pw.printf("%d,%s,%s,%.2f\n",
                        empleado.id, empleado.nombre, empleado.departamento, empleado.salario);
            }

        } catch (IOException e) {
            System.out.println("Error al guardar lista de empleados: " + e.getMessage());
        }
    }
}