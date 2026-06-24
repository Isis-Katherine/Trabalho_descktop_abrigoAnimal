package screen;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class App extends JFrame {

    public App(String titulo) {
        super();
        this.setTitle(titulo);
        this.setSize(1400, 800);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public static void main(String[] args) {
        utils.SistemaDados.load();
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipalAbrigo();
        });
    }
}