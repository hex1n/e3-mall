package x.e3mall;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * @Author: hex1n
 * @Date: 2018/4/22 13:36
 */
public class SpringActiveMqTest {


    /**
     * 以点对点形式发送消息
     */

    public void testSpringActiveMQ() {
        //初始化spring容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-mq.xml");
        //从spring容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //冲spring容器中获取Destination对象
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        //使用JmsTemplate对象发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建一个消息对象返回
                TextMessage textMessage = session.createTextMessage("spring activemq queue message");

                return textMessage;
            }
        });
    }

}
