// src/main/java/com/example/ecommerce/services/AddressService.java
package com.example.ecommerce.services;

import com.example.ecommerce.dtos.AddressDTO;
import com.example.ecommerce.models.Address;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired private AddressRepository repo;

    public List<AddressDTO> getMyAddresses(User user) {
        return repo.findByUser_Id(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }
    public Address getByIdForUser(User user, Long id) {
        return repo.findById(id)
                .filter(addr -> addr.getUser().getId().equals(user.getId()))
                .orElseThrow(() ->
                        new RuntimeException("Address " + id + " not found for user " + user.getEmail()));
    }


    @Transactional
    public AddressDTO addAddress(User user, AddressDTO dto) {
        Address a = toEntity(dto);
        a.setUser(user);
        return toDTO(repo.save(a));
    }

    @Transactional
    public AddressDTO updateAddress(User user, Long id, AddressDTO dto) {
        Address a = repo.findById(id)
                .filter(addr -> addr.getUser().getId().equals(user.getId()))
                .orElseThrow();
        a.setStreet(dto.street());
        a.setCity(dto.city());
        a.setState(dto.state());
        a.setZip(dto.zip());
        return toDTO(repo.save(a));
    }

    public void deleteAddress(User user, Long id) {
        Address a = repo.findById(id)
                .filter(addr -> addr.getUser().getId().equals(user.getId()))
                .orElseThrow();
        repo.delete(a);
    }

    /* … mappers … */
    private AddressDTO toDTO(Address a) {
        return new AddressDTO(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZip());
    }
    private Address toEntity(AddressDTO d) {
        Address a = new Address();
        a.setStreet(d.street()); a.setCity(d.city());
        a.setState(d.state());   a.setZip(d.zip());
        return a;
    }


}
