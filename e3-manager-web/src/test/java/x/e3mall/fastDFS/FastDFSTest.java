package x.e3mall.fastDFS;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import x.e3mall.utils.FastDFSClient;

import java.io.IOException;

/**
 * @Author: hex1n
 * @Date: 2018/4/15 14:16
 */
public class FastDFSTest {


    @Test
    public void fastDFSUploadTest() throws IOException, MyException {
        /**
         * 1、添加FastDFS的jar包
         * 	2、创建一个配置文件，指定trackerServer的ip及端口号。
         * 	3、加载配置文件。
         * 	4、创建一个TrackerClient对象
         * 	5、使用TrackerClient获得一个TrackerServer对象。
         * 	6、创建一个StorageClient对象，两个构造参数1、TrackerServer 2、StorageServer（可以是null）
         * 	7、使用StorageClient上传文件。
         */

        //加载配置
        ClientGlobal.init("D:\\ideaWorkSpace\\e3mall\\e3-manager-web\\src\\main\\resources\\client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获得TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StorageServer引用,可以为null
        StorageServer storageServer = null;
        //创建StorageClient对象,两个构造参数:1.TrackerServer 2. StorageServer
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用StorageClient上传文件
        String[] jpgs = storageClient.upload_file("C:\\Users\\hex1n\\Downloads\\01f340a4456862c5bc4dbbd5faa2307f.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }

    }

    /**
     * 测试fastDFS上传工具类
     *
     * @throws Exception
     */
    @Test
    public void fastDFSUploadUtilsTest() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("D:\\ideaWorkSpace\\e3mall\\e3-manager-web\\src\\main\\resources\\client.conf");
        String file = fastDFSClient.uploadFile("C:\\Users\\hex1n\\Downloads\\35cfb049e431df7125837bdb39a0f2ba.jpg");
        System.out.println(file);
    }
}
