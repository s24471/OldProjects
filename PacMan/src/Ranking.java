import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public record Ranking(String playerName, int score) implements Serializable, Comparable<Ranking> {
    @Serial
    private static final long serialVersionUID = 0L;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Ranking) obj;
        return Objects.equals(this.playerName, that.playerName) &&
                this.score == that.score;
    }

    @Override
    public String toString() {
        return "Ranking[" +
                "playerName=" + playerName + ", " +
                "score=" + score + ']';
    }

    @Override
    public int compareTo(Ranking o) {
        return Integer.compare(o.score, this.score);
    }

}