package cn.allay.server.block.type;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.component.BlockComponentImpl;
import cn.allay.api.block.component.annotation.RequireBlockProperty;
import cn.allay.api.block.component.impl.attribute.BlockAttributeComponentImpl;
import cn.allay.api.block.component.impl.attribute.VanillaBlockAttributeRegistry;
import cn.allay.api.block.component.impl.base.BlockBaseComponentImpl;
import cn.allay.api.block.component.impl.custom.CustomBlockComponentImpl;
import cn.allay.api.block.palette.BlockStateHashPalette;
import cn.allay.api.block.property.type.BlockPropertyType;
import cn.allay.api.block.type.BlockState;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.block.type.BlockTypeRegistry;
import cn.allay.api.component.annotation.AutoRegister;
import cn.allay.api.component.interfaces.ComponentImpl;
import cn.allay.api.component.interfaces.ComponentProvider;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.datastruct.UnmodifiableLinkedHashMap;
import cn.allay.api.identifier.Identifier;
import cn.allay.api.utils.HashUtils;
import cn.allay.server.component.exception.BlockComponentInjectException;
import cn.allay.server.component.injector.AllayComponentInjector;
import cn.allay.server.utils.ComponentClassCacheUtils;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;

/**
 * Allay Project 2023/4/15
 *
 * @author daoge_cmd | Cool_Loong
 */
@Getter
public final class AllayBlockType<T extends BlockBehavior> implements BlockType<T> {
    public static int computeSpecialValue(BlockPropertyType.BlockPropertyValue<?, ?, ?>[] propertyValues) {
        int nbits = 0;
        for (var value : propertyValues) nbits += value.getPropertyType().getBitSize();
        return computeSpecialValue(nbits, propertyValues);
    }

    public static int computeSpecialValue(int nbits, BlockPropertyType.BlockPropertyValue<?, ?, ?>[] propertyValues) {
        int specialValue = 0;
        for (var value : propertyValues) {
            specialValue |= value.getIndex() << (nbits - value.getPropertyType().getBitSize());
            nbits -= value.getPropertyType().getBitSize();
        }
        return specialValue;
    }

    private final Class<T> interfaceClass;
    private Class<T> injectedClass;
    private final List<BlockComponentImpl> components;
    private final Map<String, BlockPropertyType<?>> properties;
    private final Identifier identifier;
    private final Map<Integer, BlockState> blockStateHashMap;
    private final int specialValueBits;
    private BlockState defaultState;
    private T blockBehavior;
    @Nullable
    private Map<Integer, BlockState> specialValueMap;


    private AllayBlockType(Class<T> interfaceClass,
                           List<BlockComponentImpl> components,
                           Map<String, BlockPropertyType<?>> properties,
                           Identifier identifier) {
        this.interfaceClass = interfaceClass;
        this.components = components;
        this.properties = Collections.unmodifiableMap(properties);
        this.identifier = identifier;
        this.blockStateHashMap = initStates();
        int nbits = 0;
        for (var value : properties.values()) nbits += value.getBitSize();
        this.specialValueBits = nbits;
        if (nbits <= 32) {
            this.specialValueMap = Collections.unmodifiableMap(blockStateHashMap.values().stream().collect(Collectors.toMap(BlockState::specialValue, Function.identity(), (v1, v2) -> v1, Int2ObjectArrayMap::new)));
        }
    }

    public static <T extends BlockBehavior> BlockTypeBuilder<T> builder(Class<T> interfaceClass) {
        return new Builder<>(interfaceClass);
    }

    @Override
    public BlockState ofState(List<BlockPropertyType.BlockPropertyValue<?, ?, ?>> propertyValues) {
        return BlockStateHashPalette.getRegistry().get(HashUtils.computeBlockStateHash(identifier, propertyValues));
    }

    @Override
    @UnmodifiableView
    public Collection<BlockState> getAllStates() {
        return Collections.unmodifiableCollection(blockStateHashMap.values());
    }

    private Map<Integer, BlockState> initStates() {
        List<BlockPropertyType<?>> propertyTypeList = this.properties.values().stream().toList();
        int size = propertyTypeList.size();
        if (size == 0) {
            this.defaultState = new AllayBlockState(this, new BlockPropertyType.BlockPropertyValue[]{});
            var singleBlockStateMap = new Int2ObjectArrayMap<BlockState>();
            singleBlockStateMap.put(defaultState.blockStateHash(), defaultState);
            return singleBlockStateMap;
        }

        var blockStates = new Int2ObjectArrayMap<BlockState>();

        // to keep track of next element in each of
        // the n arrays
        int[] indices = new int[size];

        // initialize with first element's index
        Arrays.fill(indices, 0);

        while (true) {
            // Generate BlockState
            ImmutableList.Builder<BlockPropertyType.BlockPropertyValue<?, ?, ?>> values = ImmutableList.builder();
            for (int i = 0; i < size; ++i) {
                BlockPropertyType<?> type = propertyTypeList.get(i);
                values.add(type.tryCreateValue(type.getValidValues().get(indices[i])));
            }
            var state = new AllayBlockState(this, values.build().toArray(BlockPropertyType.BlockPropertyValue[]::new));
            blockStates.put(state.blockStateHash(), state);

            // find the rightmost array that has more
            // elements left after the current element
            // in that array
            int next = size - 1;
            while (next >= 0 && (indices[next] + 1 >= propertyTypeList.get(next).getValidValues().size())) {
                next--;
            }

            // no such array is found so no more
            // combinations left
            if (next < 0) break;

            // if found move to next element in that
            // array
            indices[next]++;

            // for all arrays to the right of this
            // array current index again points to
            // first element
            for (int i = next + 1; i < size; i++) {
                indices[i] = 0;
            }
        }
        int defaultStateHash = HashUtils.computeBlockStateHash(this.identifier, properties.values().stream().map(p -> p.tryCreateValue(p.getDefaultValue())).collect(Collectors.toList()));
        for (var s : blockStates.values()) {
            if (s.blockStateHash() == defaultStateHash) {
                this.defaultState = s;
            }
        }
        return Collections.unmodifiableMap(blockStates);
    }

    /**
     * Each {@link AllayBlockState} is a singleton, stored in the {@link AllayBlockStateHashPalette AllayBlockPaletteRegistry}, which means you can directly use == to compare whether two Block States are equal
     */
    record AllayBlockState(BlockType<?> blockType,
                           BlockPropertyType.BlockPropertyValue<?, ?, ?>[] blockPropertyValues,
                           int blockStateHash,
                           int specialValue) implements BlockState {
        public AllayBlockState(BlockType<?> blockType, BlockPropertyType.BlockPropertyValue<?, ?, ?>[] propertyValues, int blockStateHash) {
            this(blockType,
                    propertyValues,
                    blockStateHash,
                    AllayBlockType.computeSpecialValue(propertyValues));
        }

        public AllayBlockState(BlockType<?> blockType, BlockPropertyType.BlockPropertyValue<?, ?, ?>[] propertyValues) {
            this(blockType, propertyValues, HashUtils.computeBlockStateHash(blockType.getIdentifier(), Arrays.stream(propertyValues).toList()));
        }

        @Override
        public long unsignedBlockStateHash() {
            return Integer.toUnsignedLong(blockStateHash);
        }

        @Override
        public @UnmodifiableView Map<BlockPropertyType<?>, BlockPropertyType.BlockPropertyValue<?, ?, ?>> getPropertyValues() {
            return UnmodifiableLinkedHashMap.warp(Arrays.stream(blockPropertyValues).collect(
                    LinkedHashMap<BlockPropertyType<?>, BlockPropertyType.BlockPropertyValue<?, ?, ?>>::new,
                    (hashMap, blockPropertyValue) -> hashMap.put(blockPropertyValue.getPropertyType(), blockPropertyValue),
                    LinkedHashMap::putAll));
        }

        @Override
        public <DATATYPE, PROPERTY extends BlockPropertyType<DATATYPE>> DATATYPE getPropertyValue(PROPERTY p) {
            for (var property : blockPropertyValues) {
                if (property.getPropertyType() == p) {
                    return (DATATYPE) property.getValue();
                }
            }
            throw new IllegalArgumentException("Property " + p + " is not supported by this block");
        }

        @Override
        public BlockState setProperty(BlockPropertyType.BlockPropertyValue<?, ?, ?> propertyValue) {
            var newPropertyValues = new BlockPropertyType.BlockPropertyValue<?, ?, ?>[this.blockPropertyValues.length];
            var succeed = false;
            for (int i = 0; i < blockPropertyValues.length; i++) {
                if (blockPropertyValues[i].getPropertyType() == propertyValue.getPropertyType()) {
                    succeed = true;
                    newPropertyValues[i] = propertyValue;
                } else newPropertyValues[i] = blockPropertyValues[i];
            }
            if (!succeed) {
                throw new IllegalArgumentException("Property " + propertyValue.getPropertyType() + " is not supported by this block");
            }
            return getNewBlockState(newPropertyValues);
        }

        @Override
        public <DATATYPE, PROPERTY extends BlockPropertyType<DATATYPE>> BlockState setProperty(PROPERTY property, DATATYPE value) {
            return setProperty(property.createValue(value));
        }

        @Override
        public BlockState setProperties(List<BlockPropertyType.BlockPropertyValue<?, ?, ?>> propertyValues) {
            var newPropertyValues = new BlockPropertyType.BlockPropertyValue<?, ?, ?>[this.blockPropertyValues.length];
            var succeedCount = 0;
            var succeed = new boolean[propertyValues.size()];
            for (int i = 0; i < blockPropertyValues.length; i++) {
                int index;
                if ((index = propertyValues.indexOf(blockPropertyValues[i])) != -1) {
                    succeedCount++;
                    succeed[index] = true;
                    newPropertyValues[i] = propertyValues.get(index);
                } else newPropertyValues[i] = blockPropertyValues[i];
            }
            if (succeedCount != propertyValues.size()) {
                var errorMsgBuilder = new StringBuilder("Properties ");
                for (int i = 0; i < propertyValues.size(); i++) {
                    if (!succeed[i]) {
                        errorMsgBuilder.append(propertyValues.get(i).getPropertyType().getName());
                        if (i != propertyValues.size() - 1)
                            errorMsgBuilder.append(", ");
                    }
                }
                errorMsgBuilder.append(" are not supported by this block");
                throw new IllegalArgumentException(errorMsgBuilder.toString());
            }
            return getNewBlockState(newPropertyValues);
        }

        private BlockState getNewBlockState(BlockPropertyType.BlockPropertyValue<?, ?, ?>[] values) {
            int bits = blockType.getSpecialValueBits();
            if (bits < 32) {
                Map<Integer, BlockState> specialValueMap = blockType.getSpecialValueMap();
                assert specialValueMap != null;
                return specialValueMap.get(computeSpecialValue(bits, values));
            } else {
                return blockType.getBlockStateHashMap().get(HashUtils.computeBlockStateHash(this.blockType.getIdentifier(), values));
            }
        }
    }

    public static class Builder<T extends BlockBehavior> implements BlockTypeBuilder<T> {
        protected Class<T> interfaceClass;
        protected List<BlockComponentImpl> components = new ArrayList<>();
        protected Map<String, BlockPropertyType<?>> properties = new HashMap<>();
        protected Identifier identifier;
        protected boolean isCustomBlock = false;

        public Builder(Class<T> interfaceClass) {
            if (interfaceClass == null)
                throw new BlockTypeBuildException("Interface class cannot be null!");
            this.interfaceClass = interfaceClass;
        }

        @Override
        public Builder<T> identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        @Override
        public Builder<T> identifier(String identifier) {
            this.identifier = new Identifier(identifier);
            return this;
        }

        @Override
        public Builder<T> vanillaBlock(VanillaBlockId vanillaBlockId) {
            return vanillaBlock(vanillaBlockId, true);
        }

        @Override
        public Builder<T> vanillaBlock(VanillaBlockId vanillaBlockId, boolean initVanillaBlockAttributeComponent) {
            this.identifier = vanillaBlockId.getIdentifier();
            if (initVanillaBlockAttributeComponent) {
                var attributeMap = VanillaBlockAttributeRegistry.getRegistry().get(vanillaBlockId);
                if (attributeMap == null)
                    throw new BlockTypeBuildException("Cannot find vanilla block attribute component for " + vanillaBlockId + " from vanilla block attribute registry!");
                components.add(BlockAttributeComponentImpl.ofMappedBlockStateHash(attributeMap));
            }
            return this;
        }

        @Override
        public Builder<T> setProperties(BlockPropertyType<?>... properties) {
            this.properties = Arrays.stream(properties).collect(Collectors.toMap(BlockPropertyType::getName, Function.identity()));
            return this;
        }

        @Override
        public Builder<T> setProperties(List<BlockPropertyType<?>> properties) {
            this.properties = properties.stream().collect(Collectors.toMap(BlockPropertyType::getName, Function.identity()));
            return this;
        }

        @Override
        public Builder<T> setComponents(List<BlockComponentImpl> components) {
            if (components == null)
                throw new BlockTypeBuildException("Component providers cannot be null");
            this.components = new ArrayList<>(components);
            return this;
        }

        @Override
        public Builder<T> addComponents(List<BlockComponentImpl> components) {
            this.components.addAll(components);
            return this;
        }

        @Override
        public Builder<T> addComponent(BlockComponentImpl component) {
            this.components.add(component);
            return this;
        }

        @Override
        public Builder<T> addBasicComponents() {
            Arrays.stream(interfaceClass.getDeclaredFields())
                    .filter(field -> isStatic(field.getModifiers()))
                    .filter(field -> field.getDeclaredAnnotation(AutoRegister.class) != null)
                    .filter(field -> BlockComponentImpl.class.isAssignableFrom(field.getType()))
                    .sorted(Comparator.comparingInt(field -> field.getDeclaredAnnotation(AutoRegister.class).order()))
                    .forEach(field -> {
                        try {
                            addComponent((BlockComponentImpl) field.get(null));
                        } catch (IllegalAccessException e) {
                            throw new BlockTypeBuildException(e);
                        } catch (ClassCastException e) {
                            throw new BlockTypeBuildException("Field " + field.getName() + "in class" + interfaceClass + " is not a ComponentProvider<? extends BlockComponentImpl>!", e);
                        }
                    });
            return this;
        }

        @Override
        public Builder<T> addCustomBlockComponent(CustomBlockComponentImpl customBlockComponent) {
            components.add(customBlockComponent);
            isCustomBlock = true;
            return this;
        }

        @Override
        public AllayBlockType<T> build() {
            if (identifier == null) throw new BlockTypeBuildException("identifier cannot be null!");
            var type = new AllayBlockType<>(interfaceClass, components, properties, identifier);
            components.add(new BlockBaseComponentImpl(type));
            List<ComponentProvider<? extends ComponentImpl>> componentProviders = components.stream().map(ComponentProvider::ofSingleton).collect(Collectors.toList());
            try {
                checkPropertyValid();
                Class<T> clazz = ComponentClassCacheUtils.loadBlockType(interfaceClass);
                if (clazz == null) {
                    type.injectedClass = new AllayComponentInjector<T>()
                            .interfaceClass(interfaceClass)
                            .component(componentProviders)
                            .inject(true);
                } else {
                    type.injectedClass = clazz;
                }
                AllayComponentInjector.injectInitializer(type.injectedClass, componentProviders);
                //Cache constructor
                type.blockBehavior = type.injectedClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new BlockTypeBuildException("Failed to create block type!", e);
            }
            type.register(BlockTypeRegistry.getRegistry());
            type.register(BlockStateHashPalette.getRegistry());
            return type;
        }

        private void checkPropertyValid() {
            for (var component : components) {
                var annotation = component.getClass().getAnnotation(RequireBlockProperty.Requirements.class);
                if (annotation == null) continue;
                var requirements = annotation.value();
                for (var requirement : requirements) {
                    var type = requirement.type();
                    var name = requirement.name();
                    var match = properties.get(name);
                    if (match == null)
                        throw new BlockComponentInjectException("Component " + component.getClass().getSimpleName() + " expects a block property of type " + type + " named " + name + ", but there is no match");
                    if (match.getType() != type)
                        throw new BlockComponentInjectException("Component " + component.getClass().getSimpleName() + " expects a block property of type " + type + " named " + name + ", but the type is " + properties.get(name).getType());
                }
            }
        }
    }
}
