package balik.englishbot.database;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class UserDAO {
    private final String DB_URL;
    private final String USERNAME;
    private final String PASSWORD;

    private static Logger LOG = Logger.getLogger(UserDAO.class);
    private static UserDAO instance;

    /**
     * QUERIES
     **/
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id BIGINT NOT NULL, " +
            "username VARCHAR(20), " +
            "firstname VARCHAR(20), " +
            "unit INT, " +
            "current_question INT, " +
            "score INT, " +
            "PRIMARY KEY (id) " +
            ")";
    private static final String CREATE_USER = "INSERT INTO users (id, username, firstname, unit, current_question, score)" +
            " VALUES (?,?,?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE users SET username=?, firstname=?, unit=?, current_question=?, score=?" +
            " WHERE id=?";
    private static final String GET_USER_BY_CHAT_ID = "SELECT * FROM users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";

    private UserDAO() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("database.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        DB_URL = properties.getProperty("url");
        USERNAME = properties.getProperty("username");
        PASSWORD = properties.getProperty("password");

        addMySQLToClassPath();
        createTable();
        LOG.info("Database info entered");
    }

    public static UserDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new UserDAO();
            LOG.info("UserDAO instance created.");
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    private void addMySQLToClassPath() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOG.error("Add MySQL error!");
        }
    }

    private void createTable() {
        try (Connection dbConnection = getConnection();
             Statement statement = dbConnection.createStatement()) {
            statement.execute(CREATE_TABLE);
            LOG.info("Table 'users' is created!");
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Creating table 'users' error!\n" + e.getMessage());
        }
    }

    public User getUser(long chatId) {
        PreparedStatement statement;
        ResultSet rs;

        try (Connection con = getConnection()) {
            statement = con.prepareStatement(GET_USER_BY_CHAT_ID);
            statement.setString(1, String.valueOf(chatId));

            rs = statement.executeQuery();

            if (rs.next())
                return extractUser(rs);

        } catch (SQLException e) {
            LOG.error("Can not get User\n" + e.getMessage());
        }

        return null;
    }

    public ArrayList<User> getAllUsers() {
        PreparedStatement statement;
        ResultSet rs;

        ArrayList<User> users = new ArrayList<>();

        try (Connection con = getConnection()) {
            statement = con.prepareStatement(GET_ALL_USERS);

            rs = statement.executeQuery();

            while (rs.next())
                users.add(extractUser(rs));

        } catch (SQLException e) {
            LOG.error("Can not get User\n" + e.getMessage());
        }

        return users;
    }

    private User extractUser(ResultSet rs) {
        User user = null;

        try {
            user = new User(rs.getString("id"));
            user.setName(rs.getString("firstname"));
            user.setUsername(rs.getString("username"));
            user.setUnit(Integer.parseInt(rs.getString("unit")));
            user.setCurrentQuestion(Integer.parseInt(rs.getString("current_question")));
            user.setScore(Integer.parseInt(rs.getString("score")));
        } catch (SQLException e) {
            LOG.error("Can not extract user\n" + e.getMessage());
        }

        return user;
    }

    public void updateUser(User user) {
        PreparedStatement statement;

        try (Connection con = getConnection()) {
            statement = con.prepareStatement(UPDATE_USER);
            int k = 1;
            k = setUserData(k, user, statement);
            statement.setString(k, String.valueOf(user.getChatId()));

            int rs = statement.executeUpdate();
            LOG.info("User update result: " + rs);

        } catch (SQLException e) {
            LOG.error("Can not get User\n" + e.getMessage());
        }
    }

    private int setUserData(int k, User user, PreparedStatement statement) throws SQLException {
        statement.setString(k++, user.getUsername());
        statement.setString(k++, user.getName());
        statement.setInt(k++, user.getUnit());
        statement.setInt(k++, user.getCurrentQuestion());
        statement.setInt(k++, user.getScore());

        return k;
    }

    public void createUser(User user) {
        PreparedStatement statement;

        if (getUser(user.getChatId()) != null) {
            updateUser(user);
            return;
        }

        try (Connection con = getConnection()) {


            statement = con.prepareStatement(CREATE_USER);
            int k = 1;
            statement.setString(k++, String.valueOf(user.getChatId()));
            setUserData(k, user, statement);

            int rs = statement.executeUpdate();
            LOG.info("User create result: " + rs);

        } catch (SQLException e) {
            LOG.error("Can not create User\n" + e.getMessage());
        }
    }
}
