package com.example.edit;

import com.example.experiment.InputGenerator;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EditDistanceTest {

    private final EditDistance[] versions = {
            new RecursiveEditDistance(), new TableEditDistance(), new TwoRowEditDistance()
    };

    @Test
    void referenceValues() {
        for (EditDistance v : versions) {
            assertEquals(3, v.distance("kitten", "sitting"), v.name());
            assertEquals(2, v.distance("flaw", "lawn"), v.name());
            assertEquals(5, v.distance("intention", "execution"), v.name());
            assertEquals(3, v.distance("", "abc"), v.name());
            assertEquals(0, v.distance("abc", "abc"), v.name());
        }
    }

    @Test
    void emptyStrings() {
        for (EditDistance v : versions) {
            assertEquals(0, v.distance("", ""), v.name());
            assertEquals(3, v.distance("abc", ""), v.name());
            assertEquals(1, v.distance("a", ""), v.name());
        }
    }

    @Test
    void threeVersionsAgreeOnRandomShortStrings() {
        InputGenerator gen = new InputGenerator(42);
        Random rnd = new Random(7);
        for (int k = 0; k < 100; k++) {
            String a = gen.randomString(rnd.nextInt(8), 3);
            String b = gen.randomString(rnd.nextInt(8), 3);
            int expected = versions[1].distance(a, b);
            assertEquals(expected, versions[0].distance(a, b), a + " / " + b);
            assertEquals(expected, versions[2].distance(a, b), a + " / " + b);
        }
    }

    @Test
    void countersAreMeasuredInsideTheAlgorithms() {
        TableEditDistance table = new TableEditDistance();
        TwoRowEditDistance twoRows = new TwoRowEditDistance();
        table.distance("kitten", "sitting");
        twoRows.distance("kitten", "sitting");
        assertEquals(7 * 8, table.getOps());
        assertEquals(table.getOps(), twoRows.getOps());
        assertEquals(7 * 8, table.getMemoryCells());
        assertEquals(2 * 8, twoRows.getMemoryCells());
    }

    @Test
    void recursionIsLinearOnIdenticalStringsAndBlowsUpOnDisjointOnes() {
        RecursiveEditDistance rec = new RecursiveEditDistance();
        rec.distance("AAAAAAAA", "AAAAAAAA");
        assertEquals(9, rec.getOps());
        rec.distance("AAAAAAAA", "BBBBBBBB");
        assertTrue(rec.getOps() > 100_000);
    }
}