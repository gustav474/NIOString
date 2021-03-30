package com.gustav474.NIOString;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class Parser {
    //TODO ? передавать инстансы Files or Path вместо стринга
    private Path pathToDir = null;
    private Path pathFromDir = null;
    private Path pathToDict = null;
    private final String delimiter = " - ";

    //Start parsing
    public void parse() {
        parseFromDir();
    }

    private void parseFromDir() {
        //TODO собрать инфу о файлах в этой директории
        //TODO сделать кастомное Exception если нет ни одного текестового файла
        List<Path> filesInDir = null;
        try {
            filesInDir = Files.list(pathFromDir).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filesInDir);

        //TODO Выводим через стрим данные из файла построчно
        for (Path path : filesInDir) {
            cleanup(path);
            getTextByPath2(path);
        }

    }

    private void getTextByPath (Path path) {
        try {
            Stream<String> list = Files.lines(path);
            Stream<String> dict = Files.lines(pathToDict);

            list
                    //TODO слово может быть вхождением в другое
                    //TODO брать слова из словаря
                    .map(str -> str.replace(" " + "и" + " ", " " + "*" + " "))
//                    .collect(Collectors.toList())
                    //TODO складывать все это в другой файл, а не печатать
                    .forEach(str -> {
                        try {
                            //TODO записывать только один раз в файл, если запускать парсер еще раз, дублирование текста в файле не происходит
                            Files.write(Paths.get(pathToDir.toString(), path.getFileName().toString()), Collections.singleton(str), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
//            System.out.println(pathToDir.toString() + path.getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getTextByPath2 (Path path) {
        try {
            BufferedReader text = Files.newBufferedReader(path);
//            BufferedReader dict = Files.newBufferedReader(pathToDict);

            while(true) {
                String line = text.readLine();
                System.out.println(line);
                if (line == null) break;
                StringBuilder str = new StringBuilder(line);

                BufferedReader dict = Files.newBufferedReader(pathToDict);
                String dic = dict.readLine();
                if (dic != null) {

                    System.out.println(dic);
                    //TODO парсим слова из полученной строки
                    List<String> words = Arrays.asList(dic.toString().split(delimiter));
//                    System.out.println(words);

                    for (String word : words) {
                        Integer index = str.indexOf(word);
                        while(index != -1) {
                            str.replace(index, index + word.length(), new String(new char[word.length()]).replace("\0", "*"));
                            index = str.indexOf(word, index);
                            System.out.println(str.toString());
                        }
                    }

                    //TODO записывать только один раз в файл, если запускать парсер еще раз, дублирование текста в файле не происходит
//                    try {
                        Files.write(Paths.get(pathToDir.toString(), path.getFileName().toString()), Collections.singleton(str), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void cleanup (Path path) {
        try {
            Files.delete(Paths.get(pathToDir.toString(), path.getFileName().toString()));
            Files.createFile(Paths.get(pathToDir.toString(), path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
