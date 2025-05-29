import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static BufferedImage cropAndScale(BufferedImage original, int targetH, int maxW) {
        if (original == null) {
            return null;
        }
        int w = original.getWidth(), h = original.getHeight();
        BufferedImage cropped = original;
        if (w > h) {
            if (h > 0) {
                int cropX = (w - h) / 2;
                cropped = original.getSubimage(cropX, 0, h, h);
            } else {
                return new BufferedImage(Math.max(1, maxW), Math.max(1, targetH), BufferedImage.TYPE_INT_ARGB);
            }
        }

        int newW = 0;
        if (cropped.getHeight() > 0) { // Evitar divisão por zero se a altura do 'cropped' for 0
            newW = (int) ((double) cropped.getWidth() / cropped.getHeight() * targetH);
        }

        if (newW <= 0) newW = 1;
        if (targetH <= 0) targetH = 1; // Assegura que targetH seja positivo
        if (newW > maxW) newW = maxW;


        Image tmp = cropped.getScaledInstance(newW, targetH, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public static BufferedImage loadAndCropImage(String path, int targetHeight, int maxWidth) throws IOException {
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new IOException("Arquivo de imagem não encontrado em: " + path);
        }
        BufferedImage original = ImageIO.read(imageFile);
        if (original == null) {
            throw new IOException("Não foi possível ler a imagem do arquivo (formato não suportado ou arquivo corrompido): " + path);
        }
        return cropAndScale(original, targetHeight, maxWidth);
    }

    public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        if (bi == null) {
            return null;
        }
        // Usando try-with-resources para ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bi, format, baos);
            baos.flush();
            return baos.toByteArray();
        } // baos.close() é chamado automaticamente
    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        // Usando try-with-resources para ByteArrayInputStream
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(bais);
        } // bais.close() é chamado automaticamente
    }
}