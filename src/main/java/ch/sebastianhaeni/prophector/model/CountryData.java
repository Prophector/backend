package ch.sebastianhaeni.prophector.model;

import java.math.BigInteger;
import java.time.LocalDate;

public interface CountryData extends BaseCountry {
    String getName();

    BigInteger getPopulation();

    String getIsoCode();

    LocalDate getDate();

    String getRegion();

    BigInteger getCases();

    BigInteger getDeaths();

    BigInteger getTests();

    BigInteger getVaccinations();
}
