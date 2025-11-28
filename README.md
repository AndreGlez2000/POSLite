# POSLite

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-red.svg)

**Un sistema de Punto de Venta (POS) ligero y 100% offline para pequeños negocios.**

POSLite es una aplicación nativa de Android diseñada para simplificar las ventas y la gestión de inventario en pequeños comercios como tiendas de abarrotes, papelerías, neverías y puestos locales. Construida con arquitectura moderna de Android, ofrece una experiencia rápida, intuitiva y que no requiere conexión a internet.

---

## Características

### Funcionalidades Actuales (v1.0 - MVP)

- **Gestión de Inventario**
  - Crear y administrar categorías de productos.
  - Agregar productos con SKU, nombre, precio y categoría.
  - Validación de SKU para evitar duplicados.
  - Eliminar productos y categorías con diálogos de confirmación.

- **Flujo de Venta Inteligente**
  - Selección manual de productos desde un catálogo visual.
  - Escaneo rápido de códigos de barras usando la cámara del dispositivo (Google ML Kit).
  - Carrito de compras en tiempo real con ajuste de cantidades.
  - Cálculo automático del total.
  - Retroalimentación visual y táctil al escanear artículos.

- **Proceso de Cobro**
  - Entrada del monto de pago con cálculo de cambio.
  - Confirmación de la transacción antes de guardar en la base de datos.
  - Generación de recibo con detalles de la transacción.

- **Diseño Responsivo**
  - **Modo Teléfono**: Navegación inferior con botón de acción flotante (FAB) dinámico.
  - **Modo Tablet**: Pantalla dividida con navegación lateral y vista de carrito persistente.
  - Diseños adaptables para diferentes tamaños de pantalla.

- **Historial de Ventas**
  - Ver todas las transacciones completadas.
  - Vista detallada del recibo con productos desglosados.
  - Preservación histórica de precios (el ticket guarda el precio al momento de la venta).

- **100% Offline**
  - Todos los datos se almacenan localmente usando base de datos Room (SQLite).
  - No requiere conexión a internet.
  - Rendimiento rápido con persistencia de datos local.

---

## Capturas de Pantalla

> **Nota**: Toma capturas de las siguientes pantallas y agrégalas aquí:

### Diseño en Teléfono

![Pantalla de Categorías](docs/screenshots/phone_categories.png)
*Categorías de productos en diseño de cuadrícula*

![Pantalla de Productos](docs/screenshots/phone_products.png)
*Lista de productos filtrada por categoría*

![Pantalla del Carrito](docs/screenshots/phone_cart.png)
*Carrito de compras con diseño estilo ticket*

![Escáner de Códigos de Barras](docs/screenshots/phone_scanner.png)
*Escaneo de códigos de barras en tiempo real*

![Historial de Recibos](docs/screenshots/phone_receipts.png)
*Historial de transacciones*

### Diseño en Tablet

![Vista Principal Tablet](docs/screenshots/tablet_main.png)
*Diseño de pantalla dividida con barra lateral y carrito persistente*

---

## Comenzando

### Requisitos Previos

- **Android Studio**: Flamingo (2022.2.1) o superior
- **SDK Mínimo**: Android 7.0 (API nivel 24)
- **SDK Objetivo**: Android 14 (API nivel 36)
- **Kotlin**: 1.9+

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tuusuario/POSLite.git
   cd POSLite
   ```

2. **Abrir en Android Studio**
   - Abre Android Studio.
   - Selecciona "Open" y navega al proyecto clonado.
   - Espera a que termine la sincronización de Gradle.

3. **Compilar y Ejecutar**
   - Conecta un dispositivo Android o inicia un emulador.
   - Haz clic en "Run" (▶️) o presiona `Shift + F10`.
   - La aplicación se instalará y ejecutará automáticamente.

---

## Pila Tecnológica (Tech Stack)

### Tecnologías Principales
- **Lenguaje**: Kotlin
- **Min SDK**: API 24 (Android 7.0)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Patrón UI**: Single-Activity con Navigation Component

### Componentes Android Jetpack
- **Room Database**: Persistencia de datos local con abstracción SQLite.
- **ViewModel & LiveData**: Gestión reactiva del estado de la UI.
- **Navigation Component**: Navegación entre fragmentos y deep linking.
- **ViewBinding**: Acceso seguro a vistas.

### Librerías Externas
- **Google ML Kit Barcode Scanning** (v17.3.0): Detección de códigos de barras basada en cámara.
- **CameraX** (v1.5.1): Implementación moderna de cámara.
- **iText7 PDF** (v7.2.5): Generación de recibos PDF.
- **Kotlin Coroutines** (v1.7.3): Programación asíncrona.

### UI/UX
- **Material Design 3**: Componentes modernos de UI de Android.
- **Bottom App Bar**: Navegación con integración FAB.
- **RecyclerView**: Renderizado eficiente de listas.
- **Drawables Personalizados**: Diseño de recibo estilo ticket.

---

## Estructura del Proyecto

```
app/src/main/java/com/example/testlite/
├── database/
│   ├── entities/          # Entidades Room (Product, Category, Ticket, TicketItem)
│   ├── dao/               # Objetos de Acceso a Datos (DAOs)
│   ├── relations/         # Relaciones de base de datos y joins
│   ├── AppDatabase.kt     # Configuración de la base de datos
│   └── Mappers.kt         # Mappers de Entidad a data class
├── repository/
│   ├── InventoryRepository.kt
│   └── TicketRepository.kt
├── MainActivity.kt        # Activity contenedor único
├── CartViewModel.kt       # Estado compartido del carrito
├── InventoryViewModel.kt  # Operaciones de productos/categorías
├── TicketViewModel.kt     # Historial de ventas
├── fragments/             # Pantallas de UI
│   ├── CartFragment.kt
│   ├── BarcodeScannerFragment.kt
│   ├── CategoriesFragment.kt
│   ├── ProductsFragment.kt
│   └── ReceiptFragment.kt
└── adapters/              # Adaptadores para RecyclerView
```

Para documentación detallada de la arquitectura, ver [ARCHITECTURE.md](ARCHITECTURE.md).

---

## Esquema de Base de Datos

POSLite utiliza una base de datos relacional normalizada con cuatro tablas principales:

- **Producto**: SKU, nombre, precio, FK categoría.
- **Categoria**: ID, nombre.
- **Ticket**: ID transacción, fecha/hora, monto total.
- **TicketItem**: Artículos de línea con producto, cantidad, precio histórico.

Ver [DATABASE.md](DATABASE.md) para el esquema detallado y relaciones.

---

## Roadmap

### Versión 1.1 (Próximo Lanzamiento)
- **Modo Admin/Cajero**: Control de acceso basado en roles.
  - Admin: Acceso total a inventario, configuración y reportes.
  - Cajero: Modo solo ventas con configuración restringida.
- **Dashboard y Analíticas**:
  - Niveles de stock con alertas de inventario bajo.
  - Productos más vendidos.
  - Gráficas de ventas (tendencias diarias/semanales/mensuales).
- **Operaciones de Caja**:
  - "Corte X": Ver total de ventas del día sin cerrar turno.
  - "Corte Z": Cierre de día/turno con totales y reinicio de contadores.

### Mejoras Futuras
- **Respaldo y Restauración**: Exportar/importar base de datos a la nube o almacenamiento local.
- **Soporte Multi-Tienda**: Gestionar inventario para múltiples sucursales.
- **Control de Stock**: Rastreo de inventario con deducciones automáticas.
- **Soporte Impresora Térmica**: Impresión de tickets vía Bluetooth.
- **Descuentos y Promociones**: Descuentos por porcentaje, ofertas 2x1.
- **Teclado Numérico Personalizado**: Entrada más rápida de cantidades.

Ver [ROADMAP.md](ROADMAP.md) para la planificación detallada de características.

---

## Contribuir

¡Las contribuciones son bienvenidas! Ya sea corrigiendo errores, agregando características o mejorando la documentación, apreciamos tu ayuda.

Por favor lee [CONTRIBUTING.md](CONTRIBUTING.md) para detalles sobre nuestro código de conducta y el proceso para enviar pull requests.

### Inicio Rápido para Colaboradores

1. Haz un Fork del repositorio.
2. Crea una rama para tu característica (`git checkout -b feature/caracteristica-increible`).
3. Haz commit de tus cambios (`git commit -m 'Agregar caracteristica increible'`).
4. Haz Push a la rama (`git push origin feature/caracteristica-increible`).
5. Abre un Pull Request.

---

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

## Agradecimientos

- **Google ML Kit** por las capacidades de escaneo de códigos de barras.
- Equipo de Android Jetpack por los componentes de arquitectura moderna.
- La comunidad open-source por la inspiración y mejores prácticas.

---

## Contacto y Soporte

- **Issues**: [GitHub Issues](https://github.com/tuusuario/POSLite/issues)
- **Discusiones**: [GitHub Discussions](https://github.com/tuusuario/POSLite/discussions)
- **Autor**: Andre Gonzalez

---

**Hecho para dueños de pequeños negocios**