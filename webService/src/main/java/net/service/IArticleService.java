package net.service;

import net.pojo.Article;
import net.pojo.ArticleDetail;

import java.util.List;
import java.util.Map;

public interface IArticleService {
    Article getArticleByID(String id);
    Map<String,Object> getArticleByUser(Map<String, Object> map);
    Map<String,Object> getDraftByUser(Map<String, Object> map);
    int insertArticle(Map<String, Object> map);
    int insertDraft(Map<String, Object> map);
    Article getArticleByAnswer(String id);
    Map<String,Object> getArticleByQuestion(Map<String, Object> map);
    int deleteArticle(Map<String, Object> map);
    Map<String,Object> searchArticleList(Map<String, Object> map);
    int changeArticleView(String id);
    int insertReadRecord(Map<String, Object> map);
}
