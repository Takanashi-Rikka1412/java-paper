package net.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.common.DateUtil;
import net.dao.IArticleDao;
import net.dao.ICommentDao;
import net.dao.IUserDao;
import net.pojo.*;
import net.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Takanashi
 * @since 2021/12/16
 */

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentDao commentMapper;
    @Autowired
    private IArticleDao articleMapper;
    @Autowired
    private IUserDao userMapper;


    @Override
    public Comment getCommentByID(String id) {
        int newId = Integer.parseInt(id);
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",newId);
        return commentMapper.getComment(map);
    }

    @Override
    public Map<String,Object> getCommentByArticleID(Map<String,Object> map)
    {
        int articleId = Integer.parseInt(map.get("id").toString());
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",articleId);
        map1.put("level",1);

        List<Comment> commentList = commentMapper.getCommentByArticleId(map1);
        List<FirstComment> firstCommentList = new ArrayList<>();
        // 先遍历一级评论
        for(Comment c:commentList)
        {
            List<SecondComment> secondCommentList = new ArrayList<>();
            FirstComment firstComment = new FirstComment();
            int firstId = c.getId();
            Map<String,Object> map2 = new HashMap<>();
            map2.put("id",firstId);
            map2.put("level",2);

            List<Comment> commentList2 = commentMapper.getCommentByArticleId(map2);
            // 再遍历每个评论的二级评论
            for(Comment c2:commentList2)
            {
                SecondComment secondComment = new SecondComment();
                secondComment.setId(c2.getId());
                secondComment.setContent(c2.getContent());
                secondComment.setPublish_time(DateUtil.getDateStr(map.get("timePattern").toString(),c2.getPublish_time()));
                int userId = c2.getPublisher_id();
                Map<String,Object> m = new HashMap<>();
                m.put("id",userId);
                User user = userMapper.queryUserById(m);
                secondComment.setUserId(userId);
                secondComment.setUsername(user.getUsername());
                secondComment.setUserAvatar(user.getAvatar());
                secondCommentList.add(secondComment);
            }
            firstComment.setId(c.getId());
            firstComment.setSecondComment(secondCommentList);
            firstComment.setContent(c.getContent());
            firstComment.setPublish_time(DateUtil.getDateStr(map.get("timePattern").toString(),c.getPublish_time()));
            int userId = c.getPublisher_id();
            Map<String,Object> m = new HashMap<>();
            m.put("id",userId);
            User user = userMapper.queryUserById(m);
            firstComment.setUserId(userId);
            firstComment.setUsername(user.getUsername());
            firstComment.setUserAvatar(user.getAvatar());
            firstComment.setSize(secondCommentList.size());
            firstCommentList.add(firstComment);
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("firstComment",firstCommentList);
        resultMap.put("size",firstCommentList.size());

        return resultMap;
    }

    @Override
    public Map<String,Object> getReceiveCommentByUser(Map<String, Object> map)
    {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页

        int userId = Integer.parseInt(map.get("id").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",userId);

        // 分页
        PageHelper.startPage(page,limit);

        List<Comment> commentList = commentMapper.getUserReceiveComment(queryMap);
        List<CommentResult> resultList = new ArrayList<>();

        for(Comment c : commentList)
        {
            CommentResult commentResult = new CommentResult();
            commentResult.setId(c.getId());
            commentResult.setLevel(c.getLevel());
            commentResult.setArticle_id(c.getArticle_id());
            commentResult.setPublisher_id(c.getPublisher_id());
            commentResult.setSubscriber_id(c.getSubscriber_id());

            Map<String,Object> m = new HashMap<>();

            if(c.getLevel() == 1)
            {
                m.put("id",c.getArticle_id());
                Article article = articleMapper.getArticle(m);
                commentResult.setArticle_title(article.getTitle());
            }
            else
            {
                m.put("id",c.getArticle_id());
                Comment comment = commentMapper.getComment(m);
                commentResult.setArticle_title(comment.getContent());
            }

            m.put("id",c.getPublisher_id());
            User publisher = userMapper.queryUserById(m);
            commentResult.setPublisher_name(publisher.getUsername());

            m.put("id",c.getSubscriber_id());
            User subscriber = userMapper.queryUserById(m);
            commentResult.setSubscriber_name(subscriber.getUsername());

            commentResult.setContent(c.getContent());
            commentResult.setPublish_time(DateUtil.getDateStr(map.get("timePattern").toString(),c.getPublish_time()));
            commentResult.setIs_checked(c.getIs_checked());

            resultList.add(commentResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(commentList);
        resultMap.put("comments",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    @Override
    public Map<String,Object> getToCommentByUser(Map<String, Object> map)
    {
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页

        int userId = Integer.parseInt(map.get("id").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",userId);

        // 分页
        PageHelper.startPage(page,limit);

        List<Comment> commentList = commentMapper.getUserToComment(queryMap);
        List<CommentResult> resultList = new ArrayList<>();

        for(Comment c : commentList)
        {
            CommentResult commentResult = new CommentResult();
            commentResult.setId(c.getId());
            commentResult.setLevel(c.getLevel());
            commentResult.setArticle_id(c.getArticle_id());
            commentResult.setPublisher_id(c.getPublisher_id());
            commentResult.setSubscriber_id(c.getSubscriber_id());

            Map<String,Object> m = new HashMap<>();

            if(c.getLevel() == 1)
            {
                m.put("id",c.getArticle_id());
                Article article = articleMapper.getArticle(m);
                commentResult.setArticle_title(article.getTitle());
            }
            else
            {
                m.put("id",c.getArticle_id());
                Comment comment = commentMapper.getComment(m);
                commentResult.setArticle_title(comment.getContent());
            }

            m.put("id",c.getPublisher_id());
            User publisher = userMapper.queryUserById(m);
            commentResult.setPublisher_name(publisher.getUsername());

            m.put("id",c.getSubscriber_id());
            User subscriber = userMapper.queryUserById(m);
            commentResult.setSubscriber_name(subscriber.getUsername());

            commentResult.setContent(c.getContent());
            commentResult.setPublish_time(DateUtil.getDateStr(map.get("timePattern").toString(),c.getPublish_time()));
            commentResult.setIs_checked(c.getIs_checked());

            resultList.add(commentResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(commentList);
        resultMap.put("comments",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    @Override
    public int insertComment(Map<String, Object> map) {
        return commentMapper.insertComment(map);
    }

    @Override
    public int setCommentRead(Map<String, Object> map)
    {
        if(map.get("all").toString().equals("true"))
        {
            Map<String,Object> m = new HashMap<>();
            m.put("userId",map.get("userId"));
            return commentMapper.updateAllCheck(m);
        }
        else
        {
            Map<String,Object> m = new HashMap<>();
            List<Integer> idList  = JSON.parseObject(map.get("commentId").toString(), new TypeReference<List<Integer>>(){});
            for(int id:idList)
            {
                m.put("id",id);
                if(commentMapper.updateOneCheck(m)<=0)
                    return 0;
            }
            return 1;
        }

    }

    @Override
    public Map<String,Object> searchCommentList(Map<String, Object> map)
    {
        boolean type = Boolean.parseBoolean(map.get("type").toString()); // 是用户发表的评论还是用户被评论的评论，前者为true
        int limit = Integer.parseInt(map.get("limit").toString()); //每页几条
        int page = Integer.parseInt(map.get("page").toString());  // 第几页
        String key = map.get("key").toString();
        int userId = Integer.parseInt(map.get("userId").toString());
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("userId",userId);
        queryMap.put("key",key);

        List<Comment> commentList;
        // 分页
        PageHelper.startPage(page,limit);

        if(type == true)  // 用户发表的评论
        {
            commentList = commentMapper.searchToComment(queryMap);
        }
        else  // 用户收到的评论
        {
            commentList = commentMapper.searchReceiveComment(queryMap);
        }

        List<CommentResult> resultList = new ArrayList<>();
        for(Comment c : commentList)
        {
            CommentResult commentResult = new CommentResult();
            commentResult.setId(c.getId());
            commentResult.setLevel(c.getLevel());
            commentResult.setArticle_id(c.getArticle_id());
            commentResult.setPublisher_id(c.getPublisher_id());
            commentResult.setSubscriber_id(c.getSubscriber_id());

            Map<String,Object> m = new HashMap<>();

            if(c.getLevel() == 1)
            {
                m.put("id",c.getArticle_id());
                Article article = articleMapper.getArticle(m);
                commentResult.setArticle_title(article.getTitle());
            }
            else
            {
                m.put("id",c.getArticle_id());
                Comment comment = commentMapper.getComment(m);
                commentResult.setArticle_title(comment.getContent());
            }

            m.put("id",c.getPublisher_id());
            User publisher = userMapper.queryUserById(m);
            commentResult.setPublisher_name(publisher.getUsername());

            m.put("id",c.getSubscriber_id());
            User subscriber = userMapper.queryUserById(m);
            commentResult.setSubscriber_name(subscriber.getUsername());

            commentResult.setContent(c.getContent());
            commentResult.setPublish_time(DateUtil.getDateStr(map.get("timePattern").toString(),c.getPublish_time()));
            commentResult.setIs_checked(c.getIs_checked());

            resultList.add(commentResult);
        }

        Map<String,Object> resultMap=new HashMap<>();
        PageInfo pageInfo = new PageInfo(commentList);
        resultMap.put("comments",resultList);
        resultMap.put("current",pageInfo.getPageNum());
        resultMap.put("size",pageInfo.getPageSize());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    @Override
    public int deleteComment(Map<String, Object> map) {
        return commentMapper.deleteComment(map);
    }

}

