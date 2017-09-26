package ru.tsconsulting.processors;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;

public class SoapPostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        if (message.getJMSReplyTo() == null) {
            message.setJMSReplyTo(new ActiveMQQueue("SoapResponseQueue"));
        }
        System.out.println(message);
        return message;
    }
}
