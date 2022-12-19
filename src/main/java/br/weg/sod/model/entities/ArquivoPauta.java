package br.weg.sod.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@NoArgsConstructor
@Table(name = "arquivoPauta")
public class ArquivoPauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer idArquivoPauta;

    @NonNull
    private String nome;

    @NonNull
    private String tipo;

    @Column(nullable = false)
    @Lob
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario insersor;

    public ArquivoPauta(MultipartFile multipartFile, Usuario insersor) throws IOException {
        this.nome = multipartFile.getOriginalFilename();
        this.tipo = multipartFile.getContentType();
        this.arquivo = multipartFile.getBytes();
        this.insersor = insersor;
    }
}
