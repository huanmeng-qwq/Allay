package org.allaymc.api.entity.interfaces.ravager;

import org.allaymc.api.data.VanillaEntityId;
import org.allaymc.api.entity.Entity;
import org.allaymc.api.entity.type.EntityType;
import org.allaymc.api.entity.type.EntityTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface EntityRavager extends Entity {
  EntityType<EntityRavager> RAVAGER_TYPE = EntityTypeBuilder
          .builder(EntityRavager.class)
          .vanillaEntity(VanillaEntityId.RAVAGER)
          .build();
}