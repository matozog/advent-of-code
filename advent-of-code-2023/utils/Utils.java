package utils;

public class Utils {

    public static int IntegerPower(int base, int exp) {
        if(exp < 0) return 0;
        int result = 1;
        for(int i = 0; i < exp; i ++) {
            result *= base;
        }
        return result;
    }
}
