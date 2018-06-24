package pl.sda.poznan.spring.petclinic.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Address {

    private String street;
    private String postalcode;
    private String city;
    private String country;
}
