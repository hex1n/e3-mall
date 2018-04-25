package x.e3malll.jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import x.e3mall.common.jedis.JedisClient;

/**
 * @Author: hex1n
 * @Date: 2018/4/19 14:04
 */
public class JedisClientTest {

    @Test
    public void testJedisClient(){
        //初始化spring容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("mytest","this is my first jedisClient");
        System.out.println(jedisClient.get("mytest"));
    }
}
