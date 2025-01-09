package pe.gob.bcrp.upi.process.models.entity;

import jakarta.persistence.Column;
import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;
import pe.gob.bcrp.upi.process.util.DateConverter;

import java.util.Date;

@Component
@CsvRecord(separator = ",", skipFirstLine = true,generateHeaderColumns = true)
@Data
public class TransferenciaCsvRecord {

    @DataField(pos = 1, columnName = "cod_concepto")
    private String codConcepto;

    @DataField(pos = 2, columnName = "cuenta_destino")
    private String cuentaDestino;

    @DataField(pos = 3, columnName = "cuenta_origen")
    private String cuentaOrigen;

    @DataField(pos = 4, columnName = "fecha_liquidacion")
    @BindyConverter(DateConverter.CustomConverter.class)
    private Date fechaLiquidacion;

    @DataField(pos = 5, columnName = "instrucciones_pago")
    private String instruccionesPago;

    @DataField(pos = 6, columnName = "monto_operacion")
    private Double montoOperacion;

    @DataField(pos = 7, columnName = "num_ref_lbtrenlace")
    private String numRefLBTREnlace;

    @DataField(pos = 8, columnName = "num_ref_origen")
    private String numRefOrigen;

/**
    @Override
    public String toString() {
        return "TransferenciaCsvRecord{" +
                "codConcepto='" + codConcepto + '\'' +
                ", cuentaDestino=" + cuentaDestino +
                ", cuentaOrigen=" + cuentaOrigen +
                ", fechaLiquidacion=" + fechaLiquidacion +
                ", instruccionesPago='" + instruccionesPago + '\'' +
                ", montoOperacion=" + montoOperacion +
                ", numRefLBTREnlace='" + numRefLBTREnlace + '\'' +
                ", numRefOrigen='" + numRefOrigen + '\'' +
                '}';
    }**/
}
