import e3mall.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hex1n
 * @Date: 2018/4/23 11:42
 */
public class FreeMarkerTest {


    /**
     * freemarker简单使用
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void genFile() throws IOException, TemplateException {
        //第一步:创建一个Configuration对象,直接new一个对象,构造方法的参数是freemarker对应版本号
        Configuration configuration = new Configuration(Configuration.getVersion());
        //第二部:设置模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("D:\\ideaWorkSpace\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        //第三步:设置模板文件使用的字符集,一般设置utf-8
        configuration.setDefaultEncoding("utf-8");
        //第四步:加载一个模版,创建一个模板对象
        Template template = configuration.getTemplate("hello.ftl");
        //第五步:创建一个模板使用数据集,可以是pojo,也可以是map,一般是map
        Map map = new HashMap();
        //向数据集中添加数据
        map.put("hello", "this is my first freemarker test");
        //第六步:创建一个Writer对象,一般创建FileWriter对象,指定生成的文件名
        FileWriter writer = new FileWriter(new File("D:\\wsdl\\freemarker\\hello.html"));
        //第七步:使用模板对象的process方法输出文件
        template.process(map, writer);
        //第八步:关闭流
        writer.close();
    }

    /**
     * 访问pojo中的属性
     */
    @Test
    public void freemarkerTest2() throws IOException, TemplateException {

        //第一步:创建一个Configuration对象,直接new一个对象,构造方法的参数是freemarker对应版本号
        Configuration configuration = new Configuration(Configuration.getVersion());
        //第二部:设置模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("D:\\ideaWorkSpace\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        //第三步:设置模板文件使用的字符集,一般设置utf-8
        configuration.setDefaultEncoding("utf-8");
        //第四步:加载一个模,创建一个模板对象
        Template template = configuration.getTemplate("student.ftl");
        //第五步:创建一个模板使用数据集,可以是pojo,也可以是map,一般是map
        HashMap<Object, Object> map = new HashMap<>();
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三1", 18));
        studentList.add(new Student(2, "张三2", 19));
        studentList.add(new Student(3, "张三3", 20));
        studentList.add(new Student(4, "张三4", 21));
        studentList.add(new Student(5, "张三5", 22));
        studentList.add(new Student(6, "张三6", 23));
        studentList.add(new Student(7, "张三7", 24));
        studentList.add(new Student(8, "张三8", 25));
        studentList.add(new Student(9, "张三9", 26));
        //向数据集中添加数据
        map.put("studentList", studentList);
        map.put("date", new Date());
        map.put("hello", "hello freemarker");
        //第六步:创建一个Writer对象,一般创建FileWriter对象,指定生成的文件名
        FileWriter writer = new FileWriter(new File("D:\\wsdl\\freemarker\\student.html"));
        //第七步:使用模板对象的process方法输出文件
        template.process(map, writer);
        //第八步:关闭流
        writer.close();
    }


}
