import javax.swing.JFrame;

public class App {
    public static JFrame window;

    public static void main(String[] args) throws Exception {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setUndecorated(true);
        window.setResizable(false);

        GamePanel panel = new GamePanel();
        window.add(panel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        panel.startGameThread();
    }
}
