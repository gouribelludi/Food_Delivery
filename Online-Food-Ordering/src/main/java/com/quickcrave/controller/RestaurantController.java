package com.quickcrave.controller;

import com.quickcrave.dto.ResturantDto;
import com.quickcrave.model.Resturant;
import com.quickcrave.model.User;
import com.quickcrave.request.CreteResturantRequest;
import com.quickcrave.service.RestautaurntService;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestautaurntService resturantService;

    @Autowired
    private UserService userService;


    @GetMapping("/search")

    public ResponseEntity<List<Resturant>> searchResturant
            (

                    @RequestHeader("Authorization") String jwt,
                    @RequestParam String keyword
            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        List<Resturant> resturant=resturantService.searchResturant(keyword);
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }


    @GetMapping("")

    public ResponseEntity<List<Resturant>> getAllResturant
            (

                    @RequestHeader("Authorization") String jwt

            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        List<Resturant> resturant=resturantService.getAllResturant();
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @GetMapping("/{id}")

    public ResponseEntity <Resturant> findResturantById
            (

                    @RequestHeader("Authorization") String jwt,
                    @PathVariable Long id

            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        Resturant resturant=resturantService.findResturantById(id);
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-favorites")

    public ResponseEntity <ResturantDto> addToFavorites
            (

                    @RequestHeader("Authorization") String jwt,
                    @PathVariable Long id

            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        ResturantDto resturant=resturantService.addToFavorites(id,user);

        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

}
