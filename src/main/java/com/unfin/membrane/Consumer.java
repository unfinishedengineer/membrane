package com.unfin.membrane;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static com.unfin.membrane.SharedMemoryLayout.LONG;
import static com.unfin.membrane.SharedMemoryLayout.SEQUENCE_OFFSET;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

public class Consumer {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting consumer ...");

        try (var file = new RandomAccessFile("shared-memory.dat", "rw");
             var channel = file.getChannel()) {

            MappedByteBuffer buffer =
                    channel.map(
                            READ_WRITE,
                            0,
                            SharedMemoryLayout.FILE_SIZE);

            long lastSequence = 0;

            while (true) {

                long sequence = (long) LONG.getAcquire(buffer, SEQUENCE_OFFSET);

                if (sequence != lastSequence) {

                    int length =
                            buffer.getInt(
                                    SharedMemoryLayout.LENGTH_OFFSET);

                    byte[] bytes = new byte[length];

                    buffer.get(
                            SharedMemoryLayout.DATA_OFFSET,
                            bytes);

                    String message =
                            new String(bytes, StandardCharsets.UTF_8);

                    System.out.printf(
                            "CONSUME sequence=%d message=%s%n",
                            sequence,
                            message);

                    System.out.printf(
                            "Received [%d]: %s%n",
                            sequence,
                            message);

                    lastSequence = sequence;
                }

                Thread.onSpinWait();
            }
        }
    }
}