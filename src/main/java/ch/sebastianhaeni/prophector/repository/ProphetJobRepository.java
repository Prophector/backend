package ch.sebastianhaeni.prophector.repository;

import ch.sebastianhaeni.prophector.model.ProphetJob;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProphetJobRepository extends JpaRepository<ProphetJob, Long> {
    List<ProphetJob> findAllByModel_Id(Long modelId);
}
