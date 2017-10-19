import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.LockFreeList;
import ru.spbau.mit.LockFreeListImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LockFreeListTest {

    private static final String TEST= "test";
    private static final String EMPTY = "";

    private LockFreeList<Integer> listInteger;
    private LockFreeList<String> listString;

    @Before
    public void initList() {
        listInteger = new LockFreeListImpl<>();
        listString = new LockFreeListImpl<>();
    }

    @Test
    public void duplicateValueTest() throws Exception {
        assertTrue(listInteger.isEmpty());

        listInteger.append(1);
        listInteger.append(1);
        listInteger.append(2);
        listInteger.append(3);

        assertTrue(listInteger.contains(1));
        assertTrue(listInteger.remove(1));

        assertTrue(listInteger.contains(1));
        assertTrue(listInteger.remove(1));

        assertFalse(listInteger.remove(1));
        assertFalse(listInteger.contains(1));

        assertFalse(listInteger.isEmpty());
    }


    @Test
    public void isEmptyTest() {
        assertTrue(listString.isEmpty());
    }

    @Test
    public void isNotEmptyTest() {
        listString.append(TEST);
        assertFalse(listString.isEmpty());
    }

    @Test
    public void containsTest() {
        listString.append(TEST);
        assertTrue(listString.contains(TEST));
    }

    @Test
    public void notContainsTest() {
        listString.append(EMPTY);
        assertFalse(listString.contains(TEST));
    }

    @Test
    public void addTest() {
        listString.append(TEST);
        assertTrue(listString.contains(TEST));
    }

    @Test
    public void repeatValueTest() {
        listString.append(TEST);
        assertTrue(listString.contains(TEST));
        listString.append(TEST);
        assertTrue(listString.contains(TEST));

        listString.remove(TEST);
        assertTrue(listString.contains(TEST));

        listString.remove(TEST);
        assertFalse(listString.contains(TEST));

        assertTrue(listString.isEmpty());
    }

    @Test
    public void removeTest() {
        listString.append(TEST);
        assertTrue(listString.remove(TEST));
        assertFalse(listString.contains(TEST));
    }

    @Test
    public void doNotRemoveTest() {
        assertFalse(listString.remove(TEST));
        assertFalse(listString.contains(TEST));
    }

    @Test
    public void listPropertyTest() {
        List<Integer> list = new ArrayList<>();
        int dataCount = 500;
        int bound = 10;
        Random random = new Random();
        for (int i = 0; i < dataCount; i++) {
            int value = random.nextInt(bound);
            list.add(value);
            listInteger.append(value);
            assertTrue(listInteger.contains(value));
            assertFalse(listInteger.isEmpty());
        }

        for (Integer value : list) {
            assertTrue(listInteger.contains(value));
            assertTrue(listInteger.remove(value));
        }
        assertTrue(listInteger.isEmpty());
    }

}
