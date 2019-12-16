package balik.englishbot.service.impl;

import balik.englishbot.dao.UserDAO;
import balik.englishbot.dao.impl.UserDAOImpl;
import balik.englishbot.domain.User;
import balik.englishbot.service.UserService;

import java.io.IOException;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    public UserServiceImpl() throws IOException {
        userDAO = UserDAOImpl.getInstance();//todo: refactor exception
    }

    @Override
    public User getUser(long chatId) {
        return userDAO.getUser(chatId);
    }

    @Override
    public User createUser(long chatId, String username, String firstName) {
        User user = new User(chatId);
        user.setUsername(username);
        user.setName(firstName);

        userDAO.createUser(user);

        return user;
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
}
