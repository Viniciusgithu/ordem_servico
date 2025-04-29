import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static BufferedImage cropAndScale(BufferedImage original, int targetH, int maxW) {
        // copia e adapta o código de leitura
        int w = original.getWidth(), h = original.getHeight();

        //crop central se for muito largo
        BufferedImage cropped = original;
        if (w > h) {
            int cropX = (w - h) / 2;
            cropped = original.getSubimage(cropX, 0, h, h);
        }

        //escala proporcional e limita largura
        int newW = (int)((double)cropped.getWidth()/cropped.getHeight()*targetH);
        if (newW > maxW) newW = maxW;

        Image tmp = cropped.getScaledInstance(newW, targetH, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    public static BufferedImage loadAndCropImage(String path, int targetHeight, int maxWidth) throws IOException {
        BufferedImage original = ImageIO.read(new File(path));
        return cropAndScale(original, targetHeight, maxWidth);
    }
}