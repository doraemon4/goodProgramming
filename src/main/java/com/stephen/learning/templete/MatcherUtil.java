package com.stephen.learning.templete;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: jack
 * @Date: 2018/9/28 14:29
 * @Description: 使用matcher生成模版
 */
public class MatcherUtil {
    public static String createTemplete(String formatStr) {
        Map<String, String> data = Maps.newHashMap();
        data.put("customerName", "刘明");
        data.put("accountNumber", "888888888");
        data.put("balance", "$1000000.00");
        data.put("amount", "$1000.00");

        String template = "尊敬的客户${customerName}你好！本次消费金额${amount}，"
                + "您帐户${accountNumber}上的余额为${balance}，欢迎下次光临！";

        String regex = formatStr;
        Matcher matcher = Pattern.compile(regex).matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = data.get(key);
            if (StringUtils.isNotBlank(value)) {
                //这句不加的话appendReplacement()这个方法回将$翻译成组
                value = value.replaceAll("\\$", "\\\\\\$");
            } else {
                value = "";
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(createTemplete("\\$|\\{([^}]*)\\}"));
    }
}
