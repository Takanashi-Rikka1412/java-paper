package net.dao;

import net.pojo.Article;

import java.util.List;
import java.util.Map;

public interface ISearchDao {
    List<Article> queryArticle(Map<String, Object> map);
    List<Article> queryHot(Map<String, Object> map);
    List<String> querySearchHistory(Map<String, Object> map);
    int updateSearchHistory(Map<String, Object> map);
    int insertSearchHistory(Map<String, Object> map);
}
