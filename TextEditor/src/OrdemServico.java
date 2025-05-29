import java.awt.image.BufferedImage;

public class OrdemServico {
    //chave primária gerada pelo banco
    private int id;

    //campos originais da folha
    private String cliente;
    private String data;
    private String hora;
    private String papel;
    private String tecido;
    private String larguraTecido;
    private String larguraImpressao;

    //check-boxes de tecido
    private boolean tecCliente;
    private boolean tecSublimatec;
    private boolean soImpressao;
    private boolean calandra;

    //campos de linhas “Pasta”
    private String[] refs;
    private String[] mts; 
    private String[] pastas;
    // === imagens (em memória, BLOB no futuro) ===
    private BufferedImage[] imagens;

    // construtor vazio
    public OrdemServico() {}

    //getters e setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPapel() {
        return papel;
    }
    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String[] getMts() {
        return mts;
    }

    public void setMts(String[] mts) {
        this.mts = mts;
    }

    public String getTecido() {
        return tecido;
    }
    public void setTecido(String tecido) {
        this.tecido = tecido;
    }

    public String getLarguraTecido() {
        return larguraTecido;
    }
    public void setLarguraTecido(String larguraTecido) {
        this.larguraTecido = larguraTecido;
    }

    public String getLarguraImpressao() {
        return larguraImpressao;
    }
    public void setLarguraImpressao(String larguraImpressao) {
        this.larguraImpressao = larguraImpressao;
    }

    public boolean isTecCliente() {
        return tecCliente;
    }
    public void setTecCliente(boolean tecCliente) {
        this.tecCliente = tecCliente;
    }

    public boolean isTecSublimatec() {
        return tecSublimatec;
    }
    public void setTecSublimatec(boolean tecSublimatec) {
        this.tecSublimatec = tecSublimatec;
    }

    public boolean isSoImpressao() {
        return soImpressao;
    }
    public void setSoImpressao(boolean soImpressao) {
        this.soImpressao = soImpressao;
    }

    public boolean isCalandra() {
        return calandra;
    }
    public void setCalandra(boolean calandra) {
        this.calandra = calandra;
    }

    public String[] getRefs() {
        return refs;
    }
    public void setRefs(String[] refs) {
        this.refs = refs;
    }

    public String[] getPastas() {
        return pastas;
    }
    public void setPastas(String[] pastas) {
        this.pastas = pastas;
    }

    public BufferedImage[] getImagens() {
        return imagens;
    }
    public void setImagens(BufferedImage[] imagens) {
        this.imagens = imagens;
    }
}