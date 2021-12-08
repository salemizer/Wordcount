package com.wordcount;

/*
  @author Ahmed Salem
 */

public class Util {

    // Regex removes special chars
    static String removeSpecialChars(String line){

        line = line.replaceAll("[$+,:;=?@#|'<>.^*()%!-]", " ");
//        System.out.println(line);
        return line;
    }
}
