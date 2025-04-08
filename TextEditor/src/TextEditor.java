import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.print.*;

public class TextEditor extends JFrame implements ActionListener {

    JTextField clienteField, dataField, horaField, papelField, tecidoField, larguraTecidoField, larguraImpressaoField;
    JCheckBox checkTecCliente, checkTecSublimatec, checkSoImpressao, checkCalandra;
    BufferedImage[] imagensLinhas = new BufferedImage[5];
    String[] refs = new String[5];
    String[] pastas = new String[5];
    JButton[] imageButtons = new JButton[5];


    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem printItem, exitItem;

    public TextEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Ordem de Serviço");
        this.setSize(700, 1000);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        

        // Painel de informações
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configura pesos das colunas
        gbc.weightx = 0;

        // Linha 1 - Cliente, Largura do Tecido
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        clienteField = new JTextField();
        infoPanel.add(clienteField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Tecido:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        larguraTecidoField = new JTextField();
        infoPanel.add(larguraTecidoField, gbc);

        // Linha 2 - Papel, Largura Impressão
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Papel:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        papelField = new JTextField();
        infoPanel.add(papelField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Impressão:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        larguraImpressaoField = new JTextField();
        infoPanel.add(larguraImpressaoField, gbc);

        // Linha 3 - Tecido
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Tecido:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        tecidoField = new JTextField();
        infoPanel.add(tecidoField, gbc);
        gbc.gridwidth = 1;

        // Linha 4 - Data, Hora
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Data:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        dataField = new JTextField();
        infoPanel.add(dataField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Hora:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        horaField = new JTextField();
        infoPanel.add(horaField, gbc);


        // Checkboxes
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 2));
        checkboxPanel.setBorder(BorderFactory.createTitledBorder("Opções"));

        checkTecCliente = new JCheckBox("Tec Cliente");
        checkTecSublimatec = new JCheckBox("Tec Sublimatec");
        checkSoImpressao = new JCheckBox("Só Impressão");
        checkCalandra = new JCheckBox("Calandra");

        checkboxPanel.add(checkTecCliente);
        checkboxPanel.add(checkSoImpressao);
        checkboxPanel.add(checkTecSublimatec);
        checkboxPanel.add(checkCalandra);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(checkboxPanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);

        // Painel de botões de imagem
        JPanel imagePreviewPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        imagePreviewPanel.setBorder(BorderFactory.createTitledBorder("Imagens das Linhas"));
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(imagePreviewPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        JLabel[] imageLabels = new JLabel[5];

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
                        imagensLinhas[index] = original;
                        refs[index] = file.getName();
                        pastas[index] = file.getAbsolutePath();

                        // Redimensiona mantendo proporção e cortando excesso horizontal
                        int targetHeight = 65;
                        double aspect = (double) original.getWidth() / original.getHeight();
                        int targetWidth = (int) (targetHeight * aspect);

                        if (targetWidth > 50) {
                            int cropX = (original.getWidth() - original.getHeight()) / 2;
                            if (cropX < 0) cropX = 0;
                            original = original.getSubimage(cropX, 0, original.getHeight(), original.getHeight());
                            targetWidth = 50;
                            targetHeight = 65;
                        }

                        Image scaled = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                        imageLabels[index].setIcon(new ImageIcon(scaled));
                        imageLabels[index].setText(""); // tira o texto

                        btn.setVisible(false);
                        JOptionPane.showMessageDialog(this, "Imagem " + (index + 1) + " carregada com sucesso!");

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Erro ao carregar imagem.");
                    }
                }
            });



            panel.add(imgLabel, BorderLayout.CENTER);
            panel.add(btn, BorderLayout.SOUTH);
            imagePreviewPanel.add(panel);
        }
        this.add(imagePreviewPanel, BorderLayout.WEST);


        // Menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Arquivo");
        printItem = new JMenuItem("Imprimir");
        exitItem = new JMenuItem("Sair");

        printItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(printItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }
    private void selectAndLoadImage(int i) {
    	  JFileChooser fileChooser = new JFileChooser();
    	  fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
    	  int result = fileChooser.showOpenDialog(null);
    	  if (result == JFileChooser.APPROVE_OPTION) {
    	   File selectedFile = fileChooser.getSelectedFile();
    	   String path = selectedFile.getAbsolutePath();
    	   ImageIcon cropped = loadAndCropImage(path, 100, 80);
    	   imageLabels[i].setIcon(cropped);
    	  }
    	 }

    	 private ImageIcon loadAndCropImage(String path, int targetHeight, int maxWidth) {
    	  try {
    	   BufferedImage original = ImageIO.read(new File(path));
    	   int newWidth = (int) ((double) original.getWidth() / original.getHeight() * targetHeight);
    	   Image scaledImage = original.getScaledInstance(newWidth, targetHeight, Image.SCALE_SMOOTH);

    	   BufferedImage resized = new BufferedImage(newWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
    	   Graphics2D g2d = resized.createGraphics();
    	   g2d.drawImage(scaledImage, 0, 0, null);
    	   g2d.dispose();

    	   if (newWidth > maxWidth) {
    	    int x = (newWidth - maxWidth) / 2;
    	    resized = resized.getSubimage(x, 0, maxWidth, targetHeight);
    	   }

    	   return new ImageIcon(resized);
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	   return null;
    	  }
    	 }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
        if (e.getSource() == printItem) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
                int y = 20;
                g2d.drawString("ORDEM DE SERVIÇO", 250, y);
                y += 20;
                g2d.drawString("Cliente: " + clienteField.getText(), 50, y);
                g2d.drawString("Larg.Tec: " + larguraTecidoField.getText(), 350, y);
                y += 15;
                g2d.drawString("Papel: " + papelField.getText(), 50, y);
                g2d.drawString("Larg. Impressão: " + larguraImpressaoField.getText(), 350, y);
                y += 15;
                g2d.drawString("Tecido: " + tecidoField.getText(), 50, y);
                y += 15;
                g2d.drawString("Data: " + dataField.getText(), 50, y);
                g2d.drawString("Hora: " + horaField.getText(), 200, y);

                y += 20;
                g2d.drawString("Tec Cliente: " + (checkTecCliente.isSelected() ? "✔️" : "❌"), 50, y);
                g2d.drawString("Só Impressão: " + (checkSoImpressao.isSelected() ? "✔️" : "❌"), 200, y);
                y += 15;
                g2d.drawString("Tec Sublimatec: " + (checkTecSublimatec.isSelected() ? "✔️" : "❌"), 50, y);
                g2d.drawString("Calandra: " + (checkCalandra.isSelected() ? "✔️" : "❌"), 200, y);

                int startY = y + 30;
                int spacingY = 75;

                for (int i = 0; i < 5; i++) {
                    int currentY = startY + (i * spacingY);

                    // Desenhar imagem
                    BufferedImage img = imagensLinhas[i];
                    if (img != null) {
                        int targetH = 65;
                        int maxW = 45;

                        double aspect = (double) img.getWidth() / img.getHeight();
                        int scaledW = (int) (targetH * aspect);

                        BufferedImage croppedImg = img;

                        // Se imagem for mais larga que alta, crop central horizontal
                        if (img.getWidth() > img.getHeight()) {
                            int cropX = (img.getWidth() - img.getHeight()) / 2;
                            croppedImg = img.getSubimage(cropX, 0, img.getHeight(), img.getHeight());
                        }

                        // Redimensiona para altura de 65
                        Image scaledImage = croppedImg.getScaledInstance(maxW, targetH, Image.SCALE_SMOOTH);

                        // Centraliza verticalmente no bloco (opcional)
                        int imgX = 45;
                        int imgY = currentY;

                        g2d.drawImage(scaledImage, imgX, imgY, maxW, targetH, null);
                    }


                    int blockX = 100;
                    g2d.drawRect(blockX, currentY, 500, 65);
                    String refText = refs[i] != null ? refs[i] : "__________";
                    String pastaText = pastas[i] != null ? pastas[i] : "__________";
                    g2d.drawString("REF: " + refText + "   MTS: _________   PASTA: " + pastaText, blockX + 10, currentY + 15);

                    g2d.drawString("Ploteiro: ___________  Data:__/__/__  Máquina: ______", blockX + 10, currentY + 30);
                    g2d.drawString("Op. Calandra: _________  Data:__/__/__", blockX + 10, currentY + 45);
                    g2d.drawString("Conferente: ___________  Data:__/__/__  Revisão: ___", blockX + 10, currentY + 60);
                }

                return Printable.PAGE_EXISTS;
            });

            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
