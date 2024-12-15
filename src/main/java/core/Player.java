package  core;

public class Player {
    private int id;
    private String name;
    private int score;

    // Constructor with default name
    public Player(int id) {
        this.id = id;
        this.name = "Player" + id;
        this.score = 0; // Initial score is 0
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter and setter for score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Method to update the score
    public void updateScore(int points) {
        this.score += points;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
