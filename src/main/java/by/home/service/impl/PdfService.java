package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.service.api.IPdfService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static by.home.util.Constant.Utils.FONT_COURIER_NEW_PATH;

@Slf4j
public class PdfService implements IPdfService {

    @Override
    @Loggable
    public void createPdf(String text, String pdfName) {
        Document document = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(pdfName);
            document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font font = FontFactory.getFont(FONT_COURIER_NEW_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            for (String line : text.lines().toList()) {
                document.add(new Paragraph(line, font));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (document != null) {
                document.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
