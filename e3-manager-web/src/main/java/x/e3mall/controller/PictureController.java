package x.e3mall.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import x.e3mall.common.utils.FastDFSClient;
import x.e3mall.common.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hex1n
 * @Date: 2018/4/15 15:19
 */

@Controller
public class PictureController {


    @Value("${image.server.url}")
    private String IMAGE_SERVER_URL;

    /**
     * 图片上传
     *
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String fileUpload(MultipartFile uploadFile) {

        try {
            //获取文件全名
            String originalFilename = uploadFile.getOriginalFilename();
            //获取扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //用图片上传工具类
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:client.conf");
            /**
             * 参数1:文件名
             * 参数2:文件扩展名
             */
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //补充完整的URL
            url = IMAGE_SERVER_URL + url;
            //上传文件成功
            Map map = new HashMap();
            map.put("error", 0);
            map.put("url", url);

            return JsonUtils.objectToJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("error", 1);
            map.put("massage", "上传失败");

            return JsonUtils.objectToJson(map);
        }
    }
}
