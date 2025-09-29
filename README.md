Sistema de Gestión de Cafetería Power Cafe
Descripción General
El Sistema de Gestión de Cafetería Power Cafe es una aplicación Java completa diseñada para administrar todos los aspectos operativos de una cafetería. El sistema incluye módulos para gestión de mesas, empleados, tareas, menú de alimentos y análisis de ventas, implementando estructuras de datos y algoritmos avanzados.

Arquitectura del Sistema
Main.java - Clase Principal
Función: Punto de entrada del sistema que coordina todos los módulos.

Características:

Menú principal unificado

Navegación entre módulos

Validación de entrada de usuario

Gestión del flujo de la aplicación

Funcionalidades:

Interfaz de usuario consola

Validación de opciones numéricas

Redirección a módulos específicos

Control de ciclo de vida de la aplicación

MenuAlimentos.java - Gestión del Menú
Función: Administra el catálogo de productos y pedidos.

Estructuras de Datos:

ProductoMenu[] - Array para almacenamiento eficiente

Queue<PedidoMesa> - Cola FIFO para pedidos

LinkedHashMap - Para categorización de productos

Clases Principales:

ProductoMenu: Representa items del menú (ID, nombre, categoría, precio)

PedidoMesa: Gestiona pedidos individuales con comentarios y cantidad

Ticket: Genera y muestra tickets de venta con formato profesional

Funcionalidades:

Carga automática desde CSV

Búsqueda por ID y nombre

Categorización automática

Generación de tickets con formato

GestionMesas.java - Gestión de Mesas
Función: Controla el estado y pedidos de las mesas del establecimiento.

Estructuras de Datos:

Map<Integer, Mesa> - Tabla hash para acceso rápido a mesas

Queue<PedidoMesa> - Múltiples colas para diferentes estados

Clase Mesa:

Gestión de comensales

Tres colas: Pendientes, Procesados, Cancelados

Sistema FIFO para procesamiento

Generación automática de tickets

Funcionalidades:

Creación y eliminación de mesas

Agregar pedidos por comensal

Procesamiento secuencial de pedidos

Cancelación y modificación

Persistencia en archivos

GestionEmpleados.java - Gestión de Recursos Humanos
Función: Administra empleados usando árbol binario de búsqueda.

Estructuras de Datos:

Árbol Binario de Búsqueda por ID de empleado

NodoArbolEmpleados - Nodo del árbol con referencias izquierda/derecha

Algoritmos Implementados:

Inserción recursiva en árbol

Búsqueda por ID (O(log n) promedio)

Recorrido inorden para reportes ordenados

Eliminación con reemplazo por sucesor

Funcionalidades:

CRUD completo de empleados

Cálculo recursivo de nómina total

Búsqueda eficiente por ID

Filtrado por departamento

Persistencia en CSV

GestionTareas.java - Sistema de Asignación de Tareas
Función: Gestiona tareas del personal usando cola de prioridad.

Estructuras de Datos:

PriorityQueue<Tarea> - Cola priorizada por urgencia

Comparador personalizado por prioridad

Algoritmos Implementados:

Divide y Vencerás: Distribución óptima de tareas

Búsqueda recursiva en arrays

Cálculo recursivo de tiempos

Agrupamiento por prioridad

Características de Tareas:

Prioridad (1-5, 1=máxima)

Tiempo estimado

Departamento asignado

Empleado responsable

EstadisticasVentas.java - Análisis de Ventas
Función: Procesa y analiza datos de ventas con algoritmos avanzados.

Algoritmos Implementados:

Quicksort Recursivo: Ordenamiento eficiente O(n log n)

Búsqueda Binaria: Búsqueda rápida O(log n)

Divide y Vencerás: Análisis de tendencias

Procesamiento recursivo de archivos

Funcionalidades Analíticas:

Resumen diario de ventas

Cálculo de totales recursivos

Ordenamiento por monto

Búsqueda de transacciones

Análisis de productos populares

GestionArchivosEmpleados.java - Persistencia de Datos
Función: Maneja el almacenamiento persistente de empleados.

Operaciones:

Guardado en CSV

Carga inicial

Actualización de registros

Eliminación de base de datos

Gestión de IDs únicos

DemoCafeteria.java - Sistema de Demostración
Función: Genera datos de prueba para demostrar todas las funcionalidades.

Características:

Empleados demo con datos realistas

Tareas automáticas por departamento

Mesas con pedidos simulados

Tickets de ejemplo

Carga inicial del sistema

Guía de Ejecución
Prerrequisitos
Java JDK 8 o superior

Archivo CSV del menú (menu de cafeteria 1.0.csv)

Permisos de escritura en el directorio

Instalación y Ejecución
Preparar el Entorno

bash
# Clonar o descargar los archivos .java
# Asegurar que el CSV del menú esté en:
# data/menu de cafeteria 1.0.csv
Compilación

bash
# Compilar todos los archivos
javac *.java

# O compilar individualmente
javac Main.java MenuAlimentos.java GestionMesas.java GestionEmpleados.java GestionTareas.java EstadisticasVentas.java GestionArchivosEmpleados.java DemoCafeteria.java
Ejecución

bash
# Ejecutar la aplicación
java Main
Estructura de Archivos Recomendada
text
cafeteria/
├── Main.java
├── MenuAlimentos.java
├── GestionMesas.java
├── GestionEmpleados.java
├── GestionTareas.java
├── EstadisticasVentas.java
├── GestionArchivosEmpleados.java
├── DemoCafeteria.java
└── data/
    └── menu de cafeteria 1.0.csv
Flujo de Uso Recomendado
Ejecutar Demo Inicial (Opción 6)

Carga datos de prueba

Familiariza con todas las funcionalidades

Genera archivos de ejemplo

Gestión de Empleados (Opción 4)

Agregar personal inicial

Configurar departamentos

Establecer salarios

Gestión de Mesas (Opción 1)

Crear mesas activas

Tomar pedidos de clientes

Procesar y facturar

Análisis de Ventas (Opción 5)

Revisar reportes diarios

Analizar tendencias

Optimizar inventario

Características Técnicas
Algoritmos Implementados
Árboles Binarios: Gestión eficiente de empleados

Colas de Prioridad: Sistema de tareas urgentes

Quicksort: Ordenamiento de ventas

Búsqueda Binaria: Búsqueda eficiente en datos ordenados

Divide y Vencerás: Análisis de grandes conjuntos de datos

Estructuras de Datos
Arrays, Lists, Maps, Queues, PriorityQueues

Árboles binarios de búsqueda

Tablas hash para acceso rápido

Colas FIFO para procesamiento secuencial

Persistencia
Archivos CSV para empleados

Archivos de texto para tickets

Formato legible y recuperable

Módulos Principales
Módulo	Función	Algoritmo Principal
Empleados	Gestión de personal	Árbol Binario de Búsqueda
Tareas	Asignación de trabajo	Cola de Prioridad
Ventas	Análisis de datos	Quicksort + Búsqueda Binaria
Mesas	Atención a clientes	Colas FIFO
Menú	Gestión de productos	Arrays + Búsqueda Lineal
Solución de Problemas
Error: "Archivo CSV no encontrado"
Verificar que menu de cafeteria 1.0.csv esté en data/

Revisar permisos de lectura

Verificar encoding (UTF-8 recomendado)

Error de Compilación
Asegurar que todos los archivos .java estén en el mismo directorio

Verificar versión de Java (JDK 8+)

Compilar en orden de dependencias
