package balik.englishbot.service;

import balik.englishbot.domain.User;

public interface UserService {
    User getUser(long chatId);

    User createUser(long chatId, String username, String firstName);

    void updateUser(User user);
}
