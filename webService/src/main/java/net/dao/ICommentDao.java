package net.dao;

import net.pojo.Comment;

import java.util.List;
import java.util.Map;

/**
 * @author Takanashi
 * @since 2021/12/16
 */
public interface ICommentDao {
    Comment getComment(Map<String, Object> map);
    List<Comment> getCommentByArticleId(Map<String, Object> map);
    List<Comment> getUserReceiveComment(Map<String, Object> map);
    List<Comment> getUserToComment(Map<String, Object> map);
    int insertComment(Map<String, Object> map);
    int updateAllCheck(Map<String, Object> map);
    int updateOneCheck(Map<String, Object> map);
    List<Comment> searchToComment(Map<String, Object> map);
    List<Comment> searchReceiveComment(Map<String, Object> map);
    int deleteComment(Map<String, Object> map);

}
