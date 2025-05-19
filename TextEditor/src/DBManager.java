import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String URL = "jdbc:sqlite:ordens_servico.db";

    // O bloco estático roda uma única vez, quando a classe é carregada na memória
    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Observação: esta tabela será criada se não existir.
            // A gente pode adicionar mais colunas depois conforme o projeto evolui.
            String sql = """
                CREATE TABLE IF NOT EXISTS ordem_servico (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente TEXT,
                    data TEXT,
                    hora TEXT,
                    papel TEXT,
                    tecido TEXT,
                    tamanho TEXT,
                    checkbox1 INTEGER, -- 0 = desmarcado, 1 = marcado
                    checkbox2 INTEGER, -- mesma ideia do checkbox1
                    imagem BLOB         -- esse campo é opcional, serve para guardar imagem em bytes
                );
            """;

            stmt.execute(sql);
            System.out.println("Tabela verificada/criada com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Método que retorna a conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}