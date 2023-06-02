import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void test(){
        int progress = getProgress(11125, 133380);
        System.out.println(progress);
    }

    public static int getProgress(int counter, int total) {
        return Math.round(100 * counter / total);
    }
}
