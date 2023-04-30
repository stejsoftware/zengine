/**
 *
 */
package com.stejsoftware.zengine;

import com.stejsoftware.zengine.controller.ControllerConfig;
import com.stejsoftware.zengine.data.Game;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author jon
 */

@Slf4j
@SpringBootTest(classes = {ZEngine.class})
public class ZEngineTest {

    @Autowired
    private ZEngine zEngine;

    @MockBean
    private ZGames mockZGames;
    @MockBean
    private ControllerConfig mockControllerConfig;
    @Mock
    private ZGame mockZGame;

    @Test
    void getGame() {
        when(mockZGame.toGame())
                .thenReturn(Game.builder().build());
        when(mockZGames.get(anyString()))
                .thenReturn(mockZGame);

        assertThat(zEngine.getGame("1")).isNotNull();
    }
}
