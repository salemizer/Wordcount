import com.wordcount.Wordcount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;


public class WordcountTest {

    @Before
    public void before(){

    }

    @Test
    public void testGetOccurenceMap(){

        Assert.assertNotNull(new Wordcount().getOccurenceMap());
    }

    @Test
    public void testCountWordsInText(){
         Assert.assertEquals(2, new Wordcount().countWordsInText("Hello World!"));
    }

    @Test
    public void testFindMostFrequent(){

        Wordcount wordCount = new Wordcount();
        Map <Integer, Integer> map =  new HashMap<Integer, Integer>();
        map.put(1,1);
        map.put(2,1);
        map.put(3,5);
        map.put(4,2);
        map.put(5,3);

        Assert.assertEquals(5, wordCount.findMostFrequent(map));
    }

    @Test
    public void testFindAverageLength(){
        Wordcount wordCount = new Wordcount();
        Map <Integer, Integer> map =  new HashMap<Integer, Integer>();
        map.put(1,1);
        map.put(2,1);
        map.put(3,2);
        map.put(4,3);
        map.put(5,5);

        Assert.assertEquals("3.00", wordCount.findAverageLength(map));
    }

}
