package poslovne.aplikacije.messaging.appointments;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.appointments.Appointment;

@Component
public class AppointmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRequested(Appointment appt) {
        AppointmentRequestedEvent event = AppointmentRequestedEvent.from(appt);
        rabbitTemplate.convertAndSend(
                RabbitMQConfigurator.APPOINTMENTS_TOPIC_EXCHANGE_NAME,
                "appointments.requested",
                event
        );
    }

    public void publishConfirmed(Appointment appt) {
        AppointmentConfirmedEvent event = AppointmentConfirmedEvent.from(appt);
        rabbitTemplate.convertAndSend(
                RabbitMQConfigurator.APPOINTMENTS_TOPIC_EXCHANGE_NAME,
                "appointments.confirmed",
                event
        );
    }

    public void publishRejected(Appointment appt, String reason) {
        AppointmentRejectedEvent event = AppointmentRejectedEvent.from(appt, reason);
        rabbitTemplate.convertAndSend(
                RabbitMQConfigurator.APPOINTMENTS_TOPIC_EXCHANGE_NAME,
                "appointments.rejected",
                event
        );
    }
}