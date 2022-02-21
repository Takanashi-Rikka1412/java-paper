package net.service;

import java.util.Map;

public interface ISearchService {
    Map<String, Object> getHome(Map<String, Object> map);
    Map<String, Object> getHot(Map<String, Object> map);
    Map<String, Object> getSearchResult(Map<String, Object> map);
    Map<String,Object> getSearchHistory(Map<String, Object> map);
    int insertSearchHistory(Map<String, Object> map);
}
