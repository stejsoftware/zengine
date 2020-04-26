/**
 *
 */
package com.stejsoftware.zengine.CPU;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.stejsoftware.zengine.data.Game;
import com.stejsoftware.zengine.processor.CPU;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jon
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class cpuTests {
    private static final Logger LOG = LoggerFactory.getLogger(cpuTests.class);

    @Autowired
    public CPU cpu;

    @Test
    @Ignore
    public void cpu01GameLoadTest() {
        Game game = Game.fromStoryFile("stories/minizork.z3");

        LOG.debug(game.getStoryFile());

        assertThat(game.getVersion(), is(3));
        assertThat(game.getRevision(), is(88));
        assertThat(game.getSerialNumber(), is("840726"));
        assertThat(game.getGameScore(), is(0));
        assertThat(game.getTurnsTaken(), is(0));
        assertThat(game.getStatusMessage(), is("Game Not Started"));
    }

    @Test
    @Ignore
    public void cpu02ExecuteTest() {
        Game game = Game.fromStoryFile("stories/minizork.z3");

        String response = cpu.execute(game);
        assertThat(response, is("You are standing in an open field west of a white house, with a boarded front door.\n"
                + "There is a small mailbox here."));

        assertThat(game.getGameScore(), is(0));
        assertThat(game.getStatusMessage(), is("West of House"));
        assertThat(game.getTurnsTaken(), is(0));
    }
}
