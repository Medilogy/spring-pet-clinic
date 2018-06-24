package pl.sda.poznan.spring.petclinic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.poznan.spring.petclinic.exception.OwnerNotFoundException;
import pl.sda.poznan.spring.petclinic.model.Owner;
import pl.sda.poznan.spring.petclinic.service.OwnerService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping(path = "/owner/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getOwnerById(@PathVariable Long id) {
        Owner ownerById = ownerService.findOwnerById(id);
        return ResponseEntity.ok(ownerById);
    }

    @GetMapping(path = "/owners", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<Owner>> getAllOwners() {
        return ResponseEntity.ok(ownerService.findAllOwners());
    }

    @PostMapping(path = "/owner", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        this.ownerService.saveOwner(owner);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

