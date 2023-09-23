package cn.allay.api.entity.interfaces.warden;

import cn.allay.api.data.VanillaEntityId;
import cn.allay.api.entity.Entity;
import cn.allay.api.entity.type.EntityType;
import cn.allay.api.entity.type.EntityTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface EntityWarden extends Entity {
  EntityType<EntityWarden> WARDEN_TYPE = EntityTypeBuilder
          .builder(EntityWarden.class)
          .vanillaEntity(VanillaEntityId.WARDEN)
          .build();
}