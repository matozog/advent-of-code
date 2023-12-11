package Day12;

import utils.FileUtils;

public class Day12 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    public Day12() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day12/input.txt", "\n");
    }

}
