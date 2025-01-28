package pe.gob.bcrp.upi.process.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.upi.process.models.dto.OperacionResponse;
import pe.gob.bcrp.upi.process.service.OperacionService;

import java.util.List;


@RestController
@RequestMapping("/api/")
@Slf4j
//@Tag(name = "Feriados",description = "Operaciones de feriado  - Listar Feriados, Guardar Feriado, Actualizar Feriado, Eliminar Feriado")
public class OperacionController {


     private final OperacionService operacionService;

    public OperacionController(OperacionService operacionService) {
        this.operacionService = operacionService;
    }


    @GetMapping("/operaciones")
    public ResponseEntity<?> getAllFeriados(
                        @RequestParam(name = "fecha",    required = false) String fecha,
                        @RequestParam(name = "concepto", required = false) String concepto
    ){

        log.info("INI - getAllFeriados | requestURL=feriados");
        try {
            OperacionResponse operacionesResponse=operacionService.listarOperacionesTIN(fecha, concepto);//,nombre
            return new ResponseEntity<>(operacionesResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - getAllModulos | requestURL=modulos{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
