package pl.sda.poznan.spring.petclinic.service;

import pl.sda.poznan.spring.petclinic.model.Owner;

import java.util.Collection;

public interface OwnerService {

    Owner findOwnerById(Long id);

    Collection<Owner> findAllOwners();

    void saveOwner(Owner owner);

    Collection<Owner> findOwnerByLastname(String lastname);
}
