package dev;

/**
 * Clase principal del programa.
 * 
 * Punto de entrada de la aplicación. Inicializa la interfaz gráfica de usuario
 * para la generación y visualización de señales periódicas y su espectro.
 * 
 * Ejecución:
 * <pre>
 *   java -jar nombre_del_jar.jar
 * </pre>
 * 
 * @author Zini
 * @version 1.0
 */
public class App
{
    /**
     * Método principal. Inicia la interfaz gráfica.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args)
    {
        new InterfazGrafica();
    }
}