package fr.communaywen.core.elevator.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class CoordinateManager {
    private final Map<Pair, TreeSet<Integer>> coordinates;

    public CoordinateManager(byte[] data) {
        coordinates = new HashMap<>();
        if (data == null) return;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            try (DataInputStream dis = new DataInputStream(bais)) {
                int size = dis.readInt();
                for (int i = 0; i < size; i++) {
                    int x = dis.readInt();
                    int z = dis.readInt();
                    Pair pair = new Pair(x, z);
                    int yCount = dis.readInt();
                    TreeSet<Integer> ySet = new TreeSet<>();
                    for (int j = 0; j < yCount; j++) {
                        ySet.add(dis.readInt());
                    }
                    coordinates.put(pair, ySet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addY(int x, int y, int z) {
        Pair key = new Pair(x, z);
        coordinates.computeIfAbsent(key, k -> new TreeSet<>()).add(y);
    }

    public void removeY(int x, int y, int z) {
        Pair key = new Pair(x, z);
        TreeSet<Integer> ySet = coordinates.get(key);
        if (ySet != null) {
            ySet.remove(y);
            if (ySet.isEmpty()) coordinates.remove(key);
        }
    }

    public int getTop(int x, int y, int z) {
        Pair key = new Pair(x, z);
        TreeSet<Integer> ySet = coordinates.get(key);
        if (ySet == null) return y;

        Integer top = ySet.higher(y);
        return top == null ? y : top;
    }

    public int getBottom(int x, int y, int z) {
        Pair key = new Pair(x, z);
        TreeSet<Integer> ySet = coordinates.get(key);
        if (ySet == null) return y;

        Integer bottom = ySet.lower(y);
        return bottom == null ? y : bottom;
    }

    public byte[] saveCoordinates() {
        int size = coordinates.size();
        if (size == 0) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                dos.writeInt(size);
                for (Map.Entry<Pair, TreeSet<Integer>> entry : coordinates.entrySet()) {
                    Pair pair = entry.getKey();
                    TreeSet<Integer> ySet = entry.getValue();
                    dos.writeInt(pair.getFirst());
                    dos.writeInt(pair.getSecond());
                    dos.writeInt(ySet.size());
                    for (int y : ySet) {
                        dos.writeInt(y);
                    }
                }
                dos.flush();
                return baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getCount(int x, int z) {
        Pair key = new Pair(x, z);
        TreeSet<Integer> ySet = coordinates.get(key);
        return ySet != null ? ySet.size() : 0;
    }

    public int getSize() {
        return coordinates.size();
    }

    public int getTarget(int x, int y, int z, boolean isJump) {
        return isJump ? getTop(x, y, z) : getBottom(x, y, z);
    }
}