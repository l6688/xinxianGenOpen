package com.xinxian.generator.framework.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StrUtils
 * @Description 字符串工具包
 * @Author lmy
 * @Date 2023/2/2 22:06
 */
public class StrUtils {

    //中文标点符号
    final static String[] chinesePunctuation = {"！", "，", "。", "；", "《", "》", "（", "）", "？", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’"};
    //英文标点符号
    final static String[] englishPunctuation = {"!", ",", ".", ";", "<", ">", "(", ")", "?", "{", "}", "\"", ":", "[", "]", "\"", "\'", "\'"};


    /**
     * 两端去除指定字符
     *
     * @param value        {@link }
     * @param matchContent {@link }
     * @return {@link String}
     */
    public static String trim(String value, char matchContent) {
        int length = value.length();
        char[] val = value.toCharArray();
        int len = length - 1;
        int st = 0;
        while (st < len && val[st] == matchContent) {
            st++;
        }
        while (st < len && val[len] == matchContent) {
            len--;
        }
        return ((st > 0) || (len < length)) ? value.substring(st, len + 1) : value;
    }

    /**
     * 两端去除多个指定字符
     *
     * @param value         {@link String}
     * @param matchContents {@link List<String>}
     * @return {@link String}
     */
    public static String trimMultiple(String value, List<Character> matchContents) {
        String result = value;
        for (Character matchContent : matchContents) {
            result = trim(result, matchContent);
        }
        return result;
    }

    /**
     * 两端增加字符
     * 在text中的所有match两端增加add字符串
     *
     * @param text  {@link String}
     * @param match {@link String}
     * @param end   {@link String}
     * @param add   {@link String}
     * @return {@link String}
     */
    public static String addCharAtBothEnds(String text, String start, String match, String end, String add) {
        String s = start == null ? "" : start;
        String e = end == null ? "" : end;
        return text.replace(s + match + e, s + add + match + add + e);
    }

    /**
     * 获取正则匹配到的集合
     * 在text中的通过regex做正则匹配，把所有指定group返回
     *
     * @param text  {@link String}
     * @param regex {@link String}
     * @param group {@link String}
     * @return {@link String}
     */
    public static List<String> getMatchList(String text, String regex, Integer group, Boolean first) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int matcherStart = 0;
        ArrayList<String> matchList = Lists.newArrayList();
        while (matcher.find(matcherStart)) {
            matchList.add(matcher.group(group));
            matcherStart = matcher.end();
            if (first) {
                break;
            }
        }
        return matchList;
    }

    /**
     * 获取正则匹配到的集合
     * 在text中的通过regex做正则匹配，把所有指定group返回
     *
     * @param text       {@link String}
     * @param regex      {@link String}
     * @param leftGroup  {@link String}
     * @param rightGroup {@link String}
     * @param first      {@link Boolean}
     * @return {@link String}
     */
    public static List<Pair<String, String>> getMatchPairList(String text, String regex, Integer leftGroup, Integer rightGroup, Boolean first) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int matcherStart = 0;
        ArrayList<Pair<String, String>> matchList = Lists.newArrayList();
        while (matcher.find(matcherStart)) {
            matchList.add(Pair.of(matcher.group(leftGroup), matcher.group(rightGroup)));
            matcherStart = matcher.end();
            if (first) {
                break;
            }
        }
        return matchList;
    }

    /**
     * 获取正则匹配到的集合
     * 在text中的通过regex做正则匹配，把所有指定group返回
     *
     * @param text  {@link String}
     * @param regex {@link String}
     * @param first {@link Boolean}
     * @return {@link String}
     */
    public static List<Map<Integer, String>> getMatchMapList(String text, String regex, Boolean first, Integer maxGroup) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int matcherStart = 0;
        ArrayList<Map<Integer, String>> matchList = Lists.newArrayList();
        while (matcher.find(matcherStart)) {
            HashMap<Integer, String> map = new HashMap<>();
            for (int i = 0; i <= maxGroup; i++) {
                map.put(i, matcher.group(i));
            }
            matchList.add(map);
            matcherStart = matcher.end();
            if (first) {
                break;
            }
        }
        return matchList;
    }

    /**
     * 获取首个正则匹配到结果
     * 在text中的通过regex做正则匹配，把所有指定group返回
     *
     * @param text  {@link String}
     * @param regex {@link String}
     * @param group {@link String}
     * @return {@link String}
     */
    public static String getFirstMatchList(String text, String regex, Integer group) {
        List<String> matchList = getMatchList(text, regex, group, true);
        return CollectionUtils.isEmpty(matchList) ? null : matchList.get(0);
    }

    /**
     * 正则替换
     * 在text中的通过pattern做正则匹配，把指定group替换为replacement
     *
     * @param text        {@link String}
     * @param regex       {@link String}
     * @param group       {@link String}
     * @param replacement {@link String}
     * @return {@link String}
     */
    public static String regexReplace(String text, String regex, Integer group, String replacement) {
        replacement = replacement == null ? "" : replacement;
        List<String> matchList = getMatchList(text, regex, group, false);
        for (String s : matchList) {
            text = text.replace(s, replacement);
        }
        return text;
    }

    /**
     * 正则替换
     * 在text中的通过regex做正则匹配，把指定group替换为replaceGroup内容
     *
     * @param text         {@link String}
     * @param regex        {@link String}
     * @param group        {@link String}
     * @param replaceGroup {@link String}
     * @return {@link String}
     */
    public static String regexReplace(String text, String regex, Integer group, Integer replaceGroup, String start, String end) {
        List<Pair<String, String>> matchPairList = getMatchPairList(text, regex, group, replaceGroup, false);
        for (Pair<String, String> pair : matchPairList) {
            text = text.replace(pair.getLeft(), (start == null ? "" : start) + pair.getRight() + (end == null ? "" : end));
        }
        return text;
    }

    /**
     * 转驼峰
     * symbol:- _
     *
     * @param text {@link String}
     * @return {@link String}
     */
    public static String toCamelCase(String text) {
        if (text.contains("_")) {
            text = StrUtil.toCamelCase(text, '_');
        }
        if (text.contains("-")) {
            text = StrUtil.toCamelCase(text, '-');
        }
        return text;
    }

    /**
     * 中文标点符号转英文字标点符号
     *
     * @param str {@link String}
     * @return {@link String}
     */
    public static final String chinesePunctuationToEnglish(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        for (int i = 0; i < chinesePunctuation.length; i++) {
            str = str.replaceAll(chinesePunctuation[i], englishPunctuation[i]);
        }
        return str;
    }

    public static void main(String[] args) {
        String s = toPinyinUnderlineCase("权限");
        System.out.println(s);
    }

    /**
     * 获取文本中成对出现的字符结束位置
     *
     * @param text  {@link String}
     * @param begin {@link Character}
     * @param end   {@link Character}
     * @return {@link Integer}
     */
    public static Integer getEndIndex(String text, Character begin, Character end) {
        Stack stack = new Stack();
        Integer index = text.indexOf(begin + "");
        for (char c : text.substring(index).toCharArray()) {
            index++;
            if (Objects.equals(begin, c)) {
                stack.push(begin);
            } else if (Objects.equals(end, c)) {
                stack.pop();
            }
            if (stack.isEmpty()) {
                break;
            }
        }
        return index;
    }

    /**
     * 通过开始正则和结束正则列表获取第一个匹配的文本
     *
     * @param text          {@link String}
     * @param regexPairList {@link List}
     * @return {@link String}
     */
    public static String getFirstRegexContentByPairList(String text, List<Pair<String, String>> regexPairList) {
        for (Pair<String, String> regexPair : regexPairList) {
            String firstRegexContent = getFirstRegexContent(text, regexPair.getLeft(), regexPair.getRight());
            if (StringUtils.isNotEmpty(firstRegexContent)) {
                return firstRegexContent;
            }
        }
        return null;
    }

    /**
     * 通过开始正则和结束正则获取第一个匹配的文本
     *
     * @param text       {@link String}
     * @param startRegex {@link String}
     * @param endRegex   {@link String}
     * @return {@link String}
     */
    public static String getFirstRegexContent(String text, String startRegex, String endRegex) {
        List<String> contentList = getRegexContentList(text, startRegex, endRegex, true);
        if (CollectionUtils.isNotEmpty(contentList)) {
            return contentList.get(0);
        }
        return null;
    }

    /**
     * 通过开始正则和结束正则获取匹配的文本
     * 正则中不嫩含有[]可选项
     *
     * @param text       {@link String}
     * @param startRegex {@link String}
     * @param endRegex   {@link String}
     * @param getFirst   {@link Boolean}
     * @return {@link List<String>}
     */
    public static List<String> getRegexContentList(String text, String startRegex, String endRegex, Boolean getFirst) {
        Pattern startPattern = Pattern.compile(startRegex);
        Pattern endPattern = Pattern.compile(endRegex);
        Matcher startMatcher = startPattern.matcher(text);
        ArrayList<String> resultList = Lists.newArrayList();
        while (startMatcher.find()) {
            String textPart = text.substring(startMatcher.end());
            Matcher endMatcher = endPattern.matcher(textPart);
            if (endMatcher.find()) {
                String match = text.substring(startMatcher.start(), startMatcher.end() + endMatcher.end());
                resultList.add(match);
                if (getFirst) {
                    break;
                }
            }
        }
        return resultList;
    }

    /**
     * 是否中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (PinyinUtil.isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转
     *
     * @param str
     * @return
     */
    public static String toPinyinUnderlineCase(String str) {
        char[] chars = str.toCharArray();
        ArrayList<String> list = Lists.newArrayList();
        for (char aChar : chars) {
            String pinyin = PinyinUtil.getPinyin(String.valueOf(aChar));
            list.add(pinyin.toUpperCase());
        }
        //权限 QUAN_XIAN
        String result = StrUtil.join("_", list);
        return result;
    }

    /**
     * 替换最后一个字符
     *
     * @param text        {@link String}
     * @param regex       {@link String}
     * @param replacement {@link String}
     * @return {@link String}
     */
    public static String replaceLast(String text, String regex, String replacement) {
        String newText = StrUtil.reverse(text);
        newText = newText.replaceFirst(regex, replacement);
        newText = StrUtil.reverse(newText);
        return newText;
    }

    /**
     * 字符串切分为集合
     *
     * @param str       {@link String}
     * @param separator {@link String}
     * @return {@link List<String>}
     */
    public static List<String> strToList(String str, String separator) {
        return StringUtils.isEmpty(str) ? null : Arrays.asList(str.split(separator));
    }

    /**
     * 字符串切分为集合,默认逗号为分隔符
     *
     * @param str {@link String}
     * @return {@link List<String>}
     */
    public static List<String> strToList(String str) {
        return strToList(str, ",");
    }

    /**
     * 集合字符串转json字符串
     *
     * @param str {@link String}
     * @return {@link List<String>}
     */
    public static String listStrToJsonStr(String str) {
        return StringUtils.isEmpty(str) ? null : JSON.toJSONString(StrUtils.strToList(str));
    }
}
