package aoc2024.day24;

import commons.FileUtils;
import commons.Task;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 implements Task<Long> {
    String fileLine;
    Map<String, Integer> wires = new HashMap<>();
    List<Equation> equations = new ArrayList<>();

    public Day24() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day24/input.txt", "\n");
        Pattern wirePattern = Pattern.compile("(\\w+): (\\d+)");
        Pattern equationPattern = Pattern.compile("(\\w+) (AND|OR|XOR) (\\w+) -> (\\w+)");

        Arrays.stream(fileLine.split("\n")).forEach(line -> {
            Matcher wireMatcher = wirePattern.matcher(line);
            Matcher equationMatcher = equationPattern.matcher(line);
            if (wireMatcher.find()) {
                wires.put(wireMatcher.group(1), Integer.parseInt(wireMatcher.group(2)));
            } else if (equationMatcher.find()) {
                equations.add(new Equation(equationMatcher.group(1), equationMatcher.group(2), equationMatcher.group(3), equationMatcher.group(4)));
            }
        });

        int index = 0;
        while (!equations.isEmpty()) {
            if (index == equations.size())
                index = 0;
            Integer leftElement = wires.get(equations.get(index).leftElement);
            Integer rightElement = wires.get(equations.get(index).rightElement);

            if (leftElement != null && rightElement != null) {
                wires.put(equations.get(index).result, calculateWiresOutput(leftElement, rightElement, equations.get(index).operator));
                equations.remove(index);
            } else {
                index++;
            }
        }
    }

    int calculateWiresOutput(int leftElement, int rightElement, String operator) {
        if (operator.equals("AND")) {
            return leftElement & rightElement;
        } else if (operator.equals("OR")) {
            return leftElement | rightElement;
        } else {
            return leftElement ^ rightElement;
        }
    }

    @Override
    public Long resolvePart1() {
        long result = 0L;
        Set<String> zWiresKeys = wires.keySet().stream().filter(key -> key.contains("z")).collect(Collectors.toSet());
        Pattern pattern = Pattern.compile("z(\\d+)");
        for (String zKey : zWiresKeys) {
            if (wires.get(zKey) != 0) {
                Matcher matcher = pattern.matcher(zKey);
                if (matcher.find()) {
                    int shiftIndex = Integer.parseInt(matcher.group(1));
                    long tmp = 1;
                    tmp = tmp << shiftIndex;
                    result = result | tmp;
                }
            }
        }

        return result;
    }

    @Override
    public Long resolvePart2() {
        return 0L;
    }
}

@AllArgsConstructor
class Equation {
    String leftElement, operator, rightElement, result;
}
