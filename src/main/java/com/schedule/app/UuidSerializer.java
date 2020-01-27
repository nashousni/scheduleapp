package com.schedule.app;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class UuidSerializer implements Serializer<UUID>, Serializable {

    public static final UuidSerializer INSTANCE = new UuidSerializer();

    @Override
    public void serialize(DataOutput2 out, UUID uuid) throws IOException {
        out.writeLong(uuid.getLeastSignificantBits());
        out.writeLong(uuid.getMostSignificantBits());
    }

    @Override
    public UUID deserialize(DataInput2 input, int available) throws IOException {
        long leastSignificantBits = input.readLong();
        long mostSignificantBits = input.readLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
