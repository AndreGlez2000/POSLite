# POSLite: Punto de Venta Ligero para Android

POSLite es una aplicación de Punto de Venta (POS) nativa de Android diseñada para ser rápida, ligera y funcionar 100% offline. Es un MVP (Producto Mínimo Viable) enfocado en las necesidades básicas de pequeños negocios y comerciantes.

## 🌟 Características Principales (MVP)

* **Gestión de Inventario Local:** Creación de Productos y Categorías directamente en el dispositivo.
* **Flujo de Venta Rápido:** Interfaz optimizada para añadir productos al carrito.
* **Escaneo de Códigos de Barra:** Usa la cámara del dispositivo (vía ML Kit) para escanear y añadir productos instantáneamente.
* **Persistencia Local:** Todos los datos (inventario, ventas) se guardan en una base de datos SQLite (Room) en el dispositivo.
* **Historial de Tickets:** Revisa un historial de todas las ventas completadas.

## 🛠️ Pila Tecnológica (Tech Stack)

* **Lenguaje:** Kotlin
* **Arquitectura:** MVVM (Model-View-ViewModel) + Single-Activity
* **Componentes de Jetpack:**
    * **Room:** Para la base de datos local.
    * **ViewModel:** Para gestionar la lógica y el estado de la UI (incluyendo un `SharedViewModel` para el carrito).
    * **LiveData / StateFlow:** Para comunicación reactiva entre la UI y el ViewModel.
    * **Navigation Component:** Para gestionar el flujo de navegación entre Fragments.
* **UI:** Material Design 3 (con XML y Vistas)
    * `BottomAppBar` + `FloatingActionButton` (FAB)
    * `RecyclerView`
    * `DialogFragment`
* **APIs Externas:**
    * **Google ML Kit:** Para el escaneo de códigos de barra.

## 🚀 Cómo Empezar

1.  Clona este repositorio.
2.  Abre el proyecto en Android Studio (versión recomendada: Flamingo o superior).
3.  Sincroniza Gradle para descargar todas las dependencias listadas en `build.gradle`.
4.  Ejecuta la aplicación en un emulador o un dispositivo físico.