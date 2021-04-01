package com.gustav474.NIOString;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File toDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/toDir");
        File fromDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/fromDir");
        File dict = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/dict.txt");

        List<String> fileExtensionsForCheck = Arrays.asList(new String[] {"aaaa"});

        Parser parser = new Parser(toDir, fromDir, dict, fileExtensionsForCheck);
        parser.parse();
    }
}
