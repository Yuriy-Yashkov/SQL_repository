package cipher;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * @author Andy 06.06.2018 - 12:27.
 */
public class CipherTest {

    @Test
    @Ignore
    public void testCipher() {
        CipherReader cipherReader = new CipherReader("COM4");
        List<String> list = cipherReader.ReadScanData();
        for (String s : list) {
            System.out.println("OUTPUT: " + s);
        }

    }
}
