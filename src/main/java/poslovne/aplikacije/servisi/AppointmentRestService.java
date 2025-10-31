package poslovne.aplikacije.servisi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import poslovne.aplikacije.appointments.Appointment;
import poslovne.aplikacije.appointments.AppointmentStatus;
import poslovne.aplikacije.servisi.dto.CreateAppointmentRequest;

@RestController
@RequestMapping("/appointments")
public class AppointmentRestService {

    @Autowired private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody CreateAppointmentRequest req) {
        Appointment appt = appointmentService.createAppointment(req);
        return ResponseEntity.ok(appt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> get(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.get(id));
    }

   
    @GetMapping
    public ResponseEntity<List<Appointment>> list(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        LocalDateTime fromTs = from != null && !from.isEmpty() ? LocalDateTime.parse(from) : null;
        LocalDateTime toTs = to != null && !to.isEmpty() ? LocalDateTime.parse(to) : null;
        return ResponseEntity.ok(appointmentService.listFiltered(doctorId, status, fromTs, toTs));
    }
}
