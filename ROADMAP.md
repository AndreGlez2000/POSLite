# Roadmap del Proyecto

Este documento describe los planes de desarrollo futuro para POSLite. Priorizamos características que agregan valor a los dueños de pequeños negocios mientras mantenemos la simplicidad y capacidades offline de la app.

## Prioridad Alta (Próxima Versión v1.1)

Estas características son esenciales para la siguiente actualización mayor.

- [ ] **Dashboard y Analíticas**
  - **Resumen de Stock**: Indicadores visuales para stock bajo.
  - **Más Vendidos**: Lista de productos más vendidos por cantidad e ingresos.
  - **Gráficas de Ventas**: Gráficos mostrando tendencias de ventas (Diario, Semanal, Mensual).
  
- [ ] **Operaciones de Caja**
  - **Corte X**: Ver total de ventas del día actual sin cerrar el turno.
  - **Corte Z**: Cerrar el día/turno, reiniciar contadores diarios y generar reporte final.

- [ ] **Modo Admin vs. Cajero**
  - **Admin**: Acceso total a gestión de inventario (agregar/editar/eliminar), configuración e historial completo.
  - **Cajero**: Acceso restringido. Solo puede escanear artículos, procesar ventas y ver sus propios totales de turno. Sin acceso a modificar inventario o eliminar tickets pasados.

## Mejoras Futuras

### Inventario y Stock
- [ ] **Rastreo de Stock**: Deducir cantidad automáticamente cuando se hace una venta. Prevenir ventas si no hay stock (configuración opcional).
- [ ] **Importar/Exportar Masivo**: Importar productos vía CSV/Excel.
- [ ] **Alertas de Stock Bajo**: Notificaciones cuando los artículos caen por debajo de un umbral.

### Ventas y Cobro
- [ ] **Descuentos**: Aplicar porcentaje o monto fijo de descuento a artículos específicos o al total.
- [ ] **Promociones**: Lógica automática de "Compra 2 y Llévate 1 Gratis".
- [ ] **Teclado Personalizado**: Un teclado numérico grande estilo calculadora para ingresar rápidamente cantidades o precios para "Artículos Abiertos" (artículos no en inventario).

### Soporte de Hardware
- [ ] **Impresión Térmica**: Soporte para impresoras térmicas Bluetooth ESC/POS para imprimir recibos físicos.
- [ ] **Cajón de Dinero**: Señal para abrir el cajón de dinero vía conexión de impresora.

### Gestión de Datos
- [ ] **Respaldo y Restauración**: Crear archivo de respaldo de la base de datos para compartir o guardar en Google Drive.
- [ ] **Soporte Multi-Tienda**: Gestionar inventario para múltiples sucursales (requiere arquitectura de sincronización en la nube).

## Problemas Conocidos / Bugs
- Ninguno reportado actualmente.

---

*¿Tienes una solicitud de característica? ¡Abre un issue en GitHub!*
