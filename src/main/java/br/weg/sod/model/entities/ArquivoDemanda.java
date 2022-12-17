package br.weg.sod.model.entities;

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

    @NonNull
    private String nome;

    @NonNull
    private String tipo;

    @Column(nullable = false)
    @Lob
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public ArquivoDemanda(MultipartFile multipartFile, Usuario usuario) throws IOException {
        this.nome = multipartFile.getOriginalFilename();
        this.tipo = multipartFile.getContentType();
        this.arquivo = multipartFile.getBytes();
        this.usuario = usuario;
    }
}
