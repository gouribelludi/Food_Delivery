package com.quickcrave.service;

import com.quickcrave.dto.ResturantDto;
import com.quickcrave.model.Address;
import com.quickcrave.model.Resturant;
import com.quickcrave.model.User;
import com.quickcrave.repository.AddressRepository;
import com.quickcrave.repository.ResturantRepository;
import com.quickcrave.repository.UserRepository;
import com.quickcrave.request.CreteResturantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResturantServicesImp implements RestautaurntService {

    @Autowired
    private ResturantRepository resturantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Resturant createResturant(CreteResturantRequest req, User user) {

        Address address= addressRepository.save(req.getAddress());

        Resturant resturant=new Resturant();
        resturant.setAddress(address);
        resturant.setContactInformation(req.getContactInformation());
        resturant.setCuisineType(req.getCuisineType());
        resturant.setDescription(req.getDescription());
        resturant.setImages(req.getImages());
        resturant.setName(req.getName());
        resturant.setOpeningHours(req.getOpningHours());
        resturant.setRegistrationDate(LocalDateTime.now());
        resturant.setOwner(user);

        return resturantRepository.save(resturant);
    }

//    @Override
//    public Resturant updateResturant(Long resturantid, CreteResturantRequest updatedResturant) throws Exception {
//        Resturant resturant=findResturantById(resturantid);
//        if(resturant.getCuisineType()!=null)
//        {
//            resturant.setCuisineType(updatedResturant.getCuisineType());
//        }
//        if(resturant.getDescription()!=null)
//        {
//            resturant.setDescription(updatedResturant.getDescription());
//        }
//        if(resturant.getName()!=null)
//        {
//            resturant.setName(updatedResturant.getName());
//        }
//        return resturantRepository.save(resturant);
//    }

    @Override
    public Resturant updateResturant(Long resturantid, CreteResturantRequest updatedResturant) throws Exception {
        Resturant resturant = findResturantById(resturantid);

        resturant.setCuisineType(updatedResturant.getCuisineType());
        resturant.setDescription(updatedResturant.getDescription());
        resturant.setName(updatedResturant.getName());
        resturant.setOpeningHours(updatedResturant.getOpningHours());
        resturant.setImages(updatedResturant.getImages());

        if (updatedResturant.getAddress() != null) {
            Address address = addressRepository.save(updatedResturant.getAddress());
            resturant.setAddress(address);
        }

        resturant.setContactInformation(updatedResturant.getContactInformation());

        return resturantRepository.save(resturant);
    }

    @Override
    public void deleteResturant(Long resturantId) throws Exception {

        Resturant resturant=findResturantById(resturantId);

        resturantRepository.delete(resturant);

    }

    @Override
    public List<Resturant> getAllResturant() {
        return resturantRepository.findAll();
    }

    @Override
    public List<Resturant> searchResturant(String keyword) {
        return resturantRepository.fingBySearchQuery(keyword);
    }

    @Override
    public Resturant findResturantById(Long id) throws Exception {
        Optional<Resturant> opt=resturantRepository.findById(id);

        if(opt.isEmpty())
            throw  new Exception("resturant not found wih id"+id);
        return opt.get();
    }

    @Override
    public Resturant getResturantByUserId(Long userId) throws Exception {
        Resturant resturant=resturantRepository.findByOwnerId(userId);
        if(resturant==null)
        {
            throw new Exception("resturant not found with owner id"+userId);
        }
        return resturant ;
    }

    @Override
    public ResturantDto addToFavorites(Long resturantId, User user) throws Exception {
        Resturant resturant=findResturantById(resturantId);

        ResturantDto dto=new ResturantDto();
        dto.setDescription(resturant.getDescription());
        dto.setImages(resturant.getImages());
        dto.setTitle(resturant.getName());
        dto.setId(resturantId);

//        if(user.getFavorites().contains(dto))
//        {
//            user.getFavorites().remove(dto);
//
//        }

        boolean isFavorited=false;
        List<ResturantDto> favorites=user.getFavorites();
        for (ResturantDto favorite : favorites)
        {
            if(favorite.getId().equals(resturantId))
            {
                isFavorited=true;
                break;
            }
        }

        if(isFavorited)
        {
            favorites.removeIf(favorite-> favorite.getId().equals(resturantId));
        }
        else user.getFavorites().add(dto);

        userRepository.save(user);

        return dto;
    }

    @Override
    public Resturant updateResturantStatus(Long id) throws Exception {

        Resturant resturant=findResturantById(id);
        resturant.setOpen(!resturant.isOpen());

        return resturantRepository.save(resturant);
    }
}
