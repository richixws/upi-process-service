package pe.gob.bcrp.upi.process.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum IndicadorSolicitudTIN {

    CONSTITUCION_GARANTIA ("CONSTITUCION GARANTIA","CG"),
    DEVOLUCION_GARANTIA ("DEVOLUCION GARANTIA","DG"),
    CARGO_NETEO ("CARGO NETEO","CN"),
    ABONO_NETEO ("ABONO NETEO","AN");
    private String descripcion;
    private String valor;


}
