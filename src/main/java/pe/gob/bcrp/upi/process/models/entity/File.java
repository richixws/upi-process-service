package pe.gob.bcrp.upi.process.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "size", nullable = false, precision = 25)
    private Integer size;

    @Column(name = "fecha_file")
    private String fechaFile;

    @Column(name = "date_create_file")
    private String createDateFile;

    @Column(name = "date_process_file")
    private String processDateFile;

    @Column(name = "date_persisted_file")
    private String persistedDateFile;

    @ManyToOne
    @JoinColumn(name = "id_operacion", nullable = false)
    private Operacion operacion;

}
