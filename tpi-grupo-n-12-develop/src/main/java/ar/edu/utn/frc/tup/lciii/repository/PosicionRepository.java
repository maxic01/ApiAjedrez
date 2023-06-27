package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.model.Posicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosicionRepository extends JpaRepository<Posicion, Long> {
    List<Posicion> findByPartidaId(Long partidaId);
}
