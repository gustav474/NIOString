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
    private List<String> fileExtensions = null;

    private final String delimiter = "-";
    private final String replacementMark = "*";

    public void parse() {
        parseFromDir();
    }

    private void parseFromDir() {
        List<Path> filesInDirList = null;
        try(Stream<Path> filesInDirStream = Files.list(fromDir.toPath())) {
            filesInDirList = filesInDirStream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO Выводим через стрим данные из файла построчно

        List<Path> paths = null;
        try {
            paths = getTextFilesForParsing(filesInDirList);
            for (Path path : paths) {
                cleanup(path);
                getTextByPath2(path);
            }
        } catch (CantFindAnyTextFilesForParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isTextFile(Path path) {
        int index = path.toString().lastIndexOf('.');
        if (index != -1) {
            String extension = path.toString().substring(index + 1);
            for (String ext : fileExtensions) {
                if (extension.equals(ext)) return true;
            }
        }
        return false;
    }

//TODO тест для проверки исключения файла с неподходящим расширением из списка для парсинга
    private List<Path> getTextFilesForParsing(List<Path> paths) throws CantFindAnyTextFilesForParseException{
        for (int i = 0; i < paths.size(); i++) {
            if(isTextFile(paths.get(i)) != true) {
                paths.remove(paths.get(i));
            }
        }

        if (paths.size() == 0) throw new CantFindAnyTextFilesForParseException("Can't find any text files for parse in directory");
//        List<Path> justTextFiles = null;
//        if(isTextFile(path) == true) {
//            justTextFiles.add(path);
//        }
        return paths;
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

//                        System.out.println(dic);
                        List<String> words = Arrays.asList(dic.split(delimiter));
                        System.out.println(words);
                        for (String word : words) {
                            System.out.println(word);
                            Integer index = str.indexOf(word);
                            while (index != -1) {
                                str.replace(index, index + word.length(), new String(new char[word.length()]).replace("\0", replacementMark));
                                index = str.indexOf(word, index);
//                                System.out.println(str.toString());
                            }
                        }
                    }
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
