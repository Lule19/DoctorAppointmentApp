package poslovne.aplikacije.messaging.appointments;

import java.io.Serializable;

import poslovne.aplikacije.appointments.Appointment;

public class AppointmentRejectedEvent implements Serializable {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String reason;

    public static AppointmentRejectedEvent from(Appointment a, String reason) {
        AppointmentRejectedEvent e = new AppointmentRejectedEvent();
        e.appointmentId = a.getId();
        e.patientId = a.getPatient().getId();
        e.doctorId = a.getDoctor().getId();
        e.reason = reason;
        return e;
    }

    public Long getAppointmentId() { return appointmentId; }
    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public String getReason() { return reason; }
}
