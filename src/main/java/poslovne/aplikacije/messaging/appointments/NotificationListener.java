package poslovne.aplikacije.messaging.appointments;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import poslovne.aplikacije.RabbitMQConfigurator;

@Service
@RabbitListener(queues = RabbitMQConfigurator.APPOINTMENTS_NOTIFICATION_QUEUE)
public class NotificationListener {

    @RabbitHandler
    public void onNotification(AppointmentConfirmedEvent event) {
        System.out.println("[Notification] Appointment CONFIRMED id=" + event.getAppointmentId()
                + " doctor=" + event.getDoctorId() + " patient=" + event.getPatientId());
    }

    @RabbitHandler
    public void onNotification(AppointmentRejectedEvent event) {
        System.out.println("[Notification] Appointment REJECTED id=" + event.getAppointmentId()
                + " doctor=" + event.getDoctorId() + " patient=" + event.getPatientId()
                + " reason='" + event.getReason() + "'");
    }

    @RabbitHandler(isDefault = true)
    public void onUnknown(byte[] body) {
        System.out.println("[Notification] Unknown notification payload bytes length=" + (body == null ? 0 : body.length));
    }
}
