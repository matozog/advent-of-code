package aoc2024.day5;

import commons.FileUtils;
import commons.Task;

import java.util.*;
import java.util.stream.Collectors;

public class Day5 implements Task<Integer> {
    String fileLine;
    Map<Integer, Page> rules = new HashMap<>();
    List<List<Integer>> pages = new ArrayList<>();

    public Day5() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day5/input.txt", "\n");
        String[] fileParts = fileLine.split("\n\n");

        Arrays.stream(fileParts[0].split("\n")).forEach(line -> {
            String[] pageIds = line.split("\\|");
            int idBefore = Integer.parseInt(pageIds[0]);
            int idAfter = Integer.parseInt(pageIds[1]);
            Page before = rules.get(idBefore);
            Page after = rules.get(idAfter);
            if (before == null) {
                before = new Page(idBefore);
            }
            if (after == null) {
                after = new Page(idAfter);
            }
            before.nextPages.add(idAfter);
            after.previousPages.add(idBefore);

            rules.put(idAfter, after);
            rules.put(idBefore, before);
        });

        Arrays.stream(fileParts[1].split("\n")).forEach(line -> {
            String[] pageNumbers = line.split(",");
            List<Integer> numbers = Arrays.stream(pageNumbers).map(Integer::parseInt).toList();
            pages.add(numbers);
        });
    }

    Integer getMiddleElement(List<Integer> list) {
        return list.get(list.size() / 2);
    }

    boolean isListSorted(List<Integer> numbersList) {
        return numbersList.stream()
                .map(number -> rules.get(number))
                .allMatch(page -> page.isElementSortedInList(numbersList));
    }

    @Override
    public Integer resolvePart1() {
        return pages.stream().filter(this::isListSorted).map(this::getMiddleElement).reduce(0, Integer::sum);
    }

    @Override
    public Integer resolvePart2() {
        List<List<Page>> unsortedPages = pages.stream()
                .filter(list -> !isListSorted(list))
                .map(list -> list.stream().map(id -> rules.get(id)).collect(Collectors.toList()))
                .toList();
        for (List<Page> pageList : unsortedPages) {
            pageList.sort(new PageComparator());
        }
        return unsortedPages.stream()
                .map(pagesList -> pagesList.stream().map(page -> page.id).toList())
                .map(this::getMiddleElement)
                .reduce(0, Integer::sum);
    }
}

class PageComparator implements Comparator<Page> {
    @Override
    public int compare(Page p1, Page p2) {
        if (p1.previousPages.contains(p2.id)) {
            return 1;
        }

        if (p1.nextPages.contains(p2.id)) {
            return -1;
        }
        return 0;
    }
}

class Page {
    int id;
    List<Integer> previousPages = new ArrayList<>();
    List<Integer> nextPages = new ArrayList<>();

    public Page(int id) {
        this.id = id;
    }

    boolean isElementSortedInList(List<Integer> list) {
        boolean isBefore = true;
        for (Integer integer : list) {
            if (integer == id) {
                isBefore = false;
            }

            if (isBefore && nextPages.contains(integer)) {
                return false;
            } else if (!isBefore && previousPages.contains(integer)) {
                return false;
            }
        }
        return true;
    }
}