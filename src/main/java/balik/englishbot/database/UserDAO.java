package balik.englishbot.database;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
            "username VARCHAR(20) NOT NULL, " +
            "firstname VARCHAR(20) NOT NULL, " +
            "unit INT, " +
            "current_question INT, " +
            "score INT, " +
            "PRIMARY KEY (id) " +
            ")";
    private static final String GET_BY_CHAT_ID = "SELECT * FROM users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";

    private UserDAO() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("database.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        DB_URL = properties.getProperty("url");
        USERNAME = properties.getProperty("username");
        PASSWORD = properties.getProperty("password");

        System.out.println(USERNAME);
        System.out.println(PASSWORD);

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
            // выполнить SQL запрос
            statement.execute(CREATE_TABLE);
            LOG.info("Table 'users' is created!");
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Creating table 'users' error!\n" + e.getMessage());
        }
    }

}
