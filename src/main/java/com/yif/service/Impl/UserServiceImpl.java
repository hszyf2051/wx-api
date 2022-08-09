package com.yif.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yif.entity.User;
import com.yif.service.IUserService;
import com.yif.util.HttpUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Value("${User.fileName}")
    private String fileName;

    @Override
    public String getToken(String corpid, String corpsecret) {
        Map map = new HashMap();
        map.put("corpid",corpid);
        map.put("corpsecret",corpsecret);
        String s = HttpUtil.httpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken", null, map);
        JSONObject jsonObject = JSON.parseObject(s);
        String access_token = jsonObject.getString("access_token");
        return access_token;
    }

    @GetMapping("/getUser")
    @ApiOperation(value = "获取部门成员信息", notes = "Excel获取成员信息")
    @Override
    public List<User> getUsers(String token, String id) {
        Map map = new HashMap();
        map.put("access_token", token);
        map.put("department_id", id);
        String str = HttpUtil.httpGet("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist", null, map);
        JSONObject jsonStr = JSON.parseObject(str);
        String userList = jsonStr.getString("userlist");
        // 获取部门成员 封装成集合对象
        List<User> users = JSON.parseArray(userList, User.class);
        String fileName = "C:\\Users\\admin\\Desktop\\Api\\魔梵研发组.xlsx";
        // 向Excel写入数据
        // 创建ExcelWriter对象
        ExcelWriter excelWriter = EasyExcel.write(fileName, User.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("用户信息").build();
        excelWriter.write(users, writeSheet);
        // 关闭
        excelWriter.finish();
        return users;
    }

    @Override
    public List<User> readUsers() {
        // 读取的文件路径
        // 创建一个数据格式来承装读取到数据
        Class<User> head = User.class;
        List<User> userList = new ArrayList<>();
        // 创建ExcelReader对象
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<User>() {
            @Override
            public void invoke(User user, AnalysisContext analysisContext) {
                // 获取每一个成员对象
                userList.add(user);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("数据解析已完成");
            }
        }).build();
        // 创建sheet对象,并读取Excel的第1个sheet(下标从0开始)
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 关闭流操作，在读取文件时会创建临时文件,如果不关闭,磁盘爆掉
        excelReader.finish();
        return userList;
    }

    @Override
    public List<User> findUserById(String id) {
        // 创建一个数据格式来承装读取到数据
        Class<User> head = User.class;
        // 创建ExcelReader对象
        List<User> userList = new ArrayList<>();
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<User>() {
            @Override
            public void invoke(User user, AnalysisContext analysisContext) {
                if (user.getUserid().equals(id)) {
                    userList.add(user);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (userList.size() == 0) {
                    log.info("该用户:"+ id +",查无此人");
                }
            }
        }).build();
        // 创建sheet对象,并读取Excel的第1个sheet(下标从0开始)
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 关闭流操作，在读取文件时会创建临时文件,如果不关闭,磁盘爆掉
        excelReader.finish();
        log.info(String.valueOf(userList));
        return userList;
    }

    @Override
    public String sendMsg(String token,String id) {
        // 参数
        Map<String, Object> paramMap = new HashMap<>();
        // 图文内容
        Map newsMap = new HashMap();
        // 发送图文的api中articles的类型是数组
        ArrayList<Object> arrayList = new ArrayList<>();
        Map articlesMap = new HashMap();
        articlesMap.put("title","医生节活动");
        articlesMap.put("description","救死扶伤，大爱无疆");
        articlesMap.put("url","http://192.168.3.216:8181/findDoctorById?id=ZhouYiFan");
        articlesMap.put("picurl","https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0419%2Fdd75d004p00q902bt003id200u000mig00hx00df.png&thumbnail=660x2147483647&quality=80&type=jpg");
        arrayList.add(articlesMap);
        paramMap.put("touser",id);
        paramMap.put("msgtype", "news");
//        paramMap.put("toparty", 3);
        paramMap.put("agentid", 2);
        paramMap.put("news", newsMap);
        newsMap.put("articles", arrayList);

        String jsonObject = String.valueOf(new JSONObject(paramMap));
        String httpPost = HttpUtil.httpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, (Map<String, Object>) null, jsonObject);
        return httpPost;
    }
}
