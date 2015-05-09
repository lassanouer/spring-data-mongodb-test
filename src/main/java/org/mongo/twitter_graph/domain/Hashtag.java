package org.mongo.twitter_graph.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Hashtag.
 */
@Document(collection = "T_HASHTAG")
public class Hashtag implements Serializable {

    @Id
    private String id;

    @Field("hashtag")
    private String hashtag;

    @Field("count")
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Hashtag hashtag = (Hashtag) o;

        if ( ! Objects.equals(id, hashtag.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hashtag{" +
                "id=" + id +
                ", hashtag='" + hashtag + "'" +
                ", count='" + count + "'" +
                '}';
    }
}
