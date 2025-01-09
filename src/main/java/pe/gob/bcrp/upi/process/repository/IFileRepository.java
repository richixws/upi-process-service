package pe.gob.bcrp.upi.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.upi.process.models.entity.File;

public interface IFileRepository extends JpaRepository<File, Long> {


}
