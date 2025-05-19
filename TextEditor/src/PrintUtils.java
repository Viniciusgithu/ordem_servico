import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.List;

import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.List;

public class PrintUtils {

    public static Printable createMultiPagePrintable(List<OrdemServico> ordens) {
        return (graphics, pageFormat, pageIndex) -> {
            if (pageIndex >= ordens.size()) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            OrdemServico os = ordens.get(pageIndex);

            int y = 50;
            g2d.drawString("Cliente: " + os.getCliente(), 50, y);
            g2d.drawString("Larg.Tec: " + os.getLarguraTecido(), 350, y);
            y += 15;
            g2d.drawString("Papel: " + os.getPapel(), 50, y);
            g2d.drawString("Larg. Impressão: " + os.getLarguraImpressao(), 350, y);
            y += 15;
            g2d.drawString("Tecido: " + os.getTecido(), 50, y);
            y += 15;
            g2d.drawString("Data: " + os.getData(), 50, y);
            g2d.drawString("Hora: " + os.getHora(), 200, y);

            // checkboxes
            String[] labels = { "Tec cliente", "Tec sublimatec", "Só impressão", "Calandra" };
            boolean[] states = {
                    os.isTecCliente(),
                    os.isTecSublimatec(),
                    os.isSoImpressao(),
                    os.isCalandra()
            };
            y += 30;
            for (int i = 0; i < labels.length; i++) {
                g2d.drawString(labels[i] + ": " + (states[i] ? "✔" : "✘"),
                        50 + (i % 2) * 150,
                        y + (i / 2) * 15);
            }

            // imagens (se houver)
            BufferedImage[] imgs = os.getImagens();
            if (imgs != null) {
                y += 40;
                for (int i = 0; i < imgs.length; i++) {
                    BufferedImage img = imgs[i];
                    if (img != null) {
                        g2d.drawImage(img, 50 + (i % 2) * 200,
                                y + (i / 2) * (img.getHeight() + 10), null);
                    }
                }
            }

            // linhas de pasta
            String[] refs = os.getRefs();
            String[] mts  = os.getMts();
            String[] pastas = os.getPastas();
            y += 300;
            for (int i = 0; refs != null && i < refs.length; i++) {
                String refTxt   = refs[i]   != null ? refs[i]   : "__________";
                String mtsTxt   = mts[i]    != null ? mts[i]    : "__________";
                String pastaTxt = pastas[i] != null ? pastas[i] : "__________";
                g2d.drawString("REF: " + refTxt + " MTS: " + mtsTxt + " Pasta: " + pastaTxt,
                        50, y + i * 15);
            }

            return Printable.PAGE_EXISTS;
        };
    }
}
