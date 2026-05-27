package com.chicos_ingenieros.zenkai.Users.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMySqlRepositoryTest {

    @Mock
    private SpringUserRepository springUserRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserMySqlRepository userMySqlRepository;

    @Test
    void save_ShouldReturnSavedUser() {
        // Arrange
        User domainUser = new User();
        UserEntity userEntity = new UserEntity();
        UserEntity savedEntity = new UserEntity();
        User expectedUser = new User();

        when(userMapper.userToUserEntity(domainUser)).thenReturn(userEntity);
        when(springUserRepository.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.userEntityToUser(savedEntity)).thenReturn(expectedUser);

        User actualUser = userMySqlRepository.save(domainUser);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(springUserRepository, times(1)).save(userEntity);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        User expectedUser = new User();

        when(springUserRepository.findByEmail(email)).thenReturn(userEntity);
        when(userMapper.userEntityToUser(userEntity)).thenReturn(expectedUser);

        User actualUser = userMySqlRepository.findByEmail(email);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByDocumentNumber_ShouldReturnUser_WhenDocumentExists() {
        String document = "12345678";
        UserEntity userEntity = new UserEntity();
        User expectedUser = new User();

        when(springUserRepository.findByDocumentNumber(document)).thenReturn(userEntity);
        when(userMapper.userEntityToUser(userEntity)).thenReturn(expectedUser);

        User actualUser = userMySqlRepository.findByDocumentNumber(document);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {
        Long id = 1L;
        UserEntity userEntity = new UserEntity();
        User expectedUser = new User();

        when(springUserRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userMapper.userEntityToUser(userEntity)).thenReturn(expectedUser);

        User actualUser = userMySqlRepository.findById(id);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        Long id = 1L;
        when(springUserRepository.findById(id)).thenReturn(Optional.empty());

        User actualUser = userMySqlRepository.findById(id);

        assertNull(actualUser);
        verifyNoInteractions(userMapper);
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        UserEntity entity1 = new UserEntity();
        UserEntity entity2 = new UserEntity();
        User user1 = new User();
        User user2 = new User();

        when(springUserRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(userMapper.userEntityToUser(entity1)).thenReturn(user1);
        when(userMapper.userEntityToUser(entity2)).thenReturn(user2);

        List<User> actualUsers = userMySqlRepository.findAll();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(user1, actualUsers.get(0));
        assertEquals(user2, actualUsers.get(1));
    }

    @Test
    void deleteById_ShouldInvokeRepositoryDelete() {
        Long id = 1L;
        doNothing().when(springUserRepository).deleteById(id);

        userMySqlRepository.deleteById(id);

        verify(springUserRepository, times(1)).deleteById(id);
    }

    @Test
    void countUsers_ShouldReturnTotalCount() {
        Long expectedCount = 5L;
        when(springUserRepository.count()).thenReturn(expectedCount);

        Long actualCount = userMySqlRepository.countUsers();

        assertEquals(expectedCount, actualCount);
        verify(springUserRepository, times(1)).count();
    }
}