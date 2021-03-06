/**
 *
 */
package zaxsoft.zmachine;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.stejsoftware.zengine.ZUserInterfaceImpl;
import com.zaxsoft.zmachine.ZCPU;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jon
 */

public class ZEngineTests {
    private static final Logger LOG = LoggerFactory.getLogger(ZEngineTests.class);

    @Test
    @Ignore
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
