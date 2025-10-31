package poslovne.aplikacije.servisi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateAppointmentRequest {
    @NotNull
    public Long patientId;

    @NotNull
    public Long doctorId;

    @NotBlank
    public String startTime; // ISO-8601 string

    @NotBlank
    public String endTime;   // ISO-8601 string

    @NotBlank
    public String reason;
}
