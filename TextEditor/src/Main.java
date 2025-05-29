import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;

// Se TextEditor estiver em um pacote, você precisará importá-lo:
// import seu.pacote.TextEditor; 

public class Main {

    public static void main(String[] args) {
        // 1. Executar a criação e exibição da GUI na Event Dispatch Thread (EDT).
        // Esta é a prática padrão e mais segura para aplicações Swing para evitar
        // problemas de concorrência com a thread principal da GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 2. (Opcional, mas recomendado) Definir o Look and Feel do sistema.
                // Isso faz com que sua aplicação use a aparência nativa do sistema
                // operacional do usuário (Windows, macOS, Linux com GTK, etc.),
                // tornando-a mais familiar.
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Se não conseguir definir o Look and Feel do sistema, ele usará o padrão do Java.
                    // Não é um erro crítico, então apenas logamos.
                    Logger.getLogger(Main.class.getName()).log(Level.WARNING, 
                        "Não foi possível definir o Look and Feel do sistema. Usando o padrão.", e);
                }

                // 3. Criar e exibir a janela principal da sua aplicação.
                new TextEditor(); // Isso chama o construtor do seu TextEditor, que deve torná-lo visível.
            }
        });
    }
}