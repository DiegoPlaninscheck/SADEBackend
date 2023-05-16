package br.weg.sod.util;

import br.weg.sod.model.entities.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class PDFUtil {

    public ArquivoHistoricoWorkflow criarPDF(Demanda demanda, String tipoPDF) {
        try {
            ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;
            if(tipoPDF == "criacao"){
                arquivoHistoricoWorkflow = criacaoPDFDemanda(demanda, tipoPDF);
            } else if(tipoPDF == "classificacao"){
                arquivoHistoricoWorkflow = criacaoPDFDemanda(demanda, tipoPDF);
            } else if(tipoPDF == "adicionando"){
                arquivoHistoricoWorkflow = criacaoPDFDemanda(demanda, tipoPDF);
            }

            return arquivoHistoricoWorkflow;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private ArquivoHistoricoWorkflow criacaoPDFDemanda(Demanda demanda, String tipoPDF) throws DocumentException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph(demanda.getTituloDemanda(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        addEmptyLine(skipLine, 2);

        skipLine.add(new Paragraph("Objetivo: " + demanda.getObjetivo(), tipoFonte("texto")));

        addEmptyLine(skipLine, 2);

        skipLine.add(new Paragraph("Situacao Atual: " + demanda.getSituacaoAtual(), tipoFonte("texto")));

        addEmptyLine(skipLine, 2);

        skipLine.add(new Paragraph("Frequencia de uso: " + demanda.getFrequenciaUso(), tipoFonte("texto")));

        addEmptyLine(skipLine, 2);

        skipLine.add(new Paragraph("Score: " + demanda.getScore(), tipoFonte("texto")));

        addEmptyLine(skipLine, 2);

        if(tipoPDF == "classificacao"){
            skipLine.add(new Paragraph("Tamanho: " + demanda.getTamanho(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);

            skipLine.add(new Paragraph("BU Solicitante: " + demanda.getBUSolicitante(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);

            skipLine.add(new Paragraph("BUs Beneficiadas: ", tipoFonte("texto")));

            for(BU bu : demanda.getBUsBeneficiadas()){
                skipLine.add(new Paragraph(bu.getNomeBU(), tipoFonte("texto")));

                addEmptyLine(skipLine, 1);
            }

            skipLine.add(new Paragraph("Sessão TI: " + demanda.getSecaoTIResponsavel(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);
        }

        if(tipoPDF == "adicionando"){
            skipLine.add(new Paragraph("Prazo elaboração: " + demanda.getPrazoElaboracao(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);

            skipLine.add(new Paragraph("Codigo PPM: " + demanda.getCodigoPPM(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);

            skipLine.add(new Paragraph("Link Jira: " + demanda.getLinkJira(), tipoFonte("texto")));

            addEmptyLine(skipLine, 2);
        }

        skipLine.add(new Paragraph("Beneficios", tipoFonte("texto")));

        addEmptyLine(skipLine, 1);

        skipLine.add(tabelaBeneficios(demanda));

        addEmptyLine(skipLine, 2);

        skipLine.add(new Paragraph("Centro de Custo(s)", tipoFonte("texto")));

        addEmptyLine(skipLine, 1);

        skipLine.add(tabelaCentroCusto(demanda));

        document.add(skipLine);

        document.close();

        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = new ArquivoHistoricoWorkflow();

        arquivoHistoricoWorkflow.setNome("versaoHistoricoCriacaoDemanda");
        arquivoHistoricoWorkflow.setTipo("application/pdf");
        arquivoHistoricoWorkflow.setArquivo(outputStream.toByteArray());

        return arquivoHistoricoWorkflow;
    }

//    public static void main(String[] args) throws FileNotFoundException, DocumentException {
//
//        String file = "C:\\Users\\diego_planinscheck\\Desktop\\versaoHistorico.pdf";
//
//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream(file));
//
//        document.open();
//
//        document.add(new Paragraph("Titulo", tipoFonte("titulo")));
//
//        Paragraph skipLine = new Paragraph();
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Objetivo: " + "Objetivo atual", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Situacao Atual: " + "situacao", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Frequencia de uso: " + "SEMANALMENTE", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Score: " + "1", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Beneficios", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 1);
//
//        skipLine.add(tabelaBeneficios());
//
//        addEmptyLine(skipLine, 2);
//
//        skipLine.add(new Paragraph("Centro de Custo(s)", tipoFonte("texto")));
//
//        addEmptyLine(skipLine, 1);
//
//        skipLine.add(tabelaCentroCusto());
//
//        document.add(skipLine);
//
//        document.close();
//    }

    private static Element tabelaCentroCusto(Demanda demanda) {
        PdfPTable table = new PdfPTable(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Centro Custo"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for(CentroCusto centroCusto : demanda.getCentroCustoDemanda()){
            System.out.println(centroCusto);
            table.addCell(centroCusto.getNomeCentroCusto());
        }

        return table;
    }

    private static Element tabelaBeneficios(Demanda demanda) {
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Tipo beneficio"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Descrição"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Moeda"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Valor"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for (Beneficio beneficio : demanda.getBeneficiosDemanda()){
            System.out.println(beneficio);
            if(beneficio.getTipoBeneficio().getNome() == "Qualitativo"){
                table.addCell(beneficio.getTipoBeneficio().getNome());
                table.addCell(beneficio.getDescricao());
                table.addCell("");
                table.addCell("");
            } else {
                table.addCell(beneficio.getTipoBeneficio().getNome());
                table.addCell(beneficio.getDescricao());
                table.addCell(beneficio.getMoeda().getNome());
                table.addCell(beneficio.getValor().toString());
            }
        }
        return table;
    }

    private static Font tipoFonte(String elemento) {
        Font font = new Font();
        switch (elemento) {
            case "titulo" -> {
                font.setColor(BaseColor.BLUE);
                font.setFamily(String.valueOf(Font.FontFamily.TIMES_ROMAN));
                font.setSize(16);
            }
            case "texto" -> {
                font.setColor(BaseColor.BLACK);
                font.setFamily(String.valueOf(Font.FontFamily.TIMES_ROMAN));
                font.setSize(10);
            }
        }
        return font;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
