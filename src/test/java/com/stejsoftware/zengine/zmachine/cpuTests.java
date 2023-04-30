/**
 *
 */
package com.stejsoftware.zengine.zmachine;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stejsoftware.zengine.zmachine.processor.CPU;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jon
 */

@SpringBootTest
@Slf4j
// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class cpuTests {

    @Autowired
    public CPU cpu;

    @Test
    public void cpu01GameLoadTest() {
        log.info("cpu01GameLoadTest");
        
        // Game game = Game.fromStoryFile("C:\\Projects\\zengine\\archive\\zork1.z3");
        //
        // assertThat(game.getVersion(), is(3));
        // assertThat(game.getRevision(), is(88));
        // assertThat(game.getSerialNumber(), is("840726"));
        // assertThat(game.getGameScore(), is(0));
        // assertThat(game.getTurnsTaken(), is(0));
        // assertThat(game.getStatusMessage(), is("Game Not Started"));
    }

    @Test
    @Disabled
    public void cpu02ExecuteTest() {
        // Game game = Game.fromStoryFile("C:\\Projects\\zengine\\archive\\zork1.z3");
        //
        // String response = cpu.execute(game);
        // assertThat(response, is("You are standing in an open field west of a white
        // house, with a boarded front door.\n" +
        // "There is a small mailbox here."));
        //
        // assertThat(game.getGameScore(), is(0));
        // assertThat(game.getStatusMessage(), is("West of House"));
        // assertThat(game.getTurnsTaken(), is(0));
    }
}
