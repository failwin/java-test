package home.yura.pivot_test;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class YuraMessageListener implements MessageListener {

    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBody()));
    }
}
