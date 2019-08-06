package com.cjp.web.utils;


import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date: 2019/5/22 18:03
 * @Description: sql格式化工具类
 */
@Component
public class SqlFormatUtils {

    private static final String SPACE_1 = " ";
    private static final String SPACE_4 = SPACE_1 + SPACE_1 + SPACE_1 + SPACE_1;
    private static final String TAB_1 = "\t";
    private static final String NEWLINE_CHAR = " \n";
    private static final String COMMA = ",";
    private static final String AS_SMALL = "as";
    /**
     * " as " 两测的空格不可去除，确保as 是独立单词，不是某个单词的一部分/**
     */
    private static final String AS_CAP_SPACE = SPACE_1 + "AS" + SPACE_1;
    /**
     * " as " 两测的空格不可去除，确保as 是独立单词，不是某个单词的一部分
     */
    private static final String AS_SMALL_SPACE = SPACE_1 + "as" + SPACE_1;

    private static final String FROM = SPACE_1 + "from" + SPACE_1;
    private static final String SELECT = SPACE_1 + "select" + SPACE_1;
    private static final String INSERT = SPACE_1 + "insert" + SPACE_1;
    private static final String WHERE = SPACE_1 + "where" + SPACE_1;
    private static final String ORDERBY = SPACE_1 + "order by" + SPACE_1;
    private static final String GROUPBY = SPACE_1 + "group by" + SPACE_1;
    private static final String HAVING = SPACE_1 + "having" + SPACE_1;
    private static final String into = SPACE_1 + "into" + SPACE_1;
    private static final String overwrite = SPACE_1 + "overwrite" + SPACE_1;
    private static final String table = SPACE_1 + "table" + SPACE_1;


    /**
     * 格式化sql 的返回列表
     *
     * @param sql select 和 from 之间的 sql 语句
     * @return
     * @throws Exception
     */
    public String formatSelectColumns(String sql) {
        StringBuffer strbuf;
        BufferedReader br = null;
        String rsColumnsStr = "";
        try {
            //
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sql.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            //
            ArrayList<Integer> columnSourceLengthList = new ArrayList<>();
            ArrayList<String> columnSourceList = new ArrayList<>();
            ArrayList<String> columnAsList = new ArrayList<>();

            //===========================开始：逐行读取数据，装载每行长度集合 、原始字段集合、别名字段集合
            strbuf = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                //去除右侧空格，并替换小写关键字as 成大写
                line = StringUtils.trimRight(line).replace(AS_SMALL_SPACE, AS_CAP_SPACE)
                        .replace("\t", SPACE_4);
                //获取当前行倒数第二个单词
                String wordCountdownSecond = StringUtils.wordOfCountdownSecond(line);
                String wordOfCountdownFirst = StringUtils.wordOfCountdownFirst(line);
                if (",".equalsIgnoreCase(wordOfCountdownFirst)) {
                    wordOfCountdownFirst = StringUtils.wordByCountdownIndex(line, 2);
                    wordCountdownSecond = StringUtils.wordByCountdownIndex(line, 3);
                }
                //是select返回列，且有别名时(判断倒数第二个单词是否为关键字AS)
                if (wordCountdownSecond != null
                        //倒数第2个单词必须为as 关键字
                        && wordCountdownSecond.equalsIgnoreCase(AS_SMALL)
                        //倒数第1个不能带标点符号
                        && !wordOfCountdownFirst.endsWith(")")
                ) {
                    //最后一个AS的index
                    int indexOfAs = line.lastIndexOf(AS_CAP_SPACE);
                    //获取原始字段
                    String colSource = StringUtils.trimRight(line.substring(0, indexOfAs));
                    //获取字段别名
                    String colAs = StringUtils.trimRight(line.substring(indexOfAs + 3));
                    //装载原始字段长度、原始字段、别名字段，的集合
                    columnSourceLengthList.add(colSource.length());
                    columnSourceList.add(colSource);
                    columnAsList.add(colAs);
                } else {
                    //无别名时
                    //装载原始字段长度、原始字段、别名字段，的集合
                    //无as 字段别名时，往集合添加空
                    columnAsList.add("");
                    //无as 字段别名时，往集合添加当前行完整字符串
                    columnSourceList.add(line);
                    //无as 字段别名时，往集合添加当前行完整字符串长度
                    columnSourceLengthList.add(line.length());
                }
            }
            //===========================结束：逐行读取数据，装载每行长度集合 、原始字段集合、别名字段集合
            //获取除去as 别名后，最长行的长度
            Integer max = 0;
            if (columnSourceLengthList.size() > 0) {
                max = Collections.max(columnSourceLengthList);
            }
            //==========开始：格式化数据——空格，追加别名，缩进
            for (int i = 0; i < columnSourceList.size(); i++) {
                //获取该行的列，及列别名
                String columnSource = columnSourceList.get(i);
                String columnAs = columnAsList.get(i);
                //判断改行有几个双字节字符
                List<String> dbcsList = StringUtils.findDoubleByteCharacters(columnSource);
                int dbcsSize = dbcsList.size();
                //计算需要缩进的空格（用于右侧对其）
                //当前行右移6字符，减去列的字符长度，减去双字符数量
                int imax = max + 6 - (columnSource.length() + dbcsSize);
                //columnSource 追加空格
                for (int j = 1; j < imax; j++) {
                    columnSource += SPACE_1;
                }
                //columnSource 追加别名
                if (StringUtils.isNotBlank(columnAs)) {
                    columnSource = columnSource + AS_CAP_SPACE + columnAs.trim();
                }
                //追加换行符
                strbuf.append(columnSource + NEWLINE_CHAR);
                //结果str
                rsColumnsStr = strbuf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(br);
        }
        return rsColumnsStr;
    }


    public static void main(String[] args) {
        String reg = "[^\\x00-\\xff]";
        String www = "sss你好啊ddd buha 不好啊，";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(www);
        while (m.find()) {
            System.out.println(m.group());
        }
    }

}
