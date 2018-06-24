package pl.sda.poznan.spring.petclinic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sda.poznan.spring.petclinic.aop.ApplicationErrorHandler;
import pl.sda.poznan.spring.petclinic.exception.OwnerNotFoundException;
import pl.sda.poznan.spring.petclinic.model.Address;
import pl.sda.poznan.spring.petclinic.model.Owner;
import pl.sda.poznan.spring.petclinic.service.OwnerService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class OwnerControllerTest {

    @Autowired
    private OwnerController ownerController; // ta klase chcemy przetestowac

    @MockBean
    private OwnerService ownerService;

    private MockMvc mockMvc;

    private List<Owner> owners;

    @Before
    public void initOwners() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerController)
                .setControllerAdvice(new ApplicationErrorHandler())
                .build();


        owners = new ArrayList<>();
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setFirstname("Michal");
        owner.setLastname("Madajewski");
        Address address = new Address();
        address.setCity("Poznan");
        address.setCountry("Polska");
        address.setPostalcode("60-211");
        address.setStreet("Potockiej 39");
        owner.setAddress(address);
        owners.add(owner);

        owner = new Owner();
        owner.setId(2L);
        owner.setFirstname("Lidia");
        owner.setLastname("Zietek");
        address = new Address();
        address.setCity("Konin");
        address.setCountry("Polska");
        address.setPostalcode("65-035");
        address.setStreet("Margaretkowa 5");
        owner.setAddress(address);
        owners.add(owner);
    }

    @Test
    public void should_return_owner_by_id() throws Exception {
        // mockowanie wywolania metody findOwnerById(1) -> gdy bedzie wywolana ta metoda to zwroc pierwszy element z listy owners
        given(ownerService.findOwnerById(1L)).willReturn(owners.get(0));

        // wywylanie żądania
        mockMvc
                .perform(
                        get("/api/v1/owner/1")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("Michal"))
                .andExpect(jsonPath("$.lastname").value("Madajewski"))
                .andExpect(jsonPath("$.address.country").value("Polska"))
                .andExpect(status().isOk());

    }

    @Test
    public void should_return_error_when_owner_not_found() throws Exception {
        given(ownerService.findOwnerById(Mockito.anyLong())).willThrow(OwnerNotFoundException.class);
        mockMvc.perform(get("/api/v1/owner/21"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_bad_request_when_id_is_not_a_number() throws Exception {
        mockMvc.perform(get("/api/v1/owner/this-is-not-a-number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_list_of_owners() throws Exception {
        given(ownerService.findAllOwners()).willReturn(owners);
        mockMvc.perform
                (get("/api/v1/owners")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstname").value("Michal"))
                .andExpect(jsonPath("$[0].lastname").value("Madajewski"))
                .andExpect(jsonPath("$[0].address.country").value("Polska"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstname").value("Lidia"))
                .andExpect(jsonPath("$[1].lastname").value("Zietek"))
                .andExpect(jsonPath("$[1].address.city").value("Konin"));

    }

    @Test
    public void should_create_owner() throws Exception {
        Owner owner = owners.get(0);
        owner.setId(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String ownerAsJson = objectMapper.writeValueAsString(owner);
        mockMvc
                .perform(
                        post("/api/v1/owner")
                                .content(ownerAsJson)
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isCreated());
    }
}