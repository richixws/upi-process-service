package pe.gob.bcrp.upi.process.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.upi.process.exception.ResourceNotFoundException;
import pe.gob.bcrp.upi.process.models.dto.FileDTO;
import pe.gob.bcrp.upi.process.models.dto.OperacionResponse;
import pe.gob.bcrp.upi.process.models.entity.File;
import pe.gob.bcrp.upi.process.models.entity.Operacion;
import pe.gob.bcrp.upi.process.models.entity.OperacionCsvRecord;
import pe.gob.bcrp.upi.process.repository.IFileRepository;
import pe.gob.bcrp.upi.process.repository.IOperacionRepository;
import pe.gob.bcrp.upi.process.util.Fecha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface OperacionService {

    public void persistTransferencia(OperacionCsvRecord csvOperacionReferencia, FileDTO file);
    public void persistTransferencias(List<Operacion> transferenciaList);
    //public List<Operacion> listarOperacionesTIN(Operacion operacion);
    public OperacionResponse listarOperacionesTIN(String  fecha, String concepto);


}
