package pl.sda.poznan.spring.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import pl.sda.poznan.spring.petclinic.model.Pet;

public interface PetRepository extends CrudRepository<Pet, Long> {

    Pet findByName (String name);

}
