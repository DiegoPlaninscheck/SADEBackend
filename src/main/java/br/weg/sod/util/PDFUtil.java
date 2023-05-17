package br.weg.sod.util;

import br.weg.sod.model.entities.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.Arrays;

public class PDFUtil {

    //PDF Demanda
    public ArquivoHistoricoWorkflow criarPDFDemanda(Demanda demanda, String tipoPDF) {
        try {
            ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;
            if (tipoPDF == "criacao") {
                arquivoHistoricoWorkflow = criacaoPDFDemanda(demanda, tipoPDF);
            } else if (tipoPDF == "classificacao") {
                arquivoHistoricoWorkflow = criacaoPDFDemanda(demanda, tipoPDF);
            } else if (tipoPDF == "adicionando") {
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

        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = new ArquivoHistoricoWorkflow();

        conteudoPDFDemanda(demanda, document, tipoPDF, arquivoHistoricoWorkflow);

        arquivoHistoricoWorkflow.setArquivo(outputStream.toByteArray());

        return arquivoHistoricoWorkflow;
    }

    private void conteudoPDFDemanda(Demanda demanda, Document document, String tipoPDF, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow) throws DocumentException {
        document.open();

        document.add(new Paragraph(demanda.getTituloDemanda(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Objetivo: " + demanda.getObjetivo(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Situacao Atual: " + demanda.getSituacaoAtual(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Frequencia de uso: " + demanda.getFrequenciaUso(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Score: " + demanda.getScore(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        if (tipoPDF == "classificacao") {
            classificando(skipLine, demanda, arquivoHistoricoWorkflow);
        }

        if (tipoPDF == "adicionando") {
            adicionandoInformacao(skipLine, demanda, arquivoHistoricoWorkflow);
        }

        if (demanda.getBeneficiosDemanda() != null) {
            skipLine.add(new Paragraph("Beneficios: ", tipoFonte("texto")));

            addNovaLinha(skipLine, 1);

            skipLine.add(tabelaBeneficios(demanda));

            addNovaLinha(skipLine, 2);
        }

        skipLine.add(new Paragraph("Centro de Custo(s): ", tipoFonte("texto")));

        addNovaLinha(skipLine, 1);

        skipLine.add(tabelaCentroCusto(demanda));

        document.add(skipLine);

        document.close();
    }

    private void classificando(Paragraph skipLine, Demanda demanda, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow) {
        skipLine.add(new Paragraph("Tamanho: " + demanda.getTamanho(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("BU Solicitante: " + demanda.getBUSolicitante().getNomeBU(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("BUs Beneficiadas: ", tipoFonte("texto")));

        for (BU bu : demanda.getBUsBeneficiadas()) {
            skipLine.add(new Paragraph(bu.getNomeBU(), tipoFonte("texto")));
        }

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Sessão TI Responsável: " + demanda.getSecaoTIResponsavel(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        arquivoHistoricoWorkflow.setNome("versaoHistoricoClassificacaoDemanda");
        arquivoHistoricoWorkflow.setTipo("application/pdf");
    }

    private void adicionandoInformacao(Paragraph skipLine, Demanda demanda, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow) {
        classificando(skipLine, demanda, arquivoHistoricoWorkflow);
        skipLine.add(new Paragraph("Prazo elaboração: " + demanda.getPrazoElaboracao(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Codigo PPM: " + demanda.getCodigoPPM(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Link Jira: " + demanda.getLinkJira(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        arquivoHistoricoWorkflow.setNome("versaoHistoricoAdicionandoInformacaoDemanda");
        arquivoHistoricoWorkflow.setTipo("application/pdf");
    }


    // PDF Proposta
    public ArquivoHistoricoWorkflow criarPDFProposta(Proposta proposta) {
        try {
            ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;

            System.out.println("Entrou criar PDF proposta");

            arquivoHistoricoWorkflow = criacaoPDFProposta(proposta);

            return arquivoHistoricoWorkflow;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private ArquivoHistoricoWorkflow criacaoPDFProposta(Proposta proposta) throws DocumentException {
        System.out.println("Entrou criacao PDF proposta");
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = new ArquivoHistoricoWorkflow();

        ConteudoPDFProposta(proposta, document, arquivoHistoricoWorkflow, outputStream);

        System.out.println(Arrays.toString(outputStream.toByteArray()));

        System.out.println("criacaoPDFProposta: " + arquivoHistoricoWorkflow);

        return arquivoHistoricoWorkflow;
    }

    private void ConteudoPDFProposta(Proposta proposta, Document document, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow, ByteArrayOutputStream outputStream) throws DocumentException {
        System.out.println("entrou conteudo PDF proposta");
        document.open();

        document.add(new Paragraph(proposta.getDemanda().getTituloDemanda(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Solicitante: " +
                proposta.getDemanda().getUsuario().getNomeUsuario() +
                proposta.getDemanda().getUsuario().getDepartamento(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Objetivo: " + proposta.getDemanda().getObjetivo(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Situação atual: " + proposta.getDemanda().getSituacaoAtual(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Escopo do projeto: " + proposta.getEscopo(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Situação atual: " + proposta.getDemanda().getSituacaoAtual(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
            if (beneficio.getTipoBeneficio().getNome() == "Qualitativo") {
                skipLine.add(new Paragraph("Resultados Esperados (Qualitativos): " + beneficio.getDescricao(), tipoFonte("texto")));
            } else if (beneficio.getTipoBeneficio().getNome() == "Potencial") {
                skipLine.add(new Paragraph("Resultados Esperados (Ganhos potenciais): " + beneficio.getDescricao(), tipoFonte("texto")));
            } else if (beneficio.getTipoBeneficio().getNome() == "Real") {
                skipLine.add(new Paragraph("Resultados Esperados (Ganhos reais): " + beneficio.getDescricao(), tipoFonte("texto")));
            } else {
                System.out.println("Beneficio invalido");
            }
        }

        addNovaLinha(skipLine, 2);

        Double valorTotal = 0.0;
        for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
            System.out.println(beneficio);
            if (beneficio.getValor() != null &&
                    (beneficio.getTipoBeneficio().getNome() == "Real" ||
                            beneficio.getTipoBeneficio().getNome() == "Potencial")) {
                valorTotal += beneficio.getValor();
            }
        }

        System.out.println(valorTotal);

        skipLine.add(new Paragraph("Custos Totais: " + valorTotal, tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        tabelasDeCustoProposta(proposta, skipLine);

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Período Execução: " +
                proposta.getPeriodoExecucaoInicio() + " à " + proposta.getPeriodoExecucaoFim(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Payback: " + proposta.getPayback(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Responsaveis Negocio: ", tipoFonte("texto")));

        addNovaLinha(skipLine, 1);

        for (Usuario usuario : proposta.getResponsaveisNegocio()) {
            skipLine.add(new Paragraph(usuario.getNomeUsuario(), tipoFonte("texto")));
        }

        arquivoHistoricoWorkflow.setArquivo(outputStream.toByteArray());
        arquivoHistoricoWorkflow.setNome("versaoHistoricoProposta");
        arquivoHistoricoWorkflow.setTipo("application/pdf");
    }

    private void tabelasDeCustoProposta(Proposta proposta, Paragraph skipLine) {
//        skipLine.add(new Paragraph("Custos Totais: " + valorTotal, tipoFonte("texto")));

        for (TabelaCusto tabelaCusto : proposta.getTabelasCustoProposta()) {
            PdfPTable table = new PdfPTable(5);

            PdfPCell c1 = new PdfPCell(new Phrase(tabelaCusto.getTituloTabela()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Esforço"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Valor Hora"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Total"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("CCs"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);

            for (LinhaTabela linhaTabela : tabelaCusto.getLinhasTabela()) {
                table.addCell(linhaTabela.getNomeRecurso());
                table.addCell(linhaTabela.getQuantidade().toString());
                table.addCell(linhaTabela.getValorQuantidade().toString());
            }
        }
    }


    // Funções para construir os PDFs
    private static Element tabelaCentroCusto(Demanda demanda) {
        PdfPTable table = new PdfPTable(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Centro Custo"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for (CentroCusto centroCusto : demanda.getCentroCustoDemanda()) {
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

        for (Beneficio beneficio : demanda.getBeneficiosDemanda()) {
            if (beneficio.getTipoBeneficio().getNome() == "Qualitativo") {
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

    private static void addNovaLinha(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
