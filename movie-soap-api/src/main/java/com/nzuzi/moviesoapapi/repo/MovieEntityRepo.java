package com.nzuzi.moviesoapapi.repo;

import com.nzuzi.moviesoapapi.entity.MovieEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieEntityRepo extends CrudRepository<MovieEntity, Long> {
    public MovieEntity findByTitle(String title);
}
