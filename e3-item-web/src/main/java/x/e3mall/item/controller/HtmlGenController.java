package x.e3mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: hex1n
 * @Date: 2018/4/23 14:17
 */

/**
 * 生成静态页面测试Controller
 */
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //加载模板对象
        Template template = configuration.getTemplate("hello.ftl");
        //创建一个数据集
        HashMap<Object, Object> map = new HashMap<>();
        map.put("hello",123456);
        //指定文件输出的路径及文件名
        FileWriter writer = new FileWriter(new File("D:\\wsdl\\freemarker/hello2.html"));
        //输出文件
        template.process(map,writer);
        //关闭流
        writer.close();
        return "ok";
    }
}
