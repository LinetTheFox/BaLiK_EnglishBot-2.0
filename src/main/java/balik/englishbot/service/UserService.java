package balik.englishbot.service;

import balik.englishbot.domain.User;

public interface UserService {
    User getUser(long id);

    User createUser(long id, String username, String firstName);

    void updateUser(User user);
}
