package commons;

import java.lang.reflect.InvocationTargetException;

public abstract class AocEdition {

    public void resolveDay(int edition, int day, int part) {
        try {
            Class<?> dayClass = Class.forName("aoc" + edition + ".day" + day + ".Day" + day);
            Object dayObject = dayClass.getDeclaredConstructor().newInstance();
            if (Task.class.isAssignableFrom(dayClass)) {
                Task<?> task = (Task<?>) dayObject;

                if (part == 1) {
                    System.out.println("Task 1: " + task.resolvePart1());
                } else if (part == 2) {
                    System.out.println("Task 2: " + task.resolvePart2());
                } else {
                    System.err.println("Incorrect task number");
                }
            } else {
                System.err.println("Class not implement Task interface");
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
