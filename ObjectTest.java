package ch.epfl.test;

public interface ObjectTest {
    public static boolean hashCodeIsCompatibleWithEquals(Object o1, Object o2) {
        return ! o1.equals(o2) || o1.hashCode() == o2.hashCode();
    }
}
