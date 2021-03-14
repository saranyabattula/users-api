package com.sample.api.users.resources;

import com.sample.api.users.models.User;
import com.sample.api.users.repository.UserRepository;
import com.sample.api.users.service.EncryptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

    @InjectMocks private UsersController usersController;
    @Mock private UserRepository userRepository;
    @Mock private EncryptionService encryptionService;

    private static final String ENCRYPTED_VALUE = "g2Nn7wUZOQ6Xc+1lenkZTQ9ZDf9a2/R";

    @Test
    public void shouldCreateUser(){
        // GIVEN
        User user = buildUser();

        // WHEN
        when(encryptionService.encrypt(any())).thenReturn(ENCRYPTED_VALUE);

        // THEN
        ResponseEntity<User> response = usersController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ENCRYPTED_VALUE, response.getBody().getEmail());
    }

    @Test
    public void shouldReturnInternalServerErrorOnStoringUserInDB(){
        // GIVEN
        User user = buildUser();

        // WHEN
        when(encryptionService.encrypt(any())).thenReturn(ENCRYPTED_VALUE);
        when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());

        // THEN
        ResponseEntity<User> response = usersController.createUser(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnDecryptedUserEmail(){
        // GIVEN
        User encryptedUser = buildEncryptedUser();

        // WHEN
        when(encryptionService.decrypt(ENCRYPTED_VALUE)).thenReturn("user@gmail.com");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(encryptedUser));

        // THEN
        ResponseEntity<User> response = usersController.getDecryptedOne("1001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user@gmail.com", response.getBody().getEmail());
    }


    private User buildUser(){
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setLastName("lastname");
        user.setFirstName("firstname");
        user.setTitle("mr");
        user.setDateOfBirth("27-08-1992");
        return user;
    }

    private User buildEncryptedUser(){
        User user = new User();
        user.setId(1001);
        user.setEmail(ENCRYPTED_VALUE);
        user.setLastName(ENCRYPTED_VALUE);
        user.setFirstName(ENCRYPTED_VALUE);
        user.setTitle(ENCRYPTED_VALUE);
        user.setDateOfBirth(ENCRYPTED_VALUE);
        return user;
    }
}
