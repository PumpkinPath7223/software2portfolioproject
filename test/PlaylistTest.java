import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Abstract JUnit test fixture for the secondary methods of {@code Playlist}
 * (the ones implemented in {@code PlaylistSecondary}). Inherits the kernel and
 * Standard tests from {@link PlaylistKernelTest}.
 */
public abstract class PlaylistTest extends PlaylistKernelTest {

    // ------------------------------------------------------------ contains

    @Test
    public final void testContainsEmpty() {
        assertFalse(this.constructor().contains(A));
    }

    @Test
    public final void testContainsPresentFirstMiddleLast() {
        Playlist p = this.create(A, B, C);
        assertTrue(p.contains(A));
        assertTrue(p.contains(B));
        assertTrue(p.contains(C));
        assertEquals(List.of(A, B, C), this.toList(p)); // restored
    }

    @Test
    public final void testContainsAbsent() {
        Playlist p = this.create(A, B);
        assertFalse(p.contains(D));
        assertEquals(List.of(A, B), this.toList(p));
    }

    @Test
    public final void testContainsUsesValueEquality() {
        Playlist p = this.create(new Song("Alpha", "Artist1", 100));
        assertTrue(p.contains(A));
    }

    // ------------------------------------------------------- totalDuration

    @Test
    public final void testTotalDurationEmpty() {
        assertEquals(0, this.constructor().totalDuration());
    }

    @Test
    public final void testTotalDurationOne() {
        assertEquals(100, this.create(A).totalDuration());
    }

    @Test
    public final void testTotalDurationManyRestores() {
        Playlist p = this.create(A, B, C, D);
        assertEquals(1000, p.totalDuration());
        assertEquals(List.of(A, B, C, D), this.toList(p));
    }

    @Test
    public final void testTotalDurationWithDuplicates() {
        assertEquals(200, this.create(A, A).totalDuration());
    }

    // ------------------------------------------------------------ moveSong

    @Test
    public final void testMoveSongSamePosition() {
        Playlist p = this.create(A, B, C);
        p.moveSong(1, 1);
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    @Test
    public final void testMoveSongSingle() {
        Playlist p = this.create(A);
        p.moveSong(0, 0);
        assertEquals(List.of(A), this.toList(p));
    }

    @Test
    public final void testMoveSongFrontToBack() {
        Playlist p = this.create(A, B, C);
        p.moveSong(0, 2);
        assertEquals(List.of(B, C, A), this.toList(p));
    }

    @Test
    public final void testMoveSongBackToFront() {
        Playlist p = this.create(A, B, C);
        p.moveSong(2, 0);
        assertEquals(List.of(C, A, B), this.toList(p));
    }

    @Test
    public final void testMoveSongMiddle() {
        Playlist p = this.create(A, B, C, D);
        p.moveSong(1, 2);
        assertEquals(List.of(A, C, B, D), this.toList(p));
    }

    // ------------------------------------------------------------- reverse

    @Test
    public final void testReverseEmpty() {
        Playlist p = this.constructor();
        p.reverse();
        assertEquals(0, p.length());
    }

    @Test
    public final void testReverseOne() {
        Playlist p = this.create(A);
        p.reverse();
        assertEquals(List.of(A), this.toList(p));
    }

    @Test
    public final void testReverseTwo() {
        Playlist p = this.create(A, B);
        p.reverse();
        assertEquals(List.of(B, A), this.toList(p));
    }

    @Test
    public final void testReverseMany() {
        Playlist p = this.create(A, B, C, D);
        p.reverse();
        assertEquals(List.of(D, C, B, A), this.toList(p));
    }

    @Test
    public final void testReverseTwiceIsIdentity() {
        Playlist p = this.create(A, B, C);
        p.reverse();
        p.reverse();
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    // ------------------------------------------------------------- shuffle

    @Test
    public final void testShuffleEmpty() {
        Playlist p = this.constructor();
        p.shuffle(new Random(1));
        assertEquals(0, p.length());
    }

    @Test
    public final void testShuffleOne() {
        Playlist p = this.create(A);
        p.shuffle(new Random(1));
        assertEquals(List.of(A), this.toList(p));
    }

    @Test
    public final void testShuffleIsPermutation() {
        Playlist p = this.create(A, B, C, D);
        p.shuffle(new Random(42));
        List<Song> result = this.toList(p);
        assertEquals(4, result.size());
        assertEquals(new HashSet<>(List.of(A, B, C, D)),
                new HashSet<>(result));
    }

    @Test
    public final void testShuffleIsReproducibleWithSameSeed() {
        Playlist p = this.create(A, B, C, D);
        Playlist q = this.create(A, B, C, D);
        p.shuffle(new Random(7));
        q.shuffle(new Random(7));
        assertEquals(this.toList(p), this.toList(q));
    }

    @Test
    public final void testShuffleCanChangeOrder() {
        // across several seeds, at least one result differs from the original
        boolean changed = false;
        for (long seed = 0; seed < 20 && !changed; seed++) {
            Playlist p = this.create(A, B, C, D);
            p.shuffle(new Random(seed));
            changed = !this.toList(p).equals(List.of(A, B, C, D));
        }
        assertTrue(changed);
    }

    // ----------------------------------------------------- removeDuplicates

    @Test
    public final void testRemoveDuplicatesEmpty() {
        Playlist p = this.constructor();
        p.removeDuplicates();
        assertEquals(0, p.length());
    }

    @Test
    public final void testRemoveDuplicatesNoDuplicates() {
        Playlist p = this.create(A, B, C);
        p.removeDuplicates();
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    @Test
    public final void testRemoveDuplicatesKeepsFirst() {
        Playlist p = this.create(A, B, A, C, B, A);
        p.removeDuplicates();
        assertEquals(List.of(A, B, C), this.toList(p));
    }

    @Test
    public final void testRemoveDuplicatesAllSame() {
        Playlist p = this.create(A, A, A);
        p.removeDuplicates();
        assertEquals(List.of(A), this.toList(p));
    }

    // -------------------------------------------------------------- append

    @Test
    public final void testAppendEmptyToEmpty() {
        Playlist p = this.constructor();
        Playlist q = this.constructor();
        p.append(q);
        assertEquals(0, p.length());
        assertEquals(0, q.length());
    }

    @Test
    public final void testAppendToEmpty() {
        Playlist p = this.constructor();
        Playlist q = this.create(A, B);
        p.append(q);
        assertEquals(List.of(A, B), this.toList(p));
        assertEquals(0, q.length());
    }

    @Test
    public final void testAppendEmptyToNonEmpty() {
        Playlist p = this.create(A, B);
        p.append(this.constructor());
        assertEquals(List.of(A, B), this.toList(p));
    }

    @Test
    public final void testAppendNonEmpty() {
        Playlist p = this.create(A, B);
        Playlist q = this.create(C, D);
        p.append(q);
        assertEquals(List.of(A, B, C, D), this.toList(p));
        assertEquals(new ArrayList<Song>(), this.toList(q)); // cleared
    }

    @Test
    public final void testAppendKeepsDuplicates() {
        Playlist p = this.create(A);
        p.append(this.create(A));
        assertEquals(List.of(A, A), this.toList(p));
    }
}
