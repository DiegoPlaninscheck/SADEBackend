package br.weg.sade.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@NoArgsConstructor
@Table(name = "arquivoHistoricoWorkflow")
public class ArquivoHistoricoWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idArquivoDemanda;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    @Lob
    private byte[] arquivo;

    public ArquivoHistoricoWorkflow(MultipartFile multipartFile) throws IOException {
        this.nome = multipartFile.getOriginalFilename();
        this.tipo = multipartFile.getContentType();
        this.arquivo = multipartFile.getBytes();
    }
}
