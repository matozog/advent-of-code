package Day5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day5Tests {
    private final Category category = new Category(CategoryType.SeedToSoil);

    @Test
    void shouldReturnEmptyListWhenUncoveredRangesAreInRange() {
        ArrayList<SeedsRange> seedsRangeArrayList = new ArrayList<>();
        seedsRangeArrayList.add(new SeedsRange(5, 10));
        assertEquals(0, category.removeRangeFromUncoveredRanges(seedsRangeArrayList, new SeedsRange(0, 10)).size());
    }
}
