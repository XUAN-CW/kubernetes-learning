package com.example.springbootconnectmysql.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 禤成伟
 * @date 2022-04-14 - 9:04
 */
@RestController
public class MySQLController {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.driver-class-name}")
    String driverName;
    @Value("${spring.datasource.username}")
    String userName;
    @Value("${spring.datasource.password}")
    String password;

    @GetMapping("connect-mysql")
    public List<String> connect() throws SQLException, ClassNotFoundException {
        //加载驱动程序
        Class.forName(driverName);
        //1.getConnection()方法，连接MySQL数据库！！
        Connection con = DriverManager.getConnection(url,userName,password);
        //2.创建statement类对象，用来执行SQL语句！！
        Statement statement = con.createStatement();
        //要执行的SQL语句
        String sql = "SELECT `SCHEMA_NAME` FROM `SCHEMATA`";
        //3.ResultSet类，用来存放获取的结果集！！
        ResultSet rs = statement.executeQuery(sql);
        //取出数据库名
        List<String> schemaNameList = new ArrayList<>();
        while(rs.next()){
            String schemaName = rs.getString("SCHEMA_NAME");
            schemaNameList.add(schemaName);
        }
        rs.close();
        con.close();
        return schemaNameList;
    }
}
