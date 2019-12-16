package balik.englishbot.dao;

import balik.englishbot.domain.User;

import java.util.ArrayList;

public interface UserDAO {
    User getUser(long chatId);

    ArrayList<User> getAllUsers();

    void updateUser(User user);

    void createUser(User user);

}
