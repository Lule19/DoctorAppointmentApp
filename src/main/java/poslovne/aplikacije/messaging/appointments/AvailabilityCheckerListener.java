package poslovne.aplikacije.messaging.appointments;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.appointments.Appointment;
import poslovne.aplikacije.appointments.AppointmentStatus;
import poslovne.aplikacije.repository.AppointmentRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class AvailabilityCheckerListener {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    
    @RabbitListener(queues = RabbitMQConfigurator.APPOINTMENTS_AVAILABILITY_QUEUE)
    public void onRequested(AppointmentRequestedEvent event) {
    System.out.println("[AvailabilityChecker] Received requested appointment id=" + event.getAppointmentId()
        + " doctor=" + event.getDoctorId() + " patient=" + event.getPatientId());

    Appointment appt = appointmentRepository.findById(event.getAppointmentId())
        .orElse(null);
    if (appt == null) {
        System.out.println("[AvailabilityChecker] Appointment not found, ignoring: id=" + event.getAppointmentId());
        return;
    }

  
    if (appt.getStatus() != AppointmentStatus.PENDING) {
        System.out.println("[AvailabilityChecker] Appointment already processed with status=" + appt.getStatus());
        return;
    }

    List<AppointmentStatus> conflictStatuses = Arrays.asList(AppointmentStatus.CONFIRMED);
    long overlaps = appointmentRepository.countOverlaps(
        event.getDoctorId(), event.getStartTime(), event.getEndTime(), conflictStatuses);
    boolean conflict = overlaps > 0;

    if (conflict) {
        appt.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appt);

        eventPublisher.publishRejected(appt, "Doctor already booked in that time range");
        System.out.println("[AvailabilityChecker] Rejected appointment id=" + appt.getId());
    } else {
        appt.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appt);

        eventPublisher.publishConfirmed(appt);
        System.out.println("[AvailabilityChecker] Confirmed appointment id=" + appt.getId());
    }
    }
}
