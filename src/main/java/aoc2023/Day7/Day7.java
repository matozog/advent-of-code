package aoc2023.Day7;

import utils.FileUtils;

import java.util.*;
import java.util.stream.IntStream;

public class Day7 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    List<Hand> hands = new ArrayList<>();

    public Day7(boolean isJokerInCards) {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day7/input.txt", "\n");

        fillDataStructures(fileLines, isJokerInCards);
    }

    void fillDataStructures(String fLines, boolean isJokerInCards) {
        Arrays.stream(fLines.split("\n")).forEach(line -> {
            String[] handData = line.split(" ");

            hands.add(new Hand(handData[0].chars(), Integer.parseInt(handData[1]), isJokerInCards));
        });
    }

    public int resolvePart1() {
        List<Hand> sortedHands = hands.stream().sorted().toList();

        return IntStream
                .range(0, sortedHands.size())
                .mapToObj(index -> sortedHands.get(index).bid * (index + 1))
                .mapToInt(Integer::valueOf)
                .reduce(0, Integer::sum);
    }

    public int resolvePart2() {
        List<Hand> sortedHands = hands.stream().sorted().toList();

        return IntStream
                .range(0, sortedHands.size())
                .mapToObj(index -> sortedHands.get(index).bid * (index + 1))
                .mapToInt(Integer::valueOf)
                .reduce(0, Integer::sum);
    }
}

enum HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_KIND,
    FULL_HOUSE,
    FOUR_OF_KIND,
    FIVE_OF_KIND,
}

class Hand implements Comparable<Hand> {
    int bid, rank;
    List<Card> cards = new ArrayList<>();
    HandType handType;

    public Hand(IntStream cardsSequence, int bid, boolean isJokerInCards) {
        this.bid = bid;
        cardsSequence.mapToObj(card -> (char) card).forEach(card -> cards.add(new Card(String.valueOf(card), isJokerInCards)));
        this.handType = this.getHandType(isJokerInCards);
    }

    HandType getHandType(boolean isJokerInCards) {
        Map<String, Integer> mapCardToRepetition = new HashMap<>();
        int numberOfJokers = this.cards.stream().filter(card -> card.cardType.equals("J")).toList().size();
        List<Card> tmpCards = isJokerInCards ? this.cards.stream().filter(card -> !card.cardType.equals("J")).toList() : this.cards;
        for (Card card : tmpCards) {
            if (mapCardToRepetition.containsKey(card.cardType)) {
                mapCardToRepetition.merge(card.cardType, 1, Integer::sum);
            } else {
                mapCardToRepetition.put(card.cardType, 1);
            }
        }

        int maxNumberOfSameCards = mapCardToRepetition.values().size() > 0 ? mapCardToRepetition.values().stream().mapToInt(Integer::valueOf).max().getAsInt() : 0;

        if (isJokerInCards) {
            for (String key : mapCardToRepetition.keySet()) {
                if (mapCardToRepetition.get(key) == maxNumberOfSameCards) {
                    mapCardToRepetition.merge(key, numberOfJokers, Integer::sum);
                    break;
                }
            }
            maxNumberOfSameCards += numberOfJokers;
        }

        switch (maxNumberOfSameCards) {
            case 5:
                return HandType.FIVE_OF_KIND;
            case 4:
                return HandType.FOUR_OF_KIND;
            case 3: {
                if (mapCardToRepetition.values().stream().anyMatch(card -> card == 2)) {
                    return HandType.FULL_HOUSE;
                } else {
                    return HandType.THREE_OF_KIND;
                }
            }
            case 2: {
                if (mapCardToRepetition.values().stream().filter(card -> card == 2).toList().size() > 1) {
                    return HandType.TWO_PAIR;
                } else {
                    return HandType.ONE_PAIR;
                }
            }
            default:
                return HandType.HIGH_CARD;
        }
    }

    @Override
    public int compareTo(Hand o) {
        if (this.handType.equals(o.handType)) {
            for (int i = 0; i < this.cards.size(); i++) {
                int cardComparison = this.cards.get(i).compareTo(o.cards.get(i));
                if (cardComparison != 0) {
                    return cardComparison;
                }
            }
            return 0;
        }
        return this.handType.compareTo(o.handType);
    }
}

class Card implements Comparable<Card> {
    String cardType;
    int power;

    public Card(String cardType, boolean isJokerInCards) {
        this.cardType = cardType;
        if (cardType.matches("\\d+")) {
            this.power = Integer.parseInt(cardType);
        } else {
            switch (cardType) {
                case "T" -> this.power = 10;
                case "J" -> this.power = isJokerInCards ? 1 : 11;
                case "Q" -> this.power = 12;
                case "K" -> this.power = 13;
                case "A" -> this.power = 14;
            }
        }
    }

    @Override
    public int compareTo(Card o) {
        if (this.power == o.power) return 0;
        return this.power > o.power ? 1 : -1;
    }
}
