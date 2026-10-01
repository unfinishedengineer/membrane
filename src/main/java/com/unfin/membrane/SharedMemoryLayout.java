package com.unfin.membrane;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public final class SharedMemoryLayout {

    public static final long FILE_SIZE = 4096;

    public static final int SEQUENCE_OFFSET = 0;
    public static final int LENGTH_OFFSET = 8;
    public static final int DATA_OFFSET = 12;

    public static final VarHandle LONG =
            MethodHandles.byteBufferViewVarHandle(
                    long[].class,
                    ByteOrder.nativeOrder());

    private SharedMemoryLayout() {}
}