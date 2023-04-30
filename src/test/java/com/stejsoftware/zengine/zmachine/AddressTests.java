package com.stejsoftware.zengine.zmachine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.stejsoftware.zengine.zmachine.processor.Address;
import com.stejsoftware.zengine.zmachine.processor.ByteAddress;

@SpringBootTest
public class AddressTests {
    @Test
    public void byteAddress() {

        Address address = new ByteAddress((short) 0x1234);
        
        assertThat(address.get(), is((short) 0x1234));
    }
}

