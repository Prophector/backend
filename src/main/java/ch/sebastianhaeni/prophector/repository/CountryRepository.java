package ch.sebastianhaeni.prophector.repository;

import ch.sebastianhaeni.prophector.model.Country;
import ch.sebastianhaeni.prophector.model.CountryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findCountryByIsoCode(String isoCode);

    @Query(value = "select c.name as name, " +
            "       c.population as population, " +
            "       c.iso_code as isoCode, " +
            "       pro_datapoint.date, " +
            "       pro_datapoint.region, " +
            "       pro_datapoint.cases, " +
            "       pro_datapoint.deaths, " +
            "       pro_datapoint.tests," +
            "       pro_datapoint.vaccinations " +
            "from pro_country c " +
            "         left join pro_datapoint on c.id = pro_datapoint.country_id and pro_datapoint.date = " +
            "                                                                        (SELECT max(d.date) " +
            "                                                                         from pro_datapoint d " +
            "                                                                         where d.country_id = c.id) " +
            "where date is not null " +
            "  and main_region = true", nativeQuery = true)
    List<CountryData> getCountryWithCovidData();
}
