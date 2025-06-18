package dev;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

/**
 * Clase utilitaria para calcular la Transformada Rápida de Fourier (FFT) de una señal.
 * 
 * Utiliza la librería Apache Commons Math para realizar la transformación y obtener
 * el espectro de magnitud de una señal temporal.
 * 
 * <p>
 * Características:
 * <ul>
 *   <li>Ajusta automáticamente el tamaño de la señal al siguiente número potencia de 2 si es necesario.</li>
 *   <li>Devuelve solo la mitad positiva del espectro (hasta la frecuencia de Nyquist).</li>
 * </ul>
 * </p>
 * 
 * Ejemplo de uso:
 * <pre>
 *   FFT fft = new FFT();
 *   double[] magnitudes = fft.calcularMagnitud(senal);
 * </pre>
 * 
 * @author Zini
 */
public class FFT
{
    /**
     * Calcula el espectro de magnitud de una señal usando la FFT.
     * 
     * @param senial Arreglo de muestras de la señal en el dominio del tiempo.
     * @return Arreglo de magnitudes (solo la mitad positiva del espectro).
     */
    public double[] calcularMagnitud(double[] senial)
    {
        int N = senial.length;
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        // Ajustamos al próximo número potencia de 2 si es necesario
        int potenciaDos = Integer.highestOneBit(N);
        if (potenciaDos != N)
        {
            potenciaDos *= 2;
        }

        double[] senialPadded = new double[potenciaDos];
        System.arraycopy(senial, 0, senialPadded, 0, Math.min(senial.length, potenciaDos));

        Complex[] transformada = fft.transform(senialPadded, TransformType.FORWARD);
        double[] magnitudes = new double[transformada.length / 2];

        for (int i = 0; i < magnitudes.length; i++)
        {
            magnitudes[i] = transformada[i].abs();
        }

        return magnitudes;
    }
}
