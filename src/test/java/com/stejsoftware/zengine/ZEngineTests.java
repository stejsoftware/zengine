/**
 *
 */
package com.stejsoftware.zengine;

import com.zaxsoft.zmachine.ZCPU;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author jon
 */

@Slf4j
public class ZEngineTests {

    @Test
    @Disabled
    public void cpuTest() {
        ZCPU cpu = new ZCPU(new ZUserInterfaceImpl());

        cpu.initialize("C:\\Projects\\z_engine\\archive\\zork1.z3");

        assertThat(cpu.is_running(), is(false));

        assertThat(cpu.start(), is(true));
        assertThat(cpu.is_running(), is(true));

        log.info("cpu");

        cpu.stop();

        assertThat(cpu.is_running(), is(false));
    }
}
