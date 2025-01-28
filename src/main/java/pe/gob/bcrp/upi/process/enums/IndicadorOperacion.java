package pe.gob.bcrp.upi.process.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IndicadorOperacion {

    REGISTRADO("REGISTRADO","1"),
    ENVIADO("ENVIADO","2"),
    EN_COLA("EN_COLA","3"),
    LIQUIDADO("LIQUIDADO","4"),
    ANULADO("ANULADO","*");
     private String descripcion;
     private String valor;

}
