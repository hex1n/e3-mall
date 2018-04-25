package x.e3malll.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

/**
 * @Author: hex1n
 * @Date: 2018/4/19 0:26
 */
public class JedisTest {


    /**
     * 连接单机版
     */
    @Test
    public void testJedis() {

        //第一步,创建一个Jedis对象,需要指定服务端的ip及端口
        Jedis jedis = new Jedis("192.168.25.128", 6379);
        //第二步:使用Jedis对象操作数据库,每一个redis命令对应一个方法
        jedis.set("haha","hehe");
        jedis.set("haha","xixi");
        //第三步,打印结果
        System.out.println(jedis.get("haha"));
        //第四步,关闭Jedis
        jedis.close();
    }

    /**
     * 连接单机版使用线程池
     */
    @Test
    public void testJedisPool() {
        //第一步,创建一个JedisPool连接池对象,需要指定服务端的ip及接口
        JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
        //第二步,从JedisPool中获取Jedis对象
        Jedis jedis = jedisPool.getResource();
        //第三步,使用jedis操作redis服务器
        jedis.set("jedis", "test");
        String result = jedis.get("jedis");
        System.out.println(result);
        //关闭Jedis连接,每次使用后关闭连接,连接池回收资源
        jedis.close();
        jedisPool.close();
    }


    /**
     * 连接集群版
     */
    @Test
    public void testJedisCluster() {
        //第一步:使用JedisCluster对象,需要一个Set<HostAndPort>参数,Redis节点列表
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.128", 7001));
        nodes.add(new HostAndPort("192.168.25.128", 7002));
        nodes.add(new HostAndPort("192.168.25.128", 7003));
        nodes.add(new HostAndPort("192.168.25.128", 7004));
        nodes.add(new HostAndPort("192.168.25.128", 7005));
        nodes.add(new HostAndPort("192.168.25.128", 7006));
        //第二步:直接使用JedisCluster对像操作redis,在系统中单例存在
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("hello","100");
        String result = jedisCluster.get("hello");
        System.out.println(result);
        jedisCluster.close();

    }
}
