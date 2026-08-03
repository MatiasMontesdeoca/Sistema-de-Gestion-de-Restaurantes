package ExcepcionesPersonalizadas;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MensajesDeExcepciones {

    // Muestra una ventana emergente gráfica de error
    public static void mostrarError(String mensaje) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }

    // Muestra una ventana emergente gráfica de información
    public static void mostrarInformacion(String mensaje) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    // Muestra una ventana emergente gráfica de advertencia
    public static void mostrarAdvertencia(String mensaje) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
        frame.dispose();
    }
}