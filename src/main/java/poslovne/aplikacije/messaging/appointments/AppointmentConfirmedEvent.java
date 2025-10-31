package poslovne.aplikacije.messaging.appointments;

import java.io.Serializable;
import java.time.LocalDateTime;

import poslovne.aplikacije.appointments.Appointment;

public class AppointmentConfirmedEvent implements Serializable {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static AppointmentConfirmedEvent from(Appointment a) {
        AppointmentConfirmedEvent e = new AppointmentConfirmedEvent();
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
