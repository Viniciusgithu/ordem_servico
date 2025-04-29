import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

public class PrintUtils {

    public static Printable createPrintable(
            String cliente,
            String larguraTec,
            String papel,
            String larguraImp,
            String tecido,
            String data,
            String hora,
            boolean[] checks,
            BufferedImage[] imagens,
            String[] refs,
            String[] pastas
    ) {
        return (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            Font font = new Font("Monospaced", Font.PLAIN, 12);
            g2d.setFont(font);

            int y = 20;
            g2d.drawString("ORDEM DE SERVIÇO", 250, y);
            y += 20;
            g2d.drawString("Cliente: " + cliente, 50, y);
            g2d.drawString("Larg.Tec: " + larguraTec, 350, y);
            y += 15;
            g2d.drawString("Papel: " + papel, 50, y);
            g2d.drawString("Larg. Impressão: " + larguraImp, 350, y);
            y += 15;
            g2d.drawString("Tecido: " + tecido, 50, y);
            y += 15;
            g2d.drawString("Data: " + data, 50, y);
            g2d.drawString("Hora: " + hora, 200, y);
            y += 20;

            // checks[0]=tecCliente, [1]=soImp, [2]=tecSub, [3]=calandra
            String[] labels = {"Tec Cliente", "Só Impressão", "Tec Sublimatec", "Calandra"};
            for (int i = 0; i < checks.length; i++) {
                g2d.drawString(labels[i] + ": " + (checks[i] ? "✔️" : "❌"), 50 + (i%2)*150, y + (i/2)*15);
            }

            int startY = y + 30;
            int spacingY = 75;
            for (int i = 0; i < imagens.length; i++) {
                int currentY = startY + i * spacingY;
                BufferedImage img = imagens[i];
                if (img != null) {
                    int targetH = 65, maxW = 45;
                    double aspect = (double) img.getWidth() / img.getHeight();
                    BufferedImage cropped = img;
                    if (img.getWidth() > img.getHeight()) {
                        int cropX = (img.getWidth() - img.getHeight()) / 2;
                        cropped = img.getSubimage(cropX, 0, img.getHeight(), img.getHeight());
                    }
                    Image scaled = cropped.getScaledInstance(maxW, targetH, Image.SCALE_SMOOTH);
                    g2d.drawImage(scaled, 45, currentY, maxW, targetH, null);
                }
                int blockX = 100;
                g2d.drawRect(blockX, currentY, 500, 65);
                String refText = refs[i] != null ? refs[i] : "__________";
                String pastaText = pastas[i] != null ? pastas[i] : "__________";
                g2d.drawString("REF: " + refText + "   MTS: _________   PASTA: " + pastaText, blockX + 10, currentY + 15);
                g2d.drawString("Ploteiro: ___________  Data:__/__/__  Máquina: ______", blockX + 10, currentY + 30);
                g2d.drawString("Op. Calandra: _________  Data:__/__/__", blockX + 10, currentY + 45);
                g2d.drawString("Conferente: ___________  Data:__/__/__  Revisão: ___", blockX + 10, currentY + 60);
            }
            return Printable.PAGE_EXISTS;
        };
    }
}