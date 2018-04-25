package x.e3mall;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: hex1n
 * @Date: 2018/4/22 14:00
 */
public class SpringMqTest {

    @Test
    public void testQueueConsumer() throws IOException {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-mq.xml");
        //等待
        System.in.read();
    }
}
