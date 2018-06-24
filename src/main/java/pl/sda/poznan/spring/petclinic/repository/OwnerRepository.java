package pl.sda.poznan.spring.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import pl.sda.poznan.spring.petclinic.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, Long> {

}
