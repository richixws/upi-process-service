package pe.gob.bcrp.upi.process.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.upi.process.models.entity.Operacion;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    private Long idFile;
    private String filename;
    private String path;
    private String extension;
    private String mime;
    private Integer size;

    private String createDateFile;
    private String processDateFile;
    private String persistedDateFile;


    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String fechaFile;

    private Operacion operacion;


}
