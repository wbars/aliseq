package me.wbars.aliseq.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NWAlignmentTest {
    private AlignmentAlgorithm nwAlignment;

    @Before
    public void setUp() throws Exception {
        nwAlignment = AlignmentAlgorithmFactory.createNWAlignment(Scoring.DEFAULT);
    }

    @Test
    public void simpleAlignment() throws Exception {
        Alignment alignment = nwAlignment.align("GCATGCU", "GATTACA");

        assertThat(alignment.first(), is("GCA-TGCU"));
        assertThat(alignment.second(), is("G-ATTACA"));
    }

    @Test
    public void differentLengthAlignment() throws Exception {
        Alignment alignment = nwAlignment.align("ACTTG", "TGA");

        assertThat(alignment.first(), is("ACTTG-"));
        assertThat(alignment.second(), is("---TGA"));
    }
}
