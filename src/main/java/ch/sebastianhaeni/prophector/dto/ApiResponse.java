package ch.sebastianhaeni.prophector.dto;

import lombok.Value;

@Value
public class ApiResponse {
    boolean success;
    String message;
}
