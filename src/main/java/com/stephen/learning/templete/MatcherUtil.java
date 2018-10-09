package com.stephen.learning.templete;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: jack
 * @Date: 2018/9/28 14:29
 * @Description: 使用matcher生成模版
 */
public class MatcherUtil {
    /**
     *
     * @param regex  正则表达式
     * @return
     */
    public static String createTemplete(String regex) {
        Map<String, String> data = Maps.newHashMap();
        data.put("customerName", "刘明");
        data.put("accountNumber", "888888888");
        data.put("balance", "$1000000.00");
        data.put("amount", "$1000.00");

        String template = "尊敬的客户${customerName}你好！本次消费金额${amount}，"
                + "您帐户${accountNumber}上的余额为${balance}，欢迎下次光临！";

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

    public static String createTemplete(){
        Map<String, String> data = Maps.newHashMap();
        data.put("customerName", "刘明");
        data.put("accountNumber", "888888888");
        data.put("balance", "$1000000.00");
        data.put("amount", "$1000.00");

        String template = "尊敬的客户${customerName}你好！本次消费金额${amount}，"
                + "您帐户${accountNumber}上的余额为${balance}，欢迎下次光临！";
        return replace(template,data,"${","}",true);
    }

    public static String replace(String source,Map<String, String> parameter,String prefix, String suffix,boolean enableSubstitutionInVariables){
        //StrSubstitutor不是线程安全的类
        StringSubstitutor strSubstitutor = new StringSubstitutor(parameter,prefix, suffix);
        //是否在变量名称中进行替换
        strSubstitutor.setEnableSubstitutionInVariables(enableSubstitutionInVariables);
        return strSubstitutor.replace(source);
    }

    public static void main(String[] args) {
        System.out.println(createTemplete("\\$|\\{([^}]*)\\}"));
        System.out.println(createTemplete());
    }
}
