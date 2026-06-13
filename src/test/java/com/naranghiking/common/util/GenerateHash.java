package com.naranghiking.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    @Test
    void generateHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("pass1234");
        System.out.println(hash);
        String adminHash = encoder.encode("admin1234");
        System.out.println(adminHash);
    }
}
