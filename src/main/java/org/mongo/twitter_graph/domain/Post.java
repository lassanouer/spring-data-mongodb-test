package org.mongo.twitter_graph.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.mongo.twitter_graph.domain.util.CustomDateTimeDeserializer;
import org.mongo.twitter_graph.domain.util.CustomDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A Post.
 */
@Document(collection = "T_POST")
public class Post implements Serializable {

    @Id
    private String id;

    @Field("id_long_post")
    private Long idLongPost;

    @Field("reseau")
    private String reseau;

    @Field("text")
    private String text;

    @Field("language")
    private String language;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field("date")
    private Date date;

    @Field("hashtag")
    private List<String> hashtag;

    @Field("score")
    private Long score;

    @Field("topic")
    private String topic;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdLongPost() {
        return idLongPost;
    }

    public void setIdLongPost(Long idLongPost) {
        this.idLongPost = idLongPost;
    }

    public String getReseau() {
        return reseau;
    }

    public void setReseau(String reseau) {
        this.reseau = reseau;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void addHashtag(String hashtag) {
        this.hashtag.add(hashtag);
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post post = (Post) o;

        if ( ! Objects.equals(id, post.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", idLongPost='" + idLongPost + "'" +
                ", reseau='" + reseau + "'" +
                ", text='" + text + "'" +
                ", language='" + language + "'" +
                ", date='" + date + "'" +
                ", hashtag='" + hashtag + "'" +
                ", score='" + score + "'" +
                '}';
    }
}
