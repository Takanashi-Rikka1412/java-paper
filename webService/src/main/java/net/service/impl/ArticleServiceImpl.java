package net.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.common.DateUtil;
import net.dao.IArticleDao;
import net.dao.IUserDao;
import net.pojo.*;
import net.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private IArticleDao articleMapper;

    @Autowired
    private IUserDao userMapper;

    @Override
    public Article getArticleByID(String id) {
        int newId = Integer.parseInt(id);
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",newId);
        Article article = articleMapper.getArticle(map);
        return article;
    }

    @Override
    public Map<String,Object> getArticleByUser(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页

        int userId = Integer.parseInt(map.get("id").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",userId);

        // 分页
        PageHelper.startPage(page,limit);

        List<Article> articleList = articleMapper.getUserArticle(queryMap);
        List<ArticleResult> resultList = new ArrayList<>();

        for(Article a : articleList)
        {
            ArticleResult articleResult = new ArticleResult();
            articleResult.setId(a.getId());
            articleResult.setTitle(a.getTitle());
            articleResult.setCategory(a.getCategory());
            articleResult.setViews(a.getPage_view());
            articleResult.setType(a.getType());
            articleResult.setTabloid(a.getTabloid());
            Map<String,Object> m = new HashMap<>();
            m.put("id",a.getPublisher_id());
            User user = userMapper.queryUserById(m);
            articleResult.setAuthor(user.getUsername());
            articleResult.setTags(a.getTag().split(","));
            articleResult.setGmtCreate(DateUtil.getDateStr(map.get("timePattern").toString(),a.getPublish_time()));
            resultList.add(articleResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(articleList);
        resultMap.put("records",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;

    }

    @Override
    public Map<String,Object> getDraftByUser(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页

        int userId = Integer.parseInt(map.get("id").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",userId);

        // 分页
        PageHelper.startPage(page,limit);

        List<Article> articleList = articleMapper.getUserDraft(queryMap);
        List<ArticleResult> resultList = new ArrayList<>();

        for(Article a : articleList)
        {
            ArticleResult articleResult = new ArticleResult();
            articleResult.setId(a.getId());
            articleResult.setTitle(a.getTitle());
            articleResult.setCategory(a.getCategory());
            articleResult.setViews(a.getPage_view());
            articleResult.setType(a.getType());
            articleResult.setTabloid(a.getTabloid());
            Map<String,Object> m = new HashMap<>();
            m.put("id",a.getPublisher_id());
            User user = userMapper.queryUserById(m);
            articleResult.setAuthor(user.getUsername());
            if(a.getTag()== null || a.getTag().length() == 0){
                articleResult.setTags(new String[]{});
            }
            else{
                articleResult.setTags(a.getTag().split(","));
            }
            articleResult.setGmtCreate(DateUtil.getDateStr(map.get("timePattern").toString(),a.getPublish_time()));
            resultList.add(articleResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(articleList);
        resultMap.put("records",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;

    }

    @Override
    public Article getArticleByAnswer(String id) {
        int newId = Integer.parseInt(id);
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",newId);
        return articleMapper.getQuestionArticle(map);
    }

    @Override
    public Map<String,Object> getArticleByQuestion(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页

        // 分页
        PageHelper.startPage(page,limit);
        List<Article> articleList = articleMapper.getAnswerArticle(map);
        List<ArticleResult> resultList = new ArrayList<>();

        for(Article a : articleList)
        {
            ArticleResult articleResult = new ArticleResult();
            articleResult.setId(a.getId());
            articleResult.setTitle(a.getTitle());
            articleResult.setCategory(a.getCategory());
            articleResult.setViews(a.getPage_view());
            articleResult.setType(a.getType());
            articleResult.setTabloid(a.getTabloid());
            Map<String,Object> m = new HashMap<>();
            m.put("id",a.getPublisher_id());
            User user = userMapper.queryUserById(m);
            articleResult.setAuthor(user.getUsername());
            articleResult.setTags(a.getTag().split(","));
            articleResult.setGmtCreate(DateUtil.getDateStr(map.get("timePattern").toString(),a.getPublish_time()));
            resultList.add(articleResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(articleList);
        resultMap.put("records",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    @Override
    public int insertArticle(Map<String, Object> map) {

        map.put("is_a_draft",false);
        if(!map.containsKey("tabloid") || map.get("tabloid").toString().equals("")){
            String content = map.get("content").toString();
            map.put("tabloid",content.substring(0,Math.min(content.length(),125)));
        }
        String id = map.get("articleId").toString();
        if(Integer.parseInt(id)>0){//update
            map.put("id",id);
            return articleMapper.updateArticle(map);
        }
        else{//insert
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            map.put("publish_time", DateUtil.getCurrentDateStr(map.get("timePattern").toString()));
            return articleMapper.insertArticle(map);
        }
    }

    @Override
    public int insertDraft(Map<String, Object> map) {
        map.put("is_a_draft",true);
        String id = map.get("articleId").toString();
        if(Integer.parseInt(id)>0){//update
            map.put("id",id);
            return articleMapper.updateDraft(map);
        }
        else{//insert
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            map.put("publish_time",DateUtil.getCurrentDateStr(map.get("timePattern").toString()));
            return articleMapper.insertDraft(map);
        }
    }

    @Override
    public int deleteArticle(Map<String, Object> map) {
        return articleMapper.deleteArticle(map);
    }

    @Override
    public Map<String,Object> searchArticleList(Map<String, Object> map) {
        boolean type = Boolean.parseBoolean(map.get("type").toString()); // 是文章还是草稿，前者为true
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页
        String key = map.get("key").toString();
        int userId = Integer.parseInt(map.get("userId").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("userId",userId);
        queryMap.put("key",key);

        List<Article> articleList;
        // 分页
        PageHelper.startPage(page,limit);
        if(type == true)  // 文章
        {
            articleList = articleMapper.searchUserArticle(queryMap);
        }
        else  // 草稿
        {
            articleList = articleMapper.searchUserDraft(queryMap);
        }

        List<ArticleResult> resultList = new ArrayList<>();
        for(Article a : articleList)
        {
            ArticleResult articleResult = new ArticleResult();
            articleResult.setId(a.getId());
            articleResult.setTitle(a.getTitle());
            articleResult.setCategory(a.getCategory());
            articleResult.setViews(a.getPage_view());
            articleResult.setType(a.getType());
            articleResult.setTabloid(a.getTabloid());
            Map<String,Object> m = new HashMap<>();
            m.put("id",a.getPublisher_id());
            User user = userMapper.queryUserById(m);
            articleResult.setAuthor(user.getUsername());
            articleResult.setTags(a.getTag().split(","));
            articleResult.setGmtCreate(DateUtil.getDateStr(map.get("timePattern").toString(),a.getPublish_time()));
            resultList.add(articleResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(articleList);
        resultMap.put("records",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    @Override
    public int changeArticleView(String id){
        return articleMapper.updateArticleView(id);
    }

    @Override
    public int insertReadRecord(Map<String, Object> map) {
        return articleMapper.insertReadRecord(map);
    }
}
