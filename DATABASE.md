# Esquema de la Base de Datos (Room) - POSLite

Este documento define la estructura de la base de datos local (Room/SQLite) de la aplicación.

## Tabla: `Producto`
Almacena cada producto individual que se puede vender.

| Columna | Tipo de Dato | Propósito | Notas |
| :--- | :--- | :--- | :--- |
| `sku` | `String` | **Primary Key.** Identificador único (código de barras). | |
| `nombre` | `String` | Nombre del producto (ej. "Coca-Cola 600ml"). | |
| `precio` | `Double` | Precio de venta actual. | |
| `id_categoria_fk`| `Int` | **Foreign Key** que apunta a `Categoria.id`. | `index = true` |

## Tabla: `Categoria`
Almacena las categorías para organizar los productos.

| Columna | Tipo de Dato | Propósito | Notas |
| :--- | :--- | :--- | :--- |
| `id` | `Int` | **Primary Key.** | `autoGenerate = true` |
| `nombre` | `String` | Nombre de la categoría (ej. "Bebidas"). | |

## Tabla: `Ticket`
Almacena el resumen global de cada venta completada. Se usa para la lista principal de "Tickets".

| Columna | Tipo de Dato | Propósito | Notas |
| :--- | :--- | :--- | :--- |
| `ticket_id` | `Int` | **Primary Key.** | `autoGenerate = true` |
| `fecha_hora` | `Long` | Timestamp (Unix Epoch) de cuándo se completó la venta. | |
| `total` | `Double` | Monto total final de la venta. | |

## Tabla: `TicketItem`
Almacena el desglose (los productos individuales) de cada `Ticket`.

| Columna | Tipo de Dato | Propósito | Notas |
| :--- | :--- | :--- | :--- |
| `item_id` | `Int` | **Primary Key.** | `autoGenerate = true` |
| `ticket_id_fk`| `Int` | **Foreign Key** que apunta a `Ticket.ticket_id`. | `index = true` |
| `sku_producto_fk`| `String`| **Foreign Key** que apunta a `Producto.sku`. | |
| `cantidad` | `Int` | Cuántas unidades de este producto se vendieron. | |
| `precio_en_ese_momento`| `Double`| El precio del producto al momento exacto de la venta (vital para historial). | |

## Relaciones (Queries)

Para obtener un ticket completo con sus productos, se usará una consulta con `JOIN` o, preferiblemente, una **Clase de Relación** en Room:

```kotlin
// Clase de datos para la consulta
data class TicketConItems(
    @Embedded val ticket: Ticket,
    @Relation(
        parentColumn = "ticket_id",
        entityColumn = "ticket_id_fk"
    )
    val items: List<TicketItem>
)

// En el DAO
@Transaction
@Query("SELECT * FROM Ticket WHERE ticket_id = :ticketId")
suspend fun getTicketConItems(ticketId: Int): TicketConItems