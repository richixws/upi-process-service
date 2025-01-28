package pe.gob.bcrp.upi.process.models.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.bcrp.upi.process.enums.IndicadorOperacion;
import pe.gob.bcrp.upi.process.enums.IndicadorSolicitudTIN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "operaciones")
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private  Long id;

    @Column(name = "entidad")
    private String entidad;

    @Column(name = "tipo_operacion")
    private String tipoOperacion;

    @Column(name = "monto_operacion")
    private Double montoOperacion;

    @Column(name = "fecha_operacion")
    private Date fechaOperacion;

    //@Enumerated(EnumType.STRING)
    //private IndicadorSolicitudTIN indicadorsolicitudTIN;

    //@Enumerated(EnumType.STRING)
    //private IndicadorOperacion indicadorOperacion;
    @Column(name = "codigo_concepto")
    private String codConcepto;


    @OneToMany(mappedBy = "operacion", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<File> files = new ArrayList<>();


}
