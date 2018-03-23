// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

/**
 * A matcher for a single currency instance (not the full trie).
 */
public class CurrencyCustomMatcher implements NumberParseMatcher {

    private final String isoCode;
    private final String currency1;
    private final String currency2;

    public static CurrencyCustomMatcher getInstance(Currency currency, ULocale loc) {
        return new CurrencyCustomMatcher(currency.getSubtype(),
                currency.getSymbol(loc),
                currency.getCurrencyCode());
    }

    private CurrencyCustomMatcher(String isoCode, String currency1, String currency2) {
        this.isoCode = isoCode;
        this.currency1 = currency1;
        this.currency2 = currency2;
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        if (result.currencyCode != null) {
            return false;
        }

        int overlap1 = segment.getCommonPrefixLength(currency1);
        if (overlap1 == currency1.length()) {
            result.currencyCode = isoCode;
            segment.adjustOffset(overlap1);
            result.setCharsConsumed(segment);
        }

        int overlap2 = segment.getCommonPrefixLength(currency2);
        if (overlap2 == currency2.length()) {
            result.currencyCode = isoCode;
            segment.adjustOffset(overlap2);
            result.setCharsConsumed(segment);
        }

        return overlap1 == segment.length() || overlap2 == segment.length();
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        return segment.startsWith(currency1) || segment.startsWith(currency2);
    }

    @Override
    public void postProcess(ParsedNumber result) {
        // No-op
    }

    @Override
    public String toString() {
        return "<CurrencyMatcher " + isoCode + ">";
    }
}