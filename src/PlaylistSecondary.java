import java.util.Random;

/**
 * Layered implementations of the secondary methods of {@code Playlist}, plus
 * the {@code Object} methods {@code toString}, {@code equals} and
 * {@code hashCode}.
 *
 * <p>
 * Everything here uses only the kernel methods ({@code add}, {@code remove},
 * {@code length}) and the {@code Standard} methods ({@code newInstance},
 * {@code transferFrom}); there are no fields and no knowledge of the
 * representation. Each kernel call is guarded so that its precondition holds:
 * loops only call {@code remove(pos)} while {@code length() > 0} with
 * {@code pos} in range, and only call {@code add(pos, s)} with {@code pos}
 * between 0 and {@code length()}.
 */
public abstract class PlaylistSecondary implements Playlist {

    /*
     * Secondary methods
     */

    @Override
    public final boolean contains(Song s) {
        assert s != null : "Violation of: s is not null";

        boolean found = false;
        Playlist temp = this.newInstance();
        while (this.length() > 0) {
            Song next = this.remove(0);
            if (next.equals(s)) {
                found = true;
            }
            temp.add(temp.length(), next);
        }
        this.transferFrom(temp);
        return found;
    }

    @Override
    public final int totalDuration() {
        int total = 0;
        Playlist temp = this.newInstance();
        while (this.length() > 0) {
            Song next = this.remove(0);
            total += next.seconds();
            temp.add(temp.length(), next);
        }
        this.transferFrom(temp);
        return total;
    }

    @Override
    public final void moveSong(int from, int to) {
        assert 0 <= from && from < this.length() : "Violation of: 0 <= from < |this|";
        assert 0 <= to && to < this.length() : "Violation of: 0 <= to < |this|";

        // after removing one song, |this| - 1 >= to, so add's precondition holds
        Song s = this.remove(from);
        this.add(to, s);
    }

    @Override
    public final void reverse() {
        Playlist temp = this.newInstance();
        while (this.length() > 0) {
            // inserting each song at the front of temp reverses the order
            temp.add(0, this.remove(0));
        }
        this.transferFrom(temp);
    }

    @Override
    public final void shuffle(Random rnd) {
        assert rnd != null : "Violation of: rnd is not null";

        Playlist temp = this.newInstance();
        while (this.length() > 0) {
            int pos = rnd.nextInt(this.length()); // 0 <= pos < |this|
            temp.add(temp.length(), this.remove(pos));
        }
        this.transferFrom(temp);
    }

    @Override
    public final void removeDuplicates() {
        Playlist kept = this.newInstance();
        while (this.length() > 0) {
            Song next = this.remove(0);
            if (!kept.contains(next)) {
                kept.add(kept.length(), next);
            }
        }
        this.transferFrom(kept);
    }

    @Override
    public final void append(Playlist other) {
        assert other != null : "Violation of: other is not null";
        assert other != this : "Violation of: other is not this";

        while (other.length() > 0) {
            this.add(this.length(), other.remove(0));
        }
    }

    /*
     * Common Object methods
     */

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        Playlist temp = this.newInstance();
        boolean first = true;
        while (this.length() > 0) {
            Song next = this.remove(0);
            if (!first) {
                result.append(", ");
            }
            first = false;
            result.append(next.title()).append(" - ").append(next.artist());
            temp.add(temp.length(), next);
        }
        this.transferFrom(temp);
        return result.append(">").toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Playlist)) {
            return false;
        }
        Playlist other = (Playlist) obj;
        if (this.length() != other.length()) {
            return false;
        }

        // compare front to back; keep going (no early exit) so that both
        // playlists are always restored before returning
        boolean same = true;
        Playlist thisTemp = this.newInstance();
        Playlist otherTemp = other.newInstance();
        while (this.length() > 0) {
            Song a = this.remove(0);
            Song b = other.remove(0);
            if (!a.equals(b)) {
                same = false;
            }
            thisTemp.add(thisTemp.length(), a);
            otherTemp.add(otherTemp.length(), b);
        }
        this.transferFrom(thisTemp);
        other.transferFrom(otherTemp);
        return same;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        Playlist temp = this.newInstance();
        while (this.length() > 0) {
            Song next = this.remove(0);
            hash = 31 * hash + next.hashCode();
            temp.add(temp.length(), next);
        }
        this.transferFrom(temp);
        return hash;
    }
}
