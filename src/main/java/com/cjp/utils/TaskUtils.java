package com.cjp.utils;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import sun.rmi.rmic.iiop.DirectoryLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: junping.chi@luckincoffee.com
 * @Date: 2019/5/28 16:25
 * @Description:
 */
public class TaskUtils {
    //读取外部xml（或其他配置）生成一个task
    public static void main(String[] args) throws Exception {
        createStudentByFile();
    }

    private static void createStudentByFile() throws Exception {
        String studentString = "" +
                "package com.lucky.dw.utils;\n" +
                "\n" +
                "import com.lucky.dw.core.DwdEtlSyncTask;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "/**\n" +
                " * @Description:部门维快照表任务\n" +
                " * @Author: chenli\n" +
                " * @Date: 2019-05-07 14:37\n" +
                " */\n" +
                "public class DictionaryDiffTask extends DwdEtlSyncTask {\n" +
                "\n" +
                "    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryDiffTask.class);\n" +
                "\n" +
                "    private String date;\n" +
                "\n" +
                "    private String selectSql = \"${sql_defind}\";\n" +
                "\n" +
                "    public DictionaryDiffTask(String jobId, String date) {\n" +
                "        super(jobId);\n" +
                "        this.date = date;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void doTask() throws Exception {\n" +
                "        final String resultSql = selectSql\n" +
                "                .replace(\"${target_date}\", date)\n" +
                "                .replace(\"${dw_create_time}\", getDwCreateTime())\n" +
                "                .replace(\"${dw_program}\", getDwProgram());\n" +
                "        sparkSession.sql(resultSql).collect();\n" +
                "    }\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        LOGGER.info(\"jobId:{}, date:{}\", args[0], args[1]);\n" +
                "        new DictionaryDiffTask(args[0], args[1]).call();\n" +
                "    }\n" +
                "}\n";

        String replaceStr = studentString.replace("${sql_defind}", "insert overwrite table dw_dim.dim_dictionary_err select src_db_name ,src_tab_name,src_col_name,'${dw_create_time}' as dw_create_time,'${dw_program}'  as dw_program from dw_dim.dim_dictionary_diff limit 100");

        //===================根据String语句生成.java源码文件，并存放在指定位置
        //String fileName = System.getProperty("user.dir") + "/src/main/java/com/lucky/dw/utils/DictionaryDiffTask.java";
        String fileName = "C:\\Users\\admin\\IdeaProjects\\spring-boot-data-jpa-easyui-datagrid/src/main/java/com/lucky/dw/utils/DictionaryDiffTask.java";
        FileUtils.touch(new File(fileName));
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(replaceStr);
        fileWriter.flush();
        fileWriter.close();
        //===================根据String语句生成.java源码文件，并存放在指定位置

        //===================编译生成.class文件，并存放在指定位置
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(fileName);

        //options就是指定编译输入目录，与我们命令行写javac -d C://是一样的
        //String dest = System.getProperty("user.dir") + "/target/classes";
        String dest = "C:\\Users\\admin\\IdeaProjects\\spring-boot-data-jpa-easyui-datagrid\\target\\classes";
        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(dest);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, options, null, javaFileObjects);
        task.call();
        manager.close();
        //===================编译生成.class文件，并存放在指定位置

        //===================使用“类加载器——目录加载器”加载指定目录下的.class文件
        DirectoryLoader directoryLoader = new DirectoryLoader(new File("C:\\Users\\admin\\IdeaProjects\\spring-boot-data-jpa-easyui-datagrid\\target\\classes"));
        Class<?> clazz = directoryLoader.loadClass("com.lucky.dw.utils.DictionaryDiffTask");
        /*URL[] urls = new URL[]{new URL("file:/" + dest)};
        ClassLoader classLoader = new URLClassLoader(urls);
        Class<?> clazz = classLoader.loadClass("com.lucky.dw.utils.DictionaryDiffTask");*/
        //===================使用“类加载器——目录加载器”加载指定目录下的.class文件

        //===================hadoop权限验证
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        System.setProperty("spark.master", "local[32]");
        String userName = "li.chen";
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser(userName);
        ugi.addToken(new Text(userName),
                new Token(userName.getBytes(), "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoibGkuY2hlbiIsImF0eSI6InVzciIsImVudiI6InRlc3QiLCJpc3MiOiJsdWNraW5jb2ZmZWUiLCJzdWIiOiJvbmVkYXRhIiwiZXhwIjoyOTM1MDY1NjAwMH0.HBbX1JWuI40JTaDIgEgLi8mQEKvRsq6Huzmc9-WYeB8".getBytes(),
                        new Text("luckin"),
                        new Text()));
        UserGroupInformation.setLoginUser(ugi);
        //===================hadoop权限验证

        //===================反射调用clazz的指定方法——main方法
        String curDate = DateUtil.dateToString(new Date(), DateUtil.YYYY_MM_DD);
        Constructor<?> constructor = clazz.getConstructor(String.class, String.class);//(byte[].class
        Object instance = constructor.newInstance("jobid_sssss", "2019-23-23");
        //直接java反射得到方法
        Method method = clazz.getMethod("main", String[].class);
        Object obj1 = method.invoke(instance, new Object[]{new String[]{"DictionaryDiffTask", curDate}});
        System.out.println("测试：" + obj1);
        //===================反射调用clazz的指定方法——main方法
    }

}
