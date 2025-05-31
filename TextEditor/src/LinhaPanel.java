import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class LinhaPanel extends JPanel {

    private static final int MAX_LINHAS = 6;

    private JLabel[] labels = new JLabel[MAX_LINHAS];
    private JTextField[] refFields = new JTextField[MAX_LINHAS];
    private JTextField[] pastaFields = new JTextField[MAX_LINHAS];
    private JButton[] btnAddImage = new JButton[MAX_LINHAS];
    private JLabel[] imageLabels = new JLabel[MAX_LINHAS];  // para mostrar a imagem
    private BufferedImage[] imagens = new BufferedImage[MAX_LINHAS];

    public LinhaPanel() {
        setLayout(new GridLayout(MAX_LINHAS, 1, 5, 5));

        for (int i = 0; i < MAX_LINHAS; i++) {
            JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

            labels[i] = new JLabel("Imagem " + (i + 1) + ":");
            refFields[i] = new JTextField(15);
            refFields[i].setEditable(false);
            pastaFields[i] = new JTextField(30);
            pastaFields[i].setEditable(false);

            btnAddImage[i] = new JButton("Adicionar");
            final int index = i;
            btnAddImage[i].addActionListener(e -> adicionarImagem(index));

            imageLabels[i] = new JLabel();
            imageLabels[i].setPreferredSize(new Dimension(100, 65)); // tamanho da miniatura
            imageLabels[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));

            linha.add(labels[i]);
            linha.add(imageLabels[i]);   // exibe a imagem
            linha.add(new JLabel("REF:"));
            linha.add(refFields[i]);
            linha.add(new JLabel("PASTA:"));
            linha.add(pastaFields[i]);
            linha.add(btnAddImage[i]);

            add(linha);

            // Configura drag & drop para cada linha individualmente
            int currentIndex = i;
            new DropTarget(linha, new DropTargetListener() {
                @Override
                public void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable t = dtde.getTransferable();
                        List<File> droppedFiles = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                        for (File file : droppedFiles) {
                            if (imagens[currentIndex] != null) {
                                // Se já existe imagem nesta linha, substitui
                            }
                            BufferedImage img = ImageIO.read(file);
                            if (img != null) {
                                // Preenche campos e imagem na linha
                                refFields[currentIndex].setText(file.getName());
                                pastaFields[currentIndex].setText(file.getParent());

                                // Processa a imagem igual ao botão
                                int size = Math.min(img.getWidth(), img.getHeight());
                                int cropX = (img.getWidth() - size) / 2;
                                int cropY = (img.getHeight() - size) / 2;
                                BufferedImage cropped = img.getSubimage(cropX, cropY, size, size);
                                
                                

                                Image scaled = cropped.getScaledInstance(125, 137, Image.SCALE_SMOOTH);
                                BufferedImage finalImg = new BufferedImage(125, 137, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2d = finalImg.createGraphics();
                                g2d.drawImage(scaled, 0, 0, null);
                                g2d.dispose();
                                
                                imagens[currentIndex] = finalImg;
                                
                                Image scaled2 = cropped.getScaledInstance(100, 65, Image.SCALE_SMOOTH);
                                BufferedImage finalImg2 = new BufferedImage(100, 65, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2d2 = finalImg2.createGraphics();
                                g2d2.drawImage(scaled2, 0, 0, null);
                                g2d2.dispose();
                                
                                imageLabels[currentIndex].setIcon(new ImageIcon(finalImg2));
                                imageLabels[currentIndex].setText("");
                            } else {
                                JOptionPane.showMessageDialog(LinhaPanel.this,
                                        "Arquivo não é uma imagem válida.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        dtde.dropComplete(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dtde.dropComplete(false);
                    }
                }

                @Override public void dragEnter(DropTargetDragEvent dtde) {}
                @Override public void dragOver(DropTargetDragEvent dtde) {}
                @Override public void dropActionChanged(DropTargetDragEvent dtde) {}
                @Override public void dragExit(DropTargetEvent dte) {}
            });
        }
    }

    private void adicionarImagem(int index) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Selecione uma imagem");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imagens", "jpg", "jpeg", "png", "bmp", "gif"));

            chooser.putClientProperty("JFileChooser.useSystemFileChooser", Boolean.TRUE);

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File arquivo = chooser.getSelectedFile();

                refFields[index].setText(arquivo.getName());
                pastaFields[index].setText(arquivo.getParent());

                BufferedImage original = ImageIO.read(arquivo);
                if (original != null) {
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
                } else {
                    JOptionPane.showMessageDialog(this, "Arquivo selecionado não é uma imagem válida.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar imagem: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public BufferedImage[] getImagens() {
        return imagens;
    }

    public String[] getRefs() {
        String[] refs = new String[MAX_LINHAS];
        for (int i = 0; i < MAX_LINHAS; i++) {
            refs[i] = refFields[i].getText();
        }
        return refs;
    }

    public String[] getPastas() {
        String[] pastas = new String[MAX_LINHAS];
        for (int i = 0; i < MAX_LINHAS; i++) {
            pastas[i] = pastaFields[i].getText();
        }
        return pastas;
    }
}
