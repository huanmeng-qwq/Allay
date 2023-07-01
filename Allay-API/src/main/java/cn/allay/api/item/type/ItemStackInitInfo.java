package cn.allay.api.item.type;

import cn.allay.api.component.interfaces.ComponentInitInfo;
import org.cloudburstmc.nbt.NbtMap;

/**
 * Allay Project 2023/5/19
 *
 * @author daoge_cmd
 */
public interface ItemStackInitInfo extends ComponentInitInfo {

    int count();

    int meta();

    NbtMap nbt();

    record Simple(int count, int meta, NbtMap nbt) implements ItemStackInitInfo {
        public Simple(int count, int meta, NbtMap nbt) {
            this.count = count;
            this.meta = meta;
            this.nbt = nbt;
        }

        public Simple(int count) {
            //todo: 空nbt有关的逻辑也许可以优化？
            this(count, 0, NbtMap.builder().build());
        }

        public Simple(int count, int meta) {
            this(count, meta, NbtMap.builder().build());
        }
    }

}