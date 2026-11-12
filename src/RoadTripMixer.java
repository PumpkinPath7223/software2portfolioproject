import java.util.Random;

/**
 * Use case 1: a client that works directly with {@code Playlist} in
 * {@code main}. Three friends each contribute songs for a road trip; the
 * program merges the lists, removes repeats, shuffles, and trims the result so
 * it fits the length of the drive.
 */
public final class RoadTripMixer {

    /** Length of the drive, in seconds. */
    private static final int DRIVE_SECONDS = 20 * 60;

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RoadTripMixer() {
    }

    /**
     * Removes songs from the end of {@code trip} until its total runtime is at
     * most {@code limit} seconds.
     *
     * @param trip
     *            the playlist to trim
     * @param limit
     *            the maximum total runtime in seconds
     * @updates trip
     * @requires limit >= 0
     * @ensures totalDuration(trip) <= limit and trip is a prefix of #trip
     */
    private static void trimToFit(Playlist trip, int limit) {
        assert limit >= 0 : "Violation of: limit >= 0";
        while (trip.totalDuration() > limit) {
            trip.remove(trip.length() - 1);
        }
    }

    /**
     * Prints {@code p} with a title and its total runtime.
     *
     * @param title
     *            the heading to print
     * @param p
     *            the playlist to print
     * @restores p
     */
    private static void show(String title, Playlist p) {
        int total = p.totalDuration();
        System.out.println(title + " (" + p.length() + " songs, "
                + total / 60 + " min " + total % 60 + " sec)");
        System.out.println("  " + p);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        Playlist alex = new Playlist1L();
        alex.add(0, new Song("Mr. Brightside", "The Killers", 223));
        alex.add(1, new Song("Dog Days Are Over", "Florence", 252));
        alex.add(2, new Song("Take On Me", "a-ha", 225));

        Playlist sam = new Playlist1L();
        sam.add(0, new Song("Take On Me", "a-ha", 225)); // repeat of Alex's
        sam.add(1, new Song("Africa", "Toto", 295));
        sam.add(2, new Song("Under Pressure", "Queen", 248));

        Playlist jo = new Playlist1L();
        jo.add(0, new Song("September", "Earth, Wind & Fire", 215));
        jo.add(1, new Song("Dancing Queen", "ABBA", 231));
        jo.add(2, new Song("Africa", "Toto", 295)); // repeat of Sam's

        Playlist trip = new Playlist1L();
        trip.append(alex);
        trip.append(sam);
        trip.append(jo);
        show("Everyone's songs merged", trip);

        trip.removeDuplicates();
        show("After removing repeats", trip);

        trip.shuffle(new Random(2026));
        show("Shuffled", trip);

        trimToFit(trip, DRIVE_SECONDS);
        show("Trimmed to fit a " + DRIVE_SECONDS / 60 + "-minute drive", trip);
    }
}
