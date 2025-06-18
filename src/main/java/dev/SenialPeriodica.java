package dev;

/**
 * Clase que representa una señal periódica y permite generar diferentes formas de onda:
 * senoidal, cuadrada, triangular y diente de sierra.
 * 
 * Permite configurar la amplitud, frecuencia, fase, frecuencia de muestreo y duración de la señal.
 * 
 * Métodos principales:
 * <ul>
 *   <li>{@link #generarSenoidal()} - Genera una señal senoidal.</li>
 *   <li>{@link #generarCuadrada(double)} - Genera una señal cuadrada con duty cycle especificado.</li>
 *   <li>{@link #generarTriangular()} - Genera una señal triangular.</li>
 *   <li>{@link #generarDienteDeSierra(double)} - Genera una señal diente de sierra con ancho de subida especificado.</li>
 * </ul>
 * 
 * Ejemplo de uso:
 * <pre>
 *   SenialPeriodica senial = new SenialPeriodica(1.0, 50.0, 0.0, 1000.0, 1.0);
 *   double[] datos = senial.generarSenoidal();
 * </pre>
 * 
 * @author Zini
 */
public class SenialPeriodica
{
    /** Amplitud de la señal */
    private double amplitud;
    /** Frecuencia de la señal en Hz */
    private double frecuencia;
    /** Fase inicial de la señal en radianes */
    private double fase;
    /** Frecuencia de muestreo en Hz */
    private double fs;
    /** Duración de la señal en segundos */
    private double duracion;

    /**
     * Constructor de la clase SenialPeriodica.
     * 
     * @param amplitud  Amplitud de la señal
     * @param frecuencia Frecuencia de la señal en Hz
     * @param fase      Fase inicial en radianes
     * @param fs        Frecuencia de muestreo en Hz
     * @param duracion  Duración de la señal en segundos
     */
    public SenialPeriodica(double amplitud, double frecuencia, double fase, double fs, double duracion)
    {
        this.setAmplitud(amplitud);
        this.setFrecuencia(frecuencia);
        this.setFase(fase);
        this.setFs(fs);
        this.setDuracion(duracion);
    }

    /** @return Amplitud de la señal */
    public double getAmplitud()
    {
        return this.amplitud;
    }

    /** @param amplitud Amplitud de la señal */
    private void setAmplitud(double amplitud)
    {
        this.amplitud = amplitud;
    }

    /** @return Frecuencia de la señal en Hz */
    public double getFrecuencia()
    {
        return this.frecuencia;
    }

    /** @param frecuencia Frecuencia de la señal en Hz */
    private void setFrecuencia(double frecuencia)
    {
        this.frecuencia = frecuencia;
    }

    /** @return Fase inicial de la señal en radianes */
    public double getFase()
    {
        return this.fase;
    }

    /** @param fase Fase inicial en radianes */
    private void setFase(double fase)
    {
        this.fase = fase;
    }

    /** @return Frecuencia de muestreo en Hz */
    public double getFs()
    {
        return this.fs;
    }

    /** @param fs Frecuencia de muestreo en Hz */
    private void setFs(double fs)
    {
        this.fs = fs;
    }

    /** @return Duración de la señal en segundos */
    public double getDuracion()
    {
        return this.duracion;
    }

    /** @param duracion Duración de la señal en segundos */
    private void setDuracion(double duracion)
    {
        this.duracion = duracion;
    }

    /**
     * Genera una señal senoidal.
     * 
     * @return Arreglo de muestras de la señal senoidal.
     */
    public double[] generarSenoidal()
    {
        int muestras = (int) (this.getFs() * this.getDuracion());
        double[] y = new double[muestras];
        double ts = 1.0 / this.getFs();

        for (int i = 0; i < muestras; i++)
        {
            double t = i * ts;
            y[i] = this.getAmplitud() * Math.sin(2 * Math.PI * this.getFrecuencia() * t + fase);
        }

        return y;
    }

    /**
     * Genera una señal cuadrada.
     * 
     * @param dutyCycle Porcentaje de ciclo de trabajo (0-100).
     * @return Arreglo de muestras de la señal cuadrada.
     */
    public double[] generarCuadrada(double dutyCycle)
    {
        int muestras = (int) (this.getFs() * this.getDuracion());
        double[] y = new double[muestras];
        double ts = 1.0 / this.getFs();
        double duty = dutyCycle / 100.0;

        for (int i = 0; i < muestras; i++)
        {
            double t = i * ts;
            double ciclo = (t * this.getFrecuencia()) % 1.0;
            y[i] = (ciclo < duty) ? this.getAmplitud() : -this.getAmplitud();
        }

        return y;
    }

    /**
     * Genera una señal triangular.
     * 
     * @return Arreglo de muestras de la señal triangular.
     */
    public double[] generarTriangular()
    {
        int muestras = (int) (this.getFs() * this.getDuracion());
        double[] y = new double[muestras];
        double ts = 1.0 / this.getFs();

        for (int i = 0; i < muestras; i++)
        {
            double t = i * ts;
            double ciclo = (t * this.getFrecuencia()) % 1.0;
            y[i] = 4 * this.getAmplitud() * Math.abs(ciclo - 0.5) - this.getAmplitud();
        }

        return y;
    }

    /**
     * Genera una señal diente de sierra.
     * 
     * @param width Ancho de subida (0-1), determina la proporción del ciclo en subida.
     * @return Arreglo de muestras de la señal diente de sierra.
     */
    public double[] generarDienteDeSierra(double width)
    {
        int muestras = (int) (this.getFs() * duracion);
        double[] y = new double[muestras];
        double ts = 1.0 / this.getFs();

        for (int i = 0; i < muestras; i++)
        {
            double t = i * ts;
            double ciclo = (t * this.getFrecuencia()) % 1.0;
            y[i] = (ciclo < width)
                    ? (2 * this.getAmplitud() * ciclo / width - this.getAmplitud())
                    : (2 * this.getAmplitud() * (1 - ciclo) / (1 - width) - this.getAmplitud());
        }

        return y;
    }
}
