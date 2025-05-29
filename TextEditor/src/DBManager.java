import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String DB_FILENAME = "ordens_servico.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILENAME;

    // Bloco estático para inicializar o banco de dados e criar tabelas se não existirem
    static {
        // --- PASSO 1: TENTAR CARREGAR O DRIVER JDBC EXPLICITAMENTE ---
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("INFO: Driver SQLite JDBC (org.sqlite.JDBC) carregado manualmente com sucesso.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO CRÍTICO: Driver SQLite JDBC (org.sqlite.JDBC) NÃO FOI ENCONTRADO no classpath! " +
                               "Verifique se o arquivo sqlite-jdbc.jar está configurado corretamente no classpath de EXECUÇÃO.");
            e.printStackTrace(); 
            // Lançar uma RuntimeException para parar a aplicação, pois sem o driver, nada do banco funcionará.
            throw new RuntimeException("Falha crítica ao inicializar o DBManager: Driver SQLite não encontrado.", e);
        }
        // --- FIM DO PASSO 1 ---

        // Tenta habilitar o suporte a chaves estrangeiras no SQLite.
        // Esta conexão é separada e temporária, apenas para o PRAGMA.
        try (Connection connForPragma = DriverManager.getConnection(URL); 
             Statement stmtForPragma = connForPragma.createStatement()) {
            stmtForPragma.execute("PRAGMA foreign_keys = ON;");
            System.out.println("INFO: Suporte a chaves estrangeiras (PRAGMA foreign_keys=ON) executado para a URL: " + URL);
        } catch (SQLException e) {
            System.err.println("AVISO: Erro ao tentar executar 'PRAGMA foreign_keys = ON'. " +
                               "Isso pode acontecer se o banco ainda não existe ou está bloqueado. Mensagem: " + e.getMessage());
            // Continuar mesmo assim, a criação da tabela tentará criar o banco se necessário.
        }

        // SQL para criar a tabela principal 'ordem_servico'
        String sqlOrdemServico = """
            CREATE TABLE IF NOT EXISTS ordem_servico (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente TEXT,
                data TEXT,
                hora TEXT,
                papel TEXT,
                tecido TEXT,
                larguraTecido TEXT,
                larguraImpressao TEXT,
                tecCliente INTEGER,      -- 0 para false, 1 para true
                tecSublimatec INTEGER,   -- 0 para false, 1 para true
                soImpressao INTEGER,     -- 0 para false, 1 para true
                calandra INTEGER         -- 0 para false, 1 para true
            );
        """;

        String sqlLinhaItemOS = """
            CREATE TABLE IF NOT EXISTS linha_item_os (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ordem_servico_id INTEGER NOT NULL, -- Chave estrangeira referenciando 'ordem_servico'
                ref_item TEXT,
                mts_item TEXT,
                pasta_item TEXT,
                FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id) ON DELETE CASCADE
            );
        """;

        String sqlImagemOS = """
            CREATE TABLE IF NOT EXISTS imagem_os (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ordem_servico_id INTEGER NOT NULL, -- Chave estrangeira referenciando 'ordem_servico'
                dados_imagem BLOB,                 -- Coluna para armazenar os bytes da imagem
                FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id) ON DELETE CASCADE
            );
        """;

        // Executa os comandos SQL para criar as tabelas
        // Esta conexão criará o arquivo do banco de dados se ele não existir.
        try (Connection connCreateTables = DriverManager.getConnection(URL);
             Statement stmtCreateTables = connCreateTables.createStatement()) {

            stmtCreateTables.execute(sqlOrdemServico);
            System.out.println("INFO: Tabela 'ordem_servico' verificada/criada com sucesso.");

            stmtCreateTables.execute(sqlLinhaItemOS);
            System.out.println("INFO: Tabela 'linha_item_os' verificada/criada com sucesso.");

            stmtCreateTables.execute(sqlImagemOS);
            System.out.println("INFO: Tabela 'imagem_os' verificada/criada com sucesso.");

        } catch (SQLException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível criar/verificar as tabelas do banco de dados. URL: " + URL + ". Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha crítica ao inicializar o banco de dados: criação de tabelas falhou.", e);
        }
    }

    /**
     * Retorna uma nova conexão com o banco de dados SQLite.
     * O chamador é responsável por fechar esta conexão.
     * O driver JDBC já deve ter sido carregado pelo bloco estático.
     * @return um objeto Connection para o banco.
     * @throws SQLException se ocorrer um erro ao obter a conexão (ex: URL inválida, banco bloqueado, ou se, apesar do Class.forName, o driver não se registrou).
     */
    public static Connection getConnection() throws SQLException {
        // O Class.forName("org.sqlite.JDBC") já foi chamado no bloco estático.
        // Se ele falhou, a aplicação já teria parado com uma RuntimeException.
        // Se ele teve sucesso, o driver deve estar registrado com o DriverManager.
        return DriverManager.getConnection(URL);
    }
}