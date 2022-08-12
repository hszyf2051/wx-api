package com.yif.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * @author Yif
 */
@Component
public class OuthUtil {

    @Value("${doctor.corpid}")
    private String corpid;

    @Value("${doctor.corpsecret}")
    private String corpsecret;

    @Value("${doctor.redirect_uri}")
    private String url;

    public String getOuth2Url() throws Exception {
        String redirect_uri = URLEncoder.encode(url, "UTF-8");
        String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI" +
                "&response_type=code" +
                "&scope=SCOPE" +
                "&state=123#wechat_redirect";
        return wxUrl.replace("APPID", corpid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", "snsapi_base");
    }
}


