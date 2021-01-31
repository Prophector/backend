package ch.sebastianhaeni.prophector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity(name = "datapoint")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class DataPoint {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Country country;

    private String region;

    private BigInteger cases;
    private BigInteger deaths;
    private BigInteger hospitalizations;
    private BigInteger tests;
    private BigInteger vaccinations;

}
