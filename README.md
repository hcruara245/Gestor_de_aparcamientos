# Sistema de Gestión de Aparcamientos

Proyecto académico desarrollado para la asignatura de Bases de Datos y Programación. Sistema integral para la gestión y administración automatizada de una cadena de tres aparcamientos.

## Descripción
Esta aplicación de escritorio ha sido diseñada para centralizar las operaciones de una cadena de parkings en Calatayud, incluyendo:
- Gestión de accesos para usuarios normales, abonados y trabajadores.
- Control de servicios adicionales como limpieza y cambios de aceite.
- Automatización de pedidos a proveedores basada en niveles de stock.
- Administración de personal y turnos rotativos.

## Arquitectura
El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** para garantizar una separación clara de responsabilidades:
- **Modelo:** Lógica de negocio y persistencia de datos (JDBC/MySQL).
- **Vista:** Interfaz gráfica desarrollada con Java Swing (JFrame).
- **Controlador:** Gestión de eventos y flujo de datos entre la interfaz y la base de datos.

## Tecnologías
- **Lenguaje:** Java
- **Interfaz:** Java Swing (JFrame)
- **Base de Datos:** MySQL
- **Conectividad:** JDBC

## Estructura del Proyecto
```bash
.
├── /database       # Scripts SQL para la creación y población de la BBDD
├── /docs           # Documentación técnica (Memoria, Diagrama E/R, etc.)
├── /src            # Código fuente (MVC)
│   ├── /model
│   ├── /view
│   └── /controller
├── README.md
└── .gitignore
## Autores del proyecto

· Jorge Fuentes Serrano
· Hugo Cruz Aranda