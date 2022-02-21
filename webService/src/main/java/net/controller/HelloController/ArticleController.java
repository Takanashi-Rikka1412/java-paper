package net.controller.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.common.DateUtil;
import net.config.KeysConfig;
import net.config.TimeConfig;
import net.dao.IArticleDao;
import net.model.ResponseBody;
import net.pojo.Article;
import net.pojo.ArticleDetail;
import net.pojo.User;
import net.service.IArticleService;
import net.service.ICategoryService;
import net.service.IQueryService;
import net.sf.jsqlparser.statement.IfElseStatement;
import net.util.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ArticleController {

    @Autowired
    private TimeConfig timeConfig;
    @Autowired
    private KeysConfig keysConfig;

    @Autowired
    ICategoryService categoryService;
    @Autowired
    IArticleService articleService;
    @Autowired
    IQueryService queryService;


    // 获取文章所有类别（已测试）
    @RequestMapping(value = "/article/categories",produces = "application/json;charset=utf-8")
    public ResponseBody getArticleCategories(){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<Category> cl = categoryService.getAllCategory();
        resultMap.put("categories",cl);
        return new AssembleResponseMsg().success(resultMap);
    }

    //根据文章id获取文章详情（已测试）
    @RequestMapping(value = "/article/getArticleById",produces = "application/json;charset=utf-8")
    public ResponseBody getArticleById(@RequestParam String id){
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        Article article = articleService.getArticleByID(id);
        if(article == null) return new AssembleResponseMsg().failure(200,"error","article not find");
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setId(article.getId());
        Map<String,Object> m = new HashMap<>();
        m.put("id",article.getPublisher_id());
        User user = queryService.queryUserById(m);
        articleService.changeArticleView(id);
        Map<String,Object> readRecord = new HashMap<>();
        articleDetail.setPublisher_name(user.getUsername());
        articleDetail.setCategory(article.getCategory());
        articleDetail.setIs_a_draft(article.getIs_a_draft());
        if(!article.getIs_a_draft()){
            articleDetail.setTag(article.getTag().split(","));
        }
        else{
            articleDetail.setTag(new String[]{});
        }
        articleDetail.setTitle(article.getTitle());
        articleDetail.setContent(article.getContent());
        articleDetail.setTabloid(article.getTabloid());
        articleDetail.setPublish_time(DateUtil.getDateStr(timeConfig.getPattern(),article.getPublish_time()));
        articleDetail.setPage_view(article.getPage_view());
        articleDetail.setType(article.getType());
        articleDetail.setPrice(article.getPrice());
        articleDetail.setTheme(article.getTheme());
        resultMap.put("article",articleDetail);
        return new AssembleResponseMsg().success(resultMap);
    }

    // 设置文章被读（已测试）
    @RequestMapping(value = "/article/readRecord",produces = "application/json;charset=utf-8")
    public ResponseBody readArticle(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        String article_id = map.get("article_id").toString();
        HashMap<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id",id);
        insertMap.put("article_id",article_id);
        articleService.changeArticleView(article_id);
        articleService.insertReadRecord(insertMap);
        return new AssembleResponseMsg().success("success");
    }
    //根据回答获取问题详情（已测试）
    @RequestMapping(value = "/article/getArticleByAnswer",produces = "application/json;charset=utf-8")
    public ResponseBody getArticleByAnswer(@RequestParam String id){
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        Article article = articleService.getArticleByAnswer(id);
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setId(article.getId());
        Map<String,Object> m = new HashMap<>();
        m.put("id",article.getPublisher_id());
        User user = queryService.queryUserById(m);
        articleDetail.setPublisher_name(user.getUsername());
        articleDetail.setCategory(article.getCategory());
        articleDetail.setIs_a_draft(article.getIs_a_draft());
        articleDetail.setTag(article.getTag().split(","));
        articleDetail.setTitle(article.getTitle());
        articleDetail.setContent(article.getContent());
        articleDetail.setTabloid(article.getTabloid());
        articleDetail.setPublish_time(DateUtil.getDateStr(timeConfig.getPattern(),article.getPublish_time()));
        articleDetail.setPage_view(article.getPage_view());
        articleDetail.setType(article.getType());
        articleDetail.setPrice(article.getPrice());
        articleDetail.setTheme(article.getTheme());
        resultMap.put("article",articleDetail);
        return new AssembleResponseMsg().success(resultMap);
    }

    //删除文章或者草稿（已测试）
    @RequestMapping(value = "/article/deleteArticle",produces = "application/json;charset=utf-8")
    public ResponseBody deleteArticle(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int publisher_id = 1;
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
        int result = articleService.deleteArticle(executeMap);
        if(result>0){
            return new AssembleResponseMsg().success("success");
        }
        else{
            return  new AssembleResponseMsg().failure(200,"error","文章已删除或文章不存在");
        }
    }

    //上传文章（已测试）
    @RequestMapping(value = "/article/postArticle",produces = "application/json;charset=utf-8")
    public ResponseBody postArticle(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int userID;
        try{
            userID = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }

        String article = map.get("article").toString();
        Map articleMap = JSONObject.parseObject(article);
        String tags = articleMap.get("tags").toString();
        tags = tags.substring(1,tags.length()-1);
        String tag="";
        for(int i=0;i<tags.length();i++){
            if(tags.charAt(i)!='\"'){
                tag+=tags.charAt(i);
            }
        }
        articleMap.put("publisher_id",userID);
        articleMap.put("tag",tag);
        articleMap.put("timePattern",timeConfig.getPattern());
        if(!articleMap.containsKey("theme"))
            articleMap.put("theme",-1);
        int row = articleService.insertArticle(articleMap);
        if(row>0) {
            HashMap<String,Object> resultMap = new HashMap<>();
            int id = Integer.parseInt(articleMap.get("id").toString());
            if(id>0)
                resultMap.put("article_id",id);
            else
                resultMap.put("article_id",row);
            resultMap.put("message","success");
            return new AssembleResponseMsg().success(resultMap);
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","提交失败");
        }
    }

    //上传文章草稿（已测试）
    @RequestMapping(value = "/article/draftArticle",produces = "application/json;charset=utf-8")
    public ResponseBody draftArticle(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        int userID;
        try{
            userID = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        String article = map.get("article").toString();
        Map articleMap = JSONObject.parseObject(article);
        articleMap.put("publisher_id",userID);
        articleMap.put("timePattern",timeConfig.getPattern());
        if(!articleMap.containsKey("theme"))
            articleMap.put("theme",-1);
        int row = articleService.insertDraft(articleMap);
        if(row>0) {
            HashMap<String,Object> resultMap = new HashMap<>();
            int id = Integer.parseInt(articleMap.get("id").toString());
            if(id>0)
                resultMap.put("article_id",id);
            else
                resultMap.put("article_id",row);
            resultMap.put("message","success");
            return new AssembleResponseMsg().success(resultMap);
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","提交失败");
        }
    }


}
