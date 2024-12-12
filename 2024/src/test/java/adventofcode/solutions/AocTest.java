package adventofcode.solutions;

public interface AocTest {

    Solution parse(String input) throws Exception;

    default Solution sample() {
        return getSolution(true);
    }

    default Solution real() {
        return getSolution(false);
    }

    private Solution getSolution(boolean isSample) {
        String dayNumber = this.getClass().getName().replaceAll("\\D+", ""); // Extract digits from class name
        var inputPath = "src/test/resources/input/day%s%s.txt".formatted(dayNumber, isSample ? "-sample" : "");
        try {
            return parse(inputPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
