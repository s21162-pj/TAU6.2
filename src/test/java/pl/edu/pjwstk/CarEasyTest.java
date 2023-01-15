package pl.edu.pjwstk;

import org.easymock.EasyMock;
import org.easymock.*;
import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CarEasyTest {
    private final Car myFerrari = EasyMock.createMock(Car.class);

    @Test
    public void test_instance_car(){
        assertNotNull(myFerrari);
    }

    @Test
    public void test_default_behavior_needsFuel(){
        assertFalse(myFerrari.needsFuel(), "New test double should return False as boolean");
    }

    @Test
    public void test_default_behavior_temperature(){
        assertEquals(0.0, myFerrari.getEngineTemperature(), "New test double should return 0.0");
    }

    @Test
    public void test_stubbing_mock(){
        EasyMock.expect(myFerrari.needsFuel()).andReturn(true);
        EasyMock.replay(myFerrari);
        assertTrue(myFerrari.needsFuel());

    }

    @Test
    public void test_exception(){
        EasyMock.expect(myFerrari.needsFuel()).andThrow(new RuntimeException());
        EasyMock.replay(myFerrari);
        assertThrows(RuntimeException.class, myFerrari::needsFuel);
    }

    @Test
    public void test_driveTo_destination(){
        myFerrari.driveTo("Paris");
        EasyMock.expectLastCall();
        EasyMock.replay(myFerrari);
        myFerrari.driveTo("Paris");
        EasyMock.verify(myFerrari);
    }

    @Test
    public void test_driveTo_multiple_destination(){
        myFerrari.driveTo("Paris");
        EasyMock.expectLastCall();
        myFerrari.driveTo("Rome");
        EasyMock.expectLastCall();
        EasyMock.replay(myFerrari);
        myFerrari.driveTo("Paris");
        myFerrari.driveTo("Rome");
        EasyMock.verify(myFerrari);
    }

    @Test
    public void test_driveTo_destination_with_exception() {
        myFerrari.driveTo(EasyMock.anyString());
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(myFerrari);
        assertThrows(IllegalArgumentException.class, () -> myFerrari.driveTo("New York"));
        EasyMock.verify(myFerrari);
    }

    @Test
    public void test_getEngineTemperature_with_stubbing() {
        EasyMock.expect(myFerrari.getEngineTemperature()).andReturn(10.0);
        EasyMock.replay(myFerrari);
        assertEquals(10.0, myFerrari.getEngineTemperature(), 0.1);
        EasyMock.verify(myFerrari);
    }

    @Test
    public void test_needsFuel_with_consecutive_calls() {
        EasyMock.expect(myFerrari.needsFuel()).andReturn(true).times(2);
        EasyMock.expect(myFerrari.needsFuel()).andReturn(false);
        EasyMock.replay(myFerrari);
        assertTrue(myFerrari.needsFuel());
        assertTrue(myFerrari.needsFuel());
        assertFalse(myFerrari.needsFuel());
        EasyMock.verify(myFerrari);
    }

}