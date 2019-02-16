package balik.englishbot.database;

public class User {
    private long chatId;
    private String username;
    private String name;
    private int unit;
    private int currentQuestion;
    private int score;

    public User(long chatId) {
        this.chatId = chatId;
    }

    public User(String chatId) {
        this.chatId = Long.parseLong(chatId);
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
