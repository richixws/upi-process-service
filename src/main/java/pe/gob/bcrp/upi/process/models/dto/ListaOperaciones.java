package pe.gob.bcrp.upi.process.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListaOperaciones {

    private List<OperacionDTO> listarConstitucionesGarantia;
    private List<OperacionDTO> listarDevolucionGarantia;
    private List<OperacionDTO> listaCargoNeto;
    private List<OperacionDTO> listaAbonoNeto;

}
