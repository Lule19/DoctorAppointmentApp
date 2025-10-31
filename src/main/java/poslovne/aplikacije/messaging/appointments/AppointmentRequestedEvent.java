package poslovne.aplikacije.messaging.appointments;

import java.io.Serializable;
import java.time.LocalDateTime;

import poslovne.aplikacije.appointments.Appointment;

public class AppointmentRequestedEvent implements Serializable {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static AppointmentRequestedEvent from(Appointment a) {
        AppointmentRequestedEvent e = new AppointmentRequestedEvent();
        e.appointmentId = a.getId();
        e.patientId = a.getPatient().getId();
        e.doctorId = a.getDoctor().getId();
        e.startTime = a.getStartTime();
        e.endTime = a.getEndTime();
        return e;
    }

    public Long getAppointmentId() { return appointmentId; }
    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
