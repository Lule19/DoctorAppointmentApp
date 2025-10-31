package poslovne.aplikacije.messaging.appointments;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Reverted: no longer a Spring bean; kept only to preserve compile history if referenced in comments
public class NotificationLogService {

    public static class Entry {
        public LocalDateTime timestamp;
        public String type; // CONFIRMED or REJECTED
        public Long appointmentId;
        public Long doctorId;
        public Long patientId;
        public String reason;
    }

    private static final int MAX = 100;
    private final Deque<Entry> buffer = new ArrayDeque<>(MAX);

    public synchronized void addConfirmed(AppointmentConfirmedEvent e) {
        Entry en = new Entry();
        en.timestamp = LocalDateTime.now();
        en.type = "CONFIRMED";
        en.appointmentId = e.getAppointmentId();
        en.doctorId = e.getDoctorId();
        en.patientId = e.getPatientId();
        add(en);
    }

    public synchronized void addRejected(AppointmentRejectedEvent e) {
        Entry en = new Entry();
        en.timestamp = LocalDateTime.now();
        en.type = "REJECTED";
        en.appointmentId = e.getAppointmentId();
        en.doctorId = e.getDoctorId();
        en.patientId = e.getPatientId();
        en.reason = e.getReason();
        add(en);
    }

    private void add(Entry en) {
        if (buffer.size() >= MAX) {
            buffer.removeFirst();
        }
        buffer.addLast(en);
    }

    public synchronized List<Entry> list() {
        return new ArrayList<>(buffer);
    }
}
