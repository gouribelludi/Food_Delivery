package com.quickcrave.controller;

import com.quickcrave.model.Resturant;
import com.quickcrave.model.User;
import com.quickcrave.request.CreteResturantRequest;
import com.quickcrave.response.MessageResponse;
import com.quickcrave.service.RestautaurntService;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminResturantController {
    @Autowired
    private RestautaurntService resturantService;

    @Autowired
    private UserService userService;


    @PostMapping()

    public ResponseEntity<Resturant> createResturant
            (
        @RequestBody CreteResturantRequest req,
        @RequestHeader("Authorization") String jwt
        )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        Resturant resturant=resturantService.createResturant(req,user);
        return new ResponseEntity<>(resturant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")

    public ResponseEntity<Resturant> updateResturant
            (
                    @RequestBody CreteResturantRequest req,
                    @RequestHeader("Authorization") String jwt,
                    @PathVariable Long id
            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        Resturant resturant=resturantService.updateResturant(id,req);
        return new ResponseEntity<>(resturant, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")

    public ResponseEntity<MessageResponse> deleteResturant
            (

                    @RequestHeader("Authorization") String jwt,
                    @PathVariable Long id
            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

       resturantService.deleteResturant(id);
        MessageResponse res= new MessageResponse();
        res.setMessage("Resturant is Deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")

    public ResponseEntity<Resturant> updateResturantStatus
            (

                    @RequestHeader("Authorization") String jwt,
                    @PathVariable Long id
            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

       Resturant resturant= resturantService.updateResturantStatus(id);

        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @GetMapping("/user")

    public ResponseEntity<Resturant> findResturantByUserId
            (

                    @RequestHeader("Authorization") String jwt

            )throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        Resturant resturant= resturantService.getResturantByUserId(user.getId());

        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }



}
