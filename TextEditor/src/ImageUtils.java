import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static BufferedImage loadAndCropImage(String path, int targetHeight, int maxWidth) throws IOException {
        BufferedImage original = ImageIO.read(new File(path));
        int newWidth = (int) ((double) original.getWidth() / original.getHeight() * targetHeight);
        Image scaledImage = original.getScaledInstance(newWidth, targetHeight, Image.SCALE_SMOOTH);

        BufferedImage resized = new BufferedImage(newWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        if (newWidth > maxWidth) {
            int x = (newWidth - maxWidth) / 2;
            resized = resized.getSubimage(x, 0, maxWidth, targetHeight);
        }
        return resized;
    }
}