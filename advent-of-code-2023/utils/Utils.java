package utils;

import java.math.BigInteger;

public class Utils {

    public static int IntegerPower(int base, int exp) {
        if (exp < 0) return 0;
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
        }
        return result;
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return b.signum() == 0 ? a : gcd(b, a.mod(b));
    }

    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(gcd(a, b));
    }
}
