package poslovne.aplikacije.servisi;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.appointments.Appointment;
import poslovne.aplikacije.appointments.AppointmentStatus;
import poslovne.aplikacije.appointments.Doctor;
import poslovne.aplikacije.appointments.Patient;
import poslovne.aplikacije.messaging.appointments.AppointmentEventPublisher;
import poslovne.aplikacije.repository.AppointmentRepository;
import poslovne.aplikacije.repository.DoctorRepository;
import poslovne.aplikacije.repository.PatientRepository;
import poslovne.aplikacije.servisi.dto.CreateAppointmentRequest;
import poslovne.aplikacije.exceptions.NotFoundException;
import poslovne.aplikacije.exceptions.BadRequestException;

@Service
public class AppointmentService {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private RabbitTemplate rabbitTemplate; 
    @Autowired private AppointmentEventPublisher eventPublisher;

    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest req) {
        Patient patient = patientRepository.findById(req.patientId)
            .orElseThrow(() -> new NotFoundException("Patient not found: " + req.patientId));
        Doctor doctor = doctorRepository.findById(req.doctorId)
            .orElseThrow(() -> new NotFoundException("Doctor not found: " + req.doctorId));

        try {
            LocalDateTime start = LocalDateTime.parse(req.startTime);
            LocalDateTime end = LocalDateTime.parse(req.endTime);
            if (!end.isAfter(start)) {
                throw new BadRequestException("endTime must be after startTime");
            }
            Appointment appt = new Appointment(patient, doctor, start, end, req.reason);
            appointmentRepository.save(appt);

            
            eventPublisher.publishRequested(appt);
            return appt;
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Invalid date format, expected ISO-8601: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Appointment get(Long id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
    }

    @Transactional(readOnly = true)
    public java.util.List<Appointment> listFiltered(Long doctorId, AppointmentStatus status, java.time.LocalDateTime fromTs, java.time.LocalDateTime toTs) {
        return appointmentRepository.findFiltered(doctorId, status, fromTs, toTs);
    }
}
