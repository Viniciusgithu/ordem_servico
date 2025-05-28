import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextEditorTest {

    @Test
    void devePreencherCamposDeTextoCorretamente() {
        TextEditor editor = new TextEditor();

        editor.clienteField.setText("Maria");
        editor.larguraTecidoField.setText("1.50");
        editor.papelField.setText("Sublimático");
        editor.larguraImpressaoField.setText("1.60");
        editor.tecidoField.setText("Poliéster");
        editor.dataField.setText("28/05/2025");
        editor.horaField.setText("14:00");

        assertEquals("Maria", editor.clienteField.getText());
        assertEquals("1.50", editor.larguraTecidoField.getText());
        assertEquals("Sublimático", editor.papelField.getText());
        assertEquals("1.60", editor.larguraImpressaoField.getText());
        assertEquals("Poliéster", editor.tecidoField.getText());
        assertEquals("28/05/2025", editor.dataField.getText());
        assertEquals("14:00", editor.horaField.getText());
    }

    @Test
    void deveCarregarImagemValidaNoSlotZero() {
        TextEditor editor = new TextEditor();
        String imagePath = "Lo-fi_Ordem_Servico.jpg"; // nome exato da imagem no seu projeto

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            fail("A imagem 'Lo-fi_Ordem_Servico.jpg' precisa existir na raiz do projeto.");
        }

        try {
            BufferedImage img = ImageUtils.loadAndCropImage(imagePath, 65, 50);
            editor.imagensLinhas[0] = img;
            editor.imageLabels[0].setIcon(new ImageIcon(img));

            assertNotNull(editor.imagensLinhas[0], "Imagem deve estar carregada no índice 0");
            assertTrue(editor.imagensLinhas[0].getWidth() > 0 && editor.imagensLinhas[0].getHeight() > 0);
        } catch (IOException e) {
            fail("Erro ao carregar imagem: " + e.getMessage());
        }
    }

    @Test
    void deveLancarExcecaoAoCarregarImagemInexistente() {
        assertThrows(IOException.class, () -> {
            ImageUtils.loadAndCropImage("caminho/falso/nao_existe.jpg", 65, 50);
        });
    }
}
