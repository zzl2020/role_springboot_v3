package com.example.role.commons.create;

public class Const {
    public static final Integer BUFFER_SIZE =2*1024;
    public static final String RETURN = "return ";
    public  static final String JAV_TYPE="java.lang.";
    public static final String MAPPER="Mapper";
    public static final String _MAPPER=".mapper.";
    public   static final String SERVER="Server";
    public static final String _SERVER=".server.";
    public   static final String CONTROLLER="Controller";
    public static final String _CONTROLLER=".controller.";
    public static final String _SERVER_IMP=".impl";
    public static final String _ENTITY=".entity.";
    public static final String USER = "root";
    public static final String PASS = "123456";
    //    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/sysdb?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true&rewriteBatchedStatements=true";
    public static final String SQL_SUFFIX = "Returns";
    public static final String PRI_KEY="_PRI";
    public static final String ERROR_MSG= "实体类字段长度与数据库字段长度不相符";
}
