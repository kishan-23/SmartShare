package SERVER;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Password {
    String pass, hash;
    String algo = "MD5";
    public Password(String str)
    {
        this.pass = str;
        byte[] b = str.getBytes();
        this.hash = this.generateHash(b);
    }

    String generateHash(byte[] b)
    {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algo);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.reset();
        byte[] b2 = digest.digest(b);
        String output = String.format("%032X", new BigInteger(1, b2));
        return output;
    }

    String getHash()
    {
        return this.hash;
    }

}
