import components.standard.Standard;

/**
 * Kernel interface for the {@code Playlist} component: an ordered sequence of
 * {@link Song}s, like the queue in a music app. The kernel is deliberately
 * minimal; it can only insert a song at a position, remove the song at a
 * position, and report the number of songs.
 *
 * @mathmodel type PlaylistKernel is modeled by string of Song
 * @initially <pre>
 * ensures
 *  this = &lt;&gt;
 * </pre>
 */
public interface PlaylistKernel extends Standard<Playlist> {

    /**
     * Inserts {@code s} into {@code this} so that it ends up at position
     * {@code pos} (0-based); songs previously at {@code pos} or later shift
     * one position toward the end.
     *
     * @param pos
     *            the position at which to insert the song
     * @param s
     *            the song to insert
     * @updates this
     * @requires 0 <= pos <= |this| and s is not null
     * @ensures this = #this[0, pos) * &lt;s&gt; * #this[pos, |#this|)
     */
    void add(int pos, Song s);

    /**
     * Removes and returns the song at position {@code pos} (0-based); songs
     * after it shift one position toward the front.
     *
     * @param pos
     *            the position of the song to remove
     * @return the song that was at position {@code pos}
     * @updates this
     * @requires 0 <= pos < |this|
     * @ensures remove = #this[pos] and
     *          this = #this[0, pos) * #this[pos+1, |#this|)
     */
    Song remove(int pos);

    /**
     * Reports the number of songs in {@code this}.
     *
     * @return the number of songs in {@code this}
     * @ensures length = |this|
     */
    int length();
}
