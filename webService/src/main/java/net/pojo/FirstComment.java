package net.pojo;

import java.util.List;

/**
 * @author Takanashi
 * @since 2021/12/16
 */
public class FirstComment {
    private int id;
    private List<SecondComment> secondComment;   // 二级评论列表
    private String content;      // 当前一级评论的内容
    private String publish_time; // 当前一级评论发布时间
    private int userId;          // 当前一级评论的发布者id
    private String username;        // 当前一级评论的发布者名字
    private String userAvatar;   // 当前一级评论的发布者头像
    private int size;            // 当前一级评论的二级评论数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SecondComment> getSecondComment() {
        return secondComment;
    }

    public void setSecondComment(List<SecondComment> secondComment) {
        this.secondComment = secondComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
