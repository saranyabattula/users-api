package com.sample.api.users.resources;

import com.sample.api.users.models.User;
import com.sample.api.users.repository.UserRepository;
import com.sample.api.users.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users",
        produces = "application/json")
public class UsersController {

    @Autowired private UserRepository userRepository;
    @Autowired private EncryptionService encryptionService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try {
            User userEntity = buildUserEntity(user);
            userRepository.save(userEntity);
            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{id}/decrypt")
    public ResponseEntity<User> getDecryptedUserDetails(@PathVariable("id") String id){
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        return user.map(value -> new ResponseEntity<>(buildUserModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private User buildUserEntity(User user) {
        User userEntity = new User();
        userEntity.setTitle(encrypt(user.getTitle()));
        userEntity.setDateOfBirth(encrypt(user.getDateOfBirth()));
        userEntity.setFirstName(encrypt(user.getFirstName()));
        userEntity.setLastName(encrypt(user.getLastName()));
        userEntity.setEmail(encrypt(user.getEmail()));
        return userEntity;
    }

    private User buildUserModel(User userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setTitle(decrypt(userEntity.getTitle()));
        user.setDateOfBirth(decrypt(userEntity.getDateOfBirth()));
        user.setFirstName(decrypt(userEntity.getFirstName()));
        user.setLastName(decrypt(userEntity.getLastName()));
        user.setEmail(decrypt(userEntity.getEmail()));
        return user;
    }

    private String encrypt(String value){
        return encryptionService.encrypt(value);
    }

    private String decrypt(String value){
        return encryptionService.decrypt(value);
    }
}
