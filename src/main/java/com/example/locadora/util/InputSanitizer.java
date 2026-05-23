package com.example.locadora.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

    public String sanitize(String value) {
        if (value == null) {
            return null;
        }
        // Remove scripts e HTML malicioso (mitigação de XSS)
        return Jsoup.clean(value, Safelist.basic());
    }
}