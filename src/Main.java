import gui.MainFrame;
import javax.swing.SwingUtilities;

// Punto de entrada del programa
public class Main {
    public static void main(String[] args) {
        // Para iniciar la interfaz en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
