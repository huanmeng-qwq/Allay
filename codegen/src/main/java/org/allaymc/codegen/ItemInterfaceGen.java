package org.allaymc.codegen;

import com.palantir.javapoet.*;
import lombok.SneakyThrows;
import org.allaymc.dependence.ItemId;

import javax.lang.model.element.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author daoge_cmd | IWareQ
 */
public class ItemInterfaceGen extends BaseInterfaceGen {

    public static final TypeSpec.Builder ITEM_TYPE_DEFAULT_INITIALIZER_CLASS_BUILDER =
            TypeSpec.classBuilder(ClassNames.ITEM_TYPE_DEFAULT_INITIALIZER)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("Automatically generated by {@code org.allaymc.codegen.ItemInterfaceGen}");
    public static Map<Pattern, String> SUB_PACKAGE_GROUPERS = new LinkedHashMap<>();

    public static void main(String[] args) {
        ItemIdEnumGen.generate();
        generate();
    }

    @SneakyThrows
    public static void generate() {
        registerSubPackages();
        var interfaceDir = Path.of("api/src/main/java/org/allaymc/api/item/interfaces");
        if (!Files.exists(interfaceDir)) Files.createDirectories(interfaceDir);
        var typesClass = TypeSpec
                .classBuilder(ClassNames.ITEM_TYPES)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassNames.MINECRAFT_VERSION_SENSITIVE)
                .addJavadoc("Automatically generated by {@code org.allaymc.codegen.ItemInterfaceGen}");
        for (var id : ItemId.values()) {
            typesClass.addField(
                    FieldSpec.builder(ParameterizedTypeName.get(ClassNames.ITEM_TYPE, generateClassFullName(id)), id.name())
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .build()
            );
            var itemClassSimpleName = generateClassSimpleName(id);
            var itemClassFullName = generateClassFullName(id);
            var folderName = tryFindSpecifiedFolderName(itemClassSimpleName);
            var folderPath = folderName != null ? interfaceDir.resolve(folderName) : interfaceDir;
            var path = folderPath.resolve(itemClassSimpleName + ".java");
            if (!Files.exists(path)) {
                System.out.println("Generating " + itemClassSimpleName + "...");
                if (!Files.exists(folderPath))
                    Files.createDirectories(folderPath);
                generateClass(ClassNames.ITEM_STACK, itemClassFullName, path);
            }
            addDefaultItemTypeInitializer(id, itemClassFullName);
        }
        generateDefaultItemTypeInitializer();
        var javaFile = JavaFile.builder(ClassNames.ITEM_TYPES.packageName(), typesClass.build())
                .indent(CodeGenConstants.INDENT)
                .skipJavaLangImports(true)
                .build();
        System.out.println("Generating " + ClassNames.ITEM_TYPES.simpleName() + ".java ...");
        Files.writeString(Path.of("api/src/main/java/org/allaymc/api/item/type/" + ClassNames.ITEM_TYPES.simpleName() + ".java"), javaFile.toString());
    }

    private static void addDefaultItemTypeInitializer(ItemId id, ClassName itemClassName) {
        var initializer = CodeBlock.builder();
        initializer
                .add("$T.$N = $T\n", ClassNames.ITEM_TYPES, id.name(), ClassNames.ALLAY_ITEM_TYPE)
                .add("        .builder($T.class)\n", itemClassName)
                .add("        .vanillaItem($T.$N)\n", ClassNames.ITEM_ID, id.name())
                .add("        .build();");
        ITEM_TYPE_DEFAULT_INITIALIZER_CLASS_BUILDER
                .addMethod(
                        MethodSpec.methodBuilder(generateInitializerMethodName(id))
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addStatement("if ($T.$N != null) return", ClassNames.ITEM_TYPES, id.name())
                                .addCode(initializer.build())
                                .build()
                );
    }

    @SneakyThrows
    private static void generateDefaultItemTypeInitializer() {
        var filePath = Path.of("server/src/main/java/org/allaymc/server/item/type/ItemTypeDefaultInitializer.java");
        Files.deleteIfExists(filePath);
        var folderPath = filePath.getParent();
        if (!Files.exists(folderPath))
            Files.createDirectories(folderPath);
        var javaFile = JavaFile.builder(ClassNames.ITEM_TYPE_DEFAULT_INITIALIZER.packageName(), ITEM_TYPE_DEFAULT_INITIALIZER_CLASS_BUILDER.build())
                .indent(CodeGenConstants.INDENT)
                .skipJavaLangImports(true)
                .build();
        System.out.println("Generating " + ClassNames.ITEM_TYPE_DEFAULT_INITIALIZER.simpleName() + ".java ...");
        Files.writeString(filePath, javaFile.toString());
    }

    private static ClassName generateClassFullName(ItemId id) {
        var simpleName = generateClassSimpleName(id);
        var folderName = tryFindSpecifiedFolderName(simpleName);
        return ClassName.get("org.allaymc.api.item.interfaces" + (folderName != null ? "." + folderName : ""), simpleName);
    }

    private static String generateClassSimpleName(ItemId id) {
        // The Windows environment is not case-sensitive, so some item IDs need to be specially processed.
        // netherbrick and nether_brick require special handling
        if (id == ItemId.NETHERBRICK) return "ItemNetherbrick0Stack";
        // tallgrass and tall_grass require special handling
        if (id == ItemId.TALLGRASS) return "ItemTallgrass0Stack";
        return "Item" + Utils.convertToPascalCase(id.getIdentifier().path().replace(".", "_")) + "Stack";
    }

    private static String generateInitializerMethodName(ItemId id) {
        // 同上
        if (id == ItemId.NETHERBRICK) return "initNetherbrick0";
        if (id == ItemId.TALLGRASS) return "initTallgrass0";
        return "init" + Utils.convertToPascalCase(id.getIdentifier().path().replace(".", "_"));
    }

    private static String tryFindSpecifiedFolderName(String blockClassSimpleName) {
        for (var entry : SUB_PACKAGE_GROUPERS.entrySet()) {
            var pattern = entry.getKey();
            if (pattern.matcher(blockClassSimpleName).find()) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static void registerSubPackage(Pattern regex, String packageName) {
        SUB_PACKAGE_GROUPERS.put(regex, packageName);
    }

    private static void registerSubPackages() {
        registerSubPackage(Pattern.compile(".*StairsStack"), "stairs");
        registerSubPackage(Pattern.compile(".*DoorStack"), "door");
        registerSubPackage(Pattern.compile(".*Slab\\d?Stack"), "slab");
        registerSubPackage(Pattern.compile(".*StandingSignStack"), "standingsign");
        registerSubPackage(Pattern.compile(".*HangingSignStack"), "hangingsign");
        registerSubPackage(Pattern.compile(".*WallSignStack"), "wallsign");
        registerSubPackage(Pattern.compile(".*SignStack"), "sign");
        registerSubPackage(Pattern.compile(".*WallStack"), "wall");
        registerSubPackage(Pattern.compile("ItemElement.*"), "element");
        registerSubPackage(Pattern.compile(".*CoralStack"), "coral");
        registerSubPackage(Pattern.compile(".*CoralBlockStack"), "coralblock");
        registerSubPackage(Pattern.compile(".*CoralFan.*"), "coralfan");
        registerSubPackage(Pattern.compile(".*BricksStack"), "bricks");
        registerSubPackage(Pattern.compile(".*WoolStack"), "wool");
        registerSubPackage(Pattern.compile(".*ButtonStack"), "button");
        registerSubPackage(Pattern.compile(".*PlanksStack"), "planks");
        registerSubPackage(Pattern.compile(".*TrapdoorStack"), "trapdoor");
        registerSubPackage(Pattern.compile(".*CandleStack"), "candle");
        registerSubPackage(Pattern.compile(".*CandleCakeStack"), "candlecake");
        registerSubPackage(Pattern.compile(".*ConcreteStack"), "concrete");
        registerSubPackage(Pattern.compile(".*ConcretePowderStack"), "concretepowder");
        registerSubPackage(Pattern.compile(".*TerracottaStack"), "terracotta");
        registerSubPackage(Pattern.compile(".*ShulkerBoxStack"), "shulkerbox");
        registerSubPackage(Pattern.compile(".*CarpetStack"), "carpet");
        registerSubPackage(Pattern.compile(".*WoodStack"), "wood");
        registerSubPackage(Pattern.compile(".*(Leaves\\d?|LeavesFlowered)Stack"), "leaves");
        registerSubPackage(Pattern.compile(".*FenceStack"), "fence");
        registerSubPackage(Pattern.compile(".*FenceGateStack"), "fencegate");
        registerSubPackage(Pattern.compile(".*Log\\d?Stack"), "log");
        registerSubPackage(Pattern.compile(".*CopperStack"), "copper");
        registerSubPackage(Pattern.compile(".*SaplingStack"), "sapling");
        registerSubPackage(Pattern.compile(".*(?:Water|Lava)Stack"), "liquid");
        registerSubPackage(Pattern.compile(".*BoatStack"), "boat");
        registerSubPackage(Pattern.compile(".*MinecartStack"), "minecart");
        registerSubPackage(Pattern.compile(".*BucketStack"), "bucket");
        registerSubPackage(Pattern.compile(".*EggStack"), "egg");
        registerSubPackage(Pattern.compile("ItemMusicDisc.*"), "musicdisc");
        registerSubPackage(Pattern.compile("ItemPiston.*"), "piston");
        registerSubPackage(Pattern.compile("ItemStickyPiston.*"), "piston");
        registerSubPackage(Pattern.compile(".*StainedGlassStack"), "stainedglass");
        registerSubPackage(Pattern.compile(".*StainedGlassPaneStack"), "stainedglasspane");
        registerSubPackage(Pattern.compile(".*GlassStack"), "glass");
        registerSubPackage(Pattern.compile(".*GlassPaneStack"), "glasspane");
        registerSubPackage(Pattern.compile(".*HelmetStack"), "helmet");
        registerSubPackage(Pattern.compile(".*ChestplateStack"), "chestplate");
        registerSubPackage(Pattern.compile(".*LeggingsStack"), "leggings");
        registerSubPackage(Pattern.compile(".*BootsStack"), "boots");
        registerSubPackage(Pattern.compile(".*SwordStack"), "sword");
        registerSubPackage(Pattern.compile(".*PickaxeStack"), "pickaxe");
        registerSubPackage(Pattern.compile(".*ShovelStack"), "shovel");
        registerSubPackage(Pattern.compile(".*HoeStack"), "hoe");
        registerSubPackage(Pattern.compile(".*AxeStack"), "axe");
        registerSubPackage(Pattern.compile(".*SandstoneStack"), "sandstone");
        registerSubPackage(Pattern.compile(".*SandStack"), "sand");
        registerSubPackage(Pattern.compile(".*Torchflower.*Stack"), "torchflower");
        registerSubPackage(Pattern.compile(".*Torch.*Stack"), "torch");
        registerSubPackage(Pattern.compile(".*LightBlock.*Stack"), "lightblock");
        registerSubPackage(Pattern.compile(".*DirtStack"), "dirt");
        registerSubPackage(Pattern.compile(".*AnvilStack"), "anvil");
        registerSubPackage(Pattern.compile(".*CoralWallFanStack"), "coralwallfan");
        registerSubPackage(Pattern.compile("ItemPurpur.*"), "purpur");
        registerSubPackage(Pattern.compile(".*SpongeStack"), "sponge");
        registerSubPackage(Pattern.compile(".*TntStack"), "tnt");
        registerSubPackage(Pattern.compile(".*(Head|Skull)Stack"), "head");
        registerSubPackage(Pattern.compile(".*BundleStack"), "bundle");
    }
}
