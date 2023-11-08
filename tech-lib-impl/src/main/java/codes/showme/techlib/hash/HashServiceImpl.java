package codes.showme.techlib.hash;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashServiceImpl implements HashService {

    private static final Logger logger = LoggerFactory.getLogger(HashServiceImpl.class);

    @Override
    public PasswordSaltPair hash(String originalStr, String algorithmName, int randomSaltNum, int hashIterations) {
        byte[] salt = generateRandomSalt(randomSaltNum);
        ByteSource saltByteSource = ByteSource.Util.bytes(salt);
        SimpleHash simpleHash = new SimpleHash(algorithmName, originalStr, saltByteSource, hashIterations);
        return PasswordSaltPair.of(simpleHash.toHex(), salt);
    }

    @Override
    public byte[] generateRandomSalt(int byteNum) {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        ByteSource salt = generator.nextBytes(byteNum);
        return salt.getBytes();
    }
}
