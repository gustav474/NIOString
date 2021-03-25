package com.gustav474.NIOString;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.StringTokenizer;
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

    //Start parsing
    public void parse() {
        parseFromDir();
    }

    private void parseFromDir() {
        //TODO собрать инфу о файлах в этой директории
        List<Path> filesInDir = null;
        try {
            filesInDir = Files.list(pathFromDir).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filesInDir);

        //TODO Выводим через стрим данные из файла построчно
        for (Path path : filesInDir) {
            try {
                List<String> list = Files.readAllLines(path);
                //Заменяем нужные слова
//                List list = streamOfStrings.map(str -> str.replace(" " + "и" + " ", " " + "*" + " "))
//                                .collect(Collectors.toList());
                list.stream()
                    .map(str -> str.replace(" " + "и" + " ", " " + "*" + " "))
//                    .collect(Collectors.toList())
                    .forEach(TestMR::printMR);
//                Files.copy(pathToDir.resolve(path), streamOfStrings);
//                System.out.println(outList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
