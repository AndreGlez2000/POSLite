# POSLite: Sistema de Punto de Venta Móvil (MVP)

**POSLite** es una aplicación nativa de Android diseñada para simplificar el proceso de cobro y gestión de inventario en pequeños comercios (tiendas de abarrotes, papelerías, puestos locales).

El objetivo principal de este MVP (Producto Mínimo Viable) es ofrecer una alternativa digital, rápida y **100% offline** al uso de cuaderno y calculadora, centrada en la agilidad operativa.

---

## 🎯 Objetivo del Proyecto

Desarrollar un sistema "Punto de Venta" ligero que permita:
1.  **Agilizar el Cobro:** Reducir el tiempo de atención al cliente mediante escaneo de códigos de barras.
2.  **Controlar Inventario:** Mantener un registro digital de productos y precios.
3.  **Historial de Ventas:** Eliminar el cálculo manual de cortes de caja mediante un registro automático de tickets.
4.  **Funcionamiento Offline:** Garantizar la operatividad total sin necesidad de conexión a internet.

---

## 🏗️ Arquitectura y Diseño Técnico

El proyecto sigue las mejores prácticas modernas de desarrollo en Android para asegurar escalabilidad y mantenimiento.

### Pila Tecnológica (Tech Stack)
* **Lenguaje:** Kotlin.
* **Entorno:** Android Studio (Minimum SDK: API 26 - Android 8.0).
* **Patrón de Arquitectura:** MVVM (Model-View-ViewModel).
* **Navegación:** Single-Activity Pattern (Una sola actividad contenedora, múltiples fragmentos).

### Componentes de Android Jetpack
* **Room Database:** Para la persistencia local de datos (SQLite abstracto).
* **Navigation Component:** Gestión del flujo entre pantallas y el `BottomAppBar`.
* **ViewModel & LiveData/StateFlow:** Gestión del estado de la UI y comunicación reactiva.
* **SharedViewModel:** Implementación específica para compartir el estado del "Carrito de Compras" entre fragmentos sin persistencia prematura.

### Librerías Externas y Hardware
* **Google ML Kit (Barcode Scanning):** Utilización de la cámara para lectura rápida de códigos SKU.
* **Haptic Feedback:** Integración con el motor de vibración para retroalimentación física al escanear.

---

## 📱 Módulos y Funcionalidades (Alcance del MVP)

### 1. Gestión de Inventario (Catálogo)
* Visualización de categorías en formato *Grid*.
* Visualización de productos filtrados por categoría.
* **CRUD Básico:** Capacidad de agregar nuevos productos (Nombre, Precio, SKU, Categoría) y nuevas categorías.

### 2. Motor de Venta (Carrito)
* **Modo Manual:** Selección de productos desde el catálogo visual.
* **Modo Scanner:**
    * Activación de cámara con botón flotante (FAB).
    * Detección automática de códigos de barras.
    * Lógica de *Cooldown* (enfriamiento) para evitar escaneos duplicados accidentales.
    * Feedback visual (Snackbar) y táctil (Vibración) al detectar un producto.
* **Edición:** Visualización de lista de venta con nombre, cantidad y precio. Cálculo automático del total.

### 3. Flujo de Cobro y Confirmación
Un proceso de dos pasos para asegurar la integridad de los datos:
1.  **Cálculo de Cambio:** `PaymentDialogFragment` para ingresar el monto recibido y calcular el cambio a devolver *antes* de cerrar la venta.
2.  **Confirmación (Commit):** Pantalla de resumen donde el usuario valida la transacción. Solo al confirmar ("Aceptar Venta") se guardan los datos en la base de datos (Room).

### 4. Historial de Tickets
* Consulta de ventas pasadas.
* Visualización de fecha, hora y monto total de cada transacción.
* Persistencia histórica de precios (el ticket guarda el precio del producto *al momento de la venta*, no el actual).

---

## 🗂️ Estructura de Base de Datos (Schema)

El sistema utiliza una base de datos relacional normalizada con las siguientes entidades principales:

1.  **Producto:** `sku` (PK), `nombre`, `precio`, `id_categoria` (FK).
2.  **Categoria:** `id` (PK), `nombre`.
3.  **Ticket:** `ticket_id` (PK), `fecha_hora`, `total`.
4.  **TicketItem:** `item_id` (PK), `ticket_id` (FK), `sku_producto` (FK), `cantidad`, `precio_momento`.

---

## 🚀 Roadmap Futuro (Post-MVP)

* Edición y eliminación de productos existentes.
* Reportes gráficos de ventas (Día/Semana/Mes).
* Exportación de historial a CSV/Excel.
* Soporte para impresoras térmicas Bluetooth (Tickets físicos).