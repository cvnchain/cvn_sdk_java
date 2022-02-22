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

import org.brewchain.sdk.contract.rlp.exception.DecodeException;

import java.util.NoSuchElementException;

/**
 * For iterating over sequentially encoded RLP items.
 */
public final class RLPIterator {

    private final RLPDecoder decoder;
    private final byte[] rlp;
    private int index;
    private final int end;

    RLPIterator(RLPDecoder decoder, byte[] rlp, int start, int end) {
        this.decoder = decoder;
        this.rlp = rlp;
        this.index = start;
        this.end = end;
    }

    public boolean hasNext() {
        return index < end;
    }

    public RLPItem next() throws DecodeException {
        if(hasNext()) {
            RLPItem item = decoder.wrap(rlp, index);
            this.index = item.endIndex;
            return item;
        }
        throw new NoSuchElementException();
    }
}
