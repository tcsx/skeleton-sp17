public class OffByN implements CharacterComparator {
    private int _n;

    OffByN( int n) {
        _n = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return x - y == _n || y - x == _n;
    }
}