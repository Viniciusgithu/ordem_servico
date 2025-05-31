import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class TextEditor extends JFrame implements ActionListener {

    JTextField clienteField, dataField, horaField, papelField, tecidoField, larguraTecidoField, larguraImpressaoField;
    JCheckBox checkTecCliente, checkTecSublimatec, checkSoImpressao, checkCalandra;

    JTabbedPane tabbedPane;
    List<LinhaPanel> paginas = new ArrayList<>();

    JMenuItem printItem, exitItem, addPageItem;

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ordem de Serviço");
        setSize(800, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel superior com campos de cabeçalho
        JPanel infoPanel = criarPainelCabecalho();
        add(infoPanel, BorderLayout.NORTH);

        // Abas
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Primeira página
        adicionarNovaPagina();

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");
        printItem = new JMenuItem("Imprimir");
        exitItem = new JMenuItem("Sair");
        addPageItem = new JMenuItem("+ Página");

        printItem.addActionListener(this);
        exitItem.addActionListener(this);
        addPageItem.addActionListener(this);

        fileMenu.add(addPageItem);
        fileMenu.add(printItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private JPanel criarPainelCabecalho() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        clienteField = new JTextField();
        infoPanel.add(clienteField, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Tecido:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        larguraTecidoField = new JTextField();
        infoPanel.add(larguraTecidoField, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        infoPanel.add(new JLabel("Papel:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        papelField = new JTextField();
        infoPanel.add(papelField, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Impressão:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        larguraImpressaoField = new JTextField();
        infoPanel.add(larguraImpressaoField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        infoPanel.add(new JLabel("Tecido:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        tecidoField = new JTextField();
        infoPanel.add(tecidoField, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 3; gbc.gridx = 0;
        infoPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        dataField = new JTextField();
        infoPanel.add(dataField, gbc);

        gbc.gridx = 2;
        infoPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 3;
        horaField = new JTextField();
        infoPanel.add(horaField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel checkboxPanel = new JPanel(new GridLayout(1, 4));
        checkTecCliente = new JCheckBox("Tec Cliente");
        checkTecSublimatec = new JCheckBox("Tec Sublimatec");
        checkSoImpressao = new JCheckBox("Só Impressão");
        checkCalandra = new JCheckBox("Calandra");

        // Adiciona os listeners para impedir seleções conflitantes
        checkTecCliente.addActionListener(e -> {
            if (checkTecCliente.isSelected()) {
                checkTecSublimatec.setSelected(false);
            }
        });
        checkTecSublimatec.addActionListener(e -> {
            if (checkTecSublimatec.isSelected()) {
                checkTecCliente.setSelected(false);
            }
        });
        checkSoImpressao.addActionListener(e -> {
            if (checkSoImpressao.isSelected()) {
                checkCalandra.setSelected(false);
            }
        });
        checkCalandra.addActionListener(e -> {
            if (checkCalandra.isSelected()) {
                checkSoImpressao.setSelected(false);
            }
        });

        checkboxPanel.add(checkTecCliente);
        checkboxPanel.add(checkTecSublimatec);
        checkboxPanel.add(checkSoImpressao);
        checkboxPanel.add(checkCalandra);
        infoPanel.add(checkboxPanel, gbc);

        configurarNavegacaoComEnter(clienteField, larguraTecidoField);
        configurarNavegacaoComEnter(larguraTecidoField, papelField);
        configurarNavegacaoComEnter(papelField, larguraImpressaoField);
        configurarNavegacaoComEnter(larguraImpressaoField, tecidoField);
        configurarNavegacaoComEnter(tecidoField, dataField);
        configurarNavegacaoComEnter(dataField, horaField);

        return infoPanel;
    }

    private void configurarNavegacaoComEnter(JTextField atual, JTextField proximo) {
        atual.addActionListener(e -> proximo.requestFocus());
    }

    private void adicionarNovaPagina() {
        LinhaPanel nova = new LinhaPanel();
        paginas.add(nova);
        tabbedPane.addTab("Página " + paginas.size(), nova);

        int index = tabbedPane.indexOfComponent(nova);
        tabbedPane.setTabComponentAt(index, criarTituloAbaComFechar("Página " + paginas.size()));

        // Atualizar visibilidade dos botões fechar em todas as abas
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component c = tabbedPane.getTabComponentAt(i);
            if (c instanceof JPanel) {
                JPanel painel = (JPanel) c;
                for (Component comp : painel.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setVisible(tabbedPane.getTabCount() > 1);
                    }
                }
            }
        }
    }
    private JPanel criarTituloAbaComFechar(String titulo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painel.setOpaque(false);

        JLabel label = new JLabel(titulo + "  ");
        JButton btnFechar = new JButton("X"); // Usar símbolo multiplicação

        btnFechar.setPreferredSize(new Dimension(18, 18));
        btnFechar.setToolTipText("Fechar aba");
        btnFechar.setFocusable(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setFont(new Font("Arial", Font.BOLD, 24));
        btnFechar.setForeground(Color.RED);

        btnFechar.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(painel);
            if (i != -1 && tabbedPane.getTabCount() > 1) {
                paginas.remove(i);
                tabbedPane.remove(i);
            }
        });

        // Se só tiver 1 aba, não mostra o botão fechar
        btnFechar.setVisible(tabbedPane.getTabCount() > 1);

        painel.add(label);
        painel.add(btnFechar);

        return painel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPageItem) {
            adicionarNovaPagina();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        } else if (e.getSource() == printItem) {
            PrinterJob job = PrinterJob.getPrinterJob();

            boolean[] checks = {
                checkTecCliente.isSelected(),
                checkSoImpressao.isSelected(),
                checkTecSublimatec.isSelected(),
                checkCalandra.isSelected()
            };

            List<OrdemServico> ordens = new ArrayList<>();
            for (LinhaPanel panel : paginas) {
                OrdemServico ordem = new OrdemServico();
                ordem.cliente = clienteField.getText();
                ordem.data = dataField.getText();
                ordem.hora = horaField.getText();
                ordem.papel = papelField.getText();
                ordem.tecido = tecidoField.getText();
                ordem.larguraTecido = larguraTecidoField.getText();
                ordem.larguraImpressao = larguraImpressaoField.getText();
                ordem.checks = checks;
                ordem.imagens = panel.getImagens();
                ordem.refs = panel.getRefs();
                ordem.pastas = panel.getPastas();
                ordens.add(ordem);
            }

            job.setPrintable(PrintUtils.createMultiPagePrintable(ordens));
            if (job.printDialog()) {
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
