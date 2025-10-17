# TareaLP3_Java
Nombre: David Carrasco
Rol: 201930028-8

Instrucciones para la correcta utilización del programa.
1. Descomprimir todos los archivos en una misma carpeta.
2. Utilizar el comando "make" para compilar los archivos y generar los binarios
3. Con el comando "make run" puede ejecutar el juego
4. (Consideraciones: El programa se desarrollo con el IDE Itellij Idea, para Java/ el sdk que se utilizó es el más actual el 24)
5. Para compilar la tarea es necesario tener el jdk actualizado a la version 24, de lo contrario tirara errores propios de la desactualización de la version.
6. En caso de no tener esta versión en la carpeta predeterminada, ahí que indicar la dirección del jdk dentro del archivo make, para que este sea efectivo. (Varia según corrector.)

# Explicación del Código
## Estructura de carpetas
--- 
Tenemos la siguiente distribución de carpetas con las clases que contiene.
´´´ 
entorno:
    * Zona.java
    * Zonas.java
    * ZonaArrecife.java
    * ZonaProfunda.java
    * ZonaVolcanica.java
    * NaveEstrellada.java
objetos:
    * AccesoProfundidad.java
    * Item.java
    * ItemTipo.java
    * Vehiculo.java
    * NaveExploradora.java
    * RobotExcavador.java
player:
    * FormulaO2.java
    * Jugador.java
    * Oxigeno.java
Main.java
´´´

## Explicación de Clases
---
### Entorno
* Zona.java: Rol: Contiene la estructura de cada zona del juego implementada como clase abstracta
* Zonas.java: Rol: Inicializa cada zona 1 sola vez para mantener la consistencia durante el juego.
* ZonaArrecife.java: Zona básica del juego con el primer acercamiento a las cosas que se pueden hacer
* ZonaProfunda.java: Zona intermedia del juego que agrega dificultad al juego
* ZonaVolcanica.java: Zona dificil del juego con la capacidad de poder obtener lo necesario para terminar el juego.
* NaveEstrellada.java: Zona inicial del juego de carácter bimodal. Lugar necesario para terminar el juego y poder escapar.
---
### Objetos
---
* AccesoProfundidad.java: Rol: Contiene la interfaz que se usa para validar cada acceso a las zonas.
* Item.java: Rol: Contiene la estructura de los items junto con sus principales caracteristicas.
* ItemTipo.java: Rol: Enumeración de todos los objetos que están disponebles en el juego.
* Vehiculo.java: Rol: Clase abstracta que contiene la estructura y propiedades de un vehiculo en el juego.
* NaveExploradora.java: Rol: Nave principal del jugador, aquí sucede gran parte del juego, hereda de vehiculo.
* RobotExcavador.java: Rol: Vehiculo opcional que te ayuda con la recolección de materiales para ganar la partida de forma más rápida.
---
### Player
---
* FormulaO2.java: Rol: Contiene las formulas de gasto de oxigeno asociadas a las acciones del jugador.
* Jugador.java: Rol: Clase del personaje principal del juego, contiene todas las flags de progreso.
* Oxigeno.java: Rol: Clase que contiene la estructura del oxígeno, tanto como su capacidad base, capacidad máxima y mejora.
---
### Main
---
* Main.java: Rol: Aquí sucede todo el juego, clase contiene la lógica necesaria para jugar el juego, haciendo la llamada de funciones, recibiendo inputs del jugador y procesando el progreso del juego.

## Explicación de libertades
---
