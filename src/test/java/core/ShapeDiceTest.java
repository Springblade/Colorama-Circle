package core;

public class ShapeDiceTest {
    public static void main(String[] args) {
        ShapeDice shapeDice = new ShapeDice();

        // Roll the shape dice multiple times and print the result each time
        for (int i = 0; i < 5; i++) {
            shapeDice.roll();
            System.out.println("Roll " + (i+1) + ": " + shapeDice);
        }
    }
}
