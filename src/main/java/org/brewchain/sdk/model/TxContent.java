package org.brewchain.sdk.model;

import java.util.List;

public class TxContent {
    /**
     * retCode : 1
     * transaction : {"hash":"0xdae99e500ee8361350e93a55683214f47753ca750e503391160adefbd8096c83e1","body":{"nonce":26,"address":"0x3c1ea4aa4974d92e0eabd5d024772af3762720a0","outputs":[{"address":"0x6b2098fd9e7ceba3033ebb83fcf272d0dcd76104"}],"fee_hi":0,"fee_low":0,"inner_codetype":2,"timestamp":1587355050675},"signature":"0xbe2f1ac3db2fa6baeb5013490e3c9d5230700a6699d3e7442dc0dcf36b66b5c72cae8251aeda966aa251690ca65bd98521be21e4e2970cf3c301739d2bc5cb3d25af7d44e920b00423cb611e9a52272d2e557e08b596c92ecb848d88ae7cfdccda0dc4019f2aede6faac42ff42368c686e4694ede82d38d71a45ea9032e592baa1496bdfdee22923ee3f1bd22a86ffdc6d364dbd","status":{"status":"0xff","result":"0x7478207072656c6f616420696e76616c69643a746f6b656e2046756e6374696f6e323020697320656d707479","height":138963,"timestamp":1587355058335},"node":{"nid":"VHEdr2PwmTy0uNzSOvGNvvnoAZFHM","address":"0x667f4062678952455d08ce3d65f423f097b4357a"},"accepttimestamp":1587355057751}
     */

    private int retCode;
    private TransactionBean transaction;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public TransactionBean getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionBean transaction) {
        this.transaction = transaction;
    }

    public static class TransactionBean {
        /**
         * hash : 0xdae99e500ee8361350e93a55683214f47753ca750e503391160adefbd8096c83e1
         * body : {"nonce":26,"address":"0x3c1ea4aa4974d92e0eabd5d024772af3762720a0","outputs":[{"address":"0x6b2098fd9e7ceba3033ebb83fcf272d0dcd76104"}],"fee_hi":0,"fee_low":0,"inner_codetype":2,"timestamp":1587355050675}
         * signature : 0xbe2f1ac3db2fa6baeb5013490e3c9d5230700a6699d3e7442dc0dcf36b66b5c72cae8251aeda966aa251690ca65bd98521be21e4e2970cf3c301739d2bc5cb3d25af7d44e920b00423cb611e9a52272d2e557e08b596c92ecb848d88ae7cfdccda0dc4019f2aede6faac42ff42368c686e4694ede82d38d71a45ea9032e592baa1496bdfdee22923ee3f1bd22a86ffdc6d364dbd
         * status : {"status":"0xff","result":"0x7478207072656c6f616420696e76616c69643a746f6b656e2046756e6374696f6e323020697320656d707479","height":138963,"timestamp":1587355058335}
         * node : {"nid":"VHEdr2PwmTy0uNzSOvGNvvnoAZFHM","address":"0x667f4062678952455d08ce3d65f423f097b4357a"}
         * accepttimestamp : 1587355057751
         */

        private String hash;
        private BodyBean body;
        private String signature;
        private StatusBean status;
        private NodeBean node;
        private long accepttimestamp;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public StatusBean getStatus() {
            return status;
        }

        public void setStatus(StatusBean status) {
            this.status = status;
        }

        public NodeBean getNode() {
            return node;
        }

        public void setNode(NodeBean node) {
            this.node = node;
        }

        public long getAccepttimestamp() {
            return accepttimestamp;
        }

        public void setAccepttimestamp(long accepttimestamp) {
            this.accepttimestamp = accepttimestamp;
        }

        public static class BodyBean {
            /**
             * nonce : 26
             * address : 0x3c1ea4aa4974d92e0eabd5d024772af3762720a0
             * outputs : [{"address":"0x6b2098fd9e7ceba3033ebb83fcf272d0dcd76104"}]
             * fee_hi : 0
             * fee_low : 0
             * inner_codetype : 2
             * timestamp : 1587355050675
             */

            private int nonce;
            private String address;
            private int fee_hi;
            private int fee_low;
            private int inner_codetype;
            private long timestamp;
            private List<OutputsBean> outputs;

            public int getNonce() {
                return nonce;
            }

            public void setNonce(int nonce) {
                this.nonce = nonce;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getFee_hi() {
                return fee_hi;
            }

            public void setFee_hi(int fee_hi) {
                this.fee_hi = fee_hi;
            }

            public int getFee_low() {
                return fee_low;
            }

            public void setFee_low(int fee_low) {
                this.fee_low = fee_low;
            }

            public int getInner_codetype() {
                return inner_codetype;
            }

            public void setInner_codetype(int inner_codetype) {
                this.inner_codetype = inner_codetype;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public List<OutputsBean> getOutputs() {
                return outputs;
            }

            public void setOutputs(List<OutputsBean> outputs) {
                this.outputs = outputs;
            }

            public static class OutputsBean {
                /**
                 * address : 0x6b2098fd9e7ceba3033ebb83fcf272d0dcd76104
                 */

                private String address;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }
        }

        public static class StatusBean {
            /**
             * status : 0xff
             * result : 0x7478207072656c6f616420696e76616c69643a746f6b656e2046756e6374696f6e323020697320656d707479
             * height : 138963
             * timestamp : 1587355058335
             */

            private String status;
            private String result;
            private int height;
            private long timestamp;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getResult() {
                if(result!=null && result.startsWith("0x")){
                    return result.substring(2);
                }
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }
        }

        public static class NodeBean {
            /**
             * nid : VHEdr2PwmTy0uNzSOvGNvvnoAZFHM
             * address : 0x667f4062678952455d08ce3d65f423f097b4357a
             */

            private String nid;
            private String address;

            public String getNid() {
                return nid;
            }

            public void setNid(String nid) {
                this.nid = nid;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
