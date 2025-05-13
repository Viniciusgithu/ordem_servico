import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.List;

public class PrintUtils {

    public static Printable createMultiPagePrintable(List<OrdemServico> ordens) {
        return (graphics, pageFormat, pageIndex) -> {
            if (pageIndex >= ordens.size()) return Printable.NO_SUCH_PAGE;

            OrdemServico os = ordens.get(pageIndex);
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));

            int y = 20;
            g2d.drawString("ORDEM DE SERVIÇO", 250, y);
            y += 20;
            g2d.drawString("Cliente: " + os.cliente, 50, y);
            g2d.drawString("Larg.Tec: " + os.larguraTecido, 350, y);
            y += 15;
            g2d.drawString("Papel: " + os.papel, 50, y);
            g2d.drawString("Larg. Impressão: " + os.larguraImpressao, 350, y);
            y += 15;
            g2d.drawString("Tecido: " + os.tecido, 50, y);
            y += 15;
            g2d.drawString("Data: " + os.data, 50, y);
            g2d.drawString("Hora: " + os.hora, 200, y);
            y += 20;

            String[] labels = {"Tec Cliente", "Só Impressão", "Tec Sublimatec", "Calandra"};
            for (int i = 0; i < os.checks.length; i++) {
                g2d.drawString(labels[i] + ": " + (os.checks[i] ? "✔️" : "❌"), 50 + (i % 2) * 150, y + (i / 2) * 15);
            }

            int startY = y + 30;
            int spacingY = 75;
            for (int i = 0; i < os.imagens.length; i++) {
                int currentY = startY + i * spacingY;
                BufferedImage img = os.imagens[i];
                if (img != null) {
                    Image scaled = img.getScaledInstance(65, 95, Image.SCALE_SMOOTH);
                    g2d.drawImage(scaled, 0, currentY, 100, 65, null);
                }
                int blockX = 100;
                g2d.drawRect(blockX, currentY, 500, 65);
                String refText = os.refs[i] != null ? os.refs[i] : "__________";
                String pastaText = os.pastas[i] != null ? os.pastas[i] : "__________";
                g2d.drawString("REF: " + refText + "   MTS: _________   PASTA: " + pastaText, blockX + 10, currentY + 15);
                g2d.drawString("Ploteiro: ___________  Data:__/__/__  Máquina: ______", blockX + 10, currentY + 30);
                g2d.drawString("Op. Calandra: _________  Data:__/__/__", blockX + 10, currentY + 45);
                g2d.drawString("Conferente: ___________  Data:__/__/__  Revisão: ___", blockX + 10, currentY + 60);
            }

            return Printable.PAGE_EXISTS;
        };
    }
}
