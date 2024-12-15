package core;


public class ColorDiceTest {
    public static void main(String[] args) {
        ColorDice colorDice = new ColorDice();

        // Roll the color dice multiple times and print the result each time
        for (int i = 0; i < 5; i++) {
            colorDice.roll();
            System.out.println("Roll " + (i+1) + ": " + colorDice);
        }
    }
}