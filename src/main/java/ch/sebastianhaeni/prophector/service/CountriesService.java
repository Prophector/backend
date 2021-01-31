package ch.sebastianhaeni.prophector.service;

import ch.sebastianhaeni.prophector.model.Country;
import ch.sebastianhaeni.prophector.model.CountryData;
import ch.sebastianhaeni.prophector.model.DataPoint;
import ch.sebastianhaeni.prophector.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountriesService {

    private final CountryRepository countryRepository;
    private final TimeseriesService timeseriesService;

    public CountriesService(CountryRepository countryRepository, TimeseriesService timeseriesService) {
        this.countryRepository = countryRepository;
        this.timeseriesService = timeseriesService;
    }

    public Optional<Country> getCountry(String isoCode) {
        return countryRepository.findCountryByIsoCode(isoCode);
    }

    public List<CountryData> getCountries() {
        return countryRepository.getCountryWithCovidData();
    }

    /**
     * Get historical data per country. This is involves many SQL queries and is an expensive operation not suited
     * for major use.
     * The return value is cached by the parameters. That's why the first parameter should be the day of today so the
     * data is invalidated for a new day.
     */
    @Cacheable("getCountriesWithHistorical")
    public Map<CountryData, List<DataPoint>> getCountriesWithHistorical(LocalDate today, int days) {
        log.info("Computing countries with historical data for {} with days={}", today, days);

        return getCountries().stream()
                .collect(Collectors.toMap(Function.identity(), country -> {
                    var from = LocalDate.now().minus(days, ChronoUnit.DAYS);
                    var to = LocalDate.now();
                    return timeseriesService.getTimeseries(from, to, country.getRegion());
                }));
    }
}
