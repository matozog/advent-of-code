package aoc2024.day1;

import commons.FileUtils;
import commons.Task;

import java.util.*;
import java.util.stream.IntStream;

public class Day1 implements Task<Integer> {
    String fileLines;
    ArrayList<Integer> list1 = new ArrayList<>();
    ArrayList<Integer> list2 = new ArrayList<>();

    public Day1() {
        fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day1/input.txt", "\n");

        Arrays.stream(fileLines.split("\n")).forEach(line -> {
            String[] locations = line.split(" {3}");

            list1.add(Integer.parseInt(locations[0]));
            list2.add(Integer.parseInt(locations[1]));
        });
    }

    @Override
    public Integer resolvePart1() {
        Collections.sort(list1);
        Collections.sort(list2);
        return IntStream.range(0, list1.size())
                .map(index -> Math.abs(list1.get(index) - list2.get(index)))
                .reduce(0, Integer::sum);
    }

    @Override
    public Integer resolvePart2() {
        return list1.stream()
                .map(location -> list2.stream().filter(id -> Objects.equals(id, location)).toList().size() * location)
                .reduce(0, Integer::sum);

    }
}
