package org.allaymc.api.item.enchantment.type;

import org.allaymc.api.identifier.Identifier;
import org.allaymc.api.item.enchantment.Rarity;
import org.allaymc.api.item.enchantment.AbstractEnchantmentType;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public class EnchantmentDepthStriderType extends AbstractEnchantmentType {
    public static final EnchantmentDepthStriderType DEPTH_STRIDER_TYPE = new EnchantmentDepthStriderType();
  private EnchantmentDepthStriderType() {
    super(new Identifier("minecraft:depth_strider"), 7, 3, Rarity.RARE);
  }
}