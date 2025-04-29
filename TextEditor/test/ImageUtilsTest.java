import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

public class ImageUtilsTest {

    @Test
    void quandoImagemMaisLarga_deveCropEscaleCorretamente() {
        //Cria imagem 200×100
        BufferedImage original = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);

        //Chama cropAndScale
        BufferedImage ajustada = ImageUtils.cropAndScale(original, 65, 50);

        //Verifica resultados
        assertEquals(65, ajustada.getHeight(), "Altura deve ser 65");
        // Após crop a imagem vira 100×100, escala para 65×65 mas width limitada a 50
        assertEquals(50, ajustada.getWidth(), "Largura deve ser limitada a 50");
    }

    @Test
    void quandoImagemMaisAlta_deveEscalarMantendoProporcao() {
        //Cria imagem 100×200 (mais alta)
        BufferedImage original = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);

        //Chama cropAndScale
        BufferedImage ajustada = ImageUtils.cropAndScale(original, 65, 50);

        //Verifica resultados
        assertEquals(65, ajustada.getHeight(), "Altura deve ser 65");
        //Proporção de: 100/200 * 65 = 32
        assertEquals(32, ajustada.getWidth(), "Largura proporcional deve ser 32");
    }
}