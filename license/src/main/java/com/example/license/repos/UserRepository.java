package com.example.license.repos;

import com.example.license.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);


    @Override
    void delete(User deleted);

    User findByResetPasswordToken(String token);
    //other method implementations are already covered by Crud repo with mongoDB
}
