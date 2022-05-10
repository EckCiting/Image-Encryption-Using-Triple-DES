package xjtlu.tdes.server.utils;

import org.apache.commons.lang3.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ManualDES {
    private static final int ENCRYPT_MODE = 0;
    private static final int DECRYPT_MODE = 1;
    private final int[] IP_table = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private final int[] IP_table_reverse = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private final int[] E_table = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private final int[] P_table = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    private final int[][][] S_boxes = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11},
            }
    };


    private final int[] drop_table = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private final int[] compression_table = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };
    private final int[] shift_table = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    public StringBuilder byteToString(byte[] bytes) {
        // convert 8 bytes (64 bits) to String
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            StringBuilder t = new StringBuilder(Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1));
            while (t.length() < 8)
                t.insert(0, 0);
            binaryString.append(t);
        }
        return binaryString;
    }

    public static byte[] stringToBytes(String s) {
        // convert 64 length String to 8 bytes (64 bits)
        byte[] t = new byte[8];
        for (int i = 0; i < 8; i++) {
            String subs = s.substring(i * 8, i * 8 + 8);
            t[i] = (byte) Integer.parseInt(subs.toString(), 2);
        }
        return t;
    }

    public byte[] Cipher(byte[] bytes, byte[] originalKey, int type){
        byte[] res = new byte[0];
        for(int i=0;i<bytes.length/8;i++){
            byte[] block = Arrays.copyOfRange(bytes,i*8,i*8+8);
            byte[] tmp = DESBlockOperation(block,originalKey,type);
            res = ArrayUtils.addAll(res,tmp);
        }
        return res;
    }


    public StringBuilder[] generateSubKeys(byte[] originalKey) {
        StringBuilder[] subKey = new StringBuilder[16];
        StringBuilder keyBinary = new StringBuilder(byteToString(originalKey));

        // Parity drop
        StringBuilder keyBinaryPart = new StringBuilder();
        for (int i = 0; i < 56; i++) {
            keyBinaryPart.append(keyBinary.charAt(drop_table[i] - 1));
        }

        // Split to 2 28-bit parts
        StringBuilder C0 = new StringBuilder();
        StringBuilder D0 = new StringBuilder();
        C0.append(keyBinaryPart.substring(0, 28));
        D0.append(keyBinaryPart.substring(28, 56));

        // 16 rounds
        for (int i = 0; i < 16; i++) {
            // shift left
            for (int j = 0; j < shift_table[i]; j++) {
                char t;
                t = C0.charAt(0);
                C0.deleteCharAt(0);
                C0.append(t);

                t = D0.charAt(0);
                D0.deleteCharAt(0);
                D0.append(t);
            }

            StringBuilder C0D0 = new StringBuilder(C0.toString() + D0.toString());

            // Compression D-box
            StringBuilder C0D0Temp = new StringBuilder();
            for (int j = 0; j < 48; j++) {
                C0D0Temp.append(C0D0.charAt(compression_table[j] - 1));
            }

            subKey[i] = C0D0Temp;
        }
        return subKey;
    }

    public byte[] DESBlockOperation(byte[] plaintext, byte[] originalKey, int type) {
        StringBuilder ciphertext = new StringBuilder();
        StringBuilder plaintextString = new StringBuilder(byteToString(plaintext));

        // Initial Permutation
        StringBuilder substitutePlaintext = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            substitutePlaintext.append(plaintextString.charAt(IP_table[i] - 1));
        }

        // Split into 32-bit Left and Right part
        StringBuilder L = new StringBuilder(substitutePlaintext.substring(0, 32));
        StringBuilder R = new StringBuilder(substitutePlaintext.substring(32));

        // generate keys
        StringBuilder[] subKey = generateSubKeys(originalKey);
        if (type == 1) {
            StringBuilder[] subKeyTmp = generateSubKeys(originalKey);
            for (int i = 0; i < 16; i++) {
                subKey[i] = subKeyTmp[15 - i];
            }
        }

        // 16 rounds operation
        for (int i = 0; i < 16; i++) {
            StringBuilder LTmp = new StringBuilder(L);
            L.replace(0, 32, R.toString());

            // Expand D-box (E permutation)
            StringBuilder RTmp = new StringBuilder();
            for (int j = 0; j < 48; j++) {
                RTmp.append(R.charAt(E_table[j] - 1));
            }

            // xor
            for (int j = 0; j < 48; j++) {
                if (RTmp.charAt(j) == subKey[i].charAt(j)) {
                    RTmp.replace(j, j + 1, "0");
                } else {
                    RTmp.replace(j, j + 1, "1");
                }
            }

            // S-Box compression
            R.setLength(0);
            for (int j = 0; j < 8; j++) {
                // each 6-bit unit
                String unit = RTmp.substring(j * 6, (j + 1) * 6);

                int row = Integer.parseInt(Character.toString(unit.charAt(0)) + unit.charAt(5), 2);
                int col = Integer.parseInt(unit.substring(1, 5), 2);
                int number = S_boxes[j][row][col];
                // string to bitString
                StringBuilder numberString = new StringBuilder(Integer.toBinaryString(number));
                while (numberString.length() < 4) {
                    numberString.insert(0, 0);
                }
                R.append(numberString);
            }

            // Straight D-box (P permutation)
            StringBuilder RTmp1 = new StringBuilder();
            for (int j = 0; j < 32; j++) {
                RTmp1.append(R.charAt(P_table[j] - 1));
            }
            R.replace(0, 32, RTmp1.toString());

            // xor
            for (int j = 0; j < 32; j++) {
                if (R.charAt(j) == LTmp.charAt(j)) {
                    R.replace(j, j + 1, "0");
                } else {
                    R.replace(j, j + 1, "1");
                }
            }
        }

        StringBuilder LR = new StringBuilder(R.toString() + L.toString());

        // Final Permutation
        ciphertext = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            ciphertext.append(LR.charAt(IP_table_reverse[i] - 1));
        }

        return stringToBytes(ciphertext.toString());
    }

}
