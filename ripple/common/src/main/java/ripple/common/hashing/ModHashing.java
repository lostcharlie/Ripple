package ripple.common.hashing;

import ripple.common.entity.NodeMetadata;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

public class ModHashing implements Hashing {
    private int count;
    private int divisor;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    public ModHashing() {
        this(3, 200);
    }

    public ModHashing(int count, int divisor) {
        this.setCount(count);
        this.setDivisor(divisor);
    }

    @Override
    public List<NodeMetadata> hashing(String key, List<NodeMetadata> nodeList) {
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        long result = crc32.getValue();
        long firstIndex = result % this.getDivisor();

        if (nodeList.size() <= this.getCount()) {
            return new ArrayList<>(nodeList);
        } else {
            List<NodeMetadata> ret = new ArrayList<>();
            int i;
            for (i = 0; i < this.getCount(); i++) {
                ret.add(nodeList.get((int) ((firstIndex + i) % nodeList.size())));
            }
            return ret;
        }
    }
}
