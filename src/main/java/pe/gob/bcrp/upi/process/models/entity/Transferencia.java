package pe.gob.bcrp.upi.process.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transferencia")
    private  Long id;

    @Column(name = "sid")
    private String sid;

    @Column(name = "cod_concepto")
    private String codConcepto;

    @Column(name = "cuenta_destino", length = 50)
    private String cuentaDestino;

    @Column(name = "cuenta_origen",length = 50)
    private String cuentaOrigen;

    @Column(name = "fecha_liquidacion")
    private Date fechaLiquidacion;

    @Column(name = "instrucciones_pago")
    private String instruccionesPago;

    @Column(name = "monto_operacion")
    private Double montoOperacion;

    @Column(name = "num_ref_lbtrenlace")
    private String numRefLBTREnlace;

    @Column(name = "num_ref_origen")
    private String numRefOrigen;

    @Column(name = "firma")
    private String firma;

    @OneToMany(mappedBy = "transferencia", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<File> files = new ArrayList<>();


}
