package nl.hilgenbos.psyichic_pies.repository;

import nl.hilgenbos.psyichic_pies.entity.PieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieRepository extends JpaRepository<PieEntity, Integer> {
}