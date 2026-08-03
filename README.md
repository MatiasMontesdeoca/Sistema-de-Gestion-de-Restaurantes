# Sistema de Gestión de Restaurantes

## Descripción

El **Sistema de Gestión de Restaurantes** es una aplicación desarrollada para administrar el funcionamiento básico de un restaurante, tanto en la atención al cliente como en la gestión interna del personal. El sistema está dividido en varios módulos, cada uno encargado de una función específica, a continuación se presentará una breve explicación de los distintos módulos del sistema:

### Gestión de clientes
- Registro de nuevos clientes.
- Consulta de clientes registrados.
- Asignación de clientes a mesas.
- Control de visitas para otorgar descuentos por fidelidad.

### Gestión de meseros
- Registro de meseros.
- Asignación y retiro de mesas.
- Consulta de la carga de trabajo de cada mesero.
- Visualización del historial de mesas atendidas.

### Gestión de mesas y reservas
- Registro de nuevas mesas.
- Administración de reservas.
- Cambio de estado de las mesas (**Libre**, **Ocupada** y **Reservada**).
- Consulta de la información de cada mesa.

### Gestión del menú
- Registro de nuevos platos clasificados por categoría:
  - Entrada
  - Plato fuerte
  - Postre
  - Bebida
- Modificación de platos.
- Consulta del menú.
- Búsqueda de platos por nombre.

### Gestión de pedidos
- Generación de pedidos.
- Modificación de pedidos.
- Busqueda de pedidos por mesa.

### Gestión de pagos y facturación
- Registro de pagos mediante diferentes métodos.
- Aplicación de descuentos.
- Generación y consulta de facturas.

### Generación de reportes
- Reporte de ventas.
- Reporte de platos más vendidos.
- Reporte de consumo por mesa.
- Reporte de consumo por cliente.

---

# Persistencia de datos

El sistema utiliza **serialización en Java** para almacenar la información en archivos **.DAT**, permitiendo que los datos permanezcan disponibles incluso después de cerrar la aplicación.

Los archivos utilizados son los siguientes:

### clientes.dat
Almacena la información de todos los clientes registrados, incluyendo sus datos personales, visitas realizadas y descuentos disponibles.

### meseros.dat
Contiene la información de los meseros registrados, junto con las mesas asignadas y su historial de trabajo.

### mesas.dat
Guarda la información de las mesas del restaurante, incluyendo su capacidad, estado (**Libre**, **Ocupada** o **Reservada**), cliente actual y reservas activas.

### platos.dat
Almacena todos los platos registrados en el menú junto con su categoría, precio y disponibilidad.

### pedidos.dat
Guarda los pedidos realizados por los clientes, incluyendo la mesa asociada, los platos solicitados, el estado del pedido y su valor.

### facturas.dat
Almacena las facturas generadas después del registro de cada pago.

---

# Manejo de excepciones

El sistema implementa **excepciones personalizadas** para controlar errores específicos del negocio y evitar comportamientos inesperados durante la ejecución.

Las principales excepciones implementadas son:

- **ClienteDuplicadoException:** se lanza cuando se intenta registrar un cliente con una cédula, teléfono o correo electrónico que ya existe.
- **MeseroDuplicadoException:** evita registrar dos meseros con la misma cédula.
- **MesaDuplicadaException:** impide registrar dos mesas con el mismo número identificador.
- **MesaReservadaException:** se produce cuando se intenta reservar una mesa que ya posee una reserva activa.
- **PlatoDuplicadoException:** evita registrar dos platos con el mismo nombre.
- **MetodoDePagoInvalidoException:** se genera cuando el usuario selecciona un método de pago que no existe.

Todas estas excepciones son capturadas mediante bloques `try-catch`, mostrando mensajes descriptivos al usuario sin interrumpir el funcionamiento del sistema.