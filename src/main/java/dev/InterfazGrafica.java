package dev;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.awt.*;

/**
 * Clase que implementa la interfaz gráfica de usuario para la generación y visualización
 * de señales periódicas y su espectro de magnitud (FFT).
 * 
 * Permite al usuario seleccionar el tipo de señal, configurar sus parámetros (amplitud, frecuencia,
 * fase, duración, duty cycle, ancho de diente de sierra), y visualizar tanto la señal en el dominio
 * del tiempo como su espectro en el dominio de la frecuencia.
 * 
 * Funcionalidades principales:
 * <ul>
 *   <li>Selección del tipo de señal: senoidal, cuadrada, triangular o diente de sierra.</li>
 *   <li>Configuración de parámetros de la señal mediante campos de texto.</li>
 *   <li>Visualización de la señal generada en el dominio del tiempo.</li>
 *   <li>Visualización del espectro de magnitud (FFT) al activar el checkbox correspondiente.</li>
 * </ul>
 * 
 * Uso:
 * <pre>
 *   new InterfazGrafica();
 * </pre>
 * 
 * @author Zini
 */
public class InterfazGrafica extends JFrame
{
    /** ComboBox para seleccionar el tipo de señal */
    private JComboBox<String> comboTipo;
    /** Campo de texto para la amplitud */
    private JTextField campoAmplitud;
    /** Campo de texto para la frecuencia */
    private JTextField campoFrecuencia;
    /** Campo de texto para la fase */
    private JTextField campoFase;
    /** Campo de texto para la duración */
    private JTextField campoDuracion;
    /** Campo de texto para el ancho del diente de sierra */
    private JTextField campoWidth;
    /** Campo de texto para el duty cycle */
    private JTextField campoDutyCycle;
    /** Botón para graficar la señal */
    private JButton botonGraficar;
    /** Panel para mostrar el gráfico */
    private ChartPanel chartPanel;
    /** Checkbox para mostrar el espectro (FFT) */
    private JCheckBox checkBoxEspectro;

    /**
     * Constructor de la interfaz gráfica.
     * Inicializa los componentes, organiza el layout y define las acciones de los controles.
     */
    public InterfazGrafica()
    {
        this.setTitle("Señales y Espectros");
        this.setSize(1280, 720);
        this.setExtendedState(Frame.MAXIMIZED_BOTH); 
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Panel de control (arriba)
        JPanel panelControl = new JPanel(new GridLayout(3, 4, 10, 5));

        this.setComboTipo(new JComboBox<>(new String[]{ "Senoidal", "Cuadrada", "Triangular", "Diente de Sierra" }));
        this.setCampoAmplitud(new JTextField("1.0"));
        this.setCampoFrecuencia(new JTextField("10.0"));
        this.setCampoFase(new JTextField("44100.0"));
        this.setCampoDuracion(new JTextField("1.0"));
        this.setCampoWidth(new JTextField("1.0"));
        this.setCampoDutyCycle(new JTextField("50.0"));

        panelControl.add(new JLabel("Tipo de señal:"));
        panelControl.add(comboTipo);
        panelControl.add(new JLabel("Amplitud:"));
        panelControl.add(campoAmplitud);
        panelControl.add(new JLabel("Frecuencia (Hz):"));
        panelControl.add(campoFrecuencia);
        panelControl.add(new JLabel("Fase (rad):"));
        panelControl.add(campoFase);
        panelControl.add(new JLabel("Duración (s):"));
        panelControl.add(campoDuracion);
        panelControl.add(new JLabel("Ancho de diente de sierra:"));
        panelControl.add(campoWidth);
        panelControl.add(new JLabel("Duty Cycle (%):"));
        panelControl.add(campoDutyCycle);

        this.setBotonGraficar(new JButton("Graficar"));
        panelControl.add(this.getBotonGraficar());

        setCheckBoxEspectro(new JCheckBox("Mostrar espectro"));
        panelControl.add(checkBoxEspectro);

        add(panelControl, BorderLayout.NORTH);

        // Panel del gráfico
        setChartPanel(new ChartPanel(null));
        add(this.getChartPanel(), BorderLayout.CENTER);

        // Acción del botón
        this.getBotonGraficar().addActionListener(e -> graficar());

        setVisible(true);
    }

    /**
     * Método que genera y muestra el gráfico de la señal seleccionada o su espectro,
     * según los parámetros ingresados y el estado del checkbox.
     * Si el checkbox está seleccionado, muestra el espectro de magnitud (FFT).
     * Si no, muestra la señal en el dominio del tiempo.
     */
    private void graficar()
    {
        try
        {
            double amplitud = Double.parseDouble(campoAmplitud.getText());
            double frecuencia = Double.parseDouble(campoFrecuencia.getText());
            double fase = Double.parseDouble(campoFase.getText());
            double duracion = Double.parseDouble(campoDuracion.getText());
            double fs = Double.parseDouble(campoFase.getText()); // frecuencia de muestreo fija por ahora
            double width = Double.parseDouble(campoWidth.getText());
            double dutyCycle = Double.parseDouble(campoDutyCycle.getText());

            SenialPeriodica senial = new SenialPeriodica(amplitud, frecuencia, fase, fs, duracion);
            double[] datos = new double[0];

            String tipoSeleccionado = (String) comboTipo.getSelectedItem();
            if ("Senoidal".equals(tipoSeleccionado))
            {
                datos = senial.generarSenoidal();
            } else if ("Cuadrada".equals(tipoSeleccionado))
            {
                datos = senial.generarCuadrada(dutyCycle);
            } else if ("Triangular".equals(tipoSeleccionado))
            {
                datos = senial.generarTriangular();
            } else if ("Diente de Sierra".equals(tipoSeleccionado))
            {
                datos = senial.generarDienteDeSierra(width);
            }

            if (checkBoxEspectro.isSelected())
            {
                // Graficar FFT
                FFT fft = new FFT();
                double[] magnitudes = fft.calcularMagnitud(datos);

                XYSeries serieFFT = new XYSeries("Magnitud FFT");
                int N = magnitudes.length;
                double df = fs / (2.0 * N); // Espaciado de frecuencia
                for (int i = 0; i < N; i++)
                {
                    double freq = i * df;
                    serieFFT.add(freq, magnitudes[i]);
                }

                XYSeriesCollection datasetFFT = new XYSeriesCollection(serieFFT);
                JFreeChart chartFFT = ChartFactory.createXYLineChart(
                        "Espectro de Magnitud (FFT)",
                        "Frecuencia (Hz)",
                        "Magnitud",
                        datasetFFT,
                        PlotOrientation.VERTICAL,
                        false, true, false);
                this.getChartPanel().setChart(chartFFT);
            } else
            {
                // Graficar señal en el tiempo
                XYSeries serie = new XYSeries("Señal");
                double ts = 1.0 / fs;
                for (int i = 0; i < datos.length; i++)
                {
                    serie.add(i * ts, datos[i]);
                }

                XYSeriesCollection dataset = new XYSeriesCollection(serie);
                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Señal generada",
                        "Tiempo (s)",
                        "Amplitud",
                        dataset,
                        PlotOrientation.VERTICAL,
                        false, true, false);

                this.getChartPanel().setChart(chart);
            }

        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Verificá que todos los campos tengan números válidos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JComboBox<String> getComboTipo()
    {
        return this.comboTipo;
    }

    private void setComboTipo(JComboBox<String> comboTipo)
    {
        this.comboTipo = comboTipo;
    }

    public JTextField getCampoAmplitud()
    {
        return this.campoAmplitud;
    }

    private void setCampoAmplitud(JTextField campoAmplitud)
    {
        this.campoAmplitud = campoAmplitud;
    }

    public JTextField getCampoFrecuencia()
    {
        return this.campoFrecuencia;
    }

    private void setCampoFrecuencia(JTextField campoFrecuencia)
    {
        this.campoFrecuencia = campoFrecuencia;
    }

    public JTextField getCampoFase()
    {
        return this.campoFase;
    }

    private void setCampoFase(JTextField campoFase)
    {
        this.campoFase = campoFase;
    }

    public JTextField getCampoDuracion()
    {
        return this.campoDuracion;
    }

    private void setCampoDuracion(JTextField campoDuracion)
    {
        this.campoDuracion = campoDuracion;
    }

    public JTextField getCampoWidth()
    {
        return this.campoWidth;
    }

    private void setCampoWidth(JTextField campoWidth)
    {
        this.campoWidth = campoWidth;
    }

    public JTextField getCampoDutyCycle()
    {
        return this.campoDutyCycle;
    }

    private void setCampoDutyCycle(JTextField campoDutyCycle)
    {
        this.campoDutyCycle = campoDutyCycle;
    }

    public JButton getBotonGraficar()
    {
        return this.botonGraficar;
    }

    private void setBotonGraficar(JButton botonGraficar)
    {
        this.botonGraficar = botonGraficar;
    }

    public ChartPanel getChartPanel()
    {
        return this.chartPanel;
    }

    private void setChartPanel(ChartPanel chartPanel)
    {
        this.chartPanel = chartPanel;
    }

    public JCheckBox getCheckBoxEspectro()
    {
        return this.checkBoxEspectro;
    }

    private void setCheckBoxEspectro(JCheckBox checkBoxEspectro)
    {
        this.checkBoxEspectro = checkBoxEspectro;
    }
}
