package core; 
import core.Player;

public class PlayerTest {
    public static void main(String[] args) {
        // Test creating players
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        // Print player details
        System.out.println(player1);
        System.out.println(player2);
        System.out.println(player3);

        // Test updating scores
        player1.updateScore(10);
        player2.updateScore(20);
        player3.updateScore(30);

        // Print player details after updating scores
        System.out.println(player1);
        System.out.println(player2);
        System.out.println(player3);

        // Test setting scores directly
        player1.setScore(50);
        player2.setScore(60);
        player3.setScore(70);

        // Print player details after setting scores directly
        System.out.println(player1);
        System.out.println(player2);
        System.out.println(player3);
    }
}
