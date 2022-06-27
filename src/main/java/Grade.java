import java.util.Arrays;

public enum Grade {
    A (0, 100000),
    B (100001, 149999),
    C (150000, Integer.MAX_VALUE);

    private final int start;
    private final int end;

    Grade(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static String compare(int km) {
        return Arrays.stream(Grade.values())
                .filter(grade -> km >= grade.start && km <= grade.end)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Element not found in range!"))
                .name();
    }
}
