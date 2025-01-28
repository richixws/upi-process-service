package pe.gob.bcrp.upi.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.upi.process.models.entity.Operacion;

import java.util.List;

public interface IOperacionRepository extends JpaRepository<Operacion,Long> {

    /**@Query("SELECT f FROM Operacion f WHERE f.isDeleted = false AND " +
            "(:fecha IS NULL OR f.fecha = :fecha) AND " +
            "(:concepto IS NULL OR LOWER(f.concepto) LIKE %:tipFeriado%)")
    List<Operacion> findByIndicadorOperacion(@Param("fecha") String fecha,
                                             @Param("concepto") String concepto);**/
}
