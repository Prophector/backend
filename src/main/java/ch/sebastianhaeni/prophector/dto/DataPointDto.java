package ch.sebastianhaeni.prophector.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class DataPointDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigInteger value;

}
