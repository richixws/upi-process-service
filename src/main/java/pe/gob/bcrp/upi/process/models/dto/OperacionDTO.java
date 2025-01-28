package pe.gob.bcrp.upi.process.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OperacionDTO {

    private Long id;
    private String entidad;
    private String tipoOperacion;
    private Double montoOperacion;
    private Date fechaOperacion;

    private String codConcepto;
    private String nombreConcepto;
    private String numReferencia;

}
