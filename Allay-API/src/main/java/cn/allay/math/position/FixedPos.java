package cn.allay.math.position;

import cn.allay.level.Level;
import cn.allay.math.vector.FixedVec3;

/**
 * Author: daoge_cmd <br>
 * Date: 2023/3/4 <br>
 * Allay Project <br>
 */
public interface FixedPos<T extends Number> extends FixedVec3<T> {

    static <T extends Number> FixedPos<T> of(T x, T y, T z, Level level) {
        return new ImplFixedPos<>(x, y, z, level);
    }

    Level getLevel();
}