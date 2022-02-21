package net.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class Article {
    private int id;
    private int publisher_id;
    private String category;
    private boolean is_a_draft;
    private String tag;
    private String title;
    private String content;
    private String tabloid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publish_time;
    private int page_view;
    private boolean type;
    private int price;
    private int theme;

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIs_a_draft() {
        return is_a_draft;
    }

    public boolean getType() {
        return type;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public int getPage_view() {
        return page_view;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getTabloid() {
        return tabloid;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIs_a_draft(boolean is_a_draft) {
        this.is_a_draft = is_a_draft;
    }

    public void setPage_view(int page_view) {
        this.page_view = page_view;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setTabloid(String tabloid) {
        this.tabloid = tabloid;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

