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
package org.brewchain.sdk.contract.abi;

import java.math.BigInteger;
import java.nio.ByteBuffer;

final class IntType extends UnitType<Integer> {

    private static final Class<Integer> CLASS = Integer.class;
    private static final String ARRAY_CLASS_NAME = int[].class.getName();

    IntType(String canonicalType, int bitLength, boolean unsigned) {
        super(canonicalType, CLASS, bitLength, unsigned);
    }

    @Override
    String arrayClassName() {
        return ARRAY_CLASS_NAME;
    }

    @Override
    int typeCode() {
        return TYPE_CODE_INT;
    }

    @Override
    int byteLengthPacked(Object value) {
        return bitLength >> 3; // div 8
    }

    @Override
    public int validate(Object value) {
        validateClass(value);
        final long longVal = ((Number) value).longValue();
        validateLongBitLen(longVal);
        return UNIT_LENGTH_BYTES;
    }

    @Override
    Integer decode(ByteBuffer bb, byte[] unitBuffer) {
        bb.get(unitBuffer, 0, UNIT_LENGTH_BYTES);
        BigInteger bi = new BigInteger(unitBuffer);
        validateBigIntBitLen(bi);
        return bi.intValue();
    }

    @Override
    public Integer parseArgument(String s) {
        Integer in = Integer.parseInt(s);
        validate(in);
        return in;
    }
}
