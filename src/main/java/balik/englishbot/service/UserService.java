package balik.englishbot.service;

import balik.englishbot.domain.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    User getUser(Update update);

    void updateUser(User user);
}
