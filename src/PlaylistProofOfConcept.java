import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Proof of concept for the {@code Playlist} component (Design #1 from the
 * brainstorming doc). This single file deliberately ignores the OSU
 * discipline: it shows a few kernel-style methods, a few secondary-style
 * methods built only from those kernel methods, a possible representation,
 * and a {@code main} that plays the client role.
 */
public final class PlaylistProofOfConcept {

    /**
     * Immutable song value, so storing and returning songs never creates a
     * harmful alias.
     *
     * @param title
     *            the song title
     * @param artist
     *            the song artist
     * @param seconds
     *            the song duration in seconds
     */
    public record Song(String title, String artist, int seconds) {
        @Override
        public String toString() {
            return this.title + " - " + this.artist + " ("
                    + this.seconds / 60 + ":"
                    + String.format("%02d", this.seconds % 60) + ")";
        }
    }

    /**
     * Representation: songs in play order.
     */
    private final List<Song> songs = new ArrayList<>();

    // ---------------- Kernel-style methods ----------------

    /**
     * Inserts {@code s} at position {@code pos}.
     *
     * @param pos
     *            0 <= pos <= length()
     * @param s
     *            the song to insert
     */
    public void add(int pos, Song s) {
        this.songs.add(pos, s);
    }

    /**
     * Removes and returns the song at position {@code pos}.
     *
     * @param pos
     *            0 <= pos < length()
     * @return the removed song
     */
    public Song remove(int pos) {
        return this.songs.remove(pos);
    }

    /**
     * Reports the number of songs.
     *
     * @return the length
     */
    public int length() {
        return this.songs.size();
    }

    // ---------------- Secondary-style methods ----------------
    // Each uses only add / remove / length (not the field directly) to show
    // they could be layered on the kernel.

    /**
     * Moves the song at {@code from} so it ends up at {@code to}.
     *
     * @param from
     *            current position
     * @param to
     *            new position
     */
    public void moveSong(int from, int to) {
        Song s = this.remove(from);
        this.add(to, s);
    }

    /**
     * Reverses the order of the songs.
     */
    public void reverse() {
        PlaylistProofOfConcept temp = new PlaylistProofOfConcept();
        while (this.length() > 0) {
            temp.add(temp.length(), this.remove(this.length() - 1));
        }
        while (temp.length() > 0) {
            this.add(this.length(), temp.remove(0));
        }
    }

    /**
     * Reports the total runtime in seconds.
     *
     * @return total seconds
     */
    public int totalDuration() {
        int total = 0;
        PlaylistProofOfConcept temp = new PlaylistProofOfConcept();
        while (this.length() > 0) {
            Song s = this.remove(0);
            total += s.seconds();
            temp.add(temp.length(), s);
        }
        while (temp.length() > 0) {
            this.add(this.length(), temp.remove(0));
        }
        return total;
    }

    /**
     * Reports whether {@code s} is in this playlist.
     *
     * @param s
     *            the song to look for
     * @return true if present
     */
    public boolean contains(Song s) {
        boolean found = false;
        PlaylistProofOfConcept temp = new PlaylistProofOfConcept();
        while (this.length() > 0) {
            Song next = this.remove(0);
            if (next.equals(s)) {
                found = true;
            }
            temp.add(temp.length(), next);
        }
        while (temp.length() > 0) {
            this.add(this.length(), temp.remove(0));
        }
        return found;
    }

    /**
     * Keeps only the first copy of each song.
     */
    public void removeDuplicates() {
        Set<Song> seen = new HashSet<>();
        PlaylistProofOfConcept temp = new PlaylistProofOfConcept();
        while (this.length() > 0) {
            Song s = this.remove(0);
            if (seen.add(s)) {
                temp.add(temp.length(), s);
            }
        }
        while (temp.length() > 0) {
            this.add(this.length(), temp.remove(0));
        }
    }

    /**
     * Randomly reorders the songs.
     *
     * @param rnd
     *            source of randomness
     */
    public void shuffle(Random rnd) {
        PlaylistProofOfConcept temp = new PlaylistProofOfConcept();
        while (this.length() > 0) {
            int pos = rnd.nextInt(this.length());
            temp.add(temp.length(), this.remove(pos));
        }
        while (temp.length() > 0) {
            this.add(this.length(), temp.remove(0));
        }
    }

    /**
     * Adds all songs of {@code other} to the end of this and empties
     * {@code other}.
     *
     * @param other
     *            the playlist to append
     */
    public void append(PlaylistProofOfConcept other) {
        while (other.length() > 0) {
            this.add(this.length(), other.remove(0));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.songs.size(); i++) {
            sb.append(i + 1).append(". ").append(this.songs.get(i))
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    // ---------------- Client view ----------------

    /**
     * Demo of a client using the component.
     *
     * @param args
     *            unused
     */
    public static void main(String[] args) {
        PlaylistProofOfConcept gym = new PlaylistProofOfConcept();
        gym.add(0, new Song("Lose Yourself", "Eminem", 326));
        gym.add(1, new Song("Stronger", "Kanye West", 312));
        gym.add(2, new Song("Till I Collapse", "Eminem", 297));
        gym.add(3, new Song("Stronger", "Kanye West", 312)); // accidental dup

        System.out.println("== Gym playlist (" + gym.length() + " songs) ==");
        System.out.print(gym);

        gym.removeDuplicates();
        System.out.println("== After removeDuplicates ==");
        System.out.print(gym);

        gym.moveSong(2, 0);
        System.out.println("== After moving last song to the front ==");
        System.out.print(gym);

        PlaylistProofOfConcept chill = new PlaylistProofOfConcept();
        chill.add(0, new Song("Sunset Lover", "Petit Biscuit", 237));
        chill.add(1, new Song("Intro", "The xx", 127));
        gym.append(chill);
        System.out.println("== After appending chill (chill now has "
                + chill.length() + " songs) ==");
        System.out.print(gym);

        gym.reverse();
        System.out.println("== Reversed ==");
        System.out.print(gym);

        gym.shuffle(new Random(7));
        System.out.println("== Shuffled (seed 7) ==");
        System.out.print(gym);

        int total = gym.totalDuration();
        System.out.println("Total runtime: " + total / 60 + " min "
                + total % 60 + " sec");
        System.out.println("Contains 'Intro'? "
                + gym.contains(new Song("Intro", "The xx", 127)));
    }
}
