package com.quickcrave.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quickcrave.dto.ResturantDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;

//    @OneToMany user has one to many relation with order
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "customer")
    @JsonIgnore
    private List<Order> orders=new ArrayList<>();

    @ElementCollection
    private List<ResturantDto>favorites=new ArrayList<>();

    @OneToMany(cascade =CascadeType.ALL ,orphanRemoval = true)

    private List<Address> addresses= new ArrayList<>();
}
