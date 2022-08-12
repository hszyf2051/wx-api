package com.yif;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    private String url = "https://ai.foemy.com/doctor/callBack";

    private String corpid = "wx1199888fb72190c9";

    @Test
    public void url() throws ParseException, UnsupportedEncodingException {
        String redirect_uri = URLEncoder.encode(url, "UTF-8");
        String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI" +
                "&response_type=code" +
                "&scope=SCOPE" +
                "&state=123#wechat_redirect";
        System.out.println(wxUrl.replace("APPID", corpid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", "snsapi_userinfo"));
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }




    public void session(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}


