import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.print.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextEditor extends JFrame implements ActionListener {

    JTextField clienteField, dataField, horaField, papelField, tecidoField, larguraTecidoField, larguraImpressaoField;
    JCheckBox checkTecCliente, checkTecSublimatec, checkSoImpressao, checkCalandra;
    BufferedImage[] imagensLinhas = new BufferedImage[5];
    String[] refs = new String[5];
    String[] pastas = new String[5];
    private final JLabel[] imageLabels = new JLabel[5];


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
                        BufferedImage img = ImageUtils.loadAndCropImage(file.getAbsolutePath(), 65, 50);

                        imageLabels[index].setIcon(new ImageIcon(img));
                        imageLabels[index].setText(""); // tira o texto

                        btn.setVisible(false);
                        JOptionPane.showMessageDialog(this, "Imagem " + (index + 1) + " carregada com sucesso!");

                    } catch (IOException ex) {
                        Logger.getLogger(TextEditor.class.getName())
                                .log(Level.SEVERE, "falha ao carregar imagem", ex);
                        JOptionPane.showMessageDialog(this,
                                "Erro ao carregar imagem:\n" + ex.getMessage(),
                                "Erro", JOptionPane.ERROR_MESSAGE);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
        if (e.getSource() == printItem) {
            PrinterJob job = PrinterJob.getPrinterJob();
            boolean[] checks = {
                    checkTecCliente.isSelected(),
                    checkSoImpressao.isSelected(),
                    checkTecSublimatec.isSelected(),
                    checkCalandra.isSelected()
            };
            job.setPrintable(PrintUtils.createPrintable(
                    clienteField.getText(),
                    larguraTecidoField.getText(),
                    papelField.getText(),
                    larguraImpressaoField.getText(),
                    tecidoField.getText(),
                    dataField.getText(),
                    horaField.getText(),
                    checks,
                    imagensLinhas,
                    refs,
                    pastas
            ));

            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
