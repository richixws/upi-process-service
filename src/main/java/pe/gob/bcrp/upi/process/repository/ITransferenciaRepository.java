package pe.gob.bcrp.upi.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.upi.process.models.entity.Transferencia;

public interface ITransferenciaRepository extends JpaRepository<Transferencia,Long> {

}
