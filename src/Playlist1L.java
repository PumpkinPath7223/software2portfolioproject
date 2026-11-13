import java.util.ArrayList;
import java.util.List;

/**
 * {@code Playlist} kernel implementation: a thin layer over
 * {@link java.util.ArrayList}.
 *
 * @convention <pre>
 * $this.songs is not null and
 * no element of $this.songs is null
 * </pre>
 * @correspondence <pre>
 * this = $this.songs, in order (index 0 is the first song to play)
 * </pre>
 */
public class Playlist1L extends PlaylistSecondary {

    /*
     * Private members
     */

    /**
     * Representation: the songs in play order.
     */
    private List<Song> songs;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.songs = new ArrayList<Song>();
    }

    /*
     * Constructors
     */

    /**
     * No-argument constructor: creates an empty playlist.
     */
    public Playlist1L() {
        this.createNewRep();
    }

    /*
     * Kernel methods
     */

    @Override
    public final void add(int pos, Song s) {
        assert s != null : "Violation of: s is not null";
        assert 0 <= pos && pos <= this.songs.size() : "Violation of: 0 <= pos <= |this|";

        this.songs.add(pos, s);
    }

    @Override
    public final Song remove(int pos) {
        assert 0 <= pos && pos < this.songs.size() : "Violation of: 0 <= pos < |this|";

        return this.songs.remove(pos);
    }

    @Override
    public final int length() {
        return this.songs.size();
    }

    /*
     * Standard methods
     */

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final Playlist newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void transferFrom(Playlist source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Playlist1L : ""
                + "Violation of: source is of dynamic type Playlist1L";

        Playlist1L localSource = (Playlist1L) source;
        this.songs = localSource.songs;
        localSource.createNewRep();
    }
}
