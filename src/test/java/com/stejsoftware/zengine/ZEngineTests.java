/**
 *
 */
package com.stejsoftware.zengine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxsoft.zmachine.ZCPU;

/**
 * @author jon
 */

public class ZEngineTests {
    private static final Logger LOG = LoggerFactory.getLogger(ZEngineTests.class);

    @Test
    @Disabled
    public void cpuTest() {
        ZCPU cpu = new ZCPU(new ZUserInterfaceImpl());

        cpu.initialize("C:\\Projects\\z_engine\\archive\\zork1.z3");

        assertThat(cpu.is_running(), is(false));

        assertThat(cpu.start(), is(true));
        assertThat(cpu.is_running(), is(true));

        LOG.info("cpu");

        cpu.stop();

        assertThat(cpu.is_running(), is(false));
    }
}
