import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;

public class LinhaPanel extends JPanel {
    private final BufferedImage[] imagens = new BufferedImage[5];
    private final String[] refs = new String[5];
    private final String[] pastas = new String[5];
    private final JLabel[] imageLabels = new JLabel[5];

    public LinhaPanel() {
        setLayout(new GridLayout(5, 1, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Imagens das Linhas"));

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JPanel panel = new JPanel(new BorderLayout());
            JLabel imgLabel = new JLabel("Imagem Linha " + (i + 1), SwingConstants.CENTER);
            imgLabel.setPreferredSize(new Dimension(50, 65));
            imgLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            imageLabels[i] = imgLabel;

            JButton btn = new JButton("Inserir");
            btn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        BufferedImage original = ImageIO.read(file);

                        int size = Math.min(original.getWidth(), original.getHeight());
                        int cropX = (original.getWidth() - size) / 2;
                        int cropY = (original.getHeight() - size) / 2;
                        BufferedImage cropped = original.getSubimage(cropX, cropY, size, size);

                        Image scaled = cropped.getScaledInstance(100, 65, Image.SCALE_SMOOTH);
                        BufferedImage finalImg = new BufferedImage(100, 65, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = finalImg.createGraphics();
                        g2d.drawImage(scaled, 0, 0, null);
                        g2d.dispose();

                        imagens[index] = finalImg;
                        imageLabels[index].setIcon(new ImageIcon(finalImg));
                        imageLabels[index].setText("");

                        imageLabels[index].setText("");
                    } catch (IOException ex) {
                        Logger.getLogger(LinhaPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            panel.add(imgLabel, BorderLayout.CENTER);
            panel.add(btn, BorderLayout.SOUTH);
            add(panel);
        }
    }

    public BufferedImage[] getImagens() {
        return imagens;
    }

    public String[] getRefs() {
        return refs;
    }

    public String[] getPastas() {
        return pastas;
    }
}
