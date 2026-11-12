/**
 * JUnit test class for {@code Playlist1L}. All test cases live in the abstract
 * parent fixtures; this class only supplies the implementation under test.
 */
public class Playlist1LTest extends PlaylistTest {

    @Override
    protected Playlist constructor() {
        return new Playlist1L();
    }
}
