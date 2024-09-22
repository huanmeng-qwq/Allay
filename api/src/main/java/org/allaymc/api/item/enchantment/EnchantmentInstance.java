package org.allaymc.api.item.enchantment;

import lombok.AllArgsConstructor;
import org.cloudburstmc.nbt.NbtMap;

/**
 * Represents an instance of an enchantment.
 *
 * @author daoge_cmd
 */
@AllArgsConstructor
public class EnchantmentInstance {
    protected final EnchantmentType type;
    protected final int level;

    /**
     * Get the type of the enchantment.
     *
     * @return the type of the enchantment.
     */
    public EnchantmentType getType() {
        return type;
    }

    /**
     * Get the level of the enchantment.
     *
     * @return the level of the enchantment.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the level of the enchantment.
     *
     * @param level the level of the enchantment.
     * @return the enchantment instance with the new level.
     */
    public EnchantmentInstance setLevel(int level) {
        return getType().createInstance(level);
    }

    /**
     * Save the enchantment instance to NBT.
     *
     * @return the NBT representation of the enchantment instance.
     */
    public NbtMap saveNBT() {
        return NbtMap.builder()
                .putShort("id", (short) getType().getId())
                .putShort("lvl", (short) getLevel())
                .build();
    }
}