package com.logmein.saulo.databaseChallenge.server.jdbc;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class H2JdbcDatabaseChallenge {

    private static String jdbcURL = "jdbc:h2:file:./keyvaluedb";
    private static String jdbcUsername = "sa";
    private static String jdbcPassword = "";
    private static final String CREATE_KEY_VALUE_TABLE =
            "CREATE TABLE KEYVALUE (KEY VARCHAR(20) PRIMARY KEY, VALUE VARCHAR(20));";

    private static final String INSERT = "INSERT INTO KEYVALUE (KEY, VALUE) VALUES(?,?);";

    private static final String QUERY = "SELECT * FROM KEYVALUE WHERE KEY = ?;";

    private static final String DELETE = "DELETE FROM KEYVALUE WHERE KEY = ?;";

    private static final String DROP = "DROP TABLE KEYVALUE";

    private static Connection databaseConnection;

    private static Map<String, Connection> connectionMap = new HashMap<>();


    public static Connection getConnection() {

        if (databaseConnection == null) {
            databaseConnection = createConnection();
        } else {
            try {
                if (databaseConnection.isClosed()) {
                    databaseConnection = createConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return databaseConnection;
    }

    public static Connection createConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void destroyDatabase() {

        try {
            if(!connectionMap.values().isEmpty()) {
                connectionMap.values().forEach(it -> {
                    try {
                        it.rollback();
                        it.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Connection connection = H2JdbcDatabaseChallenge.getConnection();

        try {

            Statement statement = connection.createStatement();
            statement.execute(DROP);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection(String transactionId) {

        Connection result = connectionMap.get(transactionId);
        if (result == null) {
            result = createConnection();
            try {
                result.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connectionMap.put(transactionId, result);
        }

        return result;
    }


    public static void main(String[] args) {

        H2JdbcDatabaseChallenge.createDatabase();
        H2JdbcDatabaseChallenge database = new H2JdbcDatabaseChallenge();

        //test 1
        database.put("example", "foo");
        System.out.println("returns: " + database.get("example"));
        database.delete("example");
        System.out.println("returns: " + database.get("example"));
        database.delete("example");


        //test 2
        System.out.println("-----------------------------------------------------");
        database.createTransaction("abc");
        database.put("a", "foo", "abc");
        System.out.println("returns: " + database.get("a", "abc"));
        System.out.println("returns: " + database.get("a"));

        System.out.println("");
        System.out.println("-----------------------------------------------------");
        database.createTransaction("xyz");
        database.put("a", "bar", "xyz");
        System.out.println("returns: " + database.get("a", "xyz"));
        database.commitTransaction("xyz");
        System.out.println("returns: " + database.get("a"));

        System.out.println("");
        System.out.println("-----------------------------------------------------");
        try {
            database.commitTransaction("abc");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //continue
        }

        System.out.println("");
        System.out.println("-----------------------------------------------------");
        System.out.println("returns: " + database.get("a"));


        System.out.println("");
        System.out.println("-----------------------------------------------------");
        database.createTransaction("abc");
        database.put("a", "foo", "abc");
        System.out.println("returns: " + database.get("a"));
        database.rollbackTransaction("abc");
        try {
            database.put("a", "foo", "abc");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //continue
        }

        System.out.println("returns: " + database.get("a"));

        System.out.println("");
        System.out.println("-----------------------------------------------------");
        database.createTransaction("def");
        database.put("b", "foo", "def");
        System.out.println("returns: " + database.get("a","def"));
        System.out.println("returns: " + database.get("b","def"));
        database.rollbackTransaction("def");
        System.out.println("returns: " + database.get("b"));



        H2JdbcDatabaseChallenge.destroyDatabase();

    }

    public static void createDatabase() {
        Connection connection = H2JdbcDatabaseChallenge.getConnection();
        try (Statement statement = connection.createStatement();) {
            statement.execute(CREATE_KEY_VALUE_TABLE);
        } catch (SQLException e) {
            H2JdbcDatabaseChallenge.printSQLException(e);
        }
    }

    public void createTransaction(String transactionId) {
        try {
            if (connectionMap.get(transactionId) != null && !connectionMap.get(transactionId).isClosed()) {
                H2JdbcDatabaseChallenge.getConnection(transactionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        String result = "";
        Connection connection = H2JdbcDatabaseChallenge.getConnection();
        result = get(key, connection);

        return result;
    }

    public String get(String key, String transactionId) {
        String result = "";
        Connection connection = H2JdbcDatabaseChallenge.getConnection(transactionId);
        result = get(key, connection);

        return result;
    }

    public String get(String key, Connection conn) {
        String result = "";

        try {
            PreparedStatement statement = conn.prepareStatement(QUERY);
            statement.setString(1, key);


            ResultSet rs = statement.executeQuery();

            String value = null;
//            KeyValueDTO dto = new KeyValueDTO();
            while (rs.next()) {
                value = rs.getString("VALUE");

            }

            result = value;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void delete(String key) {
        Connection connection = H2JdbcDatabaseChallenge.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE);) {
            statement.setString(1, key);

            statement.executeUpdate();

        } catch (SQLException e) {
            H2JdbcDatabaseChallenge.printSQLException(e);
        }
    }

    public void put(String key, String value) {
        Connection connection = H2JdbcDatabaseChallenge.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT);) {
            statement.setString(1, key);
            statement.setString(2, value);

            statement.executeUpdate();

        } catch (SQLException e) {
            H2JdbcDatabaseChallenge.printSQLException(e);
        }
    }

    public void forceCloseOtherConnection(String transactionId)
    {
        try {
            for (String keyMap: connectionMap.keySet()) {
                if (!keyMap.equals(transactionId))
                {
                    connectionMap.get(keyMap).rollback();
                    connectionMap.get(keyMap).close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value, String transactionId) {

        forceCloseOtherConnection(transactionId);
        Connection connection = H2JdbcDatabaseChallenge.getConnection(transactionId);
        try(PreparedStatement statement = connection.prepareStatement(INSERT);) {

            statement.setString(1, key);
            statement.setString(2, value);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void commitTransaction(String transactionId) {


        Connection connection = H2JdbcDatabaseChallenge.getConnection(transactionId);
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction(String transactionId)
    {
        Connection connection = H2JdbcDatabaseChallenge.getConnection(transactionId);
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
