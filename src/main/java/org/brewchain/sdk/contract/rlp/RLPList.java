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
package org.brewchain.sdk.contract.rlp;

import org.brewchain.sdk.contract.rlp.util.Integers;
import org.brewchain.sdk.contract.rlp.exception.DecodeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Evo on 1/19/2017.
 */
public final class RLPList extends RLPItem {

    RLPList(byte lead, DataType type, byte[] buffer, int index, int containerEnd, boolean lenient) throws DecodeException {
        super(lead, type, buffer, index, containerEnd, lenient);
    }

    @Override
    public boolean isList() {
        return true;
    }

    /**
     * @param srcElements pre-encoded top-level elements of the list
     */
    static RLPList withElements(Iterable<RLPItem> srcElements) {

        byte[] dest;

        int dataLen = 0;
        for (RLPItem element : srcElements) {
            dataLen += element.encodingLength();
        }

        if (dataLen < DataType.MIN_LONG_DATA_LEN) {
            dest = new byte[1 + dataLen];
            dest[0] = (byte) (DataType.LIST_SHORT_OFFSET + dataLen);
            copyElements(srcElements, dest, 1);
        } else {
            dest = encodeListLong(dataLen, srcElements);
        }

        try {
            byte lead = dest[0];
            return new RLPList(lead, DataType.type(lead), dest, 0, dest.length, false);
        } catch (DecodeException de) {
            throw new RuntimeException(de);
        }
    }

    public List<RLPItem> elements(RLPDecoder decoder) throws DecodeException {
        ArrayList<RLPItem> arrayList = new ArrayList<>();
        elements(decoder, arrayList);
        return arrayList;
    }

    public void elements(RLPDecoder decoder, Collection<RLPItem> collection) throws DecodeException {
        int i = dataIndex;
        while (i < this.endIndex) {
            RLPItem item = decoder.wrap(buffer, i, this.endIndex);
            collection.add(item);
            i = item.endIndex;
        }
    }

    public RLPListIterator iterator(RLPDecoder decoder) {
        return new RLPListIterator(this, decoder);
    }

    private static void copyElements(Iterable<RLPItem> srcElements, byte[] dest, int destIndex) {
        for (final RLPItem element : srcElements) {
            final int elementLen = element.encodingLength();
            System.arraycopy(element.buffer, element.index, dest, destIndex, elementLen);
            destIndex += elementLen;
        }
    }

    private static byte[] encodeListLong(final int srcDataLen, final Iterable<RLPItem> srcElements) {

        int t = srcDataLen;

        byte a = 0, b = 0, c = 0, d;

        int n = 1;
        d = (byte) (t & 0xFF);
        t = t >>> Byte.SIZE;
        if(t != 0) {
            n = 2;
            c = (byte) (t & 0xFF);
            t = t >>> Byte.SIZE;
            if(t != 0) {
                n = 3;
                b = (byte) (t & 0xFF);
                t = t >>> Byte.SIZE;
                if(t != 0) {
                    n = 4;
                    a = (byte) (t & 0xFF);
                }
            }
        }

        int destDataIndex = 1 + n;
        byte[] dest = new byte[destDataIndex + srcDataLen];
        dest[0] = (byte) (DataType.LIST_LONG_OFFSET + n);
        Integers.insertBytes(n, dest, 1, a, b, c, d);

        copyElements(srcElements, dest, destDataIndex);

        return dest;
    }
    
    @Override
    public RLPList duplicate(RLPDecoder decoder) throws DecodeException {
        return decoder.wrapList(encoding(), 0);
    }
}
