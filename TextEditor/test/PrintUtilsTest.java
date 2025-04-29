import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class PrintUtilsTest {

    @Test
    void printableDeveAceitarPrimeiraPaginaERejeitarDemais() throws PrinterException {
        //Dados fictícios para o teste
        String cliente    = "Cliente XYZ";
        String larguraTec = "150";
        String papel      = "Papel A4";
        String larguraImp = "140";
        String tecido     = "Tecido ABC";
        String data       = "01/05/2025";
        String hora       = "14:00";
        boolean[] checks  = { true, false, true, false };

        //Preencher arrays de imagens, refs e pastas
        BufferedImage[] imagens = new BufferedImage[5];
        for (int i = 0; i < imagens.length; i++) {
            // criei um bitmap pequeno só para exercitar o print
            imagens[i] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }
        String[] refs   = { "ref1", "ref2", "ref3", "ref4", "ref5" };
        String[] pastas = { "path1", "path2", "path3", "path4", "path5" };

        //Printable
        Printable printable = PrintUtils.createPrintable(
                cliente, larguraTec, papel, larguraImp,
                tecido, data, hora, checks,
                imagens, refs, pastas
        );

        //Prepara um "canvas" em memória
        BufferedImage canvas = new BufferedImage(600, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();

        //Página 0 deve existir
        int resultPage0 = printable.print(g2d, new PageFormat(), 0);
        assertEquals(Printable.PAGE_EXISTS, resultPage0,
                "Página 0 deveria ser impressa (PAGE_EXISTS)");

        //Página 1 (e seguintes) não devem existir
        int resultPage1 = printable.print(g2d, new PageFormat(), 1);
        assertEquals(Printable.NO_SUCH_PAGE, resultPage1,
                "Página 1 não deveria existir (NO_SUCH_PAGE)");
    }
}