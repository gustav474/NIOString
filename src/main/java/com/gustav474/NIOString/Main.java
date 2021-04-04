package com.gustav474.NIOString;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File toDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/toDir");
        File fromDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/fromDir");
        File dict = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/dict.txt");

        List<String> fileExtensionsForCheck = Arrays.asList(new String[] {"txt"});

        try {
            Parser parser2 = new Parser(toDir, fromDir, dict, fileExtensionsForCheck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(fromDir.getParent());
        Parser parser = null;
        try {
            parser = new Parser(fromDir, dict);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            parser.parse();
        } catch (CantFindAnyTextFilesForParseException e) {
            e.printStackTrace();
        }
    }
}
