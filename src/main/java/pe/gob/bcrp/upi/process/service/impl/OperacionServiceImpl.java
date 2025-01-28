package pe.gob.bcrp.upi.process.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.upi.process.exception.ResourceNotFoundException;
import pe.gob.bcrp.upi.process.models.dto.FileDTO;
import pe.gob.bcrp.upi.process.models.dto.OperacionDTO;
import pe.gob.bcrp.upi.process.models.dto.OperacionResponse;
import pe.gob.bcrp.upi.process.models.dto.ListaOperaciones;
import pe.gob.bcrp.upi.process.models.entity.File;
import pe.gob.bcrp.upi.process.models.entity.Operacion;
import pe.gob.bcrp.upi.process.models.entity.OperacionCsvRecord;
import pe.gob.bcrp.upi.process.repository.IFileRepository;
import pe.gob.bcrp.upi.process.repository.IOperacionRepository;
import pe.gob.bcrp.upi.process.service.OperacionService;
import pe.gob.bcrp.upi.process.util.Fecha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OperacionServiceImpl implements OperacionService {

    private  IOperacionRepository operacionRepository;
    private  IFileRepository iFileRepository;
    private final ModelMapper modelMapper;


    public void persistTransferencia(OperacionCsvRecord csvOperacionReferencia, FileDTO file) {
        log.info("Service - persistTransferencia");
        try {

            Operacion transferencia = mapToEntity(csvOperacionReferencia);
            log.info("transferencia object {}", transferencia.toString());

            switch (transferencia.getTipoOperacion()) {
                case "cargo":
                    transferencia.setCodConcepto("C256");
                    break;
                case "abono":
                    transferencia.setCodConcepto("C257");
                    break;
                case "devolucion":
                    transferencia.setCodConcepto("C258");
                    break;
                case "constitucion":
                    transferencia.setCodConcepto("C259");
                    break;
                default:
                    transferencia.setCodConcepto(null);
                    log.error("Tipo de operacion no reconocido");
                    throw new RuntimeException("Tipo de operacion no reconocido");
            }

            Operacion t=operacionRepository.save(transferencia);

            log.info("Transferencia guardada correctamente");
            almacenarDatosFile(t, file);

        }catch (Exception e){
            e.getLocalizedMessage();
            log.error("Error al persistir la transferencia", e);
        }

    }

    private void almacenarDatosFile(Operacion operacion, FileDTO file) {
        log.info("Service - almacenarDatosFile");
        try {
            Operacion operacionObj=operacionRepository.findById(operacion.getId()).orElseThrow(()->new ResourceNotFoundException("Operacion no encontrada"));

            file.setOperacion(operacionObj);
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

    public void persistTransferencias(List<Operacion> transferenciaList) {
        transferenciaList.forEach(o -> {
            log.info("transferencias object {}", o.toString());
            operacionRepository.save(o);
        });
    }

    @Override
    public OperacionResponse listarOperacionesTIN(String  fecha, String concepto) {
        log.info("INI - Service listarOperacionesTIN() ");
        List<Operacion> listOperaciones=null;
        OperacionResponse operacionResponse=new OperacionResponse();
        ListaOperaciones listaOpe=new ListaOperaciones();
        try {

            List<Operacion> listOperacion=operacionRepository.findAll();

            // Aplicar filtros si se proporcionan
            if (fecha != null || concepto != null) {
                listOperacion = listOperacion.stream()
                        .filter(op -> (fecha == null || fecha.isEmpty() || op.getFechaOperacion().toString().contains(fecha)))
                        .filter(op -> (concepto == null || concepto.isEmpty() || op.getCodConcepto().equals(concepto)))
                        .collect(Collectors.toList());
            }




          List<OperacionDTO> opeCargo=listOperacion.
                                      stream()
                                      .filter( e -> e.getTipoOperacion().equals("cargo"))
                                      .map(op ->{
                                       OperacionDTO dto=modelMapper.map(op, OperacionDTO.class);
                                         // dto.setCodConcepto("C265");
                                          dto.setNombreConcepto("Cargo de recursos especificos");
                                          dto.setNumReferencia("54002673584");
                                       return dto;
                                      })
                                      .toList();

          List<OperacionDTO> opeAbono=listOperacion
                                      .stream()
                                      .filter( e -> e.getTipoOperacion().equals("abono"))
                                      .map(op ->{
                                        OperacionDTO dto=modelMapper.map(op, OperacionDTO.class);
                                         // dto.setCodConcepto("C265");
                                          dto.setNombreConcepto("Abono de recursos especificos");
                                          dto.setNumReferencia("54002673584");
                                        return dto;
                                          })
                                      .toList();

            List<OperacionDTO> opeDevolucion=listOperacion
                                        .stream()
                                        .filter( e -> e.getTipoOperacion().equals("devolucion"))
                                        .map(op -> modelMapper.map(op, OperacionDTO.class))
                                        .toList();


            List<OperacionDTO> opeConstitucion=listOperacion
                                        .stream()
                                        .filter( e -> e.getTipoOperacion().equals("constitucion"))
                                        .map(op -> modelMapper.map(op, OperacionDTO.class))
                                        .toList();


          listaOpe.setListaAbonoNeto(opeAbono);
          listaOpe.setListaCargoNeto(opeCargo);
          listaOpe.setListarDevolucionGarantia(opeDevolucion);
          listaOpe.setListarConstitucionesGarantia(opeConstitucion);
          operacionResponse.setContent(listaOpe);

          return operacionResponse;

        }catch ( Exception e){
            log.error("ERROR Service - listarOperacionesTIN() {}", e.getMessage());
            throw new RuntimeException("ERROR Service - listarOperacionesTIN() "+e.getMessage());
        }

    }

    private Operacion mapToEntity(OperacionCsvRecord csvRecord) {
        return Operacion.builder()
                .entidad(csvRecord.getEntidad())
                .tipoOperacion(csvRecord.getTipoOperacion())
                .montoOperacion(csvRecord.getMontoOperacion())
                .fechaOperacion(csvRecord.getFechaOperacion())
                .build();
    }
}
