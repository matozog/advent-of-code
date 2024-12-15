package aoc2024.day13;

import commons.FileUtils;
import commons.Task;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 implements Task<Long> {
    String fileLine;
    Map<LongPoint, ClawMachine> machines = new HashMap<>();

    public Day13() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day13/input.txt", "\n");

        Pattern buttonsPattern = Pattern.compile("Button ([AB]): X\\+(\\d+), Y\\+(\\d+)");
        Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

        Arrays.stream(fileLine.split("\n\n")).forEach(machine -> {
            Matcher prizeMatcher = prizePattern.matcher(machine);
            LongPoint prizePoint = null;
            while (prizeMatcher.find()) {
                String xCoord = prizeMatcher.group(1);
                String yCoord = prizeMatcher.group(2);
                prizePoint = new LongPoint(Long.parseLong(xCoord), Long.parseLong(yCoord));
            }

            ClawMachine clawMachine = getClawMachine(machine, buttonsPattern);

            machines.put(prizePoint, clawMachine);
        });
    }

    private ClawMachine getClawMachine(String machine, Pattern buttonsPattern) {
        Matcher buttonsMatcher = buttonsPattern.matcher(machine);
        ClawMachine clawMachine = new ClawMachine();
        while (buttonsMatcher.find()) {
            String id = buttonsMatcher.group(1);
            String x = buttonsMatcher.group(2);
            String y = buttonsMatcher.group(3);
            Button button = new Button(id, Integer.parseInt(x), Integer.parseInt(y));
            if (id.equals("A")) {
                clawMachine.setButtonA(button);
            } else {
                clawMachine.setButtonB(button);
            }
        }
        return clawMachine;
    }

    void findPrize(ClawMachine clawMachine, LongPoint prize) {
        double aPressedTimes, bPressedTimes;
        bPressedTimes = (double) (prize.y * clawMachine.buttonA.x - clawMachine.buttonA.y * prize.x) / (double) (-1 * clawMachine.buttonB.x * clawMachine.buttonA.y + clawMachine.buttonA.x * clawMachine.buttonB.y);
        aPressedTimes = (prize.x - clawMachine.buttonB.x * bPressedTimes) / clawMachine.buttonA.x;

        if (aPressedTimes % 1 == 0 && bPressedTimes % 1 == 0) {
            clawMachine.buttonA.pressedTimes = (long) aPressedTimes;
            clawMachine.buttonB.pressedTimes = (long) bPressedTimes;
        }
    }

    @Override
    public Long resolvePart1() {
        machines.keySet().forEach(key -> findPrize(machines.get(key), key));
        return machines.keySet()
                .stream()
                .map(key -> machines.get(key))
                .map(ClawMachine::getCostToReachPrize).reduce(0L, Long::sum);
    }

    @Override
    public Long resolvePart2() {
        machines.keySet().forEach(key -> {
            key.x += 10000000000000L;
            key.y += 10000000000000L;
            findPrize(machines.get(key), key);
        });
        return machines.keySet()
                .stream()
                .map(key -> machines.get(key))
                .map(ClawMachine::getCostToReachPrize).reduce(0L, Long::sum);
    }
}

@AllArgsConstructor
class LongPoint {
    long x, y;
}

class Button {
    String type;
    long x, y, cost, pressedTimes = 0;

    public Button(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.cost = type.equals("A") ? 3 : 1;
    }

    Long getCost() {
        return cost * pressedTimes;
    }
}

@NoArgsConstructor
@Setter
class ClawMachine {
    Button buttonA, buttonB;

    Long getCostToReachPrize() {
        return buttonA.getCost() + buttonB.getCost();
    }
}