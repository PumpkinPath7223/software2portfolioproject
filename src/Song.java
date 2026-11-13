/**
 * Immutable value describing a single song. Because a {@code Song} can never
 * change after construction, a {@code Playlist} can safely hand songs to and
 * receive songs from clients without creating harmful aliases.
 *
 * @param title
 *            the title of the song
 * @param artist
 *            the artist of the song
 * @param seconds
 *            the duration of the song in seconds
 */
public record Song(String title, String artist, int seconds) {

    /**
     * Creates a song.
     *
     * @throws IllegalArgumentException
     *             if {@code title} or {@code artist} is null, or if
     *             {@code seconds} is negative
     */
    public Song {
        if (title == null || artist == null || seconds < 0) {
            throw new IllegalArgumentException("Violation of: valid song");
        }
    }
}
