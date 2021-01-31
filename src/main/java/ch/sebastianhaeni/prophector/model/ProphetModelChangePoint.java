package ch.sebastianhaeni.prophector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ProphetModelChangePoint {

    @Id
    @GeneratedValue(generator = "pro_prophet_model_change_point_id_seq")
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate date;

    @ManyToOne(optional = false)
    private ProphetModel model;

}
