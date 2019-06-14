package com.zhengcq.srv.core.common.support.security;

import java.util.Date;

/**
 * Created by clude on 9/28/17.
 */
public class BASE36Encoder {
    private String characters;

    /**
     * Constructs a Base36 object with the default charset (0..9a..zA..Z).
     */
    public BASE36Encoder() {
        this("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Constructs a Base36 object with a custom charset.
     *
     * @param characters
     *            the charset to use. Must be 36 characters.
     * @throws <code>IllegalArgumentException<code> if the supplied charset is not 36 characters long.
     */
    public BASE36Encoder(String characters) {
        if (!(characters.length() == 36)) {
            throw new IllegalArgumentException("Invalid string length, must be 36.");
        }
        this.characters = characters;
    }

    /**
     * Encodes a decimal value to a Base36 <code>String</code>.
     *
     * @param b10
     *            the decimal value to encode, must be nonnegative.
     * @return the number encoded as a Base36 <code>String</code>.
     */
    public String encodeBase10(long b10) {
        if (b10 < 0) {
            throw new IllegalArgumentException("b10 must be nonnegative");
        }
        String ret = "";
        while (b10 > 0) {
            ret = characters.charAt((int) (b10 % 36)) + ret;
            b10 /= 36;
        }
        return ret;

    }

    /**
     * Decodes a Base36 <code>String</code> returning a <code>long</code>.
     *
     * @param b36
     *            the Base36 <code>String</code> to decode.
     * @return the decoded number as a <code>long</code>.
     * @throws IllegalArgumentException
     *             if the given <code>String</code> contains characters not
     *             specified in the constructor.
     */
    public long decodeBase36(String b36) {
        for (char character : b36.toCharArray()) {
            if (!characters.contains(String.valueOf(character))) {
                throw new IllegalArgumentException("Invalid character(s) in string: " + character);
            }
        }
        long ret = 0;
        b36 = new StringBuffer(b36).reverse().toString();
        long count = 1;
        for (char character : b36.toCharArray()) {
            ret += characters.indexOf(character) * count;
            count *= 36;
        }
        return ret;
    }

    // Examples
    public static void main(String[] args) throws InterruptedException {
        Long ts = new Date().getTime() / 1000;

        System.out.println(ts);
        // Create a Base36 object using the default charset.
        BASE36Encoder base36 = new BASE36Encoder();
        System.gc();
        // Create a Base36 object with an alternate charset.
        BASE36Encoder Base36alt = new BASE36Encoder("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        // Convert 1673 to Base36 (qZ).
        System.out.println("1507256343l encoded to Base36: " + base36.encodeBase10(1507256343l));

        // Convert 1673 to Base36 with the alternate character set (A9).
        System.out.println("1507256343l encoded with alternate charset: " + Base36alt.encodeBase10(1507256343l));

        // Convert nHkl3S4B to decimal (83,458,179,955,437).
        System.out.println("nHkl3S4B decoded from Base36: " + base36.decodeBase36("nHkl3S4B"));

        // Encoding and decoding a number returns the original result.
        System.out.println("32442342 encoded to Base36 and back again: "
                + base36.decodeBase36(base36.encodeBase10(32442342)));

        // Using invalid characters throws a runtime exception.
        // Output was out of order with ant, adding this short sleep fixes
        // things:
        // The problem seems to be with the way ant's output handles system.err
        Thread.sleep(100);
        try {
            // Doesn't work
            System.out.println(base36.decodeBase36("_j+j%"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
}
