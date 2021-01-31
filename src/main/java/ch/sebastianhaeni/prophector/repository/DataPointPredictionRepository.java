package ch.sebastianhaeni.prophector.repository;

import ch.sebastianhaeni.prophector.model.DataPointPrediction;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPointPredictionRepository extends JpaRepository<DataPointPrediction, Long> {

    List<DataPointPrediction> findAllByModelOrderByDate(ProphetModel model);

}
