package JDBC;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.sql.*;
import java.util.Properties;

public class jdbcDemoOptimized {
    @Test
    public  void testDriver() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection();

            String sql = "select * from miaosha_user where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"12345678901");

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println("id:"+resultSet.getObject("id") + "\tpassword:" + resultSet.getObject("password"));
            }
        }finally {
            JdbcUtils.free(resultSet, preparedStatement, connection);
        }

    }
}

class JdbcUtils{
    private static Properties properties = null;
    static{
        try{//读取properties文件
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            properties = new Properties();
            properties.load(in);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        try{//加载驱动类
            Class.forName(properties.getProperty("driver"));
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(properties.getProperty("jdbcUrl"), properties.getProperty("user"), properties.getProperty("password"));
    }

    static void free(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) throws SQLException {
        //释放resultSet
        try {
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放preparedStatement
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    //释放connection
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
