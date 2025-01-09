package pe.gob.bcrp.upi.process.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_file", nullable = false)
    private Long idFile;

    @Column(name = "filename", nullable = false, length = 255)
    private String filename;

    @Column(name = "path", nullable = false, length = 1000)
    private String path;

    @Column(name = "extension", nullable = false, length = 50)
    private String extension;

    @Column(name = "mime", nullable = false, length = 50)
    private String mime;

    @Column(name = "sizes", nullable = false, precision = 25)
    private Integer sizes;

    //@Column(name = "id_transferencia", nullable = false, precision = 10)
    //private Integer idTransferencia;

    @Column(name = "id_usuario", nullable = false, length = 50)
    private String idUsuario;

    @Column(name = "fecha_hora_file")
    private LocalDateTime horaFechaFile;

    @ManyToOne
    @JoinColumn(name = "id_transferencia", nullable = false)
    private Transferencia transferencia;

}
