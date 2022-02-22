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
package org.brewchain.sdk.contract.rlp.exception;

/**
 * Indicates a failure to decode an RLP item that is unrecoverably malformed or exceeds the bounds of its parent item.
 */
public final class UnrecoverableDecodeException extends DecodeException {

    public UnrecoverableDecodeException(String msg) {
        super(msg);
    }

    public UnrecoverableDecodeException(Throwable cause) {
        super(cause);
    }

    @Override
    public boolean isRecoverable() {
        return false;
    }
}
