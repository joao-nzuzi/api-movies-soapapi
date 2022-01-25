package com.nzuzi.moviesoapapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity @Table(name = "movies")
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue
    private long movieId;
    private String title;
    private String category;

    public MovieEntity(){}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
