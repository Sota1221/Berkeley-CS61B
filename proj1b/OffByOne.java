/**
 * Created by sota_ on 2/4/2017.
 */
public class OffByOne implements CharacterComparator {

    public boolean equalChars(char x, char y) {
        if (x - y == 1 || y - x == 1) {
            return true;
        }
        return false;
    }
}
