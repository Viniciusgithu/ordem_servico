import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// Não vamosprecisar mais importar List ou ArrayList aqui, a menos que os métodos de leitura sejam implementados.

public class OrdemServicoDao {

    public OrdemServicoDao() {
    }

    /**
     *
     * @param os O objeto OrdemServico a ser salvo.
     * @throws SQLException Se ocorrer um erro de banco de dados durante a operação.
     * @throws IOException Se ocorrer um erro ao converter imagens para byte array.
     */
    public void save(OrdemServico os) throws SQLException, IOException {
        Connection conn = null;
        boolean originalAutoCommitState = true;

        // SQL para inserir na tabela principal 'ordem_servico'
        String sqlInsertOrdemServico = """
            INSERT INTO ordem_servico (
                cliente, data, hora, papel, tecido,
                larguraTecido, larguraImpressao,
                tecCliente, tecSublimatec, soImpressao, calandra
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        //inserir itens de linha na tabela 'linha_item_os'
        String sqlInsertLinhaItem = """
            INSERT INTO linha_item_os (
                ordem_servico_id, ref_item, mts_item, pasta_item
            ) VALUES (?, ?, ?, ?)
        """;

        //inserir imagens na tabela 'imagem_os'
        String sqlInsertImagem = """
            INSERT INTO imagem_os (
                ordem_servico_id, dados_imagem
            ) VALUES (?, ?)
        """;

        try {
            conn = DBManager.getConnection();
            originalAutoCommitState = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // 1. Salvar a Ordem de Serviço principal e obter o ID gerado
            try (PreparedStatement pstmtOs = conn.prepareStatement(sqlInsertOrdemServico, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOs.setString(1, os.getCliente());
                pstmtOs.setString(2, os.getData());
                pstmtOs.setString(3, os.getHora());
                pstmtOs.setString(4, os.getPapel());
                pstmtOs.setString(5, os.getTecido());
                pstmtOs.setString(6, os.getLarguraTecido());
                pstmtOs.setString(7, os.getLarguraImpressao());
                pstmtOs.setInt(8, os.isTecCliente() ? 1 : 0);
                pstmtOs.setInt(9, os.isTecSublimatec() ? 1 : 0);
                pstmtOs.setInt(10, os.isSoImpressao() ? 1 : 0);
                pstmtOs.setInt(11, os.isCalandra() ? 1 : 0);

                int affectedRows = pstmtOs.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Falha ao salvar Ordem de Serviço (tabela principal), nenhuma linha afetada.");
                }

                // Obter o ID gerado para a Ordem de Serviço
                try (ResultSet generatedKeys = pstmtOs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        os.setId(generatedKeys.getInt(1)); // Define o ID no objeto OrdemServico
                    } else {
                        throw new SQLException("Falha ao salvar Ordem de Serviço (tabela principal), ID não obtido.");
                    }
                }
            }

            // 2. Salvar os itens de linha (refs, mts, pastas) se existirem
            String[] refs = os.getRefs();
            String[] mts = os.getMts();
            String[] pastas = os.getPastas();

            if (refs != null && refs.length > 0) {
                try (PreparedStatement pstmtLinha = conn.prepareStatement(sqlInsertLinhaItem)) {
                    for (int i = 0; i < refs.length; i++) {
                        pstmtLinha.setInt(1, os.getId()); 
                        pstmtLinha.setString(2, refs[i]);
                        pstmtLinha.setString(3, (mts != null && i < mts.length) ? mts[i] : null);
                        pstmtLinha.setString(4, (pastas != null && i < pastas.length) ? pastas[i] : null);
                        pstmtLinha.addBatch();
                    }
                    pstmtLinha.executeBatch(); // Executa todas as inserções de itens de linha
                }
            }

            // 3. Salvar as imagens se existirem
            BufferedImage[] imagens = os.getImagens();
            if (imagens != null && imagens.length > 0) {
                try (PreparedStatement pstmtImagem = conn.prepareStatement(sqlInsertImagem)) {
                    for (BufferedImage img : imagens) {
                        if (img != null) {
                            
                            byte[] imgBytes = ImageUtils.toByteArray(img, "png");
                            if (imgBytes != null && imgBytes.length > 0) {
                                pstmtImagem.setInt(1, os.getId());
                                pstmtImagem.setBytes(2, imgBytes);
                                pstmtImagem.addBatch(); // Adiciona ao lote
                            }
                        }
                    }
                    pstmtImagem.executeBatch(); // Executa todas as inserções de imagens
                }
            }

            conn.commit(); // Se tudo ocorreu bem, efetiva a transação
            System.out.println("Ordem de Serviço ID " + os.getId() + ", seus itens de linha e imagens foram salvos com sucesso.");

        } catch (SQLException | IOException e) { 
            if (conn != null) {
                try {
                    System.err.println("Erro durante a transação. Tentando reverter (rollback)...");
                    conn.rollback(); // Reverte a transação em caso de erro
                    System.err.println("Rollback realizado com sucesso.");
                } catch (SQLException exRollback) {
                    System.err.println("ERRO CRÍTICO: Falha ao tentar reverter a transação: " + exRollback.getMessage());
                    e.addSuppressed(exRollback);
                }
            }
            if (e instanceof SQLException sqlEx) { // Se for SQLException, lança ela mesma
                throw sqlEx;
            } else { // Se for IOException (ou outro tipo que não seja SQLException)
                throw new SQLException("Erro de IO ao processar imagem para salvamento: " + e.getMessage(), e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommitState);
                    conn.close(); 
                } catch (SQLException exClose) {
                    System.err.println("Erro ao tentar restaurar autocommit ou fechar conexão: " + exClose.getMessage());
                }
            }
        }
    }

    // --- MÉTODOS DE LEITURA, ATUALIZAÇÃO E EXCLUSÃO (A SEREM IMPLEMENTADOS) ---
    //
    // public OrdemServico findById(int id) throws SQLException, IOException {
    //     // Lógica para buscar OrdemServico e preencher seus arrays de itens e imagens
    //     // Requer múltiplas consultas ou JOINs e reconstrução do objeto.
    //     return null;
    // }
    //
    // public List<OrdemServico> findAll() throws SQLException, IOException {
    //     // Lógica para buscar todas as OrdensServico.
    //     return new ArrayList<>();
    // }
    //
    // public void update(OrdemServico os) throws SQLException, IOException {
    //     // Lógica para atualizar. Pode ser complexo se os arrays mudarem.
    //     // Geralmente envolve deletar itens/imagens antigos e inserir os novos.
    // }
    //
    // public void delete(int id) throws SQLException {
    //     // Com ON DELETE CASCADE, deletar da tabela 'ordem_servico' deve remover
    //     // automaticamente os registros relacionados em 'linha_item_os' e 'imagem_os'.
    //     String sql = "DELETE FROM ordem_servico WHERE id = ?";
    //     try (Connection conn = DBManager.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setInt(1, id);
    //         pstmt.executeUpdate();
    //     }
    // }
}