package com.nzuzi.moviesoapapi.service.impl;

import com.nzuzi.moviesoapapi.entity.MovieEntity;
import com.nzuzi.moviesoapapi.repo.MovieEntityRepo;
import com.nzuzi.moviesoapapi.service.MovieEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service @Transactional
public class MovieEntityServiceImpl implements MovieEntityService {

    private MovieEntityRepo movieEntityRepo;

    public MovieEntityServiceImpl(){}

    @Autowired
    public MovieEntityServiceImpl (MovieEntityRepo movieEntityRepo){
        this.movieEntityRepo = movieEntityRepo;
    }

    @Override
    public MovieEntity getEntityById(long id){
        return movieEntityRepo.findById(id).get();
    }

    @Override
    public MovieEntity getEntityByTitle(String title) {
        return this.movieEntityRepo.findByTitle(title);
    }

    @Override
    public List<MovieEntity> getAllEntities() {
        List<MovieEntity> entitiesList = new ArrayList<>();
        movieEntityRepo.findAll().forEach(entity -> entitiesList.add(entity));
        return entitiesList;
    }

    @Override
    public MovieEntity addEntity(MovieEntity entity) {
        try{
            return this.movieEntityRepo.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateEntity(MovieEntity entity) {
        try{
            this.movieEntityRepo.save(entity);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEntityById(long id) {
        try{
            this.movieEntityRepo.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
