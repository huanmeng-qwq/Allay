package cn.allay.api.world.palette;

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
public interface RuntimeDataDeserializer<V> {
    V deserialize(int id);
}