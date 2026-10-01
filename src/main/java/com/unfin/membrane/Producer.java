package com.unfin.membrane;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static com.unfin.membrane.SharedMemoryLayout.*;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

public class Producer {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting producer ...");

        try (var file = new RandomAccessFile("shared-memory.dat", "rw");
             var channel = file.getChannel()) {

            file.setLength(SharedMemoryLayout.FILE_SIZE);

            MappedByteBuffer buffer =
                    channel.map(
                            READ_WRITE,
                            0,
                            SharedMemoryLayout.FILE_SIZE);

            long sequence = 0;

            while (true) {
                long nextSequence = sequence + 1;

                String message = "Hello " + nextSequence;
                byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

                buffer.putInt(LENGTH_OFFSET, bytes.length);
                buffer.put(DATA_OFFSET, bytes);

                LONG.setRelease(
                        buffer,
                        SEQUENCE_OFFSET,
                        nextSequence);

                sequence = nextSequence;

                System.out.printf(
                        "PUBLISH sequence=%d message=%s%n",
                        sequence,
                        message);

                Thread.sleep(1000);
            }
        }
    }
}