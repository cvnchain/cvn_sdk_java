/*
   Copyright 2019 Evan Saulpaugh

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.brewchain.sdk.contract.rlp.util;


import org.brewchain.sdk.contract.abi.util.Utils;

import java.nio.charset.Charset;

/**
 * Utility for encoding and decoding hexadecimal, Base64, and UTF-8-encoded {@code String}s.
 */
public final class Strings {

    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    public static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");

    public static final int BASE_64_URL_SAFE = 3; // 64
    public static final int DECIMAL = 2; // 10
    public static final int UTF_8 = 1; // 256
    public static final int HEX = 0; // 16

    public static final int URL_SAFE_FLAGS = Base64.URL_SAFE_CHARS | Base64.NO_LINE_SEP | Base64.NO_PADDING;

    public static String encode(byte[] bytes) {
        return encode(bytes, HEX);
    }

    public static String encode(byte[] bytes, int encoding) {
        return encode(bytes, 0, bytes.length, encoding);
    }

    public static String encode(byte[] buffer, int from, int len, int encoding) {
        switch (encoding) {
        case BASE_64_URL_SAFE: return Base64.encodeToString(buffer, from, len, URL_SAFE_FLAGS);
        case DECIMAL: return Decimal.encodeToString(buffer, from, len);
        case UTF_8: return new String(buffer, from, len, CHARSET_UTF_8);
        case HEX:
        default: return FastHex.encodeToString(buffer, from, len);
        }
    }

    public static byte[] decode(String encoded) {
        return decode(encoded, HEX);
    }

    public static byte[] decode(String string, int encoding) {
        if(string.isEmpty()) {
            return Utils.EMPTY_BYTE_ARRAY;
        }
        switch (encoding) {
        case BASE_64_URL_SAFE: return java.util.Base64.getUrlDecoder().decode(string);
        case DECIMAL: return Decimal.decode(string, 0, string.length());
        case UTF_8: return string.getBytes(CHARSET_UTF_8);
        case HEX:
        default: return FastHex.decode(string, 0 ,string.length());
        }
    }
}
