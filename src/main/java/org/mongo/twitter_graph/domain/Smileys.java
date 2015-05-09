package org.mongo.twitter_graph.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Smileys.
 */
@Document(collection = "T_SMILEYS")
public class Smileys implements Serializable {

    @Id
    private String id;

    @Field("langue")
    private String langue;

    @Field("symbole")
    private String symbole;

    @Field("word_equi")
    private String wordEqui;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public String getWordEqui() {
        return wordEqui;
    }

    public void setWordEqui(String wordEqui) {
        this.wordEqui = wordEqui;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Smileys smileys = (Smileys) o;

        if ( ! Objects.equals(id, smileys.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Smileys{" +
                "id=" + id +
                ", langue='" + langue + "'" +
                ", symbole='" + symbole + "'" +
                ", wordEqui='" + wordEqui + "'" +
                '}';
    }
}
