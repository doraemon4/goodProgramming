package com.stephen.learning.parse;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @Auther: jack
 * @Date: 2018/9/6 23:25
 * @Description:
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        String filePath="/Users/jack/IdeaProjects/goodProgramming/src/main/resources/user_profile.html";
        try{
            Document doc = Jsoup.parse(new File(filePath),"utf-8");
            System.out.println(JsoupParserUtils.
                    getXpathString(doc,"//*[@id=\"contact-info\"]//div[@title=\"Mobile\"]//span/span"));
            System.out.println(JsoupParserUtils.getXpathString(doc,"//*[@id=\"basic-info\"]//div[@title=\"Gender\"]/div/div[1]/text()"));
            System.out.println(JsoupParserUtils.getXpathString(doc,"//*[@id=\"basic-info\"]//div[@title=\"Religious Views\"]/div/div[1]/a"));
            System.out.println(JsoupParserUtils.getXpathString(doc,"//*[@id=\"basic-info\"]//div[@title=\"Political Views\"]/div/div[1]/text()"));
            System.out.println(JsoupParserUtils.getXpathString(doc,"//*[@id=\"contact-info\"]//div[@title=\"Websites\"]/div/div/a"));
            System.out.println(JsoupParserUtils.getXpathString(doc,"//*[@id=\"quote\"]/div/div"));

            Elements elements=JsoupParserUtils.getJsoupElements(doc,"//*[@id=\"education\"]//div[@class=\"ib cc experience\"]");
            elements.stream().forEach(element -> {
                System.out.println(JsoupParserUtils.getXpathString(element,"//div[@class=\"ib cc experience\"]//div[@class=\"_2pik\"]/span/a"));
                System.out.println(JsoupParserUtils.getXpathString(element,"//div[@class=\"ib cc experience\"]//span[@class=\"_52jc _52ja\"]"));
                System.out.println(JsoupParserUtils.getXpathString(element,"//div[@class=\"ib cc experience\"]//span[@class=\"_52jc _52j9\"]"));
            });

            elements=JsoupParserUtils.getJsoupElements(doc,"//*[@id=\"work\"]//div[@class=\"ib cc experience\"]");
            elements.stream().forEach(element -> {
                System.out.println(JsoupParserUtils.getXpathString(element,"//div[@class=\"ib cc experience\"]//div[@class=\"_2pik\"]/span/a"));
                System.out.println(JsoupParserUtils.getXpathString(element,"//div[@class=\"ib cc experience\"]//span[@class=\"_52jc _52ja\"]"));
                System.out.println(JsoupParserUtils.getXpathListString(element,"//div[@class=\"ib cc experience\"]//span[@class=\"_52jc _52j9\"]/text()"));

            });

        }catch (Exception e){
            log.info(e.getMessage());
        }

        System.out.println(Pattern.compile("[^0-9]").matcher("class of 2018").replaceAll(""));

    }

}
