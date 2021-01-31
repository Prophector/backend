package ch.sebastianhaeni.prophector.repository;

import ch.sebastianhaeni.prophector.model.Country;
import ch.sebastianhaeni.prophector.model.ModelStatus;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import ch.sebastianhaeni.prophector.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProphetModelRepository extends JpaRepository<ProphetModel, Long> {

    List<ProphetModel> findAllByCountryAndStatus(Country c, ModelStatus status);

    List<ProphetModel> findAllByCountryAndOwner(Country c, User owner);

    List<ProphetModel> findAllByOwner(User owner);

}
