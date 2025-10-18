# TareaLP3_Java
Nombre: David Carrasco
Rol: 201930028-8

Instrucciones para la correcta utilización del programa.
1. Descomprimir todos los archivos en una misma carpeta.
2. Utilizar el comando "make" para compilar los archivos y generar los binarios
3. Con el comando "make run" puede ejecutar el juego
4. (Consideraciones: El programa se desarrolló con el IDE Itellij Idea, para Java/ el sdk que se utilizó es el más actual siendo este el 24)
5. Para compilar la tarea es necesario tener el jdk actualizado a la version 24, de lo contrario tirará errores propios de la desactualización de la version.
6. En caso de no tener esta versión en la carpeta predeterminada, hay que indicar la dirección del jdk dentro del archivo make, para que este sea efectivo. (Varia según corrector.)

# Explicación del Código
## Estructura de carpetas

--- 
Tenemos la siguiente distribución de carpetas con las clases que contiene. 

## Entorno:

    * Zona.java
    * Zonas.java
    * ZonaArrecife.java
    * ZonaProfunda.java
    * ZonaVolcanica.java
    * NaveEstrellada.java
## Objetos:
    * AccesoProfundidad.java
    * Item.java
    * ItemTipo.java
    * Vehiculo.java
    * NaveExploradora.java
    * RobotExcavador.java
## Player:
    * FormulaO2.java
    * Jugador.java
    * Oxigeno.java

## Main.java


## Explicación de Clases
### Entorno

---
* Zona.java: Rol: Contiene la estructura de cada zona del juego implementada como clase abstracta
* Zonas.java: Rol: Inicializa cada zona 1 sola vez para mantener la consistencia durante el juego.
* ZonaArrecife.java: Rol: Zona básica del juego con el primer acercamiento a las cosas que se pueden hacer
* ZonaProfunda.java: Rol: Zona intermedia del juego que agrega dificultad al juego
* ZonaVolcanica.java: Rol: Zona difícil del juego con la capacidad de poder obtener lo necesario para terminar el juego.
* NaveEstrellada.java: Rol: Zona inicial del juego de carácter bimodal. Lugar necesario para terminar el juego y poder escapar.
---
## Objetos

---
* AccesoProfundidad.java: Rol: Contiene la interfaz que se usa para validar cada acceso a las zonas.
* Item.java: Rol: Contiene la estructura de los items junto con sus principales características.
* ItemTipo.java: Rol: Enumeración de todos los objetos que están disponibles en el juego.
* Vehículo.java: Rol: Clase abstracta que contiene la estructura y propiedades de un vehículo en el juego.
* NaveExploradora.java: Rol: Nave principal del jugador, aquí sucede gran parte del juego, hereda de vehículo.
* RobotExcavador.java: Rol: Vehiculo opcional que te ayuda con la recolección de materiales para ganar la partida de forma más rápida.
---
### Player

---
* FormulaO2.java: Rol: Contiene las fórmulas de gasto de oxígeno asociadas a las acciones del jugador.
* Jugador.java: Rol: Clase del personaje principal del juego, contiene todas las flags de progreso.
* Oxígeno.java: Rol: Clase que contiene la estructura del oxígeno, tanto como su capacidad base, capacidad máxima y mejora.
---
### Main

---
* Main.java: Rol: Aquí sucede todo el juego, clase contiene la lógica necesaria para jugar el juego, haciendo la llamada de funciones, recibiendo inputs del jugador y procesando el progreso del juego.

## Explicación de libertades

---
Al principio se dejó una versión comentada con el modo god, para facilitar la corrección del juego sin jugarlo derechamente (para ahorrar tiempo) de todos modos puede jugarlo c:

Con respecto al juego considerar lo siguiente:
+ La zona en donde inicia el jugador es la zona estrellada en profundidad 0, dentro de la nave exploradora. Para hacer algo en esta zona primero se tiene que salir de la nave exploradora (opción: 8)
+ La profundidad mínima de la Zona arrecife es 1[m] en vez de 0[m] como lo indicaba el pdf. Esto para evitar errores al momento de hacer el respawn en caso de muerte y la nave anclada en 0[m]
+ El jugador a nado, puede cambiar de zona. (Respetando las restricciones de progreso del mismo juego).
+ El jugador al decidir volver a la nave, gasta oxígeno en función de la distancia a la que se encuentra. Por lo que si se queda sin oxígeno al intentar volver, Se muere en el trayecto perdiendo el inventario.
+ Para poder crear cosas, lo objetos tienen que estar en la bodega de la nave y no en el inventario del jugador.
+ El robot se avería no solo cuando la capacidad de carga se supera, sino que también tiene energía y durabilidad, esto para hacer más inmersivo el juego y no ganar inmediatamente después de tener el robot. (es opcional) 