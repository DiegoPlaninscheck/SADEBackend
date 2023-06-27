package br.weg.sade.util;

import br.weg.sade.model.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private ArquivoHistoricoWorkflow criacaoPDFDemanda(Demanda demanda, String tipoPDF) throws DocumentException, IOException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = new ArquivoHistoricoWorkflow();

        conteudoPDFDemanda(demanda, document, tipoPDF, arquivoHistoricoWorkflow);

        arquivoHistoricoWorkflow.setArquivo(outputStream.toByteArray());

        return arquivoHistoricoWorkflow;
    }

    private void conteudoPDFDemanda(Demanda demanda, Document document, String tipoPDF, ArquivoHistoricoWorkflow arquivoHistoricoWorkflow) throws DocumentException, IOException {
        document.open();

        System.out.println("Entrou conteudo PDF Demanda");

        addLogoImagem(document);

        System.out.println("Passou add de imagem ao PDF");

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

            arquivoHistoricoWorkflow = criacaoPDFProposta(proposta);

            return arquivoHistoricoWorkflow;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private ArquivoHistoricoWorkflow criacaoPDFProposta(Proposta proposta) throws DocumentException, IOException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = new ArquivoHistoricoWorkflow();

        ConteudoPDFProposta(proposta, document);

        arquivoHistoricoWorkflow.setArquivo(outputStream.toByteArray());
        arquivoHistoricoWorkflow.setNome("versaoHistoricoProposta");
        arquivoHistoricoWorkflow.setTipo("application/pdf");

        return arquivoHistoricoWorkflow;
    }

    private void ConteudoPDFProposta(Proposta proposta, Document document) throws DocumentException, IOException {
        document.open();

        addLogoImagem(document);

        document.add(new Paragraph(proposta.getDemanda().getTituloDemanda(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Solicitante: " +
                proposta.getDemanda().getUsuario().getNomeUsuario() + " - " +
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

        if (proposta.getDemanda().getBeneficiosDemanda() != null) {
            for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
                switch (beneficio.getTipoBeneficio().getNome()) {
                    case "Qualitativo" ->
                            skipLine.add(new Paragraph("Resultados Esperados (Qualitativos): " + beneficio.getDescricao(), tipoFonte("texto")));
                    case "Potencial" ->
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos potenciais): " + beneficio.getDescricao(), tipoFonte("texto")));
                    case "Real" ->
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos reais): " + beneficio.getDescricao(), tipoFonte("texto")));
                    default -> System.out.println("Beneficio invalido");
                }
            }
            addNovaLinha(skipLine, 2);
        }

        Double valorTotal = 0.0;
        for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
            if (beneficio.getValor() != null &&
                    (beneficio.getTipoBeneficio().getNome().equals("Real") ||
                            beneficio.getTipoBeneficio().getNome().equals("Potencial"))) {
                valorTotal += beneficio.getValor();
            }
        }

        if (valorTotal != 0.0) {
            skipLine.add(new Paragraph("Custos Totais: " + valorTotal, tipoFonte("texto")));

            addNovaLinha(skipLine, 1);
        }

        //FALA CO ROMARIO
        List<Element> tabelas = tabelasDeCustoProposta(proposta);

        if (tabelas.size() > 0) {
            for (Element tabela : tabelas) {
                skipLine.add(tabela);

                addNovaLinha(skipLine, 2);
            }
        }

        skipLine.add(new Paragraph("Período Execução: " +
                proposta.getPeriodoExecucaoInicio() + " à " + proposta.getPeriodoExecucaoFim(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Payback: " + proposta.getPayback(), tipoFonte("texto")));

        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Responsáveis Negocio: ", tipoFonte("texto")));

        addNovaLinha(skipLine, 1);

        for (Usuario usuario : proposta.getResponsaveisNegocio()) {
            skipLine.add(new Paragraph(usuario.getNomeUsuario(), tipoFonte("texto")));
        }


        document.add(skipLine);

        document.close();
    }

    // PDF Pauta
    public ArquivoPauta criarPDFPauta(Pauta pauta, Usuario usuarioInsersor) {
        try {
            ArquivoPauta arquivoPauta = null;

            arquivoPauta = criacaoPDFPauta(pauta);
            arquivoPauta.setInsersor(usuarioInsersor);

            return arquivoPauta;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private ArquivoPauta criacaoPDFPauta(Pauta pauta) throws DocumentException, IOException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        ArquivoPauta arquivoPauta = new ArquivoPauta();

        ConteudoPDFPauta(pauta, document);

        arquivoPauta.setArquivo(outputStream.toByteArray());
        arquivoPauta.setNome("arquivoPauta");
        arquivoPauta.setTipo("application/pdf");

        return arquivoPauta;
    }

    private void ConteudoPDFPauta(Pauta pauta, Document document) throws DocumentException, IOException {
        System.out.println(pauta.getPropostasPauta());
        document.open();

        addLogoImagem(document);

        document.add(new Paragraph(pauta.getTituloReuniaoPauta(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        int indexTitulo = 1;

        for (DecisaoPropostaPauta decisaoProposta : pauta.getPropostasPauta()) {
            Proposta proposta = decisaoProposta.getProposta();

            System.out.println(proposta.getTabelasCustoProposta());

            document.add(new Paragraph(indexTitulo + ". " + proposta.getDemanda().getTituloDemanda(), tipoFonte("titulo")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Objetivo: " + proposta.getDemanda().getObjetivo(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Escopo projeto: " + proposta.getEscopo(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            if (proposta.getDemanda().getBeneficiosDemanda() != null) {
                for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
                    switch (beneficio.getTipoBeneficio().getNome()) {
                        case "Qualitativo" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Qualitativos): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        case "Potencial" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos potenciais): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        case "Real" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos reais): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        default -> System.out.println("Beneficio invalido");
                    }
                }
                addNovaLinha(skipLine, 2);
            }

            Double valorTotal = 0.0;
            for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
                if (beneficio.getValor() != null &&
                        (beneficio.getTipoBeneficio().getNome().equals("Real") ||
                                beneficio.getTipoBeneficio().getNome().equals("Potencial"))) {
                    valorTotal += beneficio.getValor();
                }
            }

            if (valorTotal != 0.0) {
                skipLine.add(new Paragraph("Custos Totais: " + valorTotal, tipoFonte("texto")));

                addNovaLinha(skipLine, 1);
            }

            //FALA CO ROMARIO
            List<Element> tabelas = tabelasDeCustoProposta(proposta);

            if (tabelas.size() > 0) {
                for (Element tabela : tabelas) {
                    skipLine.add(tabela);

                    addNovaLinha(skipLine, 2);
                }
            }

            skipLine.add(new Paragraph("Período Execução: " +
                    proposta.getPeriodoExecucaoInicio() + " à " +
                    proposta.getPeriodoExecucaoFim(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Payback: " + proposta.getPayback(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Responsáveis Negocio: ", tipoFonte("texto")));

            addNovaLinha(skipLine, 1);

            for (Usuario usuario : proposta.getResponsaveisNegocio()) {
                skipLine.add(new Paragraph(usuario.getNomeUsuario(), tipoFonte("texto")));
            }

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Parecer Comissão: " +
                    decisaoProposta.getStatusDemandaComissao(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Comentário: " +
                    decisaoProposta.getComentario(), tipoFonte("texto")));

            indexTitulo++;
        }

        document.add(skipLine);

        document.close();
    }

    // PDF ATA
    public ArquivoPauta criarPDFATA(ATA ata, Usuario usuarioInsersor) {
        try {
            ArquivoPauta arquivoPauta = null;

            arquivoPauta = criacaoPDFATA(ata);
            arquivoPauta.setInsersor(usuarioInsersor);

            return arquivoPauta;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private ArquivoPauta criacaoPDFATA(ATA ata) throws DocumentException, IOException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        ArquivoPauta arquivoPauta = new ArquivoPauta();

        ConteudoPDFATA(ata, document);

        arquivoPauta.setArquivo(outputStream.toByteArray());
        arquivoPauta.setNome("arquivoATA");
        arquivoPauta.setTipo("application/pdf");

        return arquivoPauta;
    }

    private void ConteudoPDFATA(ATA ata, Document document) throws DocumentException, IOException {
        document.open();

        addLogoImagem(document);

        document.add(new Paragraph(ata.getTituloReuniaoATA(), tipoFonte("titulo")));

        Paragraph skipLine = new Paragraph();

        int indexTitulo = 1;

        for (DecisaoPropostaATA decisaoPropostaATA : ata.getPropostasAta()) {
            Proposta proposta = decisaoPropostaATA.getProposta();

            document.add(new Paragraph(indexTitulo + ". " + proposta.getDemanda().getTituloDemanda(), tipoFonte("titulo")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Objetivo: " + proposta.getDemanda().getObjetivo(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Escopo projeto: " + proposta.getEscopo(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            if (proposta.getDemanda().getBeneficiosDemanda() != null) {
                for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
                    switch (beneficio.getTipoBeneficio().getNome()) {
                        case "Qualitativo" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Qualitativos): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        case "Potencial" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos potenciais): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        case "Real" -> {
                            skipLine.add(new Paragraph("Resultados Esperados (Ganhos reais): " + beneficio.getDescricao(), tipoFonte("texto")));

                            addNovaLinha(skipLine, 1);
                        }
                        default -> System.out.println("Beneficio invalido");
                    }
                }
                addNovaLinha(skipLine, 2);
            }

            Double valorTotal = 0.0;
            for (Beneficio beneficio : proposta.getDemanda().getBeneficiosDemanda()) {
                if (beneficio.getValor() != null &&
                        (beneficio.getTipoBeneficio().getNome().equals("Real") ||
                                beneficio.getTipoBeneficio().getNome().equals("Potencial"))) {
                    valorTotal += beneficio.getValor();
                }
            }

            if (valorTotal != 0.0) {
                skipLine.add(new Paragraph("Custos Totais: " + valorTotal, tipoFonte("texto")));

                addNovaLinha(skipLine, 1);
            }

            //FALA CO ROMARIO
            List<Element> tabelas = tabelasDeCustoProposta(proposta);

            if (tabelas.size() > 0) {
                for (Element tabela : tabelas) {
                    skipLine.add(tabela);

                    addNovaLinha(skipLine, 2);
                }
            }

            skipLine.add(new Paragraph("Período Execução: " +
                    proposta.getPeriodoExecucaoInicio() + " à " +
                    proposta.getPeriodoExecucaoFim(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Payback: " + proposta.getPayback(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Número Sequencial: " + decisaoPropostaATA.getNumeroSequencial(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Parecer Comissão: " +
                    decisaoPropostaATA.getStatusDemandaComissao(), tipoFonte("texto")));

            addNovaLinha(skipLine, 2);

            skipLine.add(new Paragraph("Comentário: " +
                    decisaoPropostaATA.getComentario(), tipoFonte("texto")));

            indexTitulo++;

        }
        addNovaLinha(skipLine, 2);

        skipLine.add(new Paragraph("Responsáveis Negocio: ", tipoFonte("texto")));

        addNovaLinha(skipLine, 1);

        for (Usuario usuario : ata.getUsuariosReuniaoATA()) {
            skipLine.add(new Paragraph(usuario.getNomeUsuario(), tipoFonte("texto")));
        }

        document.add(skipLine);

        document.close();
    }

    // Funções para construir os PDFs
    private List<Element> tabelasDeCustoProposta(Proposta proposta) {
        List<Element> tabelas = new ArrayList<>();

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

            tabelas.add(table);
        }
        return tabelas;
    }

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
            if (beneficio.getTipoBeneficio().getNome().equals("Qualitativo")) {
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

    private static void addLogoImagem(Document document) throws DocumentException, IOException {
        try {
            Image logo = Image.getInstance("src/main/resources/wegLogoAzul.png");
            logo.scaleAbsolute(40, 40);
            logo.setAbsolutePosition(document.getPageSize().getWidth() - 50, document.getPageSize().getHeight() - 70);
            document.add(logo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
