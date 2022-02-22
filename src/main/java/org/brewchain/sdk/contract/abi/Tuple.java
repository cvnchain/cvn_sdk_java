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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

public final class Tuple extends AbstractList<Object> implements RandomAccess, Serializable {

    public static final Tuple EMPTY = new Tuple();

    final Object[] elements;

    public Tuple(Object... elements) {
        this.elements = elements;
    }
    
    public Tuple setOneArray(Object array){
        this.elements[0] = array;
        return this;
    }

    public Tuple subtuple(int startIndex, int endIndex) {
        final int len = endIndex - startIndex;
        Object[] copy = new Object[len];
        System.arraycopy(elements, startIndex, copy, 0, len);
        return new Tuple(copy);
    }

    @Override
    public Object get(int index) {
        return elements[index];
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple other = (Tuple) o;
        return Arrays.deepEquals(this.elements, other.elements);
    }

    public static Tuple of(Object... elements) {
        return new Tuple(elements);
    }

    public static Tuple singleton(Object element) {
        return new Tuple(element);
    }
}
