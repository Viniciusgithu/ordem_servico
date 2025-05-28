import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;

public class TextEditorTest {

    @Test
    void devePreencherCamposDeTextoCorretamente() {
        TextEditor editor = new TextEditor();

        editor.clienteField.setText("Maria");
        editor.equipamentoField.setText("Notebook");
        editor.defeitoField.setText("Não liga");
        editor.diagnosticoField.setText("Placa mãe queimada");
        editor.servicoField.setText("Troca de placa");
        editor.valorField.setText("750");

        assertEquals("Maria", editor.clienteField.getText());
        assertEquals("Notebook", editor.equipamentoField.getText());
        assertEquals("Não liga", editor.defeitoField.getText());
        assertEquals("Placa mãe queimada", editor.diagnosticoField.getText());
        assertEquals("Troca de placa", editor.servicoField.getText());
        assertEquals("750", editor.valorField.getText());
    }

    @Test
    void deveMontarPrintableCorretamente() {
        TextEditor editor = new TextEditor();

        editor.clienteField.setText("João");
        editor.equipamentoField.setText("PC Gamer");
        editor.defeitoField.setText("Não inicia");
        editor.diagnosticoField.setText("Fonte queimada");
        editor.servicoField.setText("Substituição da fonte");
        editor.valorField.setText("500");

        try {
            Field printableField = TextEditor.class.getDeclaredField("printable");
            printableField.setAccessible(true);

            editor.printAction.doClick();

            Object printable = printableField.get(editor);
            assertNotNull(printable, "Printable deve ter sido criado");
        } catch (Exception e) {
            fail("Erro ao acessar campo printable: " + e.getMessage());
        }
    }

    @Test
    void deveLancarExcecaoAoInserirImagemInvalida() {
        TextEditor editor = new TextEditor();

        Exception exception = assertThrows(Exception.class, () -> {
            editor.insertImage("caminho/inexistente.jpg");
        });

        assertTrue(exception.getMessage().contains("caminho") || exception instanceof java.io.IOException);
    }

    @Test
    void deveCarregarImagemValida() {
        TextEditor editor = new TextEditor();

        String imagePath = "Lo-fi_Ordem_Servico.jpg"; // já presente no seu projeto

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            fail("A imagem 'Lo-fi_Ordem_Servico.jpg' precisa existir na raiz do projeto.");
        }

        try {
            editor.insertImage(imagePath);

            Field imgField = TextEditor.class.getDeclaredField("img");
            imgField.setAccessible(true);
            BufferedImage img = (BufferedImage) imgField.get(editor);

            assertNotNull(img, "Imagem deve estar carregada");
            assertTrue(img.getWidth() > 0 && img.getHeight() > 0, "Imagem não deve estar vazia");
        } catch (Exception e) {
            fail("Erro ao carregar imagem: " + e.getMessage());
        }
    }
}
