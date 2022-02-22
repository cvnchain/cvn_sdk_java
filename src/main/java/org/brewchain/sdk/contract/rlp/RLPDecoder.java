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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Decodes RLP-formatted data.
 */
public final class RLPDecoder {

    public static final RLPDecoder RLP_STRICT = new RLPDecoder(false);
    public static final RLPDecoder RLP_LENIENT = new RLPDecoder(true);

    public final boolean lenient;

    private RLPDecoder(boolean lenient) {
        this.lenient = lenient;
    }

    /**
     * Returns an iterator over a stream of RLP items.
     * @param rlpStream the stream of RLP items
     * @return  an iterator over the items in the stream
     */
    public RLPStreamIterator sequenceStreamIterator(InputStream rlpStream) {
        return new RLPStreamIterator(RLPDecoder.this, rlpStream);
    }

    public RLPIterator sequenceIterator(byte[] buffer) {
        return sequenceIterator(buffer, 0);
    }

    /**
     * Returns an iterator over the sequence of RLP items starting at {@code index}.
     *
     * @param buffer    the array containing the sequence
     * @param index the index of the sequence
     * @return  an iterator over the items in the sequence
     */
    public RLPIterator sequenceIterator(byte[] buffer, int index) {
        return new RLPIterator(RLPDecoder.this, buffer, index, buffer.length);
    }

    public RLPListIterator listIterator(byte[] buffer) throws DecodeException {
        return listIterator(buffer, 0);
    }

    /**
     * Returns an iterator over the elements in the RLP list item at {@code index}.
     *
     * @param buffer    the array containing the list item
     * @param index the index of the RLP list item
     * @return  the iterator over the elements in the list
     * @throws DecodeException  if the RLP list failed to decode
     */
    public RLPListIterator listIterator(byte[] buffer, int index) throws DecodeException {
        return wrapList(buffer, index).iterator(this);
    }
    
    public RLPString wrapString(byte lengthOneRlp) throws DecodeException {
        return wrapString(new byte[] { lengthOneRlp }, 0);
    }
    
    public RLPString wrapString(byte[] encoding) throws DecodeException {
        return wrapString(encoding, 0);
    }
    
    public RLPString wrapString(byte[] buffer, int index) throws DecodeException {
        byte lead = buffer[index];
        DataType type = DataType.type(lead);
        switch (type) {
        case SINGLE_BYTE:
        case STRING_SHORT:
        case STRING_LONG: return new RLPString(lead, type, buffer, index, Integer.MAX_VALUE, lenient);
        case LIST_SHORT:
        case LIST_LONG:
        default: throw new IllegalArgumentException("item is not a string");
        }
    }
    
    public RLPList wrapList(byte lengthOneRlp) throws DecodeException {
        return wrapList(new byte[] { lengthOneRlp }, 0);
    }

    public RLPList wrapList(byte[] encoding) throws DecodeException {
        return wrapList(encoding, 0);
    }

    public RLPList wrapList(byte[] buffer, int index) throws DecodeException {
        byte lead = buffer[index];
        DataType type = DataType.type(lead);
        switch (type) {
        case LIST_SHORT:
        case LIST_LONG: return new RLPList(lead, type, buffer, index, Integer.MAX_VALUE, lenient);
        case SINGLE_BYTE:
        case STRING_SHORT:
        case STRING_LONG:
        default: throw new IllegalArgumentException("item is not a list");
        }
    }

    /**
     * Returns an {@link RLPItem} for a length-one encoding (e.g. 0xc0)
     *
     * @param lengthOneRLP  the encoding
     * @return  the item
     * @throws DecodeException  if the byte fails to decode
     */
    public RLPItem wrap(byte lengthOneRLP) throws DecodeException {
        return wrap(new byte[] { lengthOneRLP }, 0);
    }

    public RLPItem wrap(byte[] encoding) throws DecodeException {
        return wrap(encoding, 0);
    }

    public RLPItem wrap(byte[] buffer, int index) throws DecodeException {
        return wrap(buffer, index, Integer.MAX_VALUE);
    }

    RLPItem wrap(byte[] buffer, int index, int containerEnd) throws DecodeException {
        byte lead = buffer[index];
        DataType type = DataType.type(lead);
        switch (type) {
        case SINGLE_BYTE:
        case STRING_SHORT:
        case STRING_LONG: return new RLPString(lead, type, buffer, index, containerEnd, lenient);
        case LIST_SHORT:
        case LIST_LONG: return new RLPList(lead, type, buffer, index, containerEnd, lenient);
        default: throw new RuntimeException();
        }
    }

    /*
     *  Methods for gathering sequential items into a collection
     */

    public List<RLPItem> collectAll(byte[] encodings) throws DecodeException {
        return collectAll(0, encodings);
    }

    public List<RLPItem> collectAll(int index, byte[] encodings) throws DecodeException {
        return collectBefore(index, encodings, encodings.length);
    }

    public List<RLPItem> collectBefore(byte[] encodings, int endIndex) throws DecodeException {
        return collectBefore(0, encodings, endIndex);
    }

    public List<RLPItem> collectBefore(int index, byte[] encodings, int endIndex) throws DecodeException {
        ArrayList<RLPItem> dest = new ArrayList<>();
        collectBefore(index, encodings, endIndex, dest);
        return dest;
    }

    public List<RLPItem> collectN(byte[] encodings, int n) throws DecodeException {
        return collectN(0, encodings, n);
    }

    public List<RLPItem> collectN(int index, byte[] encodings, int n) throws DecodeException {
        ArrayList<RLPItem> dest = new ArrayList<>(n);
        collect(index, encodings, (count, idx) -> count < n, dest);
        return dest;
    }
    // --------
    public int collectAll(int index, byte[] encodings, Collection<RLPItem> dest) throws DecodeException {
        return collectBefore(index, encodings, encodings.length, dest);
    }

    public int collectBefore(int index, byte[] encodings, int endIndex, Collection<RLPItem> dest) throws DecodeException {
        return collect(index, encodings, (count, idx) -> idx < endIndex, dest);
    }

    public void collectN(byte[] encodings, int index, int n, Collection<RLPItem> dest) throws DecodeException {
        collect(index, encodings, (count, idx) -> count < n, dest);
    }
    // -------
    public int collect(int index, byte[] encodings, BiPredicate<Integer, Integer> predicate, Collection<RLPItem> collection) throws DecodeException {
        int count = 0;
        while (predicate.test(count, index)) {
            RLPItem item = wrap(encodings, index);
            collection.add(item);
            count++;
            index = item.endIndex;
        }
        return count;
    }
}
