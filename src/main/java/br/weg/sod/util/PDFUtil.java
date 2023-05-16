package br.weg.sod.util;

import br.weg.sod.model.entities.Demanda;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFUtil {

    public Document criarPDFDemanda(Demanda demanda) throws DocumentException {
        Document document = new Document();

        document.open();

        document.add(new Paragraph("Titulo"));

        document.close();

        return document;
    }
//    public static void main(String[] args) throws FileNotFoundException, DocumentException {
//
//        String file = "C:\\Users\\diego_planinscheck\\Desktop\\versaoHistorico.pdf";
//
//        Document document = new Document();
//
//        PdfWriter.getInstance(document, new FileOutputStream(file));
//
//        document.open();
//
//        document.add(new Paragraph("Titulo"));
//
//        document.close();
//
//        System.out.println(document);
//    }
}
