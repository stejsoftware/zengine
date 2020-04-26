package com.stejsoftware.zengine.CPU;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.stejsoftware.zengine.processor.Address;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressTests {
    @Test
    @Ignore
    public void name() {
        Address a = new Address();

        assertThat(a.value(), is(0x1234));
    }
}
