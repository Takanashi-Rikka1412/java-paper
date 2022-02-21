package net.service;

import net.pojo.Comment;

import java.util.Map;

/**
 * @author Takanashi
 * @since 2021/12/16
 */
public interface ICommentService {
    Comment getCommentByID(String id);
    Map<String,Object> getCommentByArticleID(Map<String,Object> map);
    Map<String,Object> getReceiveCommentByUser(Map<String, Object> map);
    Map<String,Object> getToCommentByUser(Map<String, Object> map);
    int insertComment(Map<String, Object> map);
    int setCommentRead(Map<String, Object> map);
    Map<String,Object> searchCommentList(Map<String, Object> map);
    int deleteComment(Map<String, Object> map);
}
