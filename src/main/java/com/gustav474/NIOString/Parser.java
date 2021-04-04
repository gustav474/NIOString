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
public class Parser {
    private File toDir = null;
    private File fromDir = null;
    private File dict = null;
    private List<String> fileExtensions = Arrays.asList(new String[] {"txt"});  //Default file extensions
    private final String delimiter = "-";
    private final String replacementMark = "*";

    /**
     * Constructor for all params
     * @param toDir
     * @param fromDir
     * @param dict
     * @param fileExtensions
     * @throws FileNotFoundException
     */
    public Parser(File toDir, File fromDir, File dict, List<String> fileExtensions) throws FileNotFoundException{
        if (!Files.exists(fromDir.toPath())) throw new FileNotFoundException("Directory with files for parsing does not exists");
        if (!Files.exists(toDir.toPath())) throw new FileNotFoundException("Directory for parsing results does not exists");
        if (!Files.exists(dict.toPath())) throw new FileNotFoundException("Dictionary file does not exists");

        this.toDir = toDir;
        this.fromDir = fromDir;
        this.dict = dict;
        this.fileExtensions = fileExtensions;
    }

    /**
     * Constructor for creating a parser with default file extensions for parse
     * @param fromDir
     * @param toDir
     * @param dict
     * @throws FileNotFoundException
     */
    public Parser(File fromDir, File toDir, File dict) throws FileNotFoundException{
        if (!Files.exists(fromDir.toPath())) throw new FileNotFoundException("Directory with files for parsing does not exists");
        if (!Files.exists(toDir.toPath())) throw new FileNotFoundException("Directory for parsing results does not exists");
        if (!Files.exists(dict.toPath())) throw new FileNotFoundException("Dictionary file does not exists");

        this.fromDir = fromDir;
        this.toDir = toDir;
        this.dict = dict;
    }

    /**
     * Сonstructor for creating a parser with defaults directory and file extensions for parse results
     * @param fromDir
     * @param dict
     * @throws FileNotFoundException
     */
    public Parser(File fromDir, File dict) throws FileNotFoundException{
        if (!Files.exists(fromDir.toPath())) throw new FileNotFoundException("Directory with files for parsing does not exists");
        if (!Files.exists(dict.toPath())) throw new FileNotFoundException("Dictionary file does not exists");

        this.fromDir = fromDir;
        this.dict = dict;
        this.toDir = new File(this.fromDir.getParent() + "/" + this.fromDir.getName() + "Parse");
        if (!Files.exists(this.toDir.toPath())) {
            try {
                Files.createDirectory(toDir.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parse() throws CantFindAnyTextFilesForParseException{
        parseFromDir();
    }

    private void parseFromDir() throws CantFindAnyTextFilesForParseException{
        List<Path> filesInDirForParseList = null;
        try(Stream<Path> filesInDirStream = Files.list(fromDir.toPath())) {
            filesInDirForParseList = filesInDirStream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Path> paths = getTextFilesForParsing(filesInDirForParseList);
        for (Path path : paths) {
            cleanup(path);
            run(path);
        }
    }

    private List<Path> getTextFilesForParsing(List<Path> paths) throws CantFindAnyTextFilesForParseException{
        for (int i = 0; i < paths.size(); i++) {
            if(isTextFile(paths.get(i)) != true) {
                paths.remove(paths.get(i));
            }
        }

        if (paths.size() == 0) throw new CantFindAnyTextFilesForParseException("Can't find any text files for parse in directory");
        return paths;
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

    private void run(Path path) {
        try(BufferedReader textReader = Files.newBufferedReader(path)) {
            while(true) {
                String line = textReader.readLine();
                if (line == null) break;
                StringBuilder str = new StringBuilder(line);

                try(BufferedReader dictReader = Files.newBufferedReader(this.dict.toPath())) {
                    while(true) {
                        String dict = dictReader.readLine();
                        if (dict == null) break;

                        List<String> words = Arrays.asList(dict.split(delimiter));
                        changeWordsFromStringToReplacementMark(str, words);
                    }
                }
                Files.write(Paths.get(toDir.toString(), path.getFileName().toString()), Collections.singleton(str), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replacing finded words in text string by default mark "*"
     * @param str Line of text for parse
     * @param words List of words for replacement
     */
    private void changeWordsFromStringToReplacementMark(StringBuilder str, List<String> words) {
        for (String word : words) {
            System.out.println(word);
            Integer index = str.indexOf(word);
            while (index != -1) {
                str.replace(index, index + word.length(), new String(new char[word.length()]).replace("\0", replacementMark));
                index = str.indexOf(word, index);
            }
        }
    }

    /**
     * Checking file with previous parse results and delete it if exists. Creating empty file for filling with parsing results
     * @param path
     */
    private void cleanup (Path path) {
        try {
            Files.deleteIfExists(Paths.get(toDir.toString(), path.getFileName().toString()));
            Files.createFile(Paths.get(toDir.toString(), path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
