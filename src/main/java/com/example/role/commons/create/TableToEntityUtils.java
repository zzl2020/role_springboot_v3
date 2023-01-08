package com.example.role.commons.create;


import com.alibaba.excel.util.StringUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName TableToEntityUtils
 * @Author zhuozl
 * @Date 2022/12/23 17:25
 */
public class TableToEntityUtils {

    //static final String RN="\r\n";
    static List<String> tableFields = null;
    static List<Map> fieldMaps = null;

    public static void main(String[] args) throws Exception {

        // createEntity("sysdb", "sys_user","com.zzl.export",false);
        //createInterface("SysUser","com.zzl.export",MAPPER);
        //createServer("SysUser","com.zzl.export");

        creates("sysdb", "com.zzl.export", null, false);
        //List<String> fields = getFields("com.zzl.export.entity", "SysUser");
        //fields.stream().forEach(System.out::println);
        //String[] tableFieldsStr = {"id_PRI","user_name","password","icon","desc","phone","status","email","user_role_id"};
        //tableFields = Arrays.asList(tableFieldsStr);
        //String str = getInsertSql(fields, "sys_user");
        //String str = getUpdateSql(fields, "sys_user");
        //System.out.println(str);
        //     String sqlStr = createSqlReturn(fields, tableFields,"SysUser");
        //System.out.println(sqlStr);
        //String mapperPath ="com.zzl.export.mapper.SysUserMapper";
        //createMapperXml("com.zzl.export.entity",mapperPath,"SysUser","sys_user");
        // creates("sysdb","com.zzl.export",);
        // String str = "SysUserMapper";

    }

    /**
     * 首字符大小写转换
     */
    private static String lowerOrUpper(String str, boolean isLower) {
        return isLower ? str.replace(str.substring(0, 1), str.substring(0, 1).toLowerCase())
                : str.replace(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }

    /**
     * 生成controller层
     */
    private static void createController(String packageName, String entityName) throws Exception {
        String entityPath = packageName + Const._ENTITY + entityName;
        String serverPath = packageName + Const._SERVER + entityName + Const.SERVER;
        String serverClassName = entityName + Const.SERVER;
        String colClassName = entityName + Const.CONTROLLER;
        //首字符转换成小写
        String sFiledName = lowerOrUpper(serverClassName, true);
        String eFiledName = lowerOrUpper(entityName, true);
        String colStr = "import javax.annotation.Resource; \r\n"
                + "import org.springframework.web.bind.annotation.*;\r\n"
                + "import " + entityPath + ";\r\n"
                + "import " + serverPath + ";\r\n"
                + "import java.util.List;\r\n"
                + "@RequestMapping(\"/" + entityName.toLowerCase() + "\")"
                + "@RestController()\r\n";
        StringBuffer colBuffer = new StringBuffer(colStr);
        //添加头
        colBuffer.append("public class ").append(colClassName).append("{\r\n");
        colBuffer.append("@Resource\r\n").append("private ").append(serverClassName).append(" ").append(sFiledName).append(";\r\n");
        //添加数据
        colBuffer.append("@PostMapping(\"/insert\")\r\n")
                .append("public int insert(@RequestBody ")
                .append(entityName).append(" ")
                .append(eFiledName).append("){\r\n")
                .append(Const.RETURN)
                .append(sFiledName)
                .append(".")
                .append("insert(")
                .append(eFiledName)
                .append(");\r\n}")
                .append("\r\n");
        //修改数据
        colBuffer.append("@PostMapping(\"/update\")\r\n")
                .append("public int update(@RequestBody ")
                .append(entityName).append(" ")
                .append(eFiledName).append("){\r\n")
                .append(Const.RETURN)
                .append(sFiledName)
                .append(".")
                .append("update(")
                .append(eFiledName)
                .append(");\r\n}")
                .append("\r\n");
        String idType = getIdType();
        //删除数据
        colBuffer.append("@GetMapping(\"/delete/{id}\")\r\n")
                .append("public int delete(@PathVariable ")
                .append(idType).append(" id")
                .append("){\r\n")
                .append(Const.RETURN)
                .append(sFiledName)
                .append(".")
                .append("delete(")
                .append("id);\r\n}")
                .append("\r\n");
        //查询所有数据
        colBuffer.append("@GetMapping(\"/findAll\")\r\n").append("public List<")
                .append(entityName)
                .append("> findAll(){\r\n")
                .append(Const.RETURN)
                .append(sFiledName)
                .append(".")
                .append("findAll();\r\n}")
                .append("\r\n");
        //根据id查询数据
        colBuffer.append("@GetMapping(\"/findById/{id}\")\r\n").append("public ")
                .append(entityName)
                .append(" findById(@PathVariable ")
                .append(idType)
                .append(" id){\r\n")
                .append(Const.RETURN)
                .append(sFiledName)
                .append(".")
                .append("findById(id);\r\n}")
                .append("\r\n}");
        markerBean(colClassName, colBuffer.toString(), packageName);
    }

    /**
     * 获取主键类型
     */
    private static String getPRIType() {
 /*       String allName = packageName+".entity."+className;
        try {
            Class aClass = Class.forName(allName);
            Field[] fields1 = aClass.getDeclaredFields();
            String s = removeUnderline(getId());
            for (Field field : fields1) {
                if(s.equals(field.getName())) return field.getType().getName();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/
        String idType = Const.JAV_TYPE + getIdType();
        return idType;
    }

    /**
     * 创建*mapper.xml
     */
    public static void createMapperXml(String packageName, String mapperPackagePath, String entityName, String tableName) throws Exception {
        String mapperName = entityName + Const.MAPPER + ".xml";
        //System.out.println("=============="+packageName+"==================");
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper\n" +
                "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";
        StringBuffer xmlBuffer = new StringBuffer(xmlStr);
        xmlBuffer.append("<mapper namespace=\"").append(mapperPackagePath).append("\"\t>\r\n");
        List<String> fields = getFields(packageName, entityName);

        String sqlReturn = createSqlReturn(fields, entityName);
        String sqlReturnName = entityName + Const.SQL_SUFFIX;
        String idfieldType = getPRIType();
        xmlBuffer.append(sqlReturn).append("\r\n");
        //查询所有
        xmlBuffer.append("<select id=\"findAll\" resultType =\"").append(packageName).append(".entity.").append(entityName).append("\">\r\n")
                .append("select <include refid=\"").append(sqlReturnName).append("\"/>").append("from ").append(tableName).append("\r\n")
                .append("</select>\r\n");
        String id = getId();
        //根据id进行查询
        xmlBuffer.append("<select id=\"findById\" resultType =\"").append(packageName).append(".entity.").append(entityName).append("\"")
                .append(" parameterType=\"")
                .append(idfieldType)
                .append("\">\r\n")
                .append("select <include refid=\"").append(sqlReturnName).append("\"/>").append("from ").append(tableName)
                .append(" where ")
                .append(id)
                .append("=#{id}\r\n")
                .append("</select>\r\n");
        //添加数据
        xmlBuffer.append("<insert id=\"insert\"   parameterType=\"").append(packageName).append(".entity.").append(entityName)
                .append("\">\r\n");
        //获取添加数据sql
        String insertSql = getInsertSql(fields, tableName);
        xmlBuffer.append(insertSql).append("</insert>\r\n");
        //获取根据主键id修改数据sql
        String updateSql = getUpdateSql(fields, tableName);
        xmlBuffer.append("<update id=\"update\"   parameterType=\"").append(packageName).append(".entity.").append(entityName)
                .append("\">\r\n");
        xmlBuffer.append(updateSql).append("</update>\r\n");
        //获取根据主键id删除数据sql
        xmlBuffer.append("<delete id=\"delete\"  parameterType=\"").append(idfieldType).append("\"\r\n>");

        String deleteSql = getDeleteSql(tableName);
        xmlBuffer.append(deleteSql).append("</delete>\r\n");
        xmlBuffer.append("</mapper" +
                ">");
        markerMapperXml(mapperName, xmlBuffer.toString(), packageName);
    }

    /**
     * 获取添加数据sql
     */
    private static String getInsertSql(List<String> fields, String table) {
        printError(fields);
        StringBuffer insertBuffer = new StringBuffer();
        insertBuffer.append("insert into ").append(table).append("(");
        for (String tableField : tableFields) {
            if (tableField.contains(Const.PRI_KEY)) {
                continue;
            }
            insertBuffer.append(tableField).append(",");
        }
        String s = insertBuffer.substring(0, insertBuffer.lastIndexOf(","));
        insertBuffer = new StringBuffer(s);
        insertBuffer.append(") values (");
        for (String field : fields) {
            String id = removeUnderline(getId());
            if (id.equals(field)) {
                continue;
            }
            insertBuffer.append("#{").append(field).append("},");
        }
        s = insertBuffer.substring(0, insertBuffer.lastIndexOf(","));
        insertBuffer = new StringBuffer(s);
        insertBuffer.append(");\r\n");
        return insertBuffer.toString();
    }

    private static void printError(List<String> fields) {
        if (fields.size() != tableFields.size()) throw new RuntimeException(Const.ERROR_MSG);
    }

    /**
     * 获取修改数据sql
     */
    private static String getUpdateSql(List<String> fields, String table) {
        printError(fields);
        StringBuffer updateBuffer = new StringBuffer();
        String id = removeUnderline(getId());
        updateBuffer.append("update ").append(table).append(" set ");
        String idField = "";//id实体类字段
        for (int i = 0; i < tableFields.size(); i++) {
            String tableField = tableFields.get(i);
            if (tableField.contains(Const.PRI_KEY)) {
                idField = fields.get(i);
                continue;
            }
            updateBuffer.append(tableField).append("  = ").append("#{").append(fields.get(i)).append("}").append(",");
        }
        String s = updateBuffer.substring(0, updateBuffer.lastIndexOf(","));
        updateBuffer = new StringBuffer(s);
        updateBuffer.append(" where ")
                .append(id)
                .append("=")
                .append("#{")
                .append(idField)
                .append("};\r\n");
        return updateBuffer.toString();
    }

    /**
     * 获取删除的sql
     */
    private static String getDeleteSql(String table) {
        StringBuffer deleteBuffer = new StringBuffer();
        deleteBuffer.append("delete from ").append(table).append(" where ").append(getId()).append("=#{id};\r\n");
        return deleteBuffer.toString();
    }

    /**
     * 获取id
     */
    private static String getId() {
        List<String> collect = tableFields.stream().filter(tableField -> tableField.contains(Const.PRI_KEY))
                .map(t -> t.substring(0, t.indexOf(Const.PRI_KEY)))
                .collect(Collectors.toList());
        return collect.get(0);
    }

    /**
     * 创建sql查询语句返回部分
     */
    private static String createSqlReturn(List<String> fields, String sqlName) {
        printError(fields);
        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("<sql id=\"").append(sqlName).append(Const.SQL_SUFFIX).append("\"").append(">\r\n");
        for (int i = 0; i < fields.size(); i++) {
            String tableField = tableFields.get(i);
            String field = fields.get(i);
            if (tableField.contains(Const.PRI_KEY)) {
                sqlBuffer.append(tableField.substring(0, tableField.indexOf(Const.PRI_KEY))).append(",");
                continue;
            }
            if (tableField.equals(field)) {
                sqlBuffer.append(tableField).append(",");
                continue;
            }
            sqlBuffer.append(tableField).append(" ").append(field).append(",").append(" ");
        }
        String substring = sqlBuffer.substring(0, sqlBuffer.lastIndexOf(","));
        sqlBuffer = new StringBuffer(substring);
        sqlBuffer.append("\r\n</sql>");

        return sqlBuffer.toString();
    }

    /**
     * 获取当前实体类所有字段名称
     */
    private static List<String> getFields(String packageName, String className) {
    /*    String allName = packageName+".entity."+className;
        List<String> fields = null;
        try {
            Class aClass = Class.forName(allName);
            Field[] fields1 = aClass.getDeclaredFields();
            fields= Arrays.stream(fields1).map(Field::getName).collect(Collectors.toList());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/

        return fieldMaps.stream()
                .map(map -> lowerOrUpper((String) map.get("fieldName"), true))
                .collect(Collectors.toList());
    }

    /**
     * 创建Mapper,service
     */
    static boolean checkMapper(String prfix) {
        return Const.MAPPER.equals(prfix);
    }

    public static void createInterface(String entityName, String packageName, String fix) throws Exception {
        String mcn = entityName + fix;
        String paramName = entityName.replace(entityName.substring(0, 1), entityName.substring(0, 1).toLowerCase());
        StringBuilder classBuffer = new StringBuilder();
        boolean checkMapper = checkMapper(fix);
        if (checkMapper) {
            classBuffer.append("import org.apache.ibatis.annotations.Mapper;\n\r");
        }
        classBuffer.append("import java.util.List;\r\n");
        classBuffer.append("import ")
                .append(packageName)
                .append(".entity.")
                .append(entityName)
                .append(";\r\n");
        if (checkMapper) {
            classBuffer.append("@Mapper\r\n");
        }
        classBuffer.append("public interface ")
                .append(mcn)
                .append(" {\r\n")
                .append("\t/**\r\n")
                .append("\t*添加数据\r\n\t**/\r\n")
                .append("\tint insert(")
                .append(entityName)
                .append(" ")
                .append(paramName)
                .append(");\r\n\r\n")
                .append("\t/**\r\n")
                .append("\t*根据id查询数据\r\n\t**/\r\n")
                .append("\t")
                .append(entityName);
        String idType = getIdType();
        classBuffer.append(" findById(")
                .append(idType)
                .append(" id);\r\n\r\n")
                .append("\t/**\r\n")
                .append("\t*查询所有数据\r\n\t**/\r\n")
                .append("\tList<")
                .append(entityName)
                .append("> findAll();\r\n\r\n")
                .append("\t/**\r\n")
                .append("\t*修改数据\r\n\t**/\r\n")
                .append("\tint update(")
                .append(entityName)
                .append(" ")
                .append(paramName)
                .append(");\r\n\r\n")
                .append("\t/**\r\n")
                .append("\t*删除数据\r\n\t**/\r\n")
                .append("\tint delete(")
                .append(idType)
                .append(" id);\r\n\r\n")
                .append("}\r\n");
        markerBean(mcn, classBuffer.toString(), packageName);
    }

    private static String getIdType() {
        String idType = "";
        for (int i = 0; i < tableFields.size(); i++) {
            if (tableFields.get(i).contains(Const.PRI_KEY)) idType = (String) fieldMaps.get(i).get("fieldType");
            break;
        }
        return idType;
    }

    //创建server层
    public static void createServer(String entityName, String packageName) throws Exception {
        createInterface(entityName, packageName, Const.SERVER);
        String className = entityName + Const.SERVER + "Impl";
        String paramName = entityName.replace(entityName.substring(0, 1), entityName.substring(0, 1).toLowerCase());
        String maperFeildName = paramName + Const.MAPPER;
        String idType = getIdType();
        StringBuffer classBuffer = new StringBuffer();
        classBuffer.append("import org.springframework.stereotype.Service;\r\n");
        classBuffer.append("import java.util.List;\r\n");
        classBuffer.append("import javax.annotation.Resource;\r\n");
        classBuffer.append("import ").append(packageName).append(Const._SERVER).append(className.substring(0, className.indexOf("Impl"))).append(";\r\n");
        classBuffer.append("import ")
                .append(packageName)
                .append(Const._ENTITY)
                .append(entityName)
                .append(";\r\n");
        classBuffer.append("import ")
                .append(packageName)
                .append(Const._MAPPER)
                .append(entityName)
                .append(Const.MAPPER)
                .append(";\r\n");
        classBuffer.append("@Service\r\n");
        classBuffer.append("public class ").append(className).append(" implements ").append(className.substring(0, className.indexOf("Impl"))).append(" {\r\n");

        classBuffer.append("\t@Resource\r\n \tpublic ")
                .append(entityName)
                .append(Const.MAPPER)
                .append(" ")
                .append(maperFeildName)
                .append(";\r\n\r\n");
        classBuffer.append("\tpublic int insert(")
                .append(entityName)
                .append(" ")
                .append(paramName)
                .append("){\r\n")
                .append("\t\treturn ")
                .append(maperFeildName)
                .append(".insert(")
                .append(paramName)
                .append(");\r\n\t}\r\n");
        classBuffer.append("\tpublic ")
                .append(entityName)
                .append("\tfindById(")
                .append(idType)
                .append(" id){\r\n")
                .append("\t\t return ")
                .append(maperFeildName)
                .append(".findById(id);\r\n")
                .append("\t}\r\n");
        classBuffer.append("\tpublic List<")
                .append(entityName)
                .append("> findAll(){\r\n")
                .append("\t\treturn \t")
                .append(maperFeildName)
                .append(".findAll();\r\n\t}\r\n");
        classBuffer.append("\tpublic int update(")
                .append(entityName)
                .append(" ")
                .append(paramName)
                .append(")")
                .append("{\r\n")
                .append("\t\treturn ")
                .append(maperFeildName)
                .append(".update(")
                .append(paramName)
                .append(");\r\n\t}\r\n");
        classBuffer.append("\tpublic int delete(")
                .append(idType)
                .append(" id){\r\n")
                .append("\t\treturn ")
                .append(maperFeildName)
                .append(".delete(id);\r\n\t}\r\n");
        classBuffer.append("}");
        markerBean(className, classBuffer.toString(), packageName);
    }
    //创建整个数据库的实体类(entity,mapper,)

    /**
     * @param dbName            数据库名称
     * @param packageName,包名    com.XXXX.XXX
     * @param expires           需要排除的表名
     * @param isAddEntitySuffix 类名结尾是否添加Entity
     */
    public static void creates(String dbName, String packageName, List<String> expires, boolean isAddEntitySuffix) throws Exception {

        List<String> list = showTables(dbName);
        if (list.size() == 0) {
            throw new RuntimeException("数据库中没有表");
        }
        System.out.println("=============开始创建=================");
        long startTime = System.currentTimeMillis();
        list.forEach(t -> {
            boolean c = false;
            if (!ObjectUtils.isEmpty(expires) && expires.size() > 0) {
                for (int i = 0; i < expires.size(); i++) {
                    if (t.equals(expires.get(i))) {
                        c = false;
                        break;
                    }
                    c = true;
                }
            } else {
                c = true;
            }

            if (c) {
                try {
                    createEntity(dbName, t, packageName, isAddEntitySuffix);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        });

        long endTime = System.currentTimeMillis();
        System.out.println("=============创建结束=================");
        System.out.println("===本次耗时:" + (endTime - startTime) + "=======");
    }

    private static List<String> showTables(String dbName) throws Exception {
        List<String> list = new ArrayList<>();
        Class.forName(Const.JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
        // 执行查询
        Statement stmt = conn.createStatement();
        String sql = "SELECT table_name tableName FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA ='" + dbName + "' group by table_name";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getString("tableName"));
        }
        return list;
    }

    /**
     * @param dataBaseName      数据库名
     * @param tableName         表名
     * @param packageName       包名
     * @param isAddEntitySuffix 类名是否添加Entity后缀 true-添加 false-不添加
     *                          创建单张表的实体类(entity,mapper,server)
     */
    public static void createEntity(String dataBaseName, String tableName, String packageName, boolean isAddEntitySuffix) throws Exception {
        String className = tableName;
        if (tableName.startsWith("t_")) {
            StringBuilder stringBuilder = new StringBuilder(tableName);
            stringBuilder.replace(0, 2, "");
            String initialsUpperCase = stringBuilder.substring(0, 1).toUpperCase();
            className = initialsUpperCase + stringBuilder.substring(1);
        }
        className = removeUnderline(className) + (isAddEntitySuffix ? "Entity" : "");

        StringBuffer classBuffer = new StringBuffer();
        classBuffer.append("import java.util.Date;\r\n");
        classBuffer.append("import java.time.LocalDateTime;\r\n");
        classBuffer.append("import com.alibaba.fastjson.JSONObject;\r\n");
        classBuffer.append("import java.lang.*;\r\n");
        classBuffer.append("import java.math.*;\r\n");
        classBuffer.append("import java.sql.*;\r\n");
        classBuffer.append("import lombok.Data;\r\n\r\n\r\n");
        classBuffer.append("@Data\r\n");
        classBuffer.append("public class " + className + " {\r\n\r\n");
        fieldMaps = getFiledMaps(dataBaseName, tableName);
        processAllAttrs(classBuffer, fieldMaps);
        classBuffer.append("}\r\n");
        markerBean(className, classBuffer.toString(), packageName);
        createInterface(className, packageName, Const.MAPPER);
        createServer(className, packageName);
        String mapperPath = packageName + Const._MAPPER + className + Const.MAPPER;
        createMapperXml(packageName, mapperPath, className, tableName);
        createController(packageName, className);
        //创建完一张表的数据,将字段集合置空
        tableFields = null;
        fieldMaps = null;
    }

    private static void markerMapperXml(String mapperName, String content, String packageName) throws IOException {
        File f2 = new File(TableToEntityUtils.class.getResource("/").getPath());
        String homePath = f2.getCanonicalPath().replace("\\target\\classes", "");
        String folder = homePath + "/src/main/java/" + packageName.replace(".", "/") + "/mapper/xml/";
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = folder + mapperName;
        try {
            File newjava = new File(fileName);
            FileWriter fw = new FileWriter(newjava);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建实体类文件
     *
     * @param className   类名(不包含.java文件名后缀) 根据表名首字母大写并去掉开头t_和所有下划线-驼峰命名
     * @param content     添加的内容(字段注释等)
     * @param packageName 包名(com.xxx.xxx.xxx)
     */
    public static void markerBean(String className, String content, String packageName) throws Exception {
//      这里不使用System.getProperty("user.dir")了。user.dir是根据运行时环境来的
        File f2 = new File(TableToEntityUtils.class.getResource("/").getPath());
        String homePath = f2.getCanonicalPath().replace("\\target\\classes", "");
        String folder = homePath + "/src/main/java/" + packageName.replace(".", "/") + "/";
        String mapperStr = Const._MAPPER.substring(0, Const._MAPPER.length() - 1);
        String serverStr = Const._SERVER.substring(0, Const._SERVER.length() - 1);
        String controllerStr = Const._CONTROLLER.substring(0, Const._CONTROLLER.length() - 1);
        String entityStr = Const._ENTITY.substring(0, Const._ENTITY.length() - 1);
        if (packageName.lastIndexOf(entityStr) != -1
                || packageName.lastIndexOf(mapperStr) != -1
                || packageName.lastIndexOf(serverStr) != -1
                || packageName.lastIndexOf(controllerStr) != -1) {

            throw new RuntimeException(className + "类创建失败,包名中包含了铭感词汇" + packageName.substring(packageName.lastIndexOf(".") + 1));
        }
        if (className.contains("Mapper")) {
            folder += "mapper/";
            packageName += mapperStr;

        } else if (className.contains("Server")) {
            folder += "server/";
            packageName += serverStr;
            if (className.contains("Impl")) {
                folder += "impl/";
                packageName += Const._SERVER_IMP;
            }

        } else if (className.contains("Controller")) {
            folder += "controller/";
            packageName += controllerStr;
        } else {
            folder += "entity/";
            packageName += entityStr;
        }
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = folder + className + ".java";
        try {
            File newjava = new File(fileName);
            FileWriter fw = new FileWriter(newjava);
            fw.write("package\t" + packageName + ";\r\n");
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去下划线
     *
     * @param string
     * @return
     */
    public static String removeUnderline(String string) {
        StringBuilder columnNameBuilder = new StringBuilder(string);
        if (!string.contains("_")) {
            return string;
        }
        int i = columnNameBuilder.indexOf("_");
        columnNameBuilder.replace(i, i + 1, "").replace(0, 1, columnNameBuilder.substring(0, 1).toUpperCase()).replace(i, i + 1, columnNameBuilder.substring(i, i + 1).toUpperCase());
        return removeUnderline(columnNameBuilder.toString());

    }

    /**
     * 获取表字段信息(列名、类型、注释等)
     *
     * @param dataBaseName
     * @param tableName
     * @return
     */
    private static List<Map> getFiledMaps(String dataBaseName, String tableName) {
        Connection conn = null;
        Statement stmt = null;
        List<Map> tableFieldList = new ArrayList<>();
        try {
            // 注册 JDBC 驱动
            Class.forName(Const.JDBC_DRIVER);
            conn = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql = "SELECT * FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA ='" + dataBaseName + "' AND TABLE_NAME= '" + tableName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            tableFields = new ArrayList<>();
            while (rs.next()) {

                Map<String, String> fieldMap = new HashMap<>();
                String column_name = rs.getString("COLUMN_NAME");
                String column_key = rs.getString("COLUMN_KEY");
                if (column_key.equals(Const.PRI_KEY.substring(1))) {
                    tableFields.add(column_name + Const.PRI_KEY);
                } else {
                    tableFields.add(column_name);
                }
                setFieldName(column_name.toLowerCase(), fieldMap);
                String data_type = rs.getString("DATA_TYPE");
                setFieldType(data_type.toUpperCase(), fieldMap);
                fieldMap.put("fieldComment", rs.getString("COLUMN_COMMENT"));
                tableFieldList.add(fieldMap);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return tableFieldList;
    }

    /**
     * 解析输出属性
     *
     * @return
     */
    private static void processAllAttrs(StringBuffer sb, List<Map> filedMaps) {
        for (int i = 0; i < filedMaps.size(); i++) {
            Map map = filedMaps.get(i);
            String fieldType = MapUtils.getString(map, "fieldType");
            String fieldName = MapUtils.getString(map, "fieldName");
            fieldName = lowerOrUpper(fieldName, true);
            String fieldComment = MapUtils.getString(map, "fieldComment");
            if (StringUtils.isNotBlank(fieldComment)) {
                sb.append("\t/**\r\n").append("\t* ").append(fieldComment).append("\n").append("\t*/\r\n");
            }
            sb.append("\tprivate " + fieldType + " " + fieldName + ";\r\n\r\n");
        }
    }

    private static void setFieldName(String columnName, Map fieldMap) {
        fieldMap.put("fieldName", removeUnderline(columnName));
    }

    private static void setFieldType(String columnType, Map fieldMap) {
        String fieldType = "String";
        if (columnType.equals("INT") || columnType.equals("INTEGER")) {
            fieldType = "Integer";
        } else if (columnType.equals("BIGINT")) {
            fieldType = "Long";
        } else if (columnType.equals("DATETIME")) {
            fieldType = "Date";
        } else if (columnType.equals("TEXT") || columnType.equals("VARCHAR") || columnType.equals("TINYTEXT") || columnType.equals("LONGTEXT")) {
            fieldType = "String";
        } else if (columnType.equals("DOUBLE")) {
            fieldType = "Double";
        } else if (columnType.equals("BIT")) {
            fieldType = "Boolean";
        } else if (columnType.equals("FLOAT")) {
            fieldType = "Float";
        } else if (columnType.equals("DECIMAL")) {
            fieldType = "BigDecimal";
        } else if (columnType.equals("DATE")) {
            fieldType = "Date";
        } else if (columnType.equals("TIMESTAMP")) {
            fieldType = "LocalDateTime";
        } else if (columnType.equals("CHAR")) {
            fieldType = "Char";
        } else if (columnType.equals("JSON")) {//mysql5.7版本才开始有的
            fieldType = "JSONObject";
        }
        fieldMap.put("fieldType", fieldType);
    }
}
