package org.hashsplit4j.store;

import java.util.HashMap;
import java.util.Map;
import org.hashsplit4j.api.BlobStore;

/**
 *
 * @author brad
 */
public class MemoryBlobStore implements BlobStore {

    private final Map<String, Chunk> mapOfChunks = new HashMap<>();

    private long totalSize;

    @Override
    public boolean hasBlob(String hash) {
        return mapOfChunks.containsKey(hash);
    }

    @Override
    public byte[] getBlob(String hash) {
        Chunk chunk = mapOfChunks.get(hash);
        if (chunk != null) {
            return chunk.blob;
        } else {
            return null;
        }
    }

    @Override
    public void setBlob(String hash, byte[] bytes) {
        Chunk chunk = new Chunk();
        chunk.crc = hash;
        chunk.start = totalSize;
        chunk.length = bytes.length;
        chunk.blob = bytes;
        mapOfChunks.put(hash, chunk);
        totalSize += chunk.length;
        //System.out.println("setBlob: " + hash + " size: " + bytes.length);
    }

    public long getTotalSize() {
        return totalSize;
    }

    public Map<String, Chunk> getMapOfChunks() {
        return mapOfChunks;
    }

    /**
     * A chunk just identifies where the data for a given crc is. Useful on the
     * client side
     */
    public class Chunk {
        String crc;
        long start;
        int length;
        byte[] blob;
    }
}
