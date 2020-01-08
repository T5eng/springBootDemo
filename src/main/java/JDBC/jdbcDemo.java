package JDBC;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class jdbcDemo {
    @Test
    public  void testDriver() throws Exception {
//        Driver driver = new com.mysql.jdbc.Driver(); //通过驱动管理器 java.sql.Driver <- com.mysql.jdbc.Driver

        //读取数据库连接信息
        Properties info = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");//读取输入流
        info.load(inputStream);//加载输入流
        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String jdbcUrl = info.getProperty("jdbcUrl");
        String driver = info.getProperty("driver");

        //注册驱动
        Class.forName(driver);

        //建立连接
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        if(null!=connection)
            System.out.println("成功连接SQL@ " + jdbcUrl);

        //定义sql模板
        String sql = "select * from miaosha_user where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //设置sql模板参数
        preparedStatement.setString(1,"12345678901"); //写入sql语句中待定参数

        //执行sql语句, 返回结果序列
        ResultSet resultSet = preparedStatement.executeQuery();

        //输出结果s
        while(resultSet.next()){
            System.out.println("id:"+resultSet.getObject("id") + "\tpassword:" + resultSet.getObject("password"));
        }

        //释放资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
