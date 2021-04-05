import com.gustav474.NIOString.CantFindAnyTextFilesForParseException;
import com.gustav474.NIOString.Parser;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Maintest {

    File fromDir = null;
    File dict = null;
    File toDir = null;
    List<String> fileExtensionsForCheck = null;
    File testResourcesDir = Paths.get(Paths.get("").toAbsolutePath().toString(), "src/test/resources").toFile();

    @BeforeEach
    public void before() {
        fromDir = new File(testResourcesDir.toString() + "/texts");
        toDir = new File(fromDir.toString() + "Parsed");
        if (!Files.exists(this.toDir.toPath())) {
            try {
                Files.createDirectory(toDir.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dict = new File(testResourcesDir.toString() + "/dict.txt");
        fileExtensionsForCheck = Arrays.asList(new String[] {"txt"});
    }

    @Test
    @Order(1)
    public void checkConstructorForDefaultFolderWithParsingResult() {
        toDir = new File(fromDir.toString() + "Parsed");

        List<Path> filesInFromDirParseList = null;
        try(Stream<Path> filesInFromDirParseStream = Files.list(toDir.toPath())) {
            filesInFromDirParseList = filesInFromDirParseStream.collect(Collectors.toList());
            for (Path path : filesInFromDirParseList) {
                Files.delete(path);
            }

            Files.deleteIfExists(toDir.toPath());
            Parser parser = new Parser(fromDir, dict);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(toDir.exists());
    }

    @Test
    @Order(2)
    public void checkConstructorForDefaultFileExtensionsForParse() {
        Parser parser = null;
        try {
            parser = new Parser(fromDir, toDir, dict);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(parser.getFileExtensions(), fileExtensionsForCheck);
    }

    @Test
    @Order(3)
    public void getCantFindAnyTextFilesForParseException() {
        fileExtensionsForCheck = Arrays.asList(new String[] {"rtf"});
        Assertions.assertThrows(CantFindAnyTextFilesForParseException.class, () -> {
            Parser parser = new Parser(toDir, fromDir, dict, fileExtensionsForCheck);
            parser.parse();
        });

    }

    @Test
    @Order(4)
    public void checkFilesInConstructorParamsAsExisting() {
        fromDir = new File(testResourcesDir.toString() + "/fromDi");
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            Parser parser = new Parser(fromDir, toDir, dict);
        });
    }

    @Test
    @Order(5)
    public void checkParsingResults() {
        File file1 = new File(toDir.getParent() + "/parsed.txt");
        File file2 = new File(toDir.toString() + "/forParsing.txt");

        System.out.println(file1.toString());
        System.out.println(file2.toString());

        Parser parser = null;
        try {
            parser = new Parser(toDir, fromDir, dict, fileExtensionsForCheck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            parser.parse();
            boolean isFilesEquals = FileUtils.contentEquals(file1, file2);
            Assertions.assertEquals(true, isFilesEquals);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
