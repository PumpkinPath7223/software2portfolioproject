import java.util.Random;

/**
 * Enhanced interface for the {@code Playlist} component. Every method here
 * can be implemented using only {@link PlaylistKernel} methods and the
 * {@code Standard} methods ({@code clear}, {@code newInstance},
 * {@code transferFrom}).
 */
public interface Playlist extends PlaylistKernel {

    /**
     * Reports whether {@code s} appears anywhere in {@code this}.
     *
     * @param s
     *            the song to look for
     * @return true iff {@code s} is in {@code this}
     * @restores this
     * @requires s is not null
     * @ensures contains = (there exists i: 0 <= i < |this| such that
     *          this[i] = s)
     */
    boolean contains(Song s);

    /**
     * Reports the total runtime of {@code this} in seconds.
     *
     * @return the sum of the durations of all songs in {@code this}
     * @restores this
     * @ensures totalDuration = sum of the seconds of every song in this
     */
    int totalDuration();

    /**
     * Moves the song at position {@code from} so that it ends up at position
     * {@code to}; the relative order of all other songs is unchanged.
     *
     * @param from
     *            the current position of the song to move
     * @param to
     *            the position the song should have after the move
     * @updates this
     * @requires 0 <= from < |this| and 0 <= to < |this|
     * @ensures this is #this with the song at position from removed and
     *          reinserted so that it is at position to
     */
    void moveSong(int from, int to);

    /**
     * Reverses the order of the songs in {@code this}.
     *
     * @updates this
     * @ensures this = reverse(#this)
     */
    void reverse();

    /**
     * Randomly reorders the songs in {@code this}.
     *
     * @param rnd
     *            the source of randomness
     * @updates this
     * @requires rnd is not null
     * @ensures this is a permutation of #this
     */
    void shuffle(Random rnd);

    /**
     * Removes every repeated song, keeping only the first occurrence of each
     * song and preserving the order of those first occurrences.
     *
     * @updates this
     * @ensures this contains no duplicate songs and this is #this with every
     *          non-first occurrence of a song removed
     */
    void removeDuplicates();

    /**
     * Adds all songs of {@code other} to the end of {@code this}, leaving
     * {@code other} empty.
     *
     * @param other
     *            the playlist whose songs are appended
     * @updates this
     * @clears other
     * @requires other is not this
     * @ensures this = #this * #other
     */
    void append(Playlist other);
}
