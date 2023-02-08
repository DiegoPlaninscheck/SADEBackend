package br.weg.sod.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

    @NonNull
    private String nome;

    @NonNull
    private String tipo;

    @JsonIgnore
    @Column(nullable = false)
    @Lob
    private byte[] arquivo;

    public ArquivoHistoricoWorkflow(MultipartFile multipartFile) throws IOException {
        this.nome = multipartFile.getOriginalFilename();
        this.tipo = multipartFile.getContentType();
        this.arquivo = multipartFile.getBytes();
    }
}
