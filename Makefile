# ==========================
#  Makefile - Proyecto Java
# ==========================
# Compila y ejecuta el juego de exploración submarina

# --- Configuración básica ---
SRC_DIRS := player objetos entorno
SRC_FILES := $(wildcard *.java $(addsuffix /*.java, $(SRC_DIRS)))
BIN_DIR := bin
MAIN_CLASS := Main

# --- Comandos ---
JAVA_HOME = C:/Users/David/.jdks/openjdk-24 #Cambiar aquí para poder ejecutar la tarea. Se recomienda usar el IDLE de intellij
JAVAC = $(JAVA_HOME)/bin/javac.exe
JAVA  = $(JAVA_HOME)/bin/java.exe

RM := rm -rf

# --- Reglas principales ---
all: compile

compile:
	@echo "Compilando el proyecto..."
	@mkdir -p $(BIN_DIR)
	@$(JAVAC) -d $(BIN_DIR) $(SRC_FILES)
	@echo "Compilacion completada con exito."

run: compile
	@echo "Ejecutando el juego..."
	@$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	@echo "Limpiando archivos compilados..."
	@$(RM) $(BIN_DIR)
	@echo "Limpieza completa."

rebuild: clean compile

# --- Ayuda ---
help:
	@echo "Comandos disponibles:"
	@echo "  make compile   -> Compila todas las clases del proyecto"
	@echo "  make run       -> Compila y ejecuta el juego"
	@echo "  make clean     -> Elimina archivos .class generados"
	@echo "  make rebuild   -> Limpia y recompila todo"
