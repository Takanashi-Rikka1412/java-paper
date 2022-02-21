package net.dao;

import net.pojo.Article;

import java.util.List;
import java.util.Map;

public interface IArticleDao {
    Article getArticle(Map<String, Object> map);
    List<Article> getUserArticle(Map<String, Object> map);
    List<Article> getUserDraft(Map<String, Object> map);
    int insertArticle(Map<String, Object> map);
    int updateArticle(Map<String, Object> map);
    int insertDraft(Map<String, Object> map);
    int updateDraft(Map<String, Object> map);
    Article getQuestionArticle(Map<String, Object> map);
    List<Article> getAnswerArticle(Map<String, Object> map);
    int deleteArticle(Map<String, Object> map);
    List<Article> searchUserArticle(Map<String, Object> map);
    List<Article> searchUserDraft(Map<String, Object> map);
    int updateArticleView(String id);
    int insertReadRecord(Map<String,Object> map);
}
