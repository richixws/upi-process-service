package pe.gob.bcrp.upi.process.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.upi.process.exception.ResourceNotFoundException;
import pe.gob.bcrp.upi.process.models.dto.FileDTO;
import pe.gob.bcrp.upi.process.models.entity.File;
import pe.gob.bcrp.upi.process.models.entity.Transferencia;
import pe.gob.bcrp.upi.process.models.entity.TransferenciaCsvRecord;
import pe.gob.bcrp.upi.process.repository.IFileRepository;
import pe.gob.bcrp.upi.process.repository.ITransferenciaRepository;
import pe.gob.bcrp.upi.process.util.Fecha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransferenciaService {

    private final ITransferenciaRepository transferenciaRepository;
    private final IFileRepository iFileRepository;
    private final ModelMapper modelMapper;

    public TransferenciaService(ITransferenciaRepository transferenciaRepository, IFileRepository iFileRepository, ModelMapper modelMapper) {
        this.transferenciaRepository = transferenciaRepository;
        this.iFileRepository = iFileRepository;
        this.modelMapper = modelMapper;

    }

    public void persistTransferencia(TransferenciaCsvRecord csvtransferencia, FileDTO file) {
        log.info("Service - persistTransferencia");
        try {

            Transferencia transferencia = mapToEntity(csvtransferencia);
            log.info("transferencia object {}", transferencia.toString());
            Transferencia t=transferenciaRepository.save(transferencia);

            log.info("Transferencia guardada correctamente");
            almacenarDatosFile(t, file);

        }catch (Exception e){
            e.getLocalizedMessage();
            log.error("Error al persistir la transferencia", e);
        }

    }

    private void almacenarDatosFile(Transferencia transferencia,FileDTO file) {
        log.info("Service - almacenarDatosFile");
        try {
            Transferencia transferenciad=transferenciaRepository.findById(transferencia.getId()).orElseThrow(()->new ResourceNotFoundException("Transferencia no encontrada"));

            file.setTransferencia(transferenciad);
            file.setFechaFile(Fecha.formatDate(LocalDate.now())); //fecha dd-MM-yyyy
            file.setPersistedDateFile(Fecha.formatDateTime(LocalDateTime.now()));//fecha de presistencia
            File f=modelMapper.map(file, File.class);
            iFileRepository.save(f);
            log.info("Datos del archivo almacenados correctamente: {}",f);

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
                .sid(csvRecord.getSid())
                .codConcepto(csvRecord.getCodConcepto())
                .cuentaDestino(csvRecord.getCuentaDestino())
                .cuentaOrigen(csvRecord.getCuentaOrigen())
                .fechaLiquidacion(csvRecord.getFechaLiquidacion())
                .instruccionesPago(csvRecord.getInstruccionesPago())
                .montoOperacion(csvRecord.getMontoOperacion())
                .numRefLBTREnlace(csvRecord.getNumRefLBTREnlace())
                .numRefOrigen(csvRecord.getNumRefOrigen())
                .firma(csvRecord.getFirma())
                .build();
    }
}
