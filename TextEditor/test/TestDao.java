import java.sql.Connection;

public class TestDao {
    public static void main(String[] args) {
        try {
            // Chama o DBManager da pasta TextEditor/src
            Connection conn = DBManager.getConnection();
            OrdemServicoDao dao = new OrdemServicoDao(conn);

            OrdemServico os = new OrdemServico();
            os.setCliente("Cliente de Teste");
            os.setData("19/05/2025");
            os.setHora("14:30");
            os.setPapel("Papel Teste");
            os.setTecido("Algodão");
            os.setLarguraTecido("1.60m");
            os.setLarguraImpressao("1.50m");
            os.setTecCliente(true);
            os.setTecSublimatec(false);
            os.setSoImpressao(false);
            os.setCalandra(true);

            dao.save(os);
            System.out.println("✅ Ordem de serviço salva com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar ordem de serviço:");
            e.printStackTrace();
        }
    }
}