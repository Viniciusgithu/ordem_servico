import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.sql.SQLException; // Não usado diretamente aqui, mas pode ser útil
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*; // Import para SQLException
import javax.swing.*;

public class TextEditor extends JFrame implements ActionListener {

    // Campos do cabeçalho da OS (comuns a uma OS)
    JTextField clienteField, dataField, horaField, papelField, tecidoField, larguraTecidoField, larguraImpressaoField;
    JCheckBox checkTecCliente, checkTecSublimatec, checkSoImpressao, checkCalandra;

    JTabbedPane tabbedPane; // Abas, onde cada aba é um LinhaPanel representando uma OS
    // List<LinhaPanel> paginas = new ArrayList<>(); // Não precisamos mais de 'paginas' se cada aba é um LinhaPanel

    JMenuItem printItem, saveItem, exitItem, addPageItem; // Adicionado saveItem

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ordem de Serviço");
        setSize(850, 700); // Ajustar tamanho conforme necessidade
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel superior com campos de cabeçalho (comuns a OS ativa)
        JPanel infoPanel = criarPainelCabecalho();
        add(infoPanel, BorderLayout.NORTH);

        // Abas
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Adicionar a primeira "Ordem de Serviço" (aba com LinhaPanel)
        adicionarNovaAbaOS();

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");
        addPageItem = new JMenuItem("Nova Ordem de Serviço (Aba)");
        saveItem = new JMenuItem("Salvar Ordem Atual"); // Botão Salvar
        printItem = new JMenuItem("Imprimir Ordem Atual");
        exitItem = new JMenuItem("Sair");

        addPageItem.addActionListener(this);
        saveItem.addActionListener(this);
        printItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(addPageItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.addSeparator(); // Separador visual
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private JPanel criarPainelCabecalho() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações da Ordem de Serviço Ativa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.fill = GridBagConstraints.HORIZONTAL; // Removido para melhor ajuste dos JTextFields

        int y = 0;
        // Linha 0
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        clienteField = new JTextField(20); infoPanel.add(clienteField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Tecido:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        larguraTecidoField = new JTextField(10); infoPanel.add(larguraTecidoField, gbc);

        // Linha 1
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Papel:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        papelField = new JTextField(20); infoPanel.add(papelField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Largura Impressão:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        larguraImpressaoField = new JTextField(10); infoPanel.add(larguraImpressaoField, gbc);
        
        // Linha 2
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Tecido:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        tecidoField = new JTextField(); infoPanel.add(tecidoField, gbc);
        gbc.gridwidth = 1; // Resetar gridwidth

        // Linha 3
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        dataField = new JTextField(10); infoPanel.add(dataField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        infoPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        horaField = new JTextField(10); infoPanel.add(horaField, gbc);

        // Linha 4 - Checkboxes
        y++;
        gbc.gridy = y; gbc.gridx = 0; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Usar FlowLayout
        checkTecCliente = new JCheckBox("Tec Cliente");
        checkTecSublimatec = new JCheckBox("Tec Sublimatec");
        checkSoImpressao = new JCheckBox("Só Impressão");
        checkCalandra = new JCheckBox("Calandra");
        checkboxPanel.add(checkTecCliente);
        checkboxPanel.add(checkTecSublimatec);
        checkboxPanel.add(checkSoImpressao);
        checkboxPanel.add(checkCalandra);
        infoPanel.add(checkboxPanel, gbc);

        return infoPanel;
    }

    private void adicionarNovaAbaOS() {
        LinhaPanel novaOSPanel = new LinhaPanel(); // Cada aba é um LinhaPanel completo
        // Envolver o LinhaPanel em um JScrollPane para permitir rolagem se tiver muitos itens
        JScrollPane scrollPane = new JScrollPane(novaOSPanel,
                                               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        int numeroDaAba = tabbedPane.getTabCount() + 1;
        tabbedPane.addTab("OS " + numeroDaAba, scrollPane);
        tabbedPane.setSelectedComponent(scrollPane); // Seleciona a nova aba

        // Limpar campos do cabeçalho para a nova OS (opcional, mas bom para clareza)
        limparCamposCabecalho();
        novaOSPanel.limparCampos(); // Limpa os campos dentro do LinhaPanel também
    }

    private void limparCamposCabecalho() {
        clienteField.setText("");
        dataField.setText("");
        horaField.setText("");
        papelField.setText("");
        tecidoField.setText("");
        larguraTecidoField.setText("");
        larguraImpressaoField.setText("");
        checkTecCliente.setSelected(false);
        checkTecSublimatec.setSelected(false);
        checkSoImpressao.setSelected(false);
        checkCalandra.setSelected(false);
    }

    // Método para pegar a instância do LinhaPanel da aba atualmente selecionada
    private LinhaPanel getPainelDaAbaAtual() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) selectedComponent;
            Component view = scrollPane.getViewport().getView();
            if (view instanceof LinhaPanel) {
                return (LinhaPanel) view;
            }
        } else if (selectedComponent instanceof LinhaPanel) { // Caso não esteja usando JScrollPane
             return (LinhaPanel) selectedComponent;
        }
        return null; // Se nenhuma aba válida estiver selecionada ou a estrutura for diferente
    }
    
    private OrdemServico construirOSDaAbaAtual() {
        LinhaPanel painelAtual = getPainelDaAbaAtual();
        if (painelAtual == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma aba de Ordem de Serviço selecionada ou válida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        OrdemServico os = new OrdemServico();
        // Preencher dados do cabeçalho
        os.setCliente(clienteField.getText().trim());
        os.setData(dataField.getText().trim());
        os.setHora(horaField.getText().trim());
        os.setPapel(papelField.getText().trim());
        os.setTecido(tecidoField.getText().trim());
        os.setLarguraTecido(larguraTecidoField.getText().trim());
        os.setLarguraImpressao(larguraImpressaoField.getText().trim());

        os.setTecCliente(checkTecCliente.isSelected());
        os.setTecSublimatec(checkTecSublimatec.isSelected());
        os.setSoImpressao(checkSoImpressao.isSelected());
        os.setCalandra(checkCalandra.isSelected());

        // Preencher dados dos itens (do LinhaPanel)
        os.setRefs(painelAtual.getRefs());
        os.setMts(painelAtual.getMts()); // Agora deve funcionar
        os.setPastas(painelAtual.getPastas());
        os.setImagens(painelAtual.getImagens());
        
        return os;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPageItem) {
            adicionarNovaAbaOS();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        } else if (e.getSource() == saveItem) { // Lógica para SALVAR
            OrdemServico osParaSalvar = construirOSDaAbaAtual();
            if (osParaSalvar != null) {
                OrdemServicoDao dao = new OrdemServicoDao();
                try {
                    dao.save(osParaSalvar);
                    JOptionPane.showMessageDialog(this, 
                        "Ordem de Serviço (ID: " + osParaSalvar.getId() + ") salva com sucesso!", 
                        "Salvo", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException sqlEx) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro de Banco de Dados ao salvar OS:\n" + sqlEx.getMessage(), 
                        "Erro SQL", 
                        JOptionPane.ERROR_MESSAGE);
                    sqlEx.printStackTrace(); // Para debug no console
                } catch (IOException ioEx) {
                     JOptionPane.showMessageDialog(this, 
                        "Erro de I/O (provavelmente imagem) ao salvar OS:\n" + ioEx.getMessage(), 
                        "Erro de I/O", 
                        JOptionPane.ERROR_MESSAGE);
                    ioEx.printStackTrace(); // Para debug no console
                }
            }
        } else if (e.getSource() == printItem) { // Lógica para IMPRIMIR
            OrdemServico osParaImprimir = construirOSDaAbaAtual();
            if (osParaImprimir == null) {
                return; // Mensagem de erro já foi mostrada por construirOSDaAbaAtual()
            }

            PrinterJob job = PrinterJob.getPrinterJob();
            // O PrintUtils.createMultiPagePrintable espera uma Lista, então colocamos nossa OS única em uma lista
            List<OrdemServico> listaParaImpressao = new ArrayList<>();
            listaParaImpressao.add(osParaImprimir);
            
            job.setPrintable(PrintUtils.createMultiPagePrintable(listaParaImpressao)); // Assumindo que PrintUtils está ok
            
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, "Erro durante a impressão", ex);
                     JOptionPane.showMessageDialog(this, 
                        "Erro durante a impressão:\n" + ex.getMessage(), 
                        "Erro de Impressão", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Main para executar o editor (opcional, se você já tem um Main separado)
    public static void main(String[] args) {
         // Definir o Look and Feel do sistema para uma aparência mais nativa (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.INFO, "Não foi possível definir o Look and Feel do sistema.", ex);
        }
        
        SwingUtilities.invokeLater(() -> new TextEditor());
    }
}