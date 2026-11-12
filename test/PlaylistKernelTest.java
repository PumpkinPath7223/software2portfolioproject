import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Abstract JUnit test fixture for the kernel and Standard methods of
 * {@code Playlist}. Subclasses supply the implementation under test.
 *
 * <p>
 * There is no reference implementation, so every check reads state through the
 * kernel only (see {@link #toList(Playlist)}) and also verifies that observer
 * methods leave their receiver unchanged.
 */
public abstract class PlaylistKernelTest {

    /** Sample songs used throughout the tests. */
    protected static final Song A = new Song("Alpha", "Artist1", 100);
    protected static final Song B = new Song("Beta", "Artist2", 200);
    protected static final Song C = new Song("Gamma", "Artist1", 300);
    protected static final Song D = new Song("Delta", "Artist3", 400);

    /**
     * Invokes the constructor of the implementation under test.
     *
     * @return a new, empty playlist
     * @ensures constructor = <>
     */
    protected abstract Playlist constructor();

    /**
     * Builds a playlist containing {@code songs} in order, using only the
     * kernel.
     *
     * @param songs
     *            the songs to add
     * @return the new playlist
     */
    protected final Playlist create(Song... songs) {
        Playlist p = this.constructor();
        for (Song s : songs) {
            p.add(p.length(), s);
        }
        return p;
    }

    /**
     * Reads {@code p} into a Java list using only the kernel, then restores
     * {@code p}. Used as the independent oracle for the tests.
     *
     * @param p
     *            the playlist to read
     * @return the songs of {@code p} in order
     * @restores p
     */
    protected final List<Song> toList(Playlist p) {
        List<Song> result = new ArrayList<>();
        Playlist temp = p.newInstance();
        while (p.length() > 0) {
            Song s = p.remove(0);
            result.add(s);
            temp.add(temp.length(), s);
        }
        p.transferFrom(temp);
        return result;
    }

    // ---------------------------------------------------------------- ctor

    @Test
    public final void testConstructorEmpty() {
        Playlist p = this.constructor();
        assertEquals(0, p.length());
        assertEquals(new ArrayList<Song>(), this.toList(p));
    }

    // ----------------------------------------------------------------- add

    @Test
    public final void testAddToEmpty() {
        Playlist p = this.constructor();
        p.add(0, A);
        assertEquals(List.of(A), this.toList(p));
    }

    @Test
    public final void testAddAtEnd() {
        Playlist p = this.create(A, B);
        p.add(2, C);
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    @Test
    public final void testAddAtFront() {
        Playlist p = this.create(A, B);
        p.add(0, C);
        assertEquals(List.of(C, A, B), this.toList(p));
    }

    @Test
    public final void testAddInMiddle() {
        Playlist p = this.create(A, B, C);
        p.add(1, D);
        assertEquals(List.of(A, D, B, C), this.toList(p));
    }

    @Test
    public final void testAddDuplicateSong() {
        Playlist p = this.create(A);
        p.add(1, A);
        assertEquals(List.of(A, A), this.toList(p));
    }

    // -------------------------------------------------------------- remove

    @Test
    public final void testRemoveOnlySong() {
        Playlist p = this.create(A);
        Song s = p.remove(0);
        assertEquals(A, s);
        assertEquals(0, p.length());
    }

    @Test
    public final void testRemoveFirst() {
        Playlist p = this.create(A, B, C);
        assertEquals(A, p.remove(0));
        assertEquals(List.of(B, C), this.toList(p));
    }

    @Test
    public final void testRemoveLast() {
        Playlist p = this.create(A, B, C);
        assertEquals(C, p.remove(2));
        assertEquals(List.of(A, B), this.toList(p));
    }

    @Test
    public final void testRemoveMiddle() {
        Playlist p = this.create(A, B, C);
        assertEquals(B, p.remove(1));
        assertEquals(List.of(A, C), this.toList(p));
    }

    // -------------------------------------------------------------- length

    @Test
    public final void testLengthEmpty() {
        Playlist p = this.constructor();
        assertEquals(0, p.length());
        assertEquals(0, p.length()); // observer does not change state
    }

    @Test
    public final void testLengthNonEmptyRestores() {
        Playlist p = this.create(A, B, C);
        assertEquals(3, p.length());
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    @Test
    public final void testLengthTracksAddAndRemove() {
        Playlist p = this.constructor();
        p.add(0, A);
        p.add(1, B);
        assertEquals(2, p.length());
        p.remove(0);
        assertEquals(1, p.length());
    }

    // ------------------------------------------------------------ Standard

    @Test
    public final void testClearEmpty() {
        Playlist p = this.constructor();
        p.clear();
        assertEquals(0, p.length());
    }

    @Test
    public final void testClearNonEmpty() {
        Playlist p = this.create(A, B, C);
        p.clear();
        assertEquals(0, p.length());
        p.add(0, D); // still usable afterward
        assertEquals(List.of(D), this.toList(p));
    }

    @Test
    public final void testNewInstanceIsEmptyAndDistinct() {
        Playlist p = this.create(A, B);
        Playlist q = p.newInstance();
        assertNotSame(p, q);
        assertEquals(0, q.length());
        assertEquals(List.of(A, B), this.toList(p)); // p unchanged
    }

    @Test
    public final void testNewInstanceSameType() {
        Playlist p = this.constructor();
        assertEquals(p.getClass(), p.newInstance().getClass());
    }

    @Test
    public final void testTransferFromNonEmptyToEmpty() {
        Playlist source = this.create(A, B, C);
        Playlist target = this.constructor();
        target.transferFrom(source);
        assertEquals(List.of(A, B, C), this.toList(target));
        assertEquals(0, source.length());
    }

    @Test
    public final void testTransferFromReplacesTarget() {
        Playlist source = this.create(A);
        Playlist target = this.create(B, C, D);
        target.transferFrom(source);
        assertEquals(List.of(A), this.toList(target));
        assertEquals(0, source.length());
    }

    @Test
    public final void testTransferFromEmptySource() {
        Playlist source = this.constructor();
        Playlist target = this.create(A, B);
        target.transferFrom(source);
        assertEquals(0, target.length());
        assertEquals(0, source.length());
    }

    @Test
    public final void testTransferFromThenSourceIndependent() {
        Playlist source = this.create(A, B);
        Playlist target = this.constructor();
        target.transferFrom(source);
        source.add(0, C); // must not alias target's data
        assertEquals(List.of(A, B), this.toList(target));
        assertEquals(List.of(C), this.toList(source));
    }

    // ------------------------------------------------- Object methods

    @Test
    public final void testEqualsSameContents() {
        Playlist p = this.create(A, B, C);
        Playlist q = this.create(A, B, C);
        assertTrue(p.equals(q));
        assertEquals(List.of(A, B, C), this.toList(p)); // both restored
        assertEquals(List.of(A, B, C), this.toList(q));
    }

    @Test
    public final void testEqualsReflexive() {
        Playlist p = this.create(A, B);
        assertTrue(p.equals(p));
    }

    @Test
    public final void testEqualsDifferentOrder() {
        assertEquals(false, this.create(A, B).equals(this.create(B, A)));
    }

    @Test
    public final void testEqualsDifferentLength() {
        assertEquals(false, this.create(A, B).equals(this.create(A)));
    }

    @Test
    public final void testEqualsNullAndOtherType() {
        Playlist p = this.create(A);
        assertEquals(false, p.equals(null));
        assertEquals(false, p.equals("not a playlist"));
    }

    @Test
    public final void testEqualsBothEmpty() {
        assertTrue(this.constructor().equals(this.constructor()));
    }

    @Test
    public final void testHashCodeConsistentWithEquals() {
        Playlist p = this.create(A, B);
        Playlist q = this.create(A, B);
        assertEquals(p.hashCode(), q.hashCode());
        assertEquals(List.of(A, B), this.toList(p));
    }

    @Test
    public final void testToStringEmpty() {
        assertEquals("<>", this.constructor().toString());
    }

    @Test
    public final void testToStringNonEmptyRestores() {
        Playlist p = this.create(A, B);
        assertEquals("<Alpha - Artist1, Beta - Artist2>", p.toString());
        assertEquals(List.of(A, B), this.toList(p));
    }
}
