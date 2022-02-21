package net.controller.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.config.KeysConfig;
import net.config.RegexConfig;
import net.pojo.User;
import net.service.ILoginService;
import net.service.IQueryService;
import net.service.IRegisterService;
import net.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.model.ResponseBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private KeysConfig keysConfig;
    @Autowired
    private RegexConfig regexConfig;

    @Autowired
    private ILoginService loginService;
    @Autowired
    private IQueryService queryService;
    @Autowired
    private IRegisterService registerService;
    @Autowired
    private ISearchService searchService;



    // 登陆（已测试）
    @RequestMapping(value = "/user/login",produces = "application/json;charset=utf-8")//
    public ResponseBody userLogin(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);

        final String username =  map.get("username").toString();
        String encodedPassword = sha256Encoder(map.get("password").toString()); // 密码加密

        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("username", username);
        searchMap.put("password",encodedPassword);

        int flag = loginService.queryLogin(searchMap);
        if (flag==1){
            User user = queryService.queryUserByUsername(new HashMap<String, Object>(){{put("username",username);}});
            Map<String, String> responseMap = sign(user.getId());
            responseMap.put("message", "登陆成功");
            return new AssembleResponseMsg().success(JSONObject.toJSON(responseMap));
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","用户名或密码错误");
        }
    }


    // 注册（已测试）
    @RequestMapping(value = "/user/register",produces = "application/json;charset=utf-8")//
    public ResponseBody userRegister(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);

        int checkResult = checkUser(map);
        switch (checkResult){
            case 0:break;
            case 1:return new AssembleResponseMsg().failure(200,"error","用户名非法！用户名只能为大小写字母、数字或下划线，且长度为3~20个字符。");
            case 2:return new AssembleResponseMsg().failure(200,"error","密码非法！密码只能为大小写字母或数字，且长度为6~20个字符。");
            default:break;
        }

        String username =  map.get("username").toString();
        String password = map.get("password").toString();

        String encodedPassword = sha256Encoder(password); // 密码加密

        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("username", username);
        searchMap.put("password",encodedPassword);

        int flag = registerService.insertUser(searchMap);
        if (flag>0){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("returnCode", 200);
            responseMap.put("message", "注册成功");
            return new AssembleResponseMsg().success(JSONObject.toJSON(responseMap));
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","用户名已存在");
        }

    }


    // 获取用户信息（已测试）
    @RequestMapping(value = "/user/info",produces = "application/json;charset=utf-8")//
    public ResponseBody getUserInfo(@RequestParam("token") String token){
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("id",id);
        User user = queryService.queryUserById(searchMap);
        if (user!=null){
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("username", user.getUsername());
            responseMap.put("avatar", user.getAvatar());
            responseMap.put("coins", String.valueOf(user.getCoins()));
            return new AssembleResponseMsg().success(JSONObject.toJSON(responseMap));
        }
        else{
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
    }


    // 登出（已测试）
    @RequestMapping(value = "/user/logout",produces = "application/json;charset=utf-8")
    public ResponseBody userLogout(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);

        String token =  map.get("token").toString();
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("id",id);
        User user = queryService.queryUserById(searchMap);
        if (user!=null){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "登出成功");
            return new AssembleResponseMsg().success(JSONObject.toJSON(responseMap));
        }
        else{
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
    }

    //更改用户名和密码（已测试）
    @RequestMapping(value = "/user/changeInfo",produces = "application/json;charset=utf-8")//
    public ResponseBody changeInfo(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);

        String token =  map.get("token").toString();
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        int checkResult = checkUser(map);
        switch (checkResult){
            case 0:break;
            case 1:return new AssembleResponseMsg().failure(200,"error","用户名非法！用户名只能为大小写字母、数字或下划线，且长度为3~20个字符。");
            case 2:return new AssembleResponseMsg().failure(200,"error","密码非法！密码只能为大小写字母或数字，且长度为6~20个字符。");
            default:break;
        }

        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String encodedPassword = sha256Encoder(password); // 密码加密
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("id",id);
        searchMap.put("username",username);
        searchMap.put("password",encodedPassword);
        int result = registerService.updateUserInfo(searchMap);
        if (result>0){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "修改成功");
            return new AssembleResponseMsg().success(JSONObject.toJSON(responseMap));
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","用户名已存在");
        }
    }

    // 获取搜索历史（已测试）
    @RequestMapping(value = "/user/searchHistory",produces = "application/json;charset=utf-8")
    public ResponseBody searchHistory(@RequestParam(value = "token") String token,
                                      @RequestParam(value="limit") int limit){
        Map<String,Object> executeMap=new HashMap<>();

        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        executeMap.put("user_id",id);
        executeMap.put("limit",limit);
        Map<String, Object> resultMap = searchService.getSearchHistory(executeMap);
        return new AssembleResponseMsg().success(resultMap);
    }


    // 签发证书
    public Map<String, String> sign(int userId){
        return AuthProvider.sign(userId, keysConfig.getAuthKey());
    }

    // 加密器
    public String sha256Encoder(String password)
    {
        MessageDigest digest;
        String secretPassword;
        StringBuilder encodeResult = new StringBuilder();
        try {
            digest = MessageDigest.getInstance("SHA-256");
            secretPassword = keysConfig.getPasswordFrontKey() + password + keysConfig.getPasswordBackKey();
            byte[] bytes = digest.digest(secretPassword.getBytes());
            for (byte aByte : bytes) {
                encodeResult.append(String.format("%02x", aByte));
            }
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return encodeResult.toString();
    }

    //检查用户信息
    private int checkUser(Map<String,Object> map){
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        Pattern patternName = Pattern.compile(regexConfig.getNameRegex());
        Pattern patternPassword = Pattern.compile(regexConfig.getPasswordRegex());
        Matcher matcherName = patternName.matcher(username);
        Matcher matcherPassword = patternPassword.matcher(password);

        if(!matcherName.matches())
        {
            return 1;
        }
        if(!matcherPassword.matches())
        {
            return 2;
        }
        return 0;
    }

}
