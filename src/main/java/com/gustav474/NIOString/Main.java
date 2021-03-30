package com.gustav474.NIOString;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        File toDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/toDir");
        File fromDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/fromDir");
        File dict = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/dict.txt");

        Parser parser = new Parser(toDir, fromDir, dict);
        parser.parse();
    }
}
