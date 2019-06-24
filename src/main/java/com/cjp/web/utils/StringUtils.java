package com.cjp.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Auther: junping.chi@luckincoffee.com
 * @Date: 2019/5/24 15:19
 * @Description: 丰富org.apache.commons.lang3.StringUtils 工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 判断是否包含（不区分大小写）
     *
     * @param inputStr
     * @param childStr
     * @return
     */
    public static boolean containsIgnorecase(String inputStr, String childStr) {
        return inputStr.toUpperCase().contains(childStr.toUpperCase());
    }

    /**
     * 验证字符串是否为组合英文
     *
     * @param inputStr
     * @return
     */
    public static boolean isEnglishChcar(String inputStr) {
        //“\w”表示[A-Z0-9]，“\w+”表示由数字、26个英文字母或者下划线组成的字符串
        String reg = "\\w+";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(inputStr);
        boolean matches = m.matches();
        return matches;
    }

    /**
     * 获取倒数第二个单词
     *
     * @param inputStr
     * @return
     */
    public static String wordOfCountdownSecond(String inputStr) {
        return wordByCountdownIndex(inputStr, 2);
    }

    /**
     * 获取倒数第二个单词
     *
     * @param inputStr
     * @return
     */
    public static String wordOfCountdownFirst(String inputStr) {
        return wordByCountdownIndex(inputStr, 1);
    }

    /**
     * 获取倒数第 countdownIndex 个单词
     *
     * @param inputStr
     * @return
     */
    public static String wordByCountdownIndex(String inputStr, int countdownIndex) {
        if (org.apache.commons.lang3.StringUtils.isBlank(inputStr)) {
            return null;
        }
        String[] wordArr = inputStr.trim().split(" ");
        List<String> wordList = Arrays.asList(wordArr);
        wordList = wordList.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());
        if (wordList.size() >= countdownIndex) {
            return wordList.get(wordList.size() - countdownIndex);
        }
        return null;
    }


    /**
     * 判断字符串childStr 在 字符串fullStr中出现的次数
     *
     * @param childStr
     * @param fullStr
     * @return
     */
    public int numOfOccur(String childStr, String fullStr) {
        Pattern p = Pattern.compile(childStr, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fullStr);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * 判断字符串childStr 在 字符串fullStr中出现的次数（忽略大小写）
     *
     * @param childStr
     * @param fullStr
     * @return
     */
    public static int numOfOccurIgnorecase(String childStr, String fullStr) {
        Pattern p = Pattern.compile(childStr.toUpperCase(), Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fullStr.toUpperCase());
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * 获取字符串中双字节字符的集合
     *
     * @param str
     * @return
     */
    public static List<String> findDoubleByteCharacters(String str) {
        List<String> dbcsList = new ArrayList<>();
        //双字节正则表达式
        String reg = "[^\\x00-\\xff]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        while (m.find()) {
            dbcsList.add(m.group());
        }
        return dbcsList;
    }

    /**
     * 获取首个字符前,空格的数量
     *
     * @param str
     * @return
     */
    public static int numOfLeftBlank(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isSpaceChar(str.charAt(i))) {
                break;
            }
            count++;
        }
        return count;
    }

    /**
     * 右侧trim
     *
     * @param str
     * @return
     */
    public static String trimRight(String str) {
        char[] chars = str.toCharArray();
        int len = chars.length;
        int st = 0;
        while ((st < len) && (chars[len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < chars.length)) ? str.substring(st, len) : str;
    }

    /**
     * 左侧trim
     *
     * @param str
     * @return
     */
    public static String trimLeft(String str) {
        char[] chars = str.toCharArray();
        int len = chars.length;
        int st = 0;
        while ((st < len) && (chars[st] <= ' ')) {
            st++;
        }
        return ((st > 0) || (len < chars.length)) ? str.substring(st, len) : str;
    }


    public static void main(String[] args) {
        String ss = "asdd,  sd  dfsdf ";
        String s = wordByCountdownIndex(ss, 3);
        System.out.println(s);
    }
}
