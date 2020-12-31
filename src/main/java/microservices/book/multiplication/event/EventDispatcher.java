package microservices.book.multiplication.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Maneja la comunicación con el bus de eventos
 */

@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;

    // El exchange usado para enviar algo relacionado con Multiplication
    private String multiplicationExchage;

    // La key de ruta para este evento en específico
    private String multiplicationSolvedRoutingKey;

    @Autowired
    EventDispatcher(final RabbitTemplate rabbitTemplate, @Value("${multiplication.exchange}") final String multiplicationExchage, @Value("${multiplication.solved.key}") final String multiplicationSolvedRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.multiplicationExchage = multiplicationExchage;
        this.multiplicationSolvedRoutingKey = multiplicationSolvedRoutingKey;
    }

    public void send(final MultiplicationSolvedEvent multiplicationSolvedEvent) {
        rabbitTemplate.convertAndSend(multiplicationExchage, multiplicationSolvedRoutingKey, multiplicationSolvedEvent);
    }
}