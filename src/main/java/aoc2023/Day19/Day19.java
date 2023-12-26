package aoc2023.Day19;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Day19 {
    utils.FileUtils fileUtils = new utils.FileUtils();
    String fileLines;

    List<Part> partsList = new ArrayList<>();
    List<Flow> flows = new ArrayList<>();

    public Day19() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day19/input.txt", "\n");

        fillDataStructures();
    }

    Flow getFlowFromLine(String line) {
        String[] flowData = line.split("\\{");
        String[] equationsData = flowData[1].split(",");
        Flow flow = new Flow(flowData[0], equationsData[equationsData.length - 1].replace("}", ""));
        for (String equation : Arrays.copyOf(equationsData, equationsData.length - 1)) {
            String[] equationData = equation.split(":");
            String elseResult = equationData[1];
            String[] equationParam = equationData[0].split("[<>]");
            String equationInput = equationParam[0];
            String equationNumberToCompare = equationParam[1];
            char equationSymbol = equationData[0].toCharArray()[1];
            Equation equationObj = new Equation(equationSymbol, Integer.parseInt(equationNumberToCompare), elseResult, equationInput);
            flow.equations.add(equationObj);
//            switch (equationInput) {
//                case "x" -> flow.x = equationObj;
//                case "m" -> flow.m = equationObj;
//                case "a" -> flow.a = equationObj;
//                case "s" -> flow.s = equationObj;
//            }
        }
        return flow;
    }

    Part getPartFromLine(String line) {
        String[] partParams = line.replaceAll("[{}]", "").split(",");
        Part part = new Part();
        for (String partParam : partParams) {
            String[] paramData = partParam.split("=");
            switch (paramData[0]) {
                case "x" -> part.x = Integer.parseInt(paramData[1]);
                case "m" -> part.m = Integer.parseInt(paramData[1]);
                case "a" -> part.a = Integer.parseInt(paramData[1]);
                case "s" -> part.s = Integer.parseInt(paramData[1]);
            }
        }
        return part;
    }

    void fillDataStructures() {
        String[] lineData = fileLines.split("\n");
        Arrays.stream(lineData).forEach(line -> {
            if (line.startsWith("{")) {
                partsList.add(getPartFromLine(line));
            } else if (!line.equals("")) {
                flows.add(getFlowFromLine(line));
            }
        });
    }

    void runFlow(Part part) {
        Optional<Flow> currentFlow = flows.stream().filter(flow -> flow.id.equals(part.state)).findFirst();

        if (currentFlow.isPresent()) {
            for (Equation equation : currentFlow.get().equations) {
                switch (equation.input) {
                    case "x":
                        if (equation.symbol == '>') {
                            if (part.x > equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        } else if (equation.symbol == '<') {
                            if (part.x < equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        }
                        break;
                    case "m":
                        if (equation.symbol == '>') {
                            if (part.m > equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        } else if (equation.symbol == '<') {
                            if (part.m < equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        }
                        break;
                    case "a":
                        if (equation.symbol == '>') {
                            if (part.a > equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        } else if (equation.symbol == '<') {
                            if (part.a < equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        }
                        break;
                    case "s":
                        if (equation.symbol == '>') {
                            if (part.s > equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        } else if (equation.symbol == '<') {
                            if (part.s < equation.numberToCompare) {
                                part.state = equation.result;
                                return;
                            }
                        }
                }
            }
            part.state = currentFlow.get().finallyResult;
        }
    }

    public int resolvePart1() {
        while (!partsList.stream().allMatch(part -> part.state.equals("A") || part.state.equals("R"))) {
            for (Part part : partsList) {
                if (!(part.state.equals("A") || part.state.equals("R"))) {
                    runFlow(part);
                }
            }
        }

        return partsList.stream()
                .filter(part -> part.state.equals("A"))
                .map(part -> (part.x + part.m + part.a + part.s))
                .mapToInt(Integer::valueOf)
                .sum();
    }
}

@AllArgsConstructor
class Equation {
    char symbol;
    int numberToCompare;
    String result, input;
}

@AllArgsConstructor
class Flow {
    String id;
    List<Equation> equations;
    String finallyResult;

    public Flow(String id, String finallyResult) {
        this.id = id;
        this.finallyResult = finallyResult;
        this.equations = new ArrayList<>();
    }
}

@AllArgsConstructor
@NoArgsConstructor
class Part {
    int x, m, a, s;
    String state = "in";
}
