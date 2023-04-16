package org.example.hashFunctions.murmur3;


import static org.example.hashFunctions.murmur3.Util.rotl32;

public class Murmur {


    /**
     * Helps convert a byte into its unsigned value
     */
    public static final int UNSIGNED_MASK = 0xff;

    /**
     * Helps convert integer to its unsigned value
     */
    public static final long UINT_MASK = 0xFFFFFFFFl;

    private static final int C1 = 0xcc9e2d51;
    private static final int C2 = 0x1b873593;
    private static final long n = 0xe6546b64l;
    private static final int m = 5;



    public static int hash32(String data,  long seed) {
        byte[] bytes = data.getBytes();
        return hash32(bytes,seed);
    }

    public static int hash32(final byte[] data, long seed) {
        int length=data.length;
        final int nblocks = length >> 2;
        long hash = seed;

        for(int i = 0; i < nblocks; i++) {
            final int i4 = i << 2;

            long k1 = (data[i4] & UNSIGNED_MASK);
            k1 |= (data[i4 + 1] & UNSIGNED_MASK) << 8;
            k1 |= (data[i4 + 2] & UNSIGNED_MASK) << 16;
            k1 |= (data[i4 + 3] & UNSIGNED_MASK) << 24;

         hash=multRot(hash,k1);
        }
        int offset = (nblocks << 2); // nblocks * 4;
        long k1 = 0;

        // remaining bytes aka fraction block
        switch (length & 3) {
            case 3:
                k1 ^= (data[offset + 2] << 16) & UINT_MASK;

            case 2:
                k1 ^= (data[offset + 1] << 8) & UINT_MASK;

            case 1:
                k1 ^= data[offset];

        }
        hash=multRot(hash,k1);

        hash ^= length;

      // to have great performance at avalanche test we preform the following procedure

        hash = avalEnh(hash);

        return (int) hash;
    }

    public static long multRot (long hash,long temp){
        temp = (temp * C1) & UINT_MASK ;
        temp = rotl32(temp, 15);
        temp = (temp * C2) & UINT_MASK;

        hash ^= temp;
        hash = rotl32(hash,13);
        hash = (((hash * m)& UINT_MASK)  + n) & UINT_MASK;
        return hash;
    }

    public static long avalEnh(long hash){
        hash ^= (hash >> 16) & UINT_MASK;
        hash = (hash * 0x85ebca6bl) & UINT_MASK;
        hash ^= (hash >> 13) & UINT_MASK;
        hash = (hash * 0xc2b2ae35) & UINT_MASK;
        hash ^= (hash >> 16) & UINT_MASK;

        return hash;
    }




}
