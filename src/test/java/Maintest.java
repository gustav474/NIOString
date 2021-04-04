import com.gustav474.NIOString.CantFindAnyTextFilesForParseException;
import com.gustav474.NIOString.Parser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Maintest {

    File fromDir = null;
    File dict = null;
    File toDir = null;
    List<String> fileExtensionsForCheck = null;

    @Before
    public void before() {
        fromDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/test/resources/fromDir");
        toDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/test/resources/fromDirParse");
        if (!Files.exists(this.toDir.toPath())) {
            try {
                Files.createDirectory(toDir.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        dict = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/test/resources/dict.txt");
        fileExtensionsForCheck = Arrays.asList(new String[] {"txt"});
    }

    @Test
    public void checkConstructorForDefaultFolderWithParsingResult() {
        File toDir = new File(this.fromDir.getParent() + "/" + this.fromDir.getName() + "Parse");

        try {
            Files.deleteIfExists(toDir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Parser parser = new Parser(fromDir, dict);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(toDir.exists());
    }

    @Test
    public void checkConstructorForDefaultFileExtensionsForParse() {
        Parser parser = null;
        try {
            parser = new Parser(fromDir, toDir, dict);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(parser.getFileExtensions(), fileExtensionsForCheck);
    }

    @Test(expected = CantFindAnyTextFilesForParseException.class)
    public void getCantFindAnyTextFilesForParseException() throws CantFindAnyTextFilesForParseException{
        fileExtensionsForCheck = Arrays.asList(new String[] {"rtf"});
        Parser parser = null;
        try {
            parser = new Parser(toDir, fromDir, dict, fileExtensionsForCheck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parser.parse();

    }

    @Test
    public void checkParsingResults() {
        File file1 = new File(toDir.getParent().toString() + "/parsed.txt");
        File file2 = new File(toDir.toString() + "/textForParsing.txt");

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
            Assert.assertEquals(true, isFilesEquals);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void checkFilesInConstructorParamsAsExisting() throws FileNotFoundException {
        fromDir = new File("/Users/gustav474/Desktop/Desktop – MacBook Pro — Сергей/DEV/Java/NIOString/src/test/resources/fromDi");
        Parser parser = new Parser(fromDir, toDir, dict);
    }

}
