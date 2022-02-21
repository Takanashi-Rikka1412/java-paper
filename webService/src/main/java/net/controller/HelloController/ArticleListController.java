package net.controller.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.config.HomepageConfig;
import net.config.HotRankConfig;
import net.config.KeysConfig;
import net.config.TimeConfig;
import net.model.ResponseBody;
import net.pojo.Article;
import net.pojo.SearchForm;
import net.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ArticleListController {

    @Autowired
    private KeysConfig keysConfig;
    @Autowired
    private TimeConfig timeConfig;
    @Autowired
    private HotRankConfig hotRankConfig;
    @Autowired
    private HomepageConfig homepageConfig;

    @Autowired
    private ISearchService searchService;

    //获得首页文章列表（已测试）
    @RequestMapping(value = "/articleList/home",produces = "application/json;charset=utf-8")
    public ResponseBody getHome(@RequestParam("token") String token,@RequestParam("page") String page,@RequestParam("limit") String limit) {
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("viewDivRatio",hotRankConfig.getViewDivRatio());
        map.put("commentRatio",hotRankConfig.getCommentRatio());
        map.put("timeGravity",hotRankConfig.getTimeGravity());
        map.put("timePattern",timeConfig.getPattern());
        map.put("recommendCount",homepageConfig.getRecommendCount());
        map.put("recommendTagViewLimit",homepageConfig.getRecommendTagViewLimit());
        map.put("recommendHaveReadRatio",homepageConfig.getRecommendHaveReadRatio());

        map.put("user_id",-1);
        if(!token.equals("no login")) {
            try {
                map.put("user_id", AuthProvider.validate(token, keysConfig.getAuthKey()));
            } catch (JWTVerificationException e) {
                return new AssembleResponseMsg().failure(401, "Unauthorized", "证书验证错误");
            }
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",searchService.getHome(map));
        resultMap.put("message","获取首页推荐成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));
    }

    //获得热榜文章列表（已测试）
    @RequestMapping(value = "/articleList/hot",produces = "application/json;charset=utf-8")
    public ResponseBody getHot(@RequestParam("page") String page,@RequestParam("limit") String limit,@RequestParam("category") String category){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("category",category);
        map.put("viewDivRatio",hotRankConfig.getViewDivRatio());
        map.put("commentRatio",hotRankConfig.getCommentRatio());
        map.put("timeGravity",hotRankConfig.getTimeGravity());
        map.put("timePattern",timeConfig.getPattern());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",searchService.getHot(map));
        resultMap.put("message","获取热榜成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(resultMap));

    }


    //获得搜索文章列表（已测试）
    @RequestMapping(value = "/articleList/search",produces = "application/json;charset=utf-8")
    public ResponseBody searchArticle(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token = map.get("token").toString();
        SearchForm searchForm = new ObjectMapper().convertValue(map.get("form"),SearchForm.class);

        if(!token.equals("no login"))
        {
            // 若登陆了则需更新用户的搜索历史
            int id;
            try{
                id = AuthProvider.validate(token,keysConfig.getAuthKey());
            }catch (JWTVerificationException e)
            {
                return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
            }

            Map<String,Object> searchMap = new HashMap<>();
            searchMap.put("user_id",id);
            searchMap.put("search_key",searchForm.getKey());
            searchMap.put("timePattern",timeConfig.getPattern());
            int updateHistoryResult = searchService.insertSearchHistory(searchMap);
            if(updateHistoryResult <= 0)
                return new AssembleResponseMsg().failure(200,"error","更新历史搜索失败");
        }

        Map<String,Object> searchResultMap = new HashMap<>();
        map.put("timePattern",timeConfig.getPattern());
        searchResultMap.put("pageInfo",searchService.getSearchResult(map));
        searchResultMap.put("message","查询成功");
        return new AssembleResponseMsg().success(JSONObject.toJSON(searchResultMap));


    }









    //还可以再加上一个小后台：向用户发送系统消息之类的
}
