package com.wechuang.mallshop.common.utils;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.SmsDto;
import com.wechuang.mallshop.common.pojo.dto.UploadDto;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.sys.service.ThirdService;
import com.wechuang.mallshop.sys.service.impl.AliServiceImpl;
import com.wechuang.mallshop.sys.service.impl.HuaweiServiceimpl;
import com.wechuang.mallshop.sys.service.impl.ShopsuiteServiceImpl;
import com.wechuang.mallshop.sys.service.impl.TencentServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Component
public class ThirdUtil {

    private static final Map<Integer, ThirdService> thirdMap = new HashMap<>();

    public ThirdUtil(ApplicationContext applicationContext) {
        // todo 自带短信代码放出来，走设计模式，消除if else 降低维护成本
        thirdMap.put(0, applicationContext.getBean(ShopsuiteServiceImpl.class));
        thirdMap.put(1, applicationContext.getBean(AliServiceImpl.class));
        thirdMap.put(2, applicationContext.getBean(TencentServiceImpl.class));
        thirdMap.put(3, applicationContext.getBean(HuaweiServiceimpl.class));
    }

    /**
     * 发送短信
     *
     * @param smsDto
     */
    public static void send(SmsDto smsDto) {
        ThirdService thirdService = thirdMap.get(smsDto.getSmsType());
        thirdService.send(smsDto);
    }

    /**
     * 上传文件
     *
     * @param uploadDto
     */
    public static String upload(UploadDto uploadDto) {
        ThirdService thirdService = thirdMap.get(uploadDto.getUploadType());
        String url = thirdService.upload(uploadDto);

        return url;
    }

    //根据用户获取目录名
    public static String getUserDirName(ContextUser user) {
        if (user == null) {
            return "plantform/";
        }

        if (user.getUserId() == null) {
            throw new BusinessException(__("获取用户信息失败"));
        }

        if (user.isPlatform()) {
            return "media/plantform/";
        } else {
            if (user.isStore()) {
                return String.format("media/store/%d/", user.getUserId());
            } else {
                return String.format("media/user/%d/", user.getUserId());
            }
        }
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName
     * @return
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        switch (fileExtension) {
            case ".bmp":
                return "image/bmp";
            case ".gif":
                return "image/gif";
            case ".jpeg":
            case ".jpg":
            case ".png":
                return "image/jpeg";
            case ".html":
                return "text/html";
            case ".txt":
                return "text/plain";
            case ".vsd":
                return "application/vnd.visio";
            case ".ppt":
            case ".pptx":
                return "application/vnd.ms-powerpoint";
            case ".doc":
            case ".docx":
                return "application/msword";
            case ".xml":
                return "text/xml";
            case ".mp4":
                return "video/mp4";
            case ".awf":
                return "application/vnd.adobe.workflow";
            case ".wav":
                return "audio/wav";
            case ".zip":
                return "application/zip";
            case ".pdf":
                return "application/pdf";
            case ".ogg":
                return "application/ogg";
            case ".js":
                return "application/javascript";
            default:
                return "multipart/form-data";
        }
    }

}
