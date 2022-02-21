package net.controller.HelloController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.common.DateUtil;
import net.config.KeysConfig;
import net.config.TimeConfig;
import net.model.ResponseBody;
import net.pojo.Article;
import net.service.IArticleService;
import net.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CommentController {

    @Autowired
    private TimeConfig timeConfig;
    @Autowired
    private KeysConfig keysConfig;

    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;



    //发表评论（已测试）
    @RequestMapping(value = "/comment/newComment",produces = "application/json;charset=utf-8")
    public ResponseBody postComment(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int userId;
        try{
            userId = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        String commentContent = map.get("content").toString();
        String level = map.get("level").toString();
        String beCommentArticleID = map.get("beCommentID").toString();

        Map<String,Object> commentMap = new HashMap<>();
        commentMap.put("level",level);
        commentMap.put("article_id",beCommentArticleID);
        commentMap.put("publisher_id",userId);
        if(Integer.parseInt(level)==1)
        {
            commentMap.put("subscriber_id",articleService.getArticleByID(beCommentArticleID).getPublisher_id());
        }
        else
        {
            commentMap.put("subscriber_id",commentService.getCommentByID(beCommentArticleID).getPublisher_id());
        }
        commentMap.put("content",commentContent);
        commentMap.put("publish_time", DateUtil.getCurrentDateStr(timeConfig.getPattern()));
        if(userId == Integer.parseInt(commentMap.get("subscriber_id").toString()))
        {
            commentMap.put("is_checked",true);
        }
        else
        {
            commentMap.put("is_checked",false);
        }


        int row = commentService.insertComment(commentMap);
        if(row>0) {
            HashMap<String,Object> resultMap = new HashMap<>();
            resultMap.put("message","评论发布成功");
            return new AssembleResponseMsg().success(resultMap);
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","评论发布失败");
        }


    }


    //批量标记评论为已读（已测试）
    @RequestMapping(value = "/comment/setRead",produces = "application/json;charset=utf-8")
    public ResponseBody setCommentRead(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int userId;
        try{
            userId = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("userId",userId);
        if(map.get("all").toString().equals("true"))
        {
            searchMap.put("all",true);
        }
        else
        {
            searchMap.put("commentId",map.get("id"));
            searchMap.put("all",false);
        }

        int flag = commentService.setCommentRead(searchMap);
        if(flag > 0) {
            HashMap<String,Object> resultMap = new HashMap<>();
            resultMap.put("message","评论设为已读成功");
            return new AssembleResponseMsg().success(resultMap);
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","评论设为已读失败");
        }

    }



    //根据文章id获取评论（已测试）
    @RequestMapping(value = "/comment/getCommentByArticle",produces = "application/json;charset=utf-8")
    public ResponseBody getCommentByArticle(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("timePattern",timeConfig.getPattern());

        return new AssembleResponseMsg().success(commentService.getCommentByArticleID(map));
    }


    //删除评论
    @RequestMapping(value = "/comment/deleteComment",produces = "application/json;charset=utf-8")
    public ResponseBody deleteComment(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int publisher_id;
        try{
            publisher_id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }


        int id = Integer.parseInt(map.get("id").toString());
        Map<String,Object> executeMap = new HashMap<>();
        executeMap.put("id",id);
        executeMap.put("publisher_id",publisher_id);
        int result = commentService.deleteComment(executeMap);
        if(result>0){
            return new AssembleResponseMsg().success("success");
        }
        else{
            return  new AssembleResponseMsg().failure(200,"error","无权删除该评论、评论已删除或评论不存在");
        }
    }


}
