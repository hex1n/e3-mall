package x.e3mall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author: hex1n
 * @Date: 2018/4/22 13:54
 */
//接收消息
//创建一个MessageListener的实现类
public class MyMessageListener implements MessageListener {


    @Override
    public void onMessage(Message message) {


        try {
            TextMessage testMessage = (TextMessage) message;
            //取消息内容
            String text = testMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
