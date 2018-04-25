package x.e3mall.search.exception;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 19:33
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        //写日志文件
        logger.error("系统发生异常", e);
        //发邮件,发短信
        //Jmail
        //需要在购买短信,调用第三方接口
        //展示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "系统发生异常,请稍后重试");
        modelAndView.setViewName("error/exception");


        return modelAndView;
    }
}
