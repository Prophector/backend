package ch.sebastianhaeni.prophector.repository;

import ch.sebastianhaeni.prophector.model.DataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long> {

    List<DataPoint> findAllByRegionAndDateAfterAndDateBeforeOrderByDate(String region, LocalDate from, LocalDate to);

}
