package commons;

import java.math.BigInteger;
import java.util.List;

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

    public static boolean isInBounds(int x, int y, int rowLength, int columnLength) {
        return x >= 0 && x < columnLength && y >= 0 && y < rowLength;
    }

    static boolean isPoint3DBetweenTwoPoints(Point3D pointToCheck, Point3D start, Point3D end) {
        return pointToCheck.x >= start.x && pointToCheck.x <= end.x && pointToCheck.y >= start.y && pointToCheck.y <= end.y;
    }

    public static boolean isLineIntersection(Point3D firstLineStart, Point3D firstLineEnd, Point3D secondLineStart, Point3D secondLineEnd) {
        if (isPoint3DBetweenTwoPoints(firstLineStart, secondLineStart, secondLineEnd) || isPoint3DBetweenTwoPoints(firstLineEnd, secondLineStart, secondLineEnd)) {
            return true;
        }
        return isPoint3DBetweenTwoPoints(secondLineStart, firstLineStart, firstLineEnd) || isPoint3DBetweenTwoPoints(secondLineEnd, firstLineStart, firstLineEnd);
    }

    public static void generateCharactersPermutationArray(List<char[]> operators, char[] possibleChars, String currentStr, int size) {
        if (currentStr.length() == size) {
            operators.add(currentStr.toCharArray());
            return;
        }

        for (char possibleChar : possibleChars) {
            generateCharactersPermutationArray(operators, possibleChars, currentStr + possibleChar, size);
        }
    }
}

