package school.chat.core;

import java.security.SecureRandom;

/**
 * class for generate tokens for ids
 */
public class Tokens {
    private final String symbols;
    private final int hashLength;
    private final SecureRandom random = new SecureRandom();

    public Tokens(String symbols, int hashLength) {
        this.symbols = symbols;
        this.hashLength = hashLength;
    }

    /**
     * Generate token use symbols label and length
     * 
     * @return generated token
     */
    public String genToken() {
        StringBuilder sb = new StringBuilder(hashLength);
        for (int i = 0; i < hashLength; i++) {
            int idx = random.nextInt(symbols.length());
            sb.append(symbols.charAt(idx));
        }
        return sb.toString();
    }
}