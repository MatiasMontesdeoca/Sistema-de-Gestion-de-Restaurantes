# Sistema de Gestión de Restaurantes

## Descripción

El **Sistema de Gestión de Restaurantes** es una aplicación desarrollada en Java para administrar el funcionamiento básico de un restaurante, tanto en la atención al cliente como en la gestión interna del personal y la operativa diaria. El sistema está dividido en varios módulos, cada uno encargado de una función específica:

### Gestión de clientes
- Registro de nuevos clientes con validación de cédula (10 dígitos), teléfono y correo electrónico.
- Consulta de clientes registrados.
- Asignación de clientes a mesas disponibles.
- Control automático de visitas para otorgar descuentos por fidelidad.

### Gestión de meseros
- Registro de meseros.
- Asignación y retiro de mesas.
- Consulta de la carga de trabajo de cada mesero.
- Visualización del historial de mesas atendidas.

### Gestión de mesas y reservas
- Registro de nuevas mesas y consulta de su estado.
- Administración de reservas.
- Cambio de estado de las mesas (**Libre**, **Ocupada** y **Reservada**).
- Consulta de disponibilidad (al consultar disponibilidad de una mesa reservada, esta pasa a estar libre y anula la reserva activa).

### Gestión del menú
- Registro de nuevos platos clasificados por categoría:
  - Entrada
  - Plato fuerte
  - Postre
  - Bebida
- Modificación de platos (precio, disponibilidad, categoría).
- Consulta del menú completo.
- Búsqueda de platos por nombre.

### Gestión de pedidos
- Generación de pedidos con numeración autoincremental dinámica (`P1`, `P2`, ...) basada en los pedidos almacenados.
- Selección intuitiva de platos mediante menú numerado interactivo agrupado por categorías (**Entradas**, **Plato Fuerte**, **Postre**, **Bebida**), mostrando precio y disponibilidad.
- Modificación de pedidos (agregar/eliminar platos por número, cambiar estados).
- Búsqueda de pedidos activos por número de mesa.

### Gestión de pagos y facturación
- Registro de pagos mediante diferentes métodos (**Efectivo**, **Tarjeta**, **Transferencia**).
- Aplicación de descuentos por fidelidad (visitas acumuladas) o cupones.
- Generación y consulta de facturas.

### Generación de reportes
- Reporte de ventas totales y facturación.
- Reporte de platos más vendidos filtrado por categoría (Top 10).
- Reporte de mesas más utilizadas.
- Reporte de consumo por mesa.
- Reporte de historial de clientes y consumo promedio por persona.

---

# Persistencia de datos

El sistema utiliza **serialización en Java** para almacenar la información en archivos **.DAT**, permitiendo que los datos permanezcan disponibles incluso después de cerrar la aplicación.

Los archivos utilizados son los siguientes:

- ### clientes.dat
    Almacena la información de todos los clientes registrados, incluyendo sus datos personales, visitas realizadas y descuentos disponibles.

- ### meseros.dat
    Contiene la información de los meseros registrados, junto con las mesas asignadas y su historial de trabajo.

- ### mesas.dat
    Guarda la información de las mesas del restaurante, incluyendo su capacidad, estado (**Libre**, **Ocupada** o **Reservada**), cliente actual y reservas activas.

- ### platos.dat
    Almacena todos los platos registrados en el menú junto con su categoría, precio y disponibilidad.

- ### pedidos.dat
    Guarda los pedidos realizados por los clientes, incluyendo la mesa asociada, el cliente, la lista detallada de platos solicitados (`DetallePedido`), el estado del pedido y su valor total.

- ### facturas.dat
    Almacena las facturas generadas después del registro de cada pago, junto con la información detallada del pago realizado.

- ### reportes.dat
    Almacena el historial completo de todos los reportes generados en el sistema con su marca de tiempo.

---

# Manejo de excepciones

El sistema implementa **excepciones personalizadas** para controlar errores específicos del negocio y evitar comportamientos inesperados durante la ejecución.

Las principales excepciones implementadas son:

- ### ClienteDuplicadoException 
    Se lanza cuando se intenta registrar un cliente con una cédula, teléfono o correo electrónico que ya existe.
- ### MeseroDuplicadoException 
    Evita registrar dos meseros con la misma cédula.
- ### MesaDuplicadaException 
    Impide registrar dos mesas con el mismo número identificador.
- ### MesaReservadaException 
    Se produce cuando se intenta reservar una mesa que ya posee una reserva activa.
- ### NoHayMesasRegistradasException 
    Se genera cuando se intenta sentar un cliente o registrar un pedido pero no existen mesas registradas en el sistema.
- ### ElementoNoEncontradoException 
    Se dispara cuando se realiza una búsqueda de cualquier elemento (cliente, mesero, mesa, plato, pedido o factura) y no se encuentra en el sistema, mostrando una advertencia visual emergente.
- ### PlatoDuplicadoException 
    Evita registrar dos platos con el mismo nombre.
- ### MetodoDePagoInvalidoException 
    Se genera cuando el usuario selecciona un método de pago no reconocido.

Todas estas excepciones son capturadas mediante bloques `try-catch`, mostrando mensajes descriptivos al usuario mediante ventanas emergentes gráficos (`JOptionPane`) o mensajes descriptivos sin interrumpir el funcionamiento del sistema.