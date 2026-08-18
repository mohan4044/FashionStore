package com.fashionstore.dao;

import com.fashionstore.model.User;
import java.util.List;

public interface UserDAO {

    boolean register(User user);

    User login(String email, String password);

    User getUserById(int userId);

    User getUserByEmail(String email);

    boolean emailExists(String email);

    boolean phoneExists(String phone);

    boolean updateUser(User user);

    boolean updatePassword(int userId, String newPassword);

    List<User> getAllUsers();
}