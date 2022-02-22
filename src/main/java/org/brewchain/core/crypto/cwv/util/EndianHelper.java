package org.brewchain.core.crypto.cwv.util;


/**
 * 
 * @author brew
 *
 */
public class EndianHelper {

	public static byte[] revert(byte[] bb) {
		if (bb.length % 2 == 0) {
			byte[] newbb = new byte[bb.length];
			int size = bb.length;
			for (int i = 0; i < bb.length / 2; i ++) {
//				byte b1 = bb[i];
				newbb[i] = bb[size - 1 - i];
				newbb[size - 1 - i] = bb[i];
			}
			return newbb;
		} else {
			byte[] newbb = new byte[bb.length + 1];
			newbb[0] = 0;
			System.arraycopy(bb, 0, newbb, 1, bb.length);
			return revert(newbb);
		}
	}

}
