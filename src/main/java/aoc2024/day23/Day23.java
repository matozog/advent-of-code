package aoc2024.day23;

import commons.FileUtils;
import commons.Task;
import lombok.EqualsAndHashCode;

import java.util.*;

public class Day23 implements Task<Integer> {
    String fileLine;
    Map<String, Computer> computers = new HashMap<>();
    Set<String> lanParties = new HashSet<>();

    public Day23() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day23/input.txt", "\n");
        Arrays.stream(fileLine.split("\n")).forEach(line -> {
            String[] connection = line.split("-");

            addComputerToMap(connection[0], connection[1]);
            addComputerToMap(connection[1], connection[0]);
        });
    }

    void addComputerToMap(String compId, String connectionId) {
        if (computers.get(compId) == null) {
            computers.put(compId, new Computer(compId, connectionId));
        } else {
            computers.get(compId).connections.add(connectionId);
        }
    }

    boolean isConnectionPossible(Computer comp, String connectionId) {
        return comp.connections.contains(connectionId);
    }

    void generateLanParties(Computer computer) {
        List<String> partyIds = new ArrayList<>();
        for (int i = 0; i < computer.connections.size() - 1; i++) {
            for (int j = i + 1; j < computer.connections.size(); j++) {
                Computer firstComp = computers.get(computer.connections.get(i));
                Computer secondComp = computers.get(computer.connections.get(j));

                if (isConnectionPossible(firstComp, secondComp.id)) {
                    partyIds.add(computer.id);
                    partyIds.add(firstComp.id);
                    partyIds.add(secondComp.id);
                    Collections.sort(partyIds);
                    lanParties.add(String.join("-", partyIds));
                    partyIds.clear();
                }
            }
        }

    }

    @Override
    public Integer resolvePart1() {
        computers.keySet()
                .stream()
                .filter(compId -> compId.charAt(0) == 't')
                .map(compId -> computers.get(compId))
                .forEach(this::generateLanParties);
        return lanParties.size();
    }

    List<String> findBiggestLANParty(List<List<String>> parties) {
        List<String> biggestParty = new ArrayList<>();
        for (List<String> party : parties) {
            if (party.size() > biggestParty.size()) {
                biggestParty = party;
            }
        }

        return biggestParty;
    }

    List<String> generateLanParty(List<String> compIds, Computer currentComputer) {
        if (compIds.contains(currentComputer.id) || !new HashSet<>(currentComputer.connections).containsAll(compIds))
            return compIds;

        List<List<String>> possibleParties = new ArrayList<>();
        compIds.add(currentComputer.id);

        for (String connectionId : currentComputer.connections) {
            if (isConnectionPossible(computers.get(connectionId), currentComputer.id)) {
                possibleParties.add(generateLanParty(compIds, computers.get(connectionId)));
            }
        }

        return findBiggestLANParty(possibleParties);
    }

    @Override
    public Integer resolvePart2() {
        List<List<String>> lanParties = computers.keySet()
                .stream()
                .map(compId -> {
                    List<String> longestLAN = new ArrayList<>();
                    return generateLanParty(longestLAN, computers.get(compId));
                })
                .toList();

        List<String> biggestParty = findBiggestLANParty(lanParties);

        Collections.sort(biggestParty);
        System.out.println(String.join(",", biggestParty));
        return 0;
    }

}

@EqualsAndHashCode
class Computer {
    @EqualsAndHashCode.Include
    String id;
    @EqualsAndHashCode.Exclude
    List<String> connections = new ArrayList<>();

    public Computer(String id, String connectedComputer) {
        this.id = id;
        connections.add(connectedComputer);
    }
}
