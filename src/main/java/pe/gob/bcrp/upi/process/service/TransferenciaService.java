package pe.gob.bcrp.upi.process.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.upi.process.models.entity.Transferencia;
import pe.gob.bcrp.upi.process.models.entity.TransferenciaCsvRecord;
import pe.gob.bcrp.upi.process.repository.IFileRepository;
import pe.gob.bcrp.upi.process.repository.ITransferenciaRepository;

import java.util.List;

@Service
@Slf4j
public class TransferenciaService {

    private final ITransferenciaRepository transferenciaRepository;
    private final IFileRepository iFileRepository;

    public TransferenciaService(ITransferenciaRepository transferenciaRepository, IFileRepository iFileRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.iFileRepository = iFileRepository;
    }

    public void persistTransferencia(TransferenciaCsvRecord csvtransferencia) {
        log.info("Service - persistTransferencia");
        try {

            Transferencia transferencia = mapToEntity(csvtransferencia);
            log.info("transferencia object {}", transferencia.toString());
            transferenciaRepository.save(transferencia);
            log.info("Transferencia guardada correctamente");
         //   almacenarDatosFile(csvtransferencia);

        }catch (Exception e){
            e.getLocalizedMessage();
            log.error("Error al persistir la transferencia", e);
        }

    }

    private void almacenarDatosFile(TransferenciaCsvRecord csvtransferencia) {
        log.info("Service - almacenarDatosFile");
        try {

        }catch (Exception e){
            e.getLocalizedMessage();
            log.error("Error al almacenar datos del archivo", e);
        }

    }

    public void persistTransferencias(List<Transferencia> transferenciaList) {
        transferenciaList.forEach(o -> {
            log.info("transferencias object {}", o.toString());
            transferenciaRepository.save(o);
        });
    }

    private Transferencia mapToEntity(TransferenciaCsvRecord csvRecord) {
        return Transferencia.builder()
                .codConcepto(csvRecord.getCodConcepto())
                .cuentaDestino(csvRecord.getCuentaDestino())
                .cuentaOrigen(csvRecord.getCuentaOrigen())
                .fechaLiquidacion(csvRecord.getFechaLiquidacion())
                .instruccionesPago(csvRecord.getInstruccionesPago())
                .montoOperacion(csvRecord.getMontoOperacion())
                .numRefLBTREnlace(csvRecord.getNumRefLBTREnlace())
                .numRefOrigen(csvRecord.getNumRefOrigen())
                .build();
    }
}
