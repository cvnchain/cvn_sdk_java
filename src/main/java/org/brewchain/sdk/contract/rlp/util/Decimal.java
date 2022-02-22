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

public final class Decimal {

    private static final int NUM_BYTE_VALS = 1 << Byte.SIZE;
    private static final int CHARS_PER_BYTE = "255".length();
    private static final char[] ENCODING = new char[NUM_BYTE_VALS * CHARS_PER_BYTE];

    static {
        int j = 0;
        for (int i = 0; i < NUM_BYTE_VALS; i++) {
            String str = String.valueOf(i);
            int len = str.length();
            ENCODING[j++] = len == CHARS_PER_BYTE ? str.charAt(0) : '0';
            ENCODING[j++] = len >= 2 ? str.charAt(len - 2) : '0';
            ENCODING[j++] = str.charAt(len - 1);
        }
    }

    public static String encodeToString(byte[] buffer, int i, final int len) {
        char[] chars = new char[len * CHARS_PER_BYTE];
        final int end = i + len;
        for (int j = 0; i < end; ) {
            int idx = (buffer[i++] & 0xFF) * CHARS_PER_BYTE;
            chars[j++] = ENCODING[idx++];
            chars[j++] = ENCODING[idx++];
            chars[j++] = ENCODING[idx];
        }
        return new String(chars);
    }

    public static byte[] decode(String src, int i, final int len) {
        final int byteLen = len / CHARS_PER_BYTE;
        if(byteLen * CHARS_PER_BYTE != len) {
            throw new IllegalArgumentException("length must be a multiple of " + CHARS_PER_BYTE);
        }
        byte[] bytes = new byte[byteLen];
        for (int j = 0, a, b, c; i < len; bytes[j++] = (byte) (a * 100 + b * 10 + c)) {
            a = src.charAt(i++) - '0';
            b = src.charAt(i++) - '0';
            c = src.charAt(i++) - '0';
            if (a < 0 || a > 9 || b < 0 || b > 9 || c < 0 || c > 9) {
                throw new IllegalArgumentException("illegal digit");
            }
        }
        return bytes;
    }
}
