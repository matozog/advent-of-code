package commons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static String getFileLinesAsStringByDelimiter(String filePath, String delimiter) {
        try {
            File file = new File(filePath);

            Stream<String> lines;
            lines = Files.lines(file.toPath());
            String data = lines.collect(Collectors.joining(delimiter));
            lines.close();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
