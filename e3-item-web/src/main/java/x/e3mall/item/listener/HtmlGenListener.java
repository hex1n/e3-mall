package x.e3mall.item.listener;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import x.e3mall.item.pojo.Item;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbItemDesc;
import x.e3mall.service.ItemService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * @Author: hex1n
 * @Date: 2018/4/23 14:26
 */
// 监听商品添加信息,生成对应的静态页面
public class HtmlGenListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //创建一个模板
            //从消息中取商品
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = Long.parseLong(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品信息
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            //取商品描述
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //创建一个数据集,把商品数据封装
            HashMap<Object, Object> map = new HashMap<>();
            map.put("item", item);
            map.put("itemDesc", itemDesc);
            //加载模板对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流,指定输出的目录及文件名
            FileWriter writer = new FileWriter(new File(HTML_GEN_PATH + itemId + ".html"));
            //生成静态页面
            template.process(map, writer);
            //关闭流
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
