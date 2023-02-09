enum TestEnum {
    /**
     * First value.
     */
    FIRST,
    /**
     * Second value.
     */
    SECOND;

    private final int customValue;

    TestEnum(int customValue) {
        this.customValue = customValue;
    }

    public int getCustomValue() {
        return customValue;
    }
}
