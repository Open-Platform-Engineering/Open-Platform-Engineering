package codes.showme.techlib.hash;

public interface HashService {

    PasswordSaltPair hash(String originalStr, String algorithmName, int randomSaltNum, int hashIterations);

    byte[] generateRandomSalt(int byteNum);

    public class PasswordSaltPair {
        private String password;
        private byte[] salt;

        private PasswordSaltPair(String password, byte[] salt) {
            this.password = password;
            this.salt = salt;
        }

        public static PasswordSaltPair of(String password, byte[] salt) {
            return new PasswordSaltPair(password, salt);
        }

        public byte[] getSalt() {
            return salt;
        }

        public void setSalt(byte[] salt) {
            this.salt = salt;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
