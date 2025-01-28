package pe.gob.bcrp.upi.process.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacionResponse {
    @JsonProperty("contenido")
    private ListaOperaciones content;
}
