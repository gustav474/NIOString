package com.gustav474.NIOString;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        //TODO как правильно брать пути?
        //TODO проверка переданного пути на существование
        Path pathToDir = Paths.get("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/toDir");
        Path pathFromDir = Paths.get("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/fromDir");
        Path pathToDict = Paths.get("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/main/resources/dict.txt");

        Parser parser = new Parser(pathToDir, pathFromDir, pathToDict);
        parser.parse();
    }
}
