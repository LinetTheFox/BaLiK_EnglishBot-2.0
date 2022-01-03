package balik.englishbot.service.impl;

import balik.englishbot.dao.UserDAO;
import balik.englishbot.dao.impl.UserDAOImpl;
import balik.englishbot.domain.User;
import balik.englishbot.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    public UserServiceImpl() throws IOException {
        userDAO = UserDAOImpl.getInstance();//todo: refactor exception
    }

    @Override
    public User getUser(Update update) {
        final Long userId = update.getMessage().getFrom().getId();
        User user = userDAO.getUser(userId);

        if (user == null) {
            user = new User(userId);
            user.setUsername(update.getMessage().getFrom().getUserName());
            user.setName(update.getMessage().getFrom().getFirstName());

            userDAO.createUser(user);
        }

        return user;
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
}
