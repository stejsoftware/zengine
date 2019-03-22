package com.stejsoftware.zengine.zmachine;

import com.stejsoftware.zengine.zmachine.processor.Address;
import com.stejsoftware.zengine.zmachine.processor.ByteAddress;
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
    public void byteAddress() {

        Address address = new ByteAddress((short) 0x1234);
        assertThat(address.get(), is((short) 0x1234));
    }
}

