package org.kryptonmc.nbt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class CompoundListStaticsNotNullTest {

    @Test
    void ensureCompoundReaderWriterNotNull() {
        assertNotNull(CompoundTag.READER);
        assertNotNull(CompoundTag.WRITER);
        assertNotNull(CompoundTag.TYPE.reader());
    }

    @Test
    void ensureListReaderWriterNotNull() {
        assertNotNull(ListTag.READER);
        assertNotNull(ListTag.WRITER);
        assertNotNull(ListTag.TYPE.reader());
    }
}
