# Sistema de Gestión de Cafetería - Power Cafe

##  Descripción General
El **Sistema de Gestión de Cafetería Power Cafe** es una aplicación Java completa diseñada para administrar todos los aspectos operativos de una cafetería.  
Incluye módulos para **gestión de mesas, empleados, tareas, menú de alimentos y análisis de ventas**, implementando estructuras de datos y algoritmos avanzados.

---

##  Arquitectura del Sistema

### Main.java - Clase Principal
**Función:** Punto de entrada del sistema que coordina todos los módulos.  
**Características:**
- Menú principal unificado
- Navegación entre módulos
- Validación de entrada de usuario
- Gestión del flujo de la aplicación  

**Funcionalidades:**
- Interfaz de usuario en consola
- Validación de opciones numéricas
- Redirección a módulos específicos
- Control de ciclo de vida de la aplicación

---

### MenuAlimentos.java - Gestión del Menú
**Función:** Administra el catálogo de productos y pedidos.  
**Estructuras de Datos:**
- `ProductoMenu[]` → Array para almacenamiento eficiente
- `Queue<PedidoMesa>` → Cola FIFO para pedidos
- `LinkedHashMap` → Categorización de productos  

**Clases Principales:**
- `ProductoMenu`: Items del menú (ID, nombre, categoría, precio)
- `PedidoMesa`: Pedidos individuales con comentarios y cantidad
- `Ticket`: Generación de tickets con formato profesional  

**Funcionalidades:**
- Carga automática desde CSV  
- Búsqueda por ID y nombre  
- Categorización automática  
- Generación de tickets  

---

### GestionMesas.java - Gestión de Mesas
**Función:** Controla el estado y pedidos de las mesas.  
**Estructuras de Datos:**
- `Map<Integer, Mesa>` → Acceso rápido a mesas
- `Queue<PedidoMesa>` → Manejo de pedidos por estado  

**Clase Mesa:**
- Gestión de comensales
- Tres colas: Pendientes, Procesados, Cancelados
- Sistema FIFO para pedidos
- Generación automática de tickets  

**Funcionalidades:**
- Creación y eliminación de mesas
- Agregar pedidos
- Procesamiento secuencial
- Cancelación y modificación
- Persistencia en archivos

---

### GestionEmpleados.java - Gestión de Recursos Humanos
**Función:** Administra empleados con **árbol binario de búsqueda**.  
**Estructuras de Datos:**
- Árbol binario de búsqueda por ID  
- `NodoArbolEmpleados` con referencias izquierda/derecha  

**Algoritmos Implementados:**
- Inserción recursiva  
- Búsqueda por ID `O(log n)`  
- Recorrido inorden para reportes  
- Eliminación con sucesor  

**Funcionalidades:**
- CRUD completo de empleados  
- Cálculo recursivo de nómina  
- Filtrado por departamento  
- Persistencia en CSV  

---

### GestionTareas.java - Asignación de Tareas
**Función:** Gestiona tareas con **cola de prioridad**.  
**Estructuras de Datos:**
- `PriorityQueue<Tarea>` con comparador personalizado  

**Algoritmos:**
- Divide y Vencerás para distribución  
- Búsqueda recursiva en arrays  
- Cálculo de tiempos recursivos  

**Características de Tareas:**
- Prioridad (1-5, 1 = máxima)  
- Tiempo estimado  
- Departamento y responsable  

---

### EstadisticasVentas.java - Análisis de Ventas
**Función:** Procesa datos de ventas con algoritmos avanzados.  
**Algoritmos:**
- Quicksort Recursivo → Ordenamiento `O(n log n)`  
- Búsqueda Binaria → `O(log n)`  
- Divide y Vencerás para análisis  

**Funcionalidades:**
- Resumen diario  
- Totales recursivos  
- Ordenamiento por monto  
- Análisis de productos populares  

---

### GestionArchivosEmpleados.java - Persistencia
**Función:** Manejo de almacenamiento en CSV.  
**Operaciones:**
- Guardado y carga inicial  
- Actualización y eliminación  
- Gestión de IDs únicos  

---

### DemoCafeteria.java - Sistema de Demostración
**Función:** Genera datos de prueba.  
**Características:**
- Empleados demo  
- Tareas automáticas  
- Mesas con pedidos simulados  
- Tickets de ejemplo  

---

## Guía de Ejecución

### Prerrequisitos
- **Java JDK 8+**
- Archivo CSV del menú → `data/menu de cafeteria 1.0.csv`
- Permisos de escritura en el directorio

### Instalación y Ejecución

```bash
# Clonar o descargar los archivos .java
# Colocar el CSV en la carpeta data/

# Compilación
javac *.java

# Ejecución
java Main
