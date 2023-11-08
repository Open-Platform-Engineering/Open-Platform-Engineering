package codes.showme.techlib.hash;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class HashServiceTest {

    @Test
    public void testSha256() {
        String originalStr = "abc";
        int hashIterations = 3;
        String algorithmName = "SHA-256";

        HashService.PasswordSaltPair passwordSaltPair = new HashServiceImpl().hash(originalStr, algorithmName, 12, hashIterations);
        org.apache.shiro.util.ByteSource saltByteSource = ByteSource.Util.bytes(passwordSaltPair.getSalt());
        SimpleHash simpleHash = new SimpleHash(algorithmName, originalStr, saltByteSource, hashIterations);

        Assert.assertEquals(simpleHash.toHex(), passwordSaltPair.getPassword());
    }
}
