import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO; // Mantido, mas JOptionPane é mais comum para feedback ao usuário
import javax.swing.*;

public class LinhaPanel extends JPanel {
    private static final int NUM_LINHAS = 5; // Número de "slots" de item dentro deste painel

    // Arrays para armazenar os dados de cada slot
    private final BufferedImage[] imagens = new BufferedImage[NUM_LINHAS];
    private final JTextField[] refFields = new JTextField[NUM_LINHAS];
    private final JTextField[] pastaFields = new JTextField[NUM_LINHAS];
    private final JTextField[] mtsFields = new JTextField[NUM_LINHAS]; // Para as metragens
    private final JLabel[] imageLabels = new JLabel[NUM_LINHAS]; // Para exibir as imagens

    public LinhaPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Layout vertical para os slots
        setBorder(BorderFactory.createTitledBorder("Itens da Ordem de Serviço (Ref, Pasta, Mts, Imagem)"));

        for (int i = 0; i < NUM_LINHAS; i++) {
            final int index = i; // Para uso dentro do lambda
            JPanel slotPanel = new JPanel(new BorderLayout(10, 5)); // Painel para cada slot
            slotPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Item " + (i + 1)),
                BorderFactory.createEmptyBorder(5,5,5,5)
            ));

            // Painel para os campos de texto (Ref, Pasta, Mts)
            JPanel textInputPanel = new JPanel();
            textInputPanel.setLayout(new BoxLayout(textInputPanel, BoxLayout.Y_AXIS)); // Vertical para os campos de texto

            JPanel refPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            refPanel.add(new JLabel("Ref:"));
            refFields[index] = new JTextField(15);
            refPanel.add(refFields[index]);
            textInputPanel.add(refPanel);

            JPanel pastaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pastaPanel.add(new JLabel("Pasta:"));
            pastaFields[index] = new JTextField(15);
            pastaPanel.add(pastaFields[index]);
            textInputPanel.add(pastaPanel);

            JPanel mtsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            mtsPanel.add(new JLabel("Mts:"));
            mtsFields[index] = new JTextField(8); // Campo para MTS
            mtsPanel.add(mtsFields[index]);
            textInputPanel.add(mtsPanel);

            // Painel para a imagem e o botão de inserir
            JPanel imageAreaPanel = new JPanel(new BorderLayout(5, 5));
            imageLabels[index] = new JLabel("Clique 'Inserir' para adicionar imagem", SwingConstants.CENTER);
            imageLabels[index].setPreferredSize(new Dimension(120, 80)); // Tamanho para o label da imagem
            imageLabels[index].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            JButton btnInserirImagem = new JButton("Inserir Imagem Item " + (i + 1));
            btnInserirImagem.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecionar Imagem para Item " + (index + 1));
                int option = fileChooser.showOpenDialog(LinhaPanel.this); // Usar LinhaPanel.this como pai

                if (option == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        BufferedImage originalImage = ImageIO.read(file);

                        if (originalImage == null) {
                             JOptionPane.showMessageDialog(LinhaPanel.this,
                                "Não foi possível ler o arquivo de imagem selecionado.",
                                "Erro de Imagem", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Usar ImageUtils para processar a imagem
                        // Ajuste targetHeight e maxWidth conforme necessário para o display
                        BufferedImage processedImage = ImageUtils.cropAndScale(originalImage, 70, 110); 

                        if (processedImage != null) {
                            imagens[index] = processedImage; // Armazena a imagem processada (pode ser a original se não quiser processar para o DB)
                            imageLabels[index].setIcon(new ImageIcon(processedImage));
                            imageLabels[index].setText(""); // Limpa o texto do label
                        } else {
                             JOptionPane.showMessageDialog(LinhaPanel.this,
                                "Erro ao processar a imagem.",
                                "Erro de Imagem", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(LinhaPanel.class.getName()).log(Level.SEVERE, "Erro ao carregar/processar imagem", ex);
                        JOptionPane.showMessageDialog(LinhaPanel.this,
                                "Erro de I/O ao carregar imagem: " + ex.getMessage(),
                                "Erro de Imagem", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            imageAreaPanel.add(imageLabels[index], BorderLayout.CENTER);
            imageAreaPanel.add(btnInserirImagem, BorderLayout.SOUTH);

            // Adicionar painéis de texto e imagem ao painel do slot
            slotPanel.add(textInputPanel, BorderLayout.CENTER);
            slotPanel.add(imageAreaPanel, BorderLayout.EAST); // Imagem à direita dos campos de texto
            
            add(slotPanel); // Adiciona o painel do slot ao LinhaPanel principal
        }
    }

    // Getters que coletam dados dos JTextFields no momento da chamada
    public String[] getRefs() {
        String[] values = new String[NUM_LINHAS];
        for (int i = 0; i < NUM_LINHAS; i++) {
            values[i] = refFields[i].getText().trim();
        }
        return values;
    }

    public String[] getPastas() {
        String[] values = new String[NUM_LINHAS];
        for (int i = 0; i < NUM_LINHAS; i++) {
            values[i] = pastaFields[i].getText().trim();
        }
        return values;
    }

    public String[] getMts() { // Agora este método existe
        String[] values = new String[NUM_LINHAS];
        for (int i = 0; i < NUM_LINHAS; i++) {
            values[i] = mtsFields[i].getText().trim();
        }
        return values;
    }

    public BufferedImage[] getImagens() {
        // Este já retorna o array 'imagens' que é preenchido pelo listener do botão
        return imagens;
    }

    // Método para limpar todos os campos do painel (útil para "Nova OS")
    public void limparCampos() {
        for (int i = 0; i < NUM_LINHAS; i++) {
            refFields[i].setText("");
            pastaFields[i].setText("");
            mtsFields[i].setText("");
            imagens[i] = null;
            imageLabels[i].setIcon(null);
            imageLabels[i].setText("Clique 'Inserir' para adicionar imagem");
        }
    }
}