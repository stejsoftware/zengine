package com.stejsoftware.zengine.CPU;

import com.stejsoftware.zengine.processor.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressTests {
    @Test
    public void name() {
        Address a = new Address();

        assertThat(a.value(), is(0x1234));
    }
}
