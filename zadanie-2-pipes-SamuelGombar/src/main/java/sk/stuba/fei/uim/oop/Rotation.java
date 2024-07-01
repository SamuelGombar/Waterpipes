package sk.stuba.fei.uim.oop;

public enum Rotation {
    DEFAULT,
    DEGREES90,
    DEGREES180,
    DEGREES270;

    public Rotation next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
