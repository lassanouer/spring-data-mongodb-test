package org.mongo.twitter_graph.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A User.
 */
@Document(collection = "T_USER")
public class User implements Serializable {

    @Id
    private String id;

    @Field("reseau")
    private String reseau;

    @Field("id_from_twitter")
    private Long idFromTwitter;

    @Field("user")
    private String user;

    @Field("score")
    private Long score;

    private List<Post> post;

    public List<Post> getPost() {
        return post;
    }

    public void addPost(Post post) {
        this.post.add(post);
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReseau() {
        return reseau;
    }

    public void setReseau(String reseau) {
        this.reseau = reseau;
    }

    public Long getIdFromTwitter() {
        return idFromTwitter;
    }

    public void setIdFromTwitter(Long idFromTwitter) {
        this.idFromTwitter = idFromTwitter;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

        User user = (User) o;

        if ( ! Objects.equals(id, user.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", reseau='" + reseau + "'" +
                ", idFromTwitter='" + idFromTwitter + "'" +
                ", user='" + user + "'" +
                ", score='" + score + "'" +
                '}';
    }
}
