package com.compassuol.sp.challenge.msuser.feign;

import com.compassuol.sp.challenge.msuser.model.Address;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AddressFeignTest {

    @Mock
    private AddressFeign addressFeign;

    @Test
    public void getAddressByCepTest() {
        String cep = "12345678";
        Address expectedAddress = new Address();
        expectedAddress.setCep("12345678");
        expectedAddress.setCity("Cidade Exemplo");
        expectedAddress.setState("Estado Exemplo");

        when(addressFeign.getAddressByCep(cep)).thenReturn(expectedAddress);

        Address actualAddress = addressFeign.getAddressByCep(cep);

        assertEquals(expectedAddress, actualAddress);
    }
}
