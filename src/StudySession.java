/**
 * Use case 2: {@code Playlist} used as part of the representation of another
 * component. A {@code StudySession} plays songs in order, can skip, go back
 * to replay, and move a favorite to the front. It never touches the playlist's
 * internals, only the {@code Playlist} interface.
 */
public final class StudySession {

    /** Songs not yet played, in play order. */
    private final Playlist upNext = new Playlist1L();

    /** Songs already played, most recent last. */
    private final Playlist played = new Playlist1L();

    /**
     * Queues {@code s} to play after everything already queued.
     *
     * @param s
     *            the song to queue
     * @requires s is not null
     */
    public void queue(Song s) {
        assert s != null : "Violation of: s is not null";
        this.upNext.add(this.upNext.length(), s);
    }

    /**
     * Reports whether any queued songs remain.
     *
     * @return true iff at least one song is still up next
     */
    public boolean hasNext() {
        return this.upNext.length() > 0;
    }

    /**
     * Plays the next song.
     *
     * @return the song that was played
     * @requires hasNext()
     */
    public Song playNext() {
        assert this.hasNext() : "Violation of: hasNext()";
        Song s = this.upNext.remove(0);
        this.played.add(this.played.length(), s);
        return s;
    }

    /**
     * Puts the most recently played song back at the front of the queue so
     * it plays again next.
     *
     * @requires at least one song has been played
     */
    public void replayLast() {
        assert this.played.length() > 0 : "Violation of: a song has been played";
        Song s = this.played.remove(this.played.length() - 1);
        this.upNext.add(0, s);
    }

    /**
     * Moves the queued song at position {@code pos} to the front of the queue.
     *
     * @param pos
     *            the queue position of the favorite
     * @requires 0 <= pos < number of queued songs
     */
    public void bumpToFront(int pos) {
        this.upNext.moveSong(pos, 0);
    }

    /**
     * Reports the minutes of music still queued.
     *
     * @return whole minutes of queued music
     */
    public int minutesLeft() {
        return this.upNext.totalDuration() / 60;
    }

    /**
     * Shows the queue.
     *
     * @return text for the up-next songs
     */
    public String upNextToString() {
        return this.upNext.toString();
    }

    /**
     * Main method: a short study session.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        StudySession session = new StudySession();
        session.queue(new Song("Weightless", "Marconi Union", 480));
        session.queue(new Song("Clair de Lune", "Debussy", 300));
        session.queue(new Song("Intro", "The xx", 127));
        session.queue(new Song("Divenire", "Einaudi", 407));

        System.out.println("Queue: " + session.upNextToString());
        System.out.println("Minutes of music: " + session.minutesLeft());

        session.bumpToFront(2);
        System.out.println("Bumped 'Intro' to front: " + session.upNextToString());

        Song first = session.playNext();
        System.out.println("Playing: " + first.title());
        session.replayLast();
        System.out.println("Replaying it: " + session.upNextToString());

        while (session.hasNext()) {
            System.out.println("Playing: " + session.playNext().title());
        }
        System.out.println("Done. Minutes left: " + session.minutesLeft());
    }
}
