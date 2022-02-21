package net.controller.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.config.KeysConfig;
import net.config.TimeConfig;
import net.model.ResponseBody;
import net.service.IArticleService;
import net.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class TableController {

    @Autowired
    private KeysConfig keysConfig;
    @Autowired
    private TimeConfig timeConfig;

    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;

    //获得用户文章列表（已测试）
    @RequestMapping(value = "/table/articleList",produces = "application/json;charset=utf-8")
    public ResponseBody getUserArticle(@RequestParam("token") String token, @RequestParam("page") String page, @RequestParam("limit") String limit){
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("id",id);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",articleService.getArticleByUser(map));
        resultMap.put("message","获取用户文章列表成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //获取用户草稿列表（已测试）
    @RequestMapping(value = "/table/draftList",produces = "application/json;charset=utf-8")
    public ResponseBody getUserDraft(@RequestParam("token") String token, @RequestParam("page") String page, @RequestParam("limit") String limit) {
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("id",id);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",articleService.getDraftByUser(map));
        resultMap.put("message","获取用户草稿列表成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //获取用户收到的评论列表（已测试）
    @RequestMapping(value = "/table/commentList",produces = "application/json;charset=utf-8")
    public ResponseBody getUserReceiveComment(@RequestParam("token") String token, @RequestParam("page") String page, @RequestParam("limit") String limit){
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("id",id);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",commentService.getReceiveCommentByUser(map));
        resultMap.put("message","获取用户收到的评论列表成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //获取用户发布的评论列表（已测试）
    @RequestMapping(value = "/table/toCommentList",produces = "application/json;charset=utf-8")
    public ResponseBody getUserToComment(@RequestParam("token") String token, @RequestParam("page") String page, @RequestParam("limit") String limit){
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("id",id);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",commentService.getToCommentByUser(map));
        resultMap.put("message","获取用户发布的评论列表成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //获取问题的回答列表（已测试）
    @RequestMapping(value = "/table/getArticleListByQuestion",produces = "application/json;charset=utf-8")
    public ResponseBody getArticleListByQuestion(@RequestParam("id") String id,@RequestParam("page") String page, @RequestParam("limit") String limit){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("page",page);
        map.put("limit",limit);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",articleService.getArticleByQuestion(map));
        resultMap.put("message","获取问题的回答列表成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //按照条件搜索评论（已测试）
    @RequestMapping(value = "/table/searchCommentList",produces = "application/json;charset=utf-8")
    public ResponseBody searchCommentList(@RequestParam("token") String token,@RequestParam("type") String type,
                                                 @RequestParam("page") String page, @RequestParam("limit") String limit,
                                                 @RequestParam("key") String key){
        Map<String,Object> map = new HashMap<>();
        int userId;
        try{
            userId = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        map.put("userId",userId);
        map.put("type",type);
        map.put("page",page);
        map.put("limit",limit);
        map.put("key",key);
        map.put("timePattern",timeConfig.getPattern());


        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",commentService.searchCommentList(map));
        resultMap.put("message","按照条件搜索评论成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));

    }

    //按照条件在用户文章或草稿列表中搜索文章（已测试）
    @RequestMapping(value = "/table/searchArticleList",produces = "application/json;charset=utf-8")
    public ResponseBody searchArticleList(@RequestParam("token") String token,@RequestParam("type") String type,
                                          @RequestParam("page") String page, @RequestParam("limit") String limit,
                                          @RequestParam("key") String key){
        Map<String,Object> map = new HashMap<>();
        int userId;
        try{
            userId = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        map.put("userId",userId);
        map.put("type",type);
        map.put("page",page);
        map.put("limit",limit);
        map.put("key",key);
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",articleService.searchArticleList(map));
        resultMap.put("message","按照条件搜索文章成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }


}
