package com.gustav474.NIOString;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class Parser {
    private File toDir = null;
    private File fromDir = null;
    private File dict = null;

    private final String delimiter = "-";
    private final String replacementMark = "*";

    public void parse() {
        parseFromDir();
    }

    private void parseFromDir() {
        //TODO сделать кастомное Exception если нет ни одного текестового файла

        List<Path> filesInDirList = null;
        try(Stream<Path> filesInDirStream = Files.list(fromDir.toPath())) {
            filesInDirList = filesInDirStream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(filesInDir);

        //TODO Выводим через стрим данные из файла построчно
        for (Path path : filesInDirList) {
            System.out.println(path.getFileName());
            cleanup(path);
            getTextByPath2(path);
        }

    }

    private void getTextByPath (Path path) {
        try {
            Stream<String> list = Files.lines(path);
            Stream<String> dict = Files.lines(this.dict.toPath());

            list
                    //TODO слово может быть вхождением в другое
                    //TODO брать слова из словаря
                    .map(str -> str.replace(" " + "и" + " ", " " + "*" + " "))
//                    .collect(Collectors.toList())
                    //TODO складывать все это в другой файл, а не печатать
                    .forEach(str -> {
                        try {
                            //TODO записывать только один раз в файл, если запускать парсер еще раз, дублирование текста в файле не происходит
                            Files.write(Paths.get(toDir.toString(), path.getFileName().toString()), Collections.singleton(str), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
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
        try(BufferedReader text = Files.newBufferedReader(path)) {
            while(true) {
                String line = text.readLine();
                System.out.println(line);
                if (line == null) break;
                StringBuilder str = new StringBuilder(line);

                try(BufferedReader dict = Files.newBufferedReader(this.dict.toPath())) {
                    while(true) {
                        String dic = dict.readLine();
                        if (dic == null) break;

                        System.out.println(dic);
                        //TODO парсим слова из полученной строки
                        List<String> words = Arrays.asList(dic.toString().split(delimiter));
                        System.out.println(words);

                        for (String word : words) {
                            Integer index = str.indexOf(word);
                            while (index != -1) {
                                str.replace(index, index + word.length(), new String(new char[word.length()]).replace("\0", replacementMark));
                                index = str.indexOf(word, index);
                                System.out.println(str.toString());
                            }
                        }
                    }
//
//                    //TODO записывать только один раз в файл, если запускать парсер еще раз, дублирование текста в файле не происходит
                }
                Files.write(Paths.get(toDir.toString(), path.getFileName().toString()), Collections.singleton(str), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //TODO не создавать файл если он не текстовый (В одном месте должна быть проверка)
    private void cleanup (Path path) {
        try {
            Files.delete(Paths.get(toDir.toString(), path.getFileName().toString()));
            Files.createFile(Paths.get(toDir.toString(), path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
