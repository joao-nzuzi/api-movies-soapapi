package com.nzuzi.moviesoapapi.endpoint;

import com.nzuzi.moviesoapapi.entity.MovieEntity;
import com.nzuzi.moviesoapapi.service.MovieEntityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ws.soap.AddMovieRequest;
import ws.soap.AddMovieResponse;
import ws.soap.DeleteMovieRequest;
import ws.soap.DeleteMovieResponse;
import ws.soap.GetAllMovieRequest;
import ws.soap.GetAllMovieResponse;
import ws.soap.GetMovieByIdRequest;
import ws.soap.GetMovieByIdResponse;
import ws.soap.MovieType;
import ws.soap.ServiceStatus;
import ws.soap.UpdateMovieRequest;
import ws.soap.UpdateMovieResponse;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class MovieEndpoint {

    public static final String NAMESPACE_URI = "http://soap.ws";
    private MovieEntityService service;

    public MovieEndpoint(){}

    @Autowired
    public MovieEndpoint(MovieEntityService service){
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMovieByIdRequest")
    @ResponsePayload
    public GetMovieByIdResponse getMovieById(@RequestPayload GetMovieByIdRequest request){
        GetMovieByIdResponse response = new GetMovieByIdResponse();
        MovieEntity movieEntity = service.getEntityById(request.getMovieId());
        MovieType movieType = new MovieType();
        BeanUtils.copyProperties(movieEntity, movieType);
        response.setMovieType(movieType);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllMovieRequest")
    @ResponsePayload
    public GetAllMovieResponse getAllMovies(@RequestPayload GetAllMovieRequest request){
        GetAllMovieResponse response = new GetAllMovieResponse();
        List<MovieType> movieTypeList = new ArrayList<>();
        List<MovieEntity> movieEntityList = service.getAllEntities();
        for (MovieEntity entity : movieEntityList){
            MovieType movieType = new MovieType();
            BeanUtils.copyProperties(entity, movieType);
            movieTypeList.add(movieType);
        }
        response.getMovieType().addAll(movieTypeList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addMovieRequest")
    @ResponsePayload
    public AddMovieResponse addMovie(@RequestPayload AddMovieRequest request){
        AddMovieResponse response = new AddMovieResponse();
        MovieType newMovieType = new MovieType();
        ServiceStatus serviceStatus = new ServiceStatus();
        MovieEntity newMovieEntity = new MovieEntity();
        newMovieEntity.setTitle(request.getTitle());
        newMovieEntity.setCategory(request.getCategory());
        MovieEntity savedMovieEntity = service.addEntity(newMovieEntity);

        if(savedMovieEntity == null){
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Exception while adding entity");
        }else{
            BeanUtils.copyProperties(savedMovieEntity, newMovieEntity);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content added successfully");
        }

        response.setMovieType(newMovieType);
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateMovieRequest")
    @ResponsePayload
    public ws.soap.UpdateMovieResponse updateMovie(@RequestPayload UpdateMovieRequest request){
        UpdateMovieResponse response = new UpdateMovieResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        MovieEntity movieEntity = service.getEntityByTitle(request.getTitle());
        if (movieEntity == null){
            serviceStatus.setStatusCode("NOT FOUND");
            serviceStatus.setMessage("Movie "+request.getTitle()+ " not found");
        }else{
            movieEntity.setTitle(request.getTitle());
            movieEntity.setCategory(request.getCategory());
            boolean flag = service.updateEntity(movieEntity);

            if(flag == false){
                serviceStatus.setStatusCode("CONFLICT");
                serviceStatus.setMessage("Exception while updating entity "+request.getTitle());
            }else{
                serviceStatus.setStatusCode("SUCCESS");
                serviceStatus.setMessage("Content updated successfully");
            }
        }

        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteMovieRequest")
    @ResponsePayload
    public ws.soap.DeleteMovieResponse deleteMovie(@RequestPayload DeleteMovieRequest request){
        DeleteMovieResponse response = new DeleteMovieResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        boolean flag = service.deleteEntityById(request.getMovieId());
        if (flag = false){
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Exception while deletint Entity id=" + request.getMovieId());
        }else{
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setStatusCode("Content deleted successfully");
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }
}
