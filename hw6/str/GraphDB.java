import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GraphDB{
    char[][] nodes;
    int width;
    int height;
    String answer;


    public GraphDB(int width, int height) {
        this.width = width;
        this.height = height;
        nodes = new char[width][height];
    }

    public void add(String str, int rowIndex) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            nodes[i][rowIndex] = chars[i];
        }
    }

    public void createRandomBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Random r = new Random();
                nodes[x][y] = (char) (r.nextInt(26) + 'a');
            }
        }
    }

    public String getAnswer() {
        return answer;
    }

    public char[] getAdj(int x, int y) {
        if (x == 0) {
            if (y == 0) {

            }
        }
    }

    public void solver() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {


                for
            }
        }
    }



}

