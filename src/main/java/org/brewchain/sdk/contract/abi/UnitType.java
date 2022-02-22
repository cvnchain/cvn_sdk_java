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

import org.brewchain.sdk.contract.abi.util.BizarroIntegers;
import org.brewchain.sdk.contract.rlp.util.Integers;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Superclass for any 32-byte ("unit") Contract ABI type. Usually numbers or boolean. Not for arrays.
 */
abstract class UnitType<V> extends ABIType<V> { // V generally extends Number or is Boolean

    static final int UNIT_LENGTH_BYTES = 32;
    static final int LOG_2_UNIT_LENGTH_BYTES = 5;

    final int bitLength;
    final boolean unsigned;

    UnitType(String canonicalType, Class<V> clazz, int bitLength, boolean unsigned) {
        super(canonicalType, clazz, false);
        this.bitLength = bitLength;
        this.unsigned = unsigned;
    }

    @Override
    final int byteLength(Object value) {
        return UNIT_LENGTH_BYTES;
    }

    @Override
    void encodeHead(Object value, ByteBuffer dest, int[] offset) {
        Encoding.insertInt(((Number) value).longValue(), dest);
    }

    @Override
    void encodeTail(Object value, ByteBuffer dest) {
        throw new UnsupportedOperationException();
    }

    // don't do unsigned check for array element
    final void validatePrimitiveElement(long longVal) {
        checkBitLen(longVal >= 0 ? Integers.bitLen(longVal) : BizarroIntegers.bitLen(longVal));
    }

    // don't do unsigned check for array element
    final void validateBigIntElement(final BigInteger bigIntVal) {
        checkBitLen(bigIntVal.bitLength());
    }

    // --------------------------------

    final void validateLongBitLen(long longVal) {
        checkBitLen(longVal >= 0 ? Integers.bitLen(longVal) : BizarroIntegers.bitLen(longVal));
        if (unsigned && longVal < 0) {
            throw new IllegalArgumentException("signed value given for unsigned type");
        }
    }

    final void validateBigIntBitLen(final BigInteger bigIntVal) {
        checkBitLen(bigIntVal.bitLength());
        if (unsigned && bigIntVal.signum() == -1) {
            throw new IllegalArgumentException("signed value given for unsigned type");
        }
    }

    private void checkBitLen(int actual) {
        if (actual > bitLength) {
            throw new IllegalArgumentException("exceeds bit limit: " + actual + " > " + bitLength);
        }
    }
}
