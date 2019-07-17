package com.nenu.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static void main(String[] args) {
        String regex="^[0-9].*$";
        String s = "12sfsdfsdf";
        String ss = "asdasdas";
        String ssss = " asdas";
        String sss = "123";
        System.out.println(ss.substring(1));

        System.out.println(ss.contains(sss));
        System.out.println(ss.contains(ssss));

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(s);
        System.out.println(p.matcher(s).matches());
        System.out.println(p.matcher(ss).matches());
        System.out.println(p.matcher(sss).matches());

    }
}
