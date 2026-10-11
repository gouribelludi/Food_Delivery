package com.quickcrave.service;

import com.quickcrave.dto.ResturantDto;
import com.quickcrave.model.Resturant;
import com.quickcrave.model.User;
import com.quickcrave.request.CreteResturantRequest;

import java.util.List;

public interface RestautaurntService {

    public Resturant createResturant(CreteResturantRequest req, User user);

    public  Resturant updateResturant (Long resturantid, CreteResturantRequest updatedResturant) throws Exception;

    public void  deleteResturant(Long resturantId) throws Exception;

    public List<Resturant> getAllResturant();

    public List<Resturant> searchResturant(String keyword);

    public Resturant findResturantById(Long id) throws Exception;

    public Resturant getResturantByUserId(Long userId) throws Exception;

    public ResturantDto addToFavorites(Long resturantId,User user) throws Exception;

    public Resturant updateResturantStatus(Long id) throws Exception;
}
