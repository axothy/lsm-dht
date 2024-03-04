package ru.vk.itmo.test.dht.dao;

/**
 * SSTable header contains:
 * ┌─────────────────────┬─────────────────────────┬─────────────────────┐
 * │          8          │            8            │          8          │
 * │─────────────────────│─────────────────────────│─────────────────────│
 * │   BF array length   │  Hash functions count   │    Entries count    │
 * └─────────────────────┴─────────────────────────┴─────────────────────┘
 * SStable bloom filter:
 * ┌────────────────────────────┐
 * │ 8 x BloomFilter array size │
 * │────────────────────────────│
 * │           Hash_i           │
 * └────────────────────────────┘
 * where i = 1, ... , bloom filter array size
 * SStable data format:
 * ┌─────────────────────┬──────────┬──────────┬────────────┬────────────┐
 * │  8 x Entries count  │    8     │ Key size │     8      │ Value size │
 * │─────────────────────│──────────│──────────│────────────│────────────│
 * │     Key_j offset    │ Key size │    Key   │ Value size │ Value      │
 * └─────────────────────┴──────────┴──────────┴────────────┴────────────┘
 * where j = 1, ... , entries count.
 */
public class SSTableOffsets {
    private final long bloomFilterHashFunctionsOffset;
    private final long bloomFilterLengthOffset;
    private final long entriesSizeOffset;

    public SSTableOffsets(long bloomFilterHashFunctionsOffset, long bloomFilterLengthOffset, long entriesSizeOffset) {
        this.bloomFilterHashFunctionsOffset = bloomFilterHashFunctionsOffset;
        this.bloomFilterLengthOffset = bloomFilterLengthOffset;
        this.entriesSizeOffset = entriesSizeOffset;
    }

    public long getBloomFilterHashFunctionsOffset() {
        return bloomFilterHashFunctionsOffset;
    }

    public long getBloomFilterLengthOffset() {
        return bloomFilterLengthOffset;
    }

    public long getEntriesSizeOffset() {
        return entriesSizeOffset;
    }
}
