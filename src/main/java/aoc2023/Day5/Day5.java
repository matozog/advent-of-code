package aoc2023.Day5;

import utils.FileUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum CategoryType {
    SeedToSoil,
    SoilToFertilizer,
    FertilizerToWater,
    WaterToLight,
    LightToTemperature,
    TemperatureToHumidity,
    HumidityToLocation
}

public class Day5 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    long[] seeds;
    Category seedToSoilCategory, soilToFertilizerCategory, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation;

    public Day5() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day5/input.txt", "\n");

        this.seedToSoilCategory = new Category(CategoryType.SeedToSoil);
        this.soilToFertilizerCategory = new Category(CategoryType.SoilToFertilizer);
        this.fertilizerToWater = new Category(CategoryType.FertilizerToWater);
        this.waterToLight = new Category(CategoryType.WaterToLight);
        this.lightToTemperature = new Category(CategoryType.LightToTemperature);
        this.temperatureToHumidity = new Category(CategoryType.TemperatureToHumidity);
        this.humidityToLocation = new Category(CategoryType.HumidityToLocation);

        fillDataStructures(fileLines);
    }

    void fillSeedsData(String line) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(line);
        seeds = new long[Integer.parseInt(String.valueOf(matcher.results().count()))];
        matcher = pattern.matcher(line);
        int i = 0;
        while (matcher.find()) {
            seeds[i] = Long.parseLong(matcher.group());
            i++;
        }
    }

    Category getCategoryByLine(String line, Category currentCategory) {
        return switch (line) {
            case "seed-to-soil map:" -> seedToSoilCategory;
            case "soil-to-fertilizer map:" -> soilToFertilizerCategory;
            case "fertilizer-to-water map:" -> fertilizerToWater;
            case "water-to-light map:" -> waterToLight;
            case "light-to-temperature map:" -> lightToTemperature;
            case "temperature-to-humidity map:" -> temperatureToHumidity;
            case "humidity-to-location map:" -> humidityToLocation;
            default -> currentCategory;
        };
    }

    void addNewRangeToCategory(Category category, String line) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            long destinationRangeStart = Long.parseLong(matcher.group());
            matcher.find();
            long sourceRangeStart = Long.parseLong(matcher.group());
            matcher.find();
            long steps = Long.parseLong(matcher.group());
            category.rangeList.add(new Range(destinationRangeStart, sourceRangeStart, steps));
        }
    }

    void fillDataStructures(String fileLines) {
        Category currentCategory = seedToSoilCategory;
        for (String line : fileLines.split("\n")) {
            if (line.contains("seeds")) {
                fillSeedsData(line);
            } else {
                currentCategory = getCategoryByLine(line, currentCategory);
                addNewRangeToCategory(currentCategory, line);
            }
        }
    }

    public long resolvePart1() {
        return Arrays.stream(seeds).map(seed -> {
            long soil = seedToSoilCategory.findDestination(seed);
            long fertilizer = soilToFertilizerCategory.findDestination(soil);
            long water = fertilizerToWater.findDestination(fertilizer);
            long light = waterToLight.findDestination(water);
            long temperature = lightToTemperature.findDestination(light);
            long humidity = temperatureToHumidity.findDestination(temperature);
            return humidityToLocation.findDestination(humidity);
        }).min().getAsLong();
    }

    List<SeedsRange> getRangesFromSeed() {
        ArrayList<SeedsRange> seedsRanges = new ArrayList<>();
        for (int i = 0; i < seeds.length; i += 2) {
            seedsRanges.add(new SeedsRange(seeds[i], seeds[i] + seeds[i + 1] - 1));
        }
        return seedsRanges;
    }

    ArrayList<SeedsRange> transformCategory(ArrayList<SeedsRange> seedsRanges, Category category) {
        ArrayList<SeedsRange> transformedSeeds = new ArrayList<>();
        for (SeedsRange seedsRange : seedsRanges) {
            transformedSeeds.addAll(category.getPossibleRanges(seedsRange));
        }

        return transformedSeeds.size() == 0 ? seedsRanges : transformedSeeds;
    }

    ArrayList<SeedsRange> getPossibleRangesFromSeedsRange(SeedsRange seedRange) {
        ArrayList<SeedsRange> possibleRanges = new ArrayList<>();
        possibleRanges.add(seedRange);

        possibleRanges = transformCategory(possibleRanges, seedToSoilCategory);
        possibleRanges = transformCategory(possibleRanges, soilToFertilizerCategory);
        possibleRanges = transformCategory(possibleRanges, fertilizerToWater);
        possibleRanges = transformCategory(possibleRanges, waterToLight);
        possibleRanges = transformCategory(possibleRanges, lightToTemperature);
        possibleRanges = transformCategory(possibleRanges, temperatureToHumidity);
        possibleRanges = transformCategory(possibleRanges, humidityToLocation);

        return possibleRanges;
    }

    public long resolvePart2() {
        List<SeedsRange> seedsRanges = getRangesFromSeed();
        ArrayList<SeedsRange> possibleRanges = new ArrayList<>();

        for (SeedsRange seed : seedsRanges) {
            possibleRanges.addAll(getPossibleRangesFromSeedsRange(seed));
        }

        return possibleRanges.stream().map(range -> range.start).min(Long::compare).orElseThrow();
    }
}

class Category {
    CategoryType type;
    List<Range> rangeList = new ArrayList<>();

    public Category(CategoryType categoryType) {
        this.type = categoryType;
    }

    public long findDestination(long seed) {
        for (Range range : rangeList) {
            if (seed >= range.sourceStart && seed <= range.sourceEnd) {
                return range.destinationStart + (seed - range.sourceStart);
            }
        }
        return seed;
    }

    public ArrayList<SeedsRange> removeRangeFromUncoveredRanges(ArrayList<SeedsRange> rangesToCover, SeedsRange range) {
        ArrayList<SeedsRange> tmpRangesToCover = new ArrayList<>(rangesToCover);
        for (SeedsRange rangeToCover : rangesToCover) {
            if (range.start <= rangeToCover.start && range.end >= rangeToCover.end) {
                tmpRangesToCover.remove(rangeToCover);
            } else if (range.start > rangeToCover.start && range.end < rangeToCover.end) {
                tmpRangesToCover.remove(rangeToCover);
                tmpRangesToCover.add(new SeedsRange(rangeToCover.start, range.start));
                tmpRangesToCover.add(new SeedsRange(range.end, rangeToCover.end));
            } else if (range.start <= rangeToCover.start && range.end >= rangeToCover.start) {
                tmpRangesToCover.remove(rangeToCover);
                tmpRangesToCover.add(new SeedsRange(range.end + 1, rangeToCover.end));
            } else if (range.start > rangeToCover.start && range.start <= rangeToCover.end) {
                tmpRangesToCover.remove(rangeToCover);
                tmpRangesToCover.add(new SeedsRange(rangeToCover.start, range.start - 1));
            }
        }

        return tmpRangesToCover;
    }

    public ArrayList<SeedsRange> getPossibleRanges(SeedsRange seedsRange) {
        ArrayList<SeedsRange> possibleRanges = new ArrayList<>();
        ArrayList<SeedsRange> uncoveredRanges = new ArrayList<>();
        uncoveredRanges.add(seedsRange);
        for (Range range : this.rangeList) {
            if (seedsRange.start >= range.sourceStart && seedsRange.end <= range.sourceEnd) {
                possibleRanges.add(new SeedsRange(seedsRange.start + range.shift, seedsRange.end + range.shift));
                uncoveredRanges = removeRangeFromUncoveredRanges(uncoveredRanges, new SeedsRange(seedsRange.start, seedsRange.end));
                break;
            } else if (seedsRange.start < range.sourceStart && seedsRange.end >= range.sourceStart && seedsRange.end <= range.sourceEnd) {
                possibleRanges.add(new SeedsRange(range.sourceStart + range.shift, seedsRange.end + range.shift));
                uncoveredRanges = removeRangeFromUncoveredRanges(uncoveredRanges, new SeedsRange(range.sourceStart, seedsRange.end));
            } else if (seedsRange.start >= range.sourceStart && seedsRange.start <= range.sourceEnd) {
                possibleRanges.add(new SeedsRange(seedsRange.start + range.shift, range.sourceEnd + range.shift));
                uncoveredRanges = removeRangeFromUncoveredRanges(uncoveredRanges, new SeedsRange(seedsRange.start, range.sourceEnd));
            } else if (seedsRange.start < range.sourceStart && seedsRange.end > range.sourceEnd) {
                possibleRanges.add(new SeedsRange(range.sourceStart + range.shift, range.sourceEnd + range.shift));
                uncoveredRanges = removeRangeFromUncoveredRanges(uncoveredRanges, new SeedsRange(range.sourceStart, range.sourceEnd));
            }
        }

        if (uncoveredRanges.size() > 0) possibleRanges.addAll(uncoveredRanges);

        return possibleRanges;
    }
}

class Range {
    long sourceStart, sourceEnd, destinationStart, destinationEnd;
    long steps;
    long shift;

    public Range(long destinationStart, long sourceStart, long steps) {
        this.steps = steps;
        this.destinationStart = destinationStart;
        this.destinationEnd = destinationStart + steps - 1;
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceStart + steps - 1;
        this.shift = destinationStart - sourceStart;
    }
}

class SeedsRange {
    long start, end;

    public SeedsRange(long start, long end) {
        this.start = start;
        this.end = end;
    }
}
