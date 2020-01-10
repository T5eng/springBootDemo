package JDBC;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class jdbcDemo {
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    Connection connection=null;

    @Test
    public void testDriver() throws Exception {
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
        connection = DriverManager.getConnection(jdbcUrl, user, password);
        if(null!=connection)
            System.out.println("成功连接SQL@ " + jdbcUrl);

//        query("12345678901");
        update("12345678901", "b31222f53504c4969f6c3809402d12ff");

        //释放资源
        if (resultSet!=null) resultSet.close();
        preparedStatement.close();
        connection.close();
    }

    private void query(String id) throws SQLException{ //根据id查表
        //定义sql模板
        String sql = "select * from miaosha_user where id=?";
        preparedStatement = connection.prepareStatement(sql);

        //设置sql模板参数
        preparedStatement.setString(1, id); //写入sql语句中待定参数

        //执行sql语句, 返回结果序列
        resultSet = preparedStatement.executeQuery();

        //输出结果s
        while(resultSet.next()){
            System.out.println("id:"+resultSet.getObject("id") + "\tpassword:" + resultSet.getObject("password"));
        }
    }

    private boolean update(String id, String password) throws SQLException{ //根据id设置密码
        //定义sql模板
        String setPass = "UPDATE miaosha_user set nickname='Joshua' , password=? WHERE id=?";
        preparedStatement = connection.prepareStatement(setPass);

        //设置sql模板参数
        preparedStatement.setString(1,password);
        preparedStatement.setString(2,id); //写入sql语句中待定参数

        //执行sql语句, 返回执行结果
        return preparedStatement.execute();
    }


}
