package me.wbars.aliseq.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NWAlignmentTest {

    @Test
    public void simpleAlignment() throws Exception {
        AlignmentAlgorithm nwAlignment = AlignmentAlgorithmFactory.createNWAlignment("GCATGCU", "GATTACA");
        Alignment alignment = nwAlignment.align();

        assertThat(alignment.first(), is("GCA-TGCU"));
        assertThat(alignment.second(), is("G-ATTACA"));
    }

    @Test
    public void differentLengthAlignment() throws Exception {
        AlignmentAlgorithm nwAlignment = AlignmentAlgorithmFactory.createNWAlignment("ACTTG", "TGA");
        Alignment alignment = nwAlignment.align();

        assertThat(alignment.first(), is("ACTTG-"));
        assertThat(alignment.second(), is("---TGA"));
    }
}
