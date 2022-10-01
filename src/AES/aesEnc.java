package AES;

import java.util.ArrayList;

/**
 * AES algorithm encryption process
 * @author Klasnov
 */

public class aesEnc {
    private final int N = 4;
    private final ArrayList<byte[]> cph = new ArrayList<>();

    public static void main(String[] args) {
        aesEnc aes = new aesEnc();
        aes.run();
    }

    /**
     * Run the AES encryption algorithm.
     */
    public void run() {
        ArrayList<byte[]> plt;
        byte[][] iv, bef, pltMtx, aft = new byte[N][N];
        int cnt;
        /* Input data */
        keyOpt key = new keyOpt();
        cbcWrk cbc = new cbcWrk();
        key.prtAllKey();
        plt = cbc.getBlk();
        //iv = cbc.getIV();
        iv = new byte[][] {
                new byte[] {0x00, 0x00, 0x00, 0x00},
                new byte[] {0x00, 0x00, 0x00, 0x00},
                new byte[] {0x00, 0x00, 0x00, 0x00},
                new byte[] {0x00, 0x00, 0x00, 0x00}
        };
        /* Encryption process */
        System.out.println("AES encrypting..................");
        cnt = 0;
        for (byte[] p : plt) {
            pltMtx = stdMtx.aryToMtx(p);
            if (cnt != 0) {
                bef = mtxXor(aft, pltMtx);
            }
            else {
                bef = mtxXor(iv, pltMtx);
            }
            aft = enc(bef, key);
            cph.add(stdMtx.mtxToAry(aft));
            cnt++;
        }
        /* Output the ciphertext each byte */
        prtCph();
    }

    /**
     * The XOR operation between two matrix.
     * @param ipt1 The first computed matrix.
     * @param ipt2 The second computed matrix.
     * @return The matrix after XOR operation.
     */
    private byte[][] mtxXor(byte[][] ipt1, byte[][] ipt2) {
        byte[][] opt = new byte[][] {new byte[N], new byte[N], new byte[N], new byte[N]};
        int i, j;
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                opt[i][j] = (byte) (ipt1[i][j] ^ ipt2[i][j]);
            }
        }
        return opt;
    }

    /**
     * The core encryption process
     * @param ipt The input matrix needing to be encrypted.
     * @param key The keyOpt class used.
     * @return The matrix after encryption.
     */
    private byte[][] enc(byte[][] ipt, keyOpt key) {
        final int RND = 10;
        byte[][] tmp = mtxXor(ipt, key.getRdKey(0));
        for (int i = 1; i <= RND; i++) {
            tmp = sBox.bytSst(tmp, sBox.ENC);
            tmp = shfRow.rowSft(tmp, shfRow.LFT);
            tmp = mixCol.mxCol(tmp, mixCol.MIX);
            tmp = mtxXor(tmp, key.getRdKey(i));
        }
        return tmp;
    }

    /**
     * Print the ASCII code of ciphertext each byte
     */
    private void prtCph() {
        System.out.println("The ASCII code value of the encrypted ciphertext is:");
        int tmp;
        for (byte[] c : cph) {
            for (byte b : c) {
                tmp = b & 0x0ff;
                if ((tmp & 0x0f0) != 0) {
                    System.out.print("0x" + Integer.toHexString(tmp) + " ");
                }
                else {
                    System.out.print("0x0" + Integer.toHexString(tmp) + " ");
                }
            }
            System.out.println();
        }
    }
}
