package net.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.common.DateUtil;
import net.dao.IArticleDao;
import net.dao.ISearchDao;
import net.dao.IUserDao;
import net.pojo.*;
import net.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private ISearchDao searchMapper;

    @Autowired
    private IUserDao userMapper;

    @Autowired
    private IArticleDao articleMapper;


    //获得首页文章列表
    /*
     * 推荐算法：
     * 先从数据库中获取热度前一部分的文章，
     * 获取自身看过或评论过的文章的tag，评论过的权值较高，将各种tag排序，
     * 按照tag和权值推荐文章
     */
    @Override
    public Map<String, Object> getHome(Map<String, Object> map)
    {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());   //第几页
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("viewDivRatio",map.get("viewDivRatio"));
        queryMap.put("commentRatio",map.get("commentRatio"));
        queryMap.put("timeGravity",map.get("timeGravity"));

        // 分页
        PageHelper.startPage(1,Integer.parseInt(map.get("recommendCount").toString()));
        // 取出最热文章
        List<Article> hotArticleList = searchMapper.queryHot(queryMap);

        // 取出阅读历史
        Map<String,Object> userIdMap = new HashMap<>();
        userIdMap.put("user_id",map.get("user_id"));

        // 阅读过的文章id
        List<Integer> readList = userMapper.queryReadHistory(userIdMap);
        // 取出的阅读过的文章列表
        List<Article> readArticleList = new ArrayList<>();
        // 阅读过的文章tag
        Map<String, Integer> tagMap = new HashMap<>();
        // 用于取出文章
        Map<String,Object> articleMap = new HashMap<>();
        // 分解阅读过的tag
        for(int id : readList)
        {
            articleMap.put("id",id);
            Article article = articleMapper.getArticle(articleMap);
            readArticleList.add(article);
            String[] tags = article.getTag().split(",");
            for(String tag :tags)
            {
                if(!tagMap.containsKey(tag))
                    tagMap.put(tag,1);
                else
                {
                    int i = tagMap.get(tag);
                    tagMap.put(tag,i+1);
                }
            }
            articleMap.clear();
        }


        // 过滤掉阅读较少的tag
        List<String> removeTagList = new ArrayList<>();
        for(Map.Entry<String,Integer> tags:tagMap.entrySet())
        {
            if(tags.getValue()<Integer.parseInt(map.get("recommendTagViewLimit").toString()))
                removeTagList.add(tags.getKey());
        }
        for(String tag:removeTagList)
        {
            tagMap.remove(tag);
        }


        // 文章和其权值
        Map<Article, Float> articleWeightMap = new HashMap<>();
        // 对热榜文章进行权值计算
        for(Article article:hotArticleList)
        {
            if(article.getTag() == null || article.getTag().length() == 0) continue;
            String[] tags = article.getTag().split(",");
            for(String tag:tags)
            {
                if(!articleWeightMap.containsKey(article))
                    articleWeightMap.put(article,0f);
                if(tagMap.containsKey(tag))
                {
                    int w = tagMap.get(tag);
                    articleWeightMap.put(article,articleWeightMap.get(article)+w);
                }
            }
            if(readArticleList.contains(article))
            {
                // 阅读过的文章减少推荐
                articleWeightMap.put(article, Float.parseFloat(map.get("recommendHaveReadRatio").toString())*articleWeightMap.get(article));
            }
        }


        // 按权值排序
        List<Map.Entry<Article, Float>> list = new ArrayList<>(articleWeightMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Article, Float> >() {
            public int compare(Map.Entry<Article, Float> o1, Map.Entry<Article, Float> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        List<Article> sortedList = new ArrayList<>();
        for (Map.Entry<Article, Float>  aa : list) {
            sortedList.add(aa.getKey());
        }


        List<Article> articleList = sortedList.subList((page-1)*limit,Math.min(page*limit,sortedList.size()));
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
        resultMap.put("records",resultList);
        resultMap.put("current",page);
        resultMap.put("size",limit);
        resultMap.put("total",hotArticleList.size());
        return resultMap;

    }


    //获得热榜文章列表
    @Override
    public Map<String, Object> getHot(Map<String, Object> map)
    {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());   //第几页
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("category",map.get("category"));
        queryMap.put("viewDivRatio",map.get("viewDivRatio"));
        queryMap.put("commentRatio",map.get("commentRatio"));
        queryMap.put("timeGravity",map.get("timeGravity"));

        // 分页
        PageHelper.startPage(page,limit);

        List<Article> articleList = searchMapper.queryHot(queryMap);
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


    // 获取文章搜索结果
    @Override
    public Map<String, Object> getSearchResult(Map<String, Object> map)
    {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页
        SearchForm searchForm = new ObjectMapper().convertValue(map.get("form"),SearchForm.class);
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("key",searchForm.getKey());
        searchMap.put("category",searchForm.getCategory());
        searchMap.put("date1",searchForm.getDate1());
        searchMap.put("date2",searchForm.getDate2());
        searchMap.put("tag",searchForm.getTag());


        // 分页
        PageHelper.startPage(page,limit);

        List<Article> articleList = searchMapper.queryArticle(searchMap);
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


    // 获取搜索历史
    @Override
    public Map<String, Object> getSearchHistory(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString());  //每页几条
        List<String> historyList = searchMapper.querySearchHistory(map);
        Map<String, Object> resultMap = new HashMap<>();
        List<String> ll = historyList.subList(0, Math.min(limit, historyList.size()));
        resultMap.put("historyList", ll);//.subList(0,Math.min(limit,resultMap.size()))
        return resultMap;
    }

    @Override
    public int insertSearchHistory(Map<String, Object> map){
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("search_time",DateUtil.getCurrentDateStr(map.get("timePattern").toString()));
        if(searchMapper.updateSearchHistory(map) <= 0)
            return searchMapper.insertSearchHistory(map);
        return 1;
    }
}
