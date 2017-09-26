package ru.tsconsulting.listeners;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.ws.transport.jms.JmsTransportException;
import org.springframework.ws.transport.jms.WebServiceMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class SoapServiceMessageListener extends WebServiceMessageListener {

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        try {
            if (message.getJMSReplyTo() == null) {
                message.setJMSReplyTo(new ActiveMQQueue("SoapResponseQueue"));
            }
            handleMessage(message, session);
        }
        catch (JmsTransportException ex) {
            throw ex.getJmsException();
        }
        catch (Exception ex) {
            JMSException jmsException = new JMSException(ex.getMessage());
            jmsException.setLinkedException(ex);
            throw jmsException;
        }
    }

}
