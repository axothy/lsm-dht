package ru.vk.itmo.test.dht.dao;

import ru.vk.itmo.dao.Entry;
import ru.vk.itmo.dao.TimestampEntry;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Iterator;

import static ru.vk.itmo.test.dht.dao.SSTableUtils.TOMBSTONE;

public class SSTableIterator implements Iterator<Entry<MemorySegment>> {
    private long index;
    private final long keyIndexTo;
    private final long keyOffset;
    private final MemorySegment sstable;

    public SSTableIterator(MemorySegment sstable, long keyIndexFrom, long keyIndexTo, long keyOffset) {
        this.sstable = sstable;
        this.index = keyIndexFrom;
        this.keyIndexTo = keyIndexTo;
        this.keyOffset = keyOffset;
    }

    @Override
    public boolean hasNext() {
        return index < keyIndexTo;
    }

    @Override
    public Entry<MemorySegment> next() {
        Entry<MemorySegment> entry = next(index);
        index++;

        return entry;
    }

    private Entry<MemorySegment> next(long index) {
        long offset = sstable.get(ValueLayout.JAVA_LONG_UNALIGNED, keyOffset + index * Long.BYTES); //key size offset
        long keySize = sstable.get(ValueLayout.JAVA_LONG_UNALIGNED, offset);
        offset += Long.BYTES;
        MemorySegment key = sstable.asSlice(offset, keySize);
        offset += keySize;
        long valueSize = sstable.get(ValueLayout.JAVA_LONG_UNALIGNED, offset);
        offset += Long.BYTES;

        if (valueSize == TOMBSTONE) {
            long timestamp = sstable.get(ValueLayout.JAVA_LONG_UNALIGNED, offset);
            return new TimestampEntry<>(key, null, timestamp);
        } else {
            MemorySegment value = sstable.asSlice(offset, valueSize);
            offset += valueSize;
            long timestamp = sstable.get(ValueLayout.JAVA_LONG_UNALIGNED, offset);

            return new TimestampEntry<>(key, value, timestamp);
        }
    }
}
