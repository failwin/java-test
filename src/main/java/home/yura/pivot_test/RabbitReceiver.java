package home.yura.pivot_test;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitReceiver {

//    @RabbitListener(queuesToDeclare = @Queue(name = "hello-2", durable = "true"))
//    public void recievedMessage(String employee) {
//        System.out.println("Recieved Message From RabbitMQ: " + employee);
//    }
}