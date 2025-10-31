package poslovne.aplikacije;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpDevCleanup implements ApplicationRunner {

    private final AmqpAdmin amqpAdmin;

    @Value("${app.amqp.purge-on-start:false}")
    private boolean purgeOnStart;

    public AmqpDevCleanup(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!purgeOnStart) {
            System.out.println("[AMQP] Queue purge disabled (app.amqp.purge-on-start=false)");
            return;
        }
        try {
            amqpAdmin.purgeQueue(RabbitMQConfigurator.APPOINTMENTS_AVAILABILITY_QUEUE, false);
            amqpAdmin.purgeQueue(RabbitMQConfigurator.APPOINTMENTS_NOTIFICATION_QUEUE, false);
            System.out.println("[AMQP] Purged queues on startup (dev mode)");
        } catch (Exception e) {
            // Non-fatal in dev; just log.
            System.out.println("[AMQP] Queue purge skipped: " + e.getMessage());
        }
    }
}
