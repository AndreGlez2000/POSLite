# Contribuir a POSLite

Primero que nada, ¡gracias por considerar contribuir a POSLite! Es gente como tú la que hace de POSLite una gran herramienta para pequeños negocios.

## Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo puedo contribuir?](#cómo-puedo-contribuir)
- [Configuración de Desarrollo](#configuración-de-desarrollo)
- [Proceso de Pull Request](#proceso-de-pull-request)
- [Estándares de Código](#estándares-de-código)
- [Guía de Mensajes de Commit](#guía-de-mensajes-de-commit)

---

## Código de Conducta

Este proyecto y todos los que participan en él se rigen por el [Código de Conducta](CODE_OF_CONDUCT.md). Al participar, se espera que respetes este código. Por favor reporta cualquier comportamiento inaceptable a los mantenedores del proyecto.

---

## ¿Cómo puedo contribuir?

### Reportando Errores (Bugs)

Antes de crear reportes de error, por favor revisa los issues existentes para evitar duplicados. Al crear un reporte, incluye tantos detalles como sea posible:

- **Usa un título claro y descriptivo**.
- **Describe los pasos exactos para reproducir el problema**.
- **Provee ejemplos específicos** (capturas de pantalla, fragmentos de código).
- **Describe el comportamiento que observaste** y lo que esperabas que sucediera.
- **Incluye información del dispositivo**:
  - Versión de Android
  - Modelo del dispositivo
  - Versión de la App

**Plantilla para Reporte de Error:**
```markdown
**Descripción**
Una descripción clara del error.

**Pasos para Reproducir**
1. Ir a '...'
2. Clic en '...'
3. Desplazarse a '...'
4. Ver error

**Comportamiento Esperado**
Lo que esperabas que pasara.

**Comportamiento Real**
Lo que realmente pasó.

**Capturas de Pantalla**
Si aplica, agrega capturas.

**Información del Dispositivo**
- Dispositivo: [ej. Pixel 7]
- Versión de Android: [ej. Android 14]
- Versión de la App: [ej. 1.0]
```

### Sugiriendo Características

¡Las sugerencias de nuevas características son bienvenidas! Antes de sugerir:

1. **Revisa el [ROADMAP.md](ROADMAP.md)** para ver si ya está planeado.
2. **Busca en los issues existentes** para evitar duplicados.
3. **Provee un caso de uso claro** explicando por qué esta característica sería útil.

**Plantilla para Solicitud de Característica:**
```markdown
**Planteamiento del Problema**
Describe el problema que esta característica resolvería.

**Solución Propuesta**
Describe tu solución ideal.

**Alternativas Consideradas**
Cualquier solución alternativa que hayas pensado.

**Contexto Adicional**
Capturas, mockups o ejemplos de otras apps.
```

### Contribuciones de Código

¡Aceptamos contribuciones de código! Aquí es donde necesitamos ayuda:

- Corrección de errores.
- Nuevas características del roadmap.
- Mejoras de rendimiento.
- Mejoras de UI/UX.
- Mejoras en la documentación.
- Cobertura de pruebas.

---

## Configuración de Desarrollo

### Requisitos Previos

- Android Studio Flamingo (2022.2.1) o superior
- JDK 11 o superior
- Git

### Pasos de Configuración

1. **Haz un Fork del repositorio** en GitHub.

2. **Clona tu fork**
   ```bash
   git clone https://github.com/TU-USUARIO/POSLite.git
   cd POSLite
   ```

3. **Agrega el remoto upstream**
   ```bash
   git remote add upstream https://github.com/AndreGlez2000/POSLite.git
   ```

4. **Abre en Android Studio**
   - Abre Android Studio.
   - Selecciona "Open" y navega al proyecto.
   - Espera la sincronización de Gradle.

5. **Crea una rama**
   ```bash
   git checkout -b feature/nombre-de-tu-caracteristica
   ```

### Compilando el Proyecto

```bash
# Desde línea de comandos
./gradlew assembleDebug

# O usa el menú Build de Android Studio
```

### Ejecutando Pruebas

```bash
# Ejecutar pruebas unitarias
./gradlew test

# Ejecutar pruebas instrumentadas (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
```

---

## Proceso de Pull Request

1. **Actualiza tu fork** con los últimos cambios del upstream
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Haz tus cambios** en tu rama de característica
   - Escribe código limpio y legible.
   - Sigue los estándares de código descritos abajo.
   - Agrega comentarios para lógica compleja.
   - Actualiza la documentación si es necesario.

3. **Prueba tus cambios**
   - Ejecuta la app en emuladores de teléfono y tablet.
   - Prueba diferentes tamaños de pantalla.
   - Verifica que no haya cierres inesperados (crashes).
   - Revisa fugas de memoria.

4. **Haz commit de tus cambios** con mensajes significativos
   ```bash
   git add .
   git commit -m "feat: agregar seleccion de formato de codigo de barras"
   ```

5. **Haz Push a tu fork**
   ```bash
   git push origin feature/nombre-de-tu-caracteristica
   ```

6. **Abre un Pull Request**
   - Ve al repositorio original en GitHub.
   - Clic en "New Pull Request".
   - Selecciona tu fork y rama.
   - Llena la plantilla del PR.

### Lista de Verificación del Pull Request

- [ ] El código sigue el estilo del proyecto.
- [ ] Auto-revisión del código completada.
- [ ] Comentarios agregados para código complejo.
- [ ] Documentación actualizada (si aplica).
- [ ] No se introdujeron nuevas advertencias (warnings).
- [ ] Probado en diseños de teléfono y tablet.
- [ ] Capturas/GIFs agregados para cambios de UI.
- [ ] Enlazado a issue relacionado (si aplica).

---

## Estándares de Código

### Guía de Estilo Kotlin

Seguimos las [Convenciones de Código Oficiales de Kotlin](https://kotlinlang.org/docs/coding-conventions.html).

**Puntos Clave:**

- **Nombres**:
  - Clases: `PascalCase`
  - Funciones/variables: `camelCase`
  - Constantes: `UPPER_SNAKE_CASE`
  - Archivos de layout: `snake_case`

- **Formato**:
  - 4 espacios para indentación (no tabs).
  - Longitud máxima de línea: 120 caracteres.
  - Usar comas finales en listas multilínea.

- **Arquitectura**:
  - Seguir patrón MVVM.
  - Patrón Repository para acceso a datos.
  - Usar ViewModels para estado de UI.
  - Fragments para UI, no Activities.

### Estructura de Archivo

```kotlin
// Orden de elementos en una clase
class EjemploFragment : Fragment() {
    // Companion object
    companion object { }
    
    // Propiedades (públicas primero, luego privadas)
    private lateinit var binding: FragmentEjemploBinding
    private val viewModel: EjemploViewModel by viewModels()
    
    // Métodos de ciclo de vida (en orden)
    override fun onCreateView(...) { }
    override fun onViewCreated(...) { }
    override fun onDestroyView() { }
    
    // Otros métodos (públicos primero, luego privados)
    private fun configurarUI() { }
    private fun observarViewModel() { }
}
```

### Cambios en Base de Datos

Si modificas el esquema de la base de datos:

1. Incrementa el número de versión en `AppDatabase.kt`.
2. Provee una estrategia de migración o permite migración destructiva para MVP.
3. Actualiza [DATABASE.md](DATABASE.md) con los cambios de esquema.

### Guías de UI/UX

- **Material Design 3**: Seguir lineamientos de Material Design.
- **Accesibilidad**: Todos los elementos interactivos deben tener descripciones de contenido.
- **Responsivo**: Probar en teléfonos y tablets.
- **Modo Oscuro**: Asegurar compatibilidad (si se implementa).

---

## Guía de Mensajes de Commit

Usamos [Conventional Commits](https://www.conventionalcommits.org/) para un historial de cambios claro.

### Formato

```
<tipo>(<alcance>): <asunto>

<cuerpo>

<pie>
```

### Tipos

- `feat`: Nueva característica
- `fix`: Corrección de error
- `docs`: Cambios en documentación
- `style`: Cambios de estilo de código (formato, sin cambio lógico)
- `refactor`: Refactorización de código
- `perf`: Mejoras de rendimiento
- `test`: Agregar o actualizar pruebas
- `chore`: Tareas de mantenimiento

### Ejemplos

```bash
feat(carrito): agregar botones de incremento de cantidad

Se agregaron botones +/- a los items del carrito para facilitar
el ajuste de cantidad sin usar el teclado.

Cierra #42

fix(escaner): prevenir escaneos duplicados con enfriamiento

Implementado enfriamiento de 2 segundos entre escaneos para
prevenir adiciones accidentales de productos duplicados.

Arregla #38

docs(readme): actualizar instrucciones de instalacion

Actualizado README con pasos mas claros para configuracion
en Android Studio y agregada seccion de solucion de problemas.
```

---

## ¿Preguntas?

- **Preguntas Generales**: Usa [GitHub Discussions](https://github.com/AndreGlez2000/POSLite/discussions)
- **Reportes de Error**: Abre un [Issue](https://github.com/AndreGlez2000/POSLite/issues)
- **Temas de Seguridad**: Contacta a los mantenedores en privado.

---

**¡Gracias por contribuir a POSLite!**
