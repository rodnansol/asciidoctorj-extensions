import java.util.Arrays;
enum TestEnum {
    /**
     * First value.
     */
    FIRST(1),
    /**
     * Second value.
     */
    SECOND(2);

    private final int customValue;

    TestEnum(int customValue) {
        this.customValue = customValue;
    }

    /**
     * Returns the custom value.
     *
     * @return custom value.
     */
    public int getCustomValue() {
        return customValue;
    }

    TestEnum getEnumByValue(int value, String reason) {
        return Arrays.stream(values())
            .filter(actualEnum -> actualEnum.customValue == value)
            .findFirst()
            .orElse(null);
    }
}
