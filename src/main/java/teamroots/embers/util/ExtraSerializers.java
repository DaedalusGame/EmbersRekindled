package teamroots.embers.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;

public class ExtraSerializers {
    public static final DataSerializer<float[]> FLOAT_ARRAY = new DataSerializer<float[]>() {
        @Override
        public void write(PacketBuffer buf, float[] value) {
            buf.writeInt(value.length);
            for(int i=0; i < value.length; i++)
                buf.writeFloat(value[i]);
        }

        @Override
        public float[] read(PacketBuffer buf) throws IOException {
            float[] value = new float[buf.readInt()];
            for(int i=0; i < value.length; i++)
                value[i] = buf.readFloat();
            return value;
        }

        @Override
        public DataParameter<float[]> createKey(int id) {
            return new DataParameter<>(id,this);
        }

        @Override
        public float[] copyValue(float[] value) {
            return value.clone();
        }
    };
}
