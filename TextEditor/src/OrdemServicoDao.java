import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDao {
    private Connection conn;

    public OrdemServicoDao(Connection conn) throws SQLException {
        this.conn = conn;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS ordem_servico (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente TEXT,
                data TEXT,
                hora TEXT,
                papel TEXT,
                tecido TEXT,
                larguraTecido TEXT,
                larguraImpressao TEXT,
                tecCliente INTEGER,
                tecSublimatec INTEGER,
                soImpressao INTEGER,
                calandra INTEGER
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void save(OrdemServico os) throws SQLException {
        String sql = """
            INSERT INTO ordem_servico (
                cliente, data, hora, papel, tecido,
                larguraTecido, larguraImpressao,
                tecCliente, tecSublimatec, soImpressao, calandra
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, os.getCliente());
            stmt.setString(2, os.getData());
            stmt.setString(3, os.getHora());
            stmt.setString(4, os.getPapel());
            stmt.setString(5, os.getTecido());
            stmt.setString(6, os.getLarguraTecido());
            stmt.setString(7, os.getLarguraImpressao());
            stmt.setInt(8, os.isTecCliente() ? 1 : 0);
            stmt.setInt(9, os.isTecSublimatec() ? 1 : 0);
            stmt.setInt(10, os.isSoImpressao() ? 1 : 0);
            stmt.setInt(11, os.isCalandra() ? 1 : 0);

            stmt.executeUpdate();
        }
    }

    // Podemos adicionar depois: findById(), findAll(), update(), delete()
}