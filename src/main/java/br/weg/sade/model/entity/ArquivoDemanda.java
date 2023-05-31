package br.weg.sade.model.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Data
@Entity
@NoArgsConstructor
@Table(name = "arquivoDemanda")
public class ArquivoDemanda {

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

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario insersor;

    public ArquivoDemanda(MultipartFile multipartFile, Usuario insersor) throws IOException {
        this.nome = multipartFile.getOriginalFilename();
        this.tipo = multipartFile.getContentType();
        this.arquivo = multipartFile.getBytes();
        this.insersor = insersor;
    }
}
