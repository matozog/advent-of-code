package Day15;

import utils.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    List<String> sequences = new ArrayList<>();

    Box[] boxes = new Box[256];

    public Day15() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day15/input.txt", "\n");

        fillDataStructures(fileLines);

    }

    void fillDataStructures(String fLines) {
        sequences = Arrays.stream(fLines.split(",")).toList();
    }

    public int getHashCode(String sequence) {
        int result = 0;
        char[] sequenceChar = sequence.toCharArray();
        for (int i = 0; i < sequence.length(); i++) {
            int sequenceSum = result;
            sequenceSum += (int) sequenceChar[i];
            sequenceSum *= 17;
            result = sequenceSum % 256;
        }

        return result;
    }

    public int resolvePart1() {
        return sequences.stream().map(this::getHashCode).mapToInt(Integer::valueOf).sum();
    }

    void locateLensInBoxes(int boxId, int focalLength, String label) {
        String operator = focalLength == -1 ? "-" : "=";
        if (operator.equals("-")) {
            boxes[boxId].lenses = boxes[boxId].lenses.stream()
                    .filter(lens -> !lens.label.equals(label))
                    .collect(Collectors.toList());
        } else {
            Lens newLens = new Lens(label, focalLength);
            if (boxes[boxId].lenses.contains(newLens)) {
                boxes[boxId].lenses = boxes[boxId].lenses.stream()
                        .peek(lens -> {
                            if (lens.label.equals(label)) {
                                lens.focalLength = focalLength;
                            }
                        })
                        .collect(Collectors.toList());
            } else {
                boxes[boxId].lenses.add(newLens);
            }
        }
    }

    public int resolvePart2() {
        for (String sequence : sequences) {
            String[] sequenceData = sequence.split("[-=]");
            int boxId = getHashCode(sequenceData[0]);
            String label = sequenceData[0];
            int focal = -1;
            if (sequenceData.length > 1) {
                focal = Integer.parseInt(sequenceData[1]);
            }

            if (boxes[boxId] == null) boxes[boxId] = new Box(boxId);

            locateLensInBoxes(boxId, focal, label);
        }

        return Arrays.stream(boxes)
                .filter(Objects::nonNull)
                .map(box ->
                        box.lenses.stream()
                                .map(lens -> (box.id + 1) * (box.lenses.indexOf(lens) + 1) * lens.focalLength)
                                .reduce(0, Integer::sum))
                .mapToInt(Integer::valueOf)
                .sum();
    }
}

class Box {
    int id;
    List<Lens> lenses = new ArrayList<>();

    public Box(int id) {
        this.id = id;
    }
}

class Lens {
    String label;
    int focalLength;

    public Lens(String label, int focalLength) {
        this.label = label;
        this.focalLength = focalLength;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Lens other))
            return false;
        return this.label.equals(other.label);
    }
}
