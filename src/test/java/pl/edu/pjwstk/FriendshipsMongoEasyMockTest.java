package pl.edu.pjwstk;

import org.easymock.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;


import static junit.framework.TestCase.assertFalse;
import static org.easymock.EasyMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

@ExtendWith(EasyMockExtension.class)
public class FriendshipsMongoEasyMockTest {

    @TestSubject
    FriendshipsMongo friendships = new FriendshipsMongo();

    //A nice mock expects recorded calls in any order and returning null for other calls
    @Mock(type = MockType.NICE)
    FriendsCollection friends;

    @Test
    public void mockingWorksAsExpected(){
        Person joe = new Person("Joe");
        //Zapisanie zachowania - co sie ma stac
        expect(friends.findByName("Joe")).andReturn(joe);
        //Odpalenie obiektu do sprawdzenia zachowania
        replay(friends);
        assertThat(friends.findByName("Joe")).isEqualTo(joe);
    }

    @Test
    public void alexDoesNotHaveFriends(){
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

    @Test
    public void joeHas5Friends(){
        List<String> expected = Arrays.asList("Karol","Dawid","Maciej","Tomek","Adam");
        Person joe = createMock(Person.class);
        expect(friends.findByName("Joe")).andReturn(joe);
        expect(joe.getFriends()).andReturn(expected);
        replay(friends);
        replay(joe);
        assertThat(friendships.getFriendsList("Joe")).hasSize(5).containsOnly("Karol","Dawid","Maciej","Tomek","Adam");
    }

    @Test
    public void testMakeFriends() {
        Person p1 = createMock(Person.class);
        Person p2 = createMock(Person.class);
        expect(friends.findByName("person1")).andReturn(p1);
        expect(friends.findByName("person2")).andReturn(p2);
        p1.addFriend("person2");
        p2.addFriend("person1");
        replay(friends);
        replay(p1);
        replay(p2);
        friendships.makeFriends("person1", "person2");
        verify(p1);
        verify(p2);
    }

    @Test
    public void testAreFriendsWhenNotFriends() {
        Person p1 = createMock(Person.class);
        expect(friends.findByName("person1")).andReturn(p1);
        expect(p1.getFriends()).andReturn(Arrays.asList());
        replay(friends);
        replay(p1);
        assertFalse(friendships.areFriends("person1", "person2"));
        verify(p1);
    }

    @Test
    public void testAreFriendsWhenOneIsNotInDB() {
        expect(friends.findByName("person1")).andReturn(null);
        replay(friends);
        assertFalse(friendships.areFriends("person1", "person2"));
    }

    @Test
    public void testAddFriendWhenPersonNotInDB() {
        Person p = createMock(Person.class);
        expect(friends.findByName("person1")).andReturn(null);
        replay(friends);
        replay(p);
        friendships.addFriend("person1", "friend1");
        verify(friends);
        verify(p);
    }

    @Test
    public void testAddFriendWhenPersonIsNull() {
        expect(friends.findByName("person1")).andReturn(null);
        replay(friends);
        friendships.addFriend("person1", "friend1");
        verify(friends);
    }

    @Test
    public void testGetFriendsListWhenPersonNotInDB() {
        expect(friends.findByName("Alex")).andReturn(null);
        replay(friends);
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

    @Test
    public void testGetFriendsListWhenPersonHasNoFriends() {
        Person p = createMock(Person.class);
        expect(friends.findByName("Alex")).andReturn(p);
        expect(p.getFriends()).andReturn(Arrays.asList());
        replay(friends);
        replay(p);
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

}