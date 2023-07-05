package your.pckg;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author TwentyBytes
 * created in 07.03.2023
 */
@SuppressWarnings("all")
public abstract class BaseNBT {

    public static String FULL_VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];
    public static int INT_VERSION = Integer.parseInt(FULL_VERSION.split("_")[1]);
    public static String NATIVE_PACKAGE = INT_VERSION > 16 ? "net.minecraft.nbt." : "net.minecraft.server." + FULL_VERSION + ".";
    public static String CRAFT_BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + FULL_VERSION + ".";

    private static Class<?> NBT_BASE_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTBase");
    private static Class<?> NBT_TAG_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagCompound");

    // base
    public static Class<?> NBT_TAG_COMPOUND_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagCompound");

    // end
    public static Class<?> NBT_TAG_END_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagEnd");
    public static Object NBT_TAG_END_INSTANCE = null;

    static {
        try {
            for (Field field : NBT_TAG_END_CLASS.getFields()) {
                field.setAccessible(true);

                if (field.getType() == NBT_TAG_END_CLASS) {
                    NBT_TAG_END_INSTANCE = field.get(null);
                    break;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    // primitives
    public static Class<?> NBT_TAG_BYTE_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagByte");
    public static Class<?> NBT_TAG_SHORT_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagShort");
    public static Class<?> NBT_TAG_INT_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagInt");
    public static Class<?> NBT_TAG_LONG_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagLong");
    public static Class<?> NBT_TAG_FLOAT_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagFloat");
    public static Class<?> NBT_TAG_DOUBLE_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagDouble");
    public static Class<?> NBT_TAG_STRING_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagString");

    // arrays
    public static Class<?> NBT_TAG_BYTE_ARRAY_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagByteArray");
    public static Class<?> NBT_TAG_INT_ARRAY_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagIntArray");
    public static Class<?> NBT_TAG_LONG_ARRAY_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagLongArray");

    // list
    public static Class<?> NBT_TAG_LIST_CLASS = Reflect.findClass(NATIVE_PACKAGE + "NBTTagList");

    @NotNull
    public static Object byteBaseSource(byte value) {
        return Reflect.construct(NBT_TAG_BYTE_CLASS, value);
    }

    @NotNull
    public static Object shortBaseSource(int value) {
        return Reflect.construct(NBT_TAG_SHORT_CLASS, value);
    }

    @NotNull
    public static Object intBaseSource(int value) {
        return Reflect.construct(NBT_TAG_INT_CLASS, value);
    }

    @NotNull
    public static Object longBaseSource(long value) {
        return Reflect.construct(NBT_TAG_LONG_CLASS, value);
    }

    @NotNull
    public static Object floatBaseSource(float value) {
        return Reflect.construct(NBT_TAG_FLOAT_CLASS, value);
    }

    @NotNull
    public static Object doubleBaseSource(double value) {
        return Reflect.construct(NBT_TAG_DOUBLE_CLASS, value);
    }

    @NotNull
    public static Object stringBaseSource(String value) {
        return Reflect.construct(NBT_TAG_STRING_CLASS, value);
    }

    @NotNull
    public static Object nbtTagBaseSource() {
        return Reflect.construct(NBT_TAG_COMPOUND_CLASS);
    }

    @NotNull
    public static Object endBase() {
        return NBT_TAG_END_INSTANCE;
    }

    @NotNull
    public static Object byteArrayBaseSource(byte... values) {
        try {
            return Reflect.findConstructor(NBT_TAG_BYTE_ARRAY_CLASS, byte[].class).newInstance(values);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    @NotNull
    public static Object intArrayBaseSource(int... values) {
        try {
            return Reflect.findConstructor(NBT_TAG_INT_ARRAY_CLASS, int[].class).newInstance(values);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    @NotNull
    public static Object longArrayBaseSource(long... values) {
        try {
            return Reflect.findConstructor(NBT_TAG_LONG_ARRAY_CLASS, long[].class).newInstance(values);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    @NotNull
    public static Object listBaseSource() {
        try {
            return Reflect.findConstructor(NBT_TAG_LIST_CLASS).newInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    public static BaseList baseList(BaseNBT... values) {
        return new BaseList(new ArrayList<>(List.of(values)));
    }

    public static BaseShort shortBase(short value) {
        return new BaseShort(shortBaseSource(value));
    }

    public static BaseInt intBase(int value) {
        return new BaseInt(intBaseSource(value));
    }

    public static BaseLong longBase(long value) {
        return new BaseLong(longBaseSource(value));
    }

    public static BaseFloat floatBase(float value) {
        return new BaseFloat(floatBaseSource(value));
    }

    public static BaseDouble doubleBase(double value) {
        return new BaseDouble(doubleBaseSource(value));
    }

    public static BaseString stringBase(String value) {
        return new BaseString(stringBaseSource(value));
    }

    public static BaseByteArray byteArrayBase(byte[] value) {
        return new BaseByteArray(byteArrayBaseSource(value));
    }

    public static BaseIntArray byteArrayBase(int[] value) {
        return new BaseIntArray(intArrayBaseSource(value));
    }

    public static BaseLongArray byteArrayBase(long[] value) {
        return new BaseLongArray(longArrayBaseSource(value));
    }

    public static BaseTagCompound tagCompoundBase() {
        return new BaseTagCompound();
    }

    @SuppressWarnings("all")
    private static final BaseEnd END_INSTANCE = new BaseEnd();

    public static BaseEnd baseEnd() {
        return END_INSTANCE;
    }

    @NotNull
    public static BaseNBT getBase(Object object) {
        Class<?> class0 = object.getClass();

        if (!NBT_BASE_CLASS.isAssignableFrom(class0)) {
            throw new IllegalStateException("Base ins`t NBTBase instance");
        }

        if (class0 == NBT_TAG_BYTE_CLASS) {
            return new BaseByte(object);
        }
        if (class0 == NBT_TAG_SHORT_CLASS) {
            return new BaseShort(object);
        }
        if (class0 == NBT_TAG_INT_CLASS) {
            return new BaseInt(object);
        }
        if (class0 == NBT_TAG_LONG_CLASS) {
            return new BaseLong(object);
        }
        if (class0 == NBT_TAG_FLOAT_CLASS) {
            return new BaseFloat(object);
        }
        if (class0 == NBT_TAG_DOUBLE_CLASS) {
            return new BaseDouble(object);
        }
        if (class0 == NBT_TAG_STRING_CLASS) {
            return new BaseString(object);
        }

        if (class0 == NBT_TAG_BYTE_ARRAY_CLASS) {
            return new BaseByteArray(object);
        }
        if (class0 == NBT_TAG_INT_ARRAY_CLASS) {
            return new BaseIntArray(object);
        }
        if (class0 == NBT_TAG_LONG_ARRAY_CLASS) {
            return new BaseLongArray(object);
        }

        if (class0 == NBT_TAG_COMPOUND_CLASS) {
            return new BaseTagCompound(object);
        }

        if (class0 == NBT_TAG_END_CLASS) {
            return baseEnd();
        }

        if (class0 == NBT_TAG_LIST_CLASS) {
            return new BaseList(object);
        }

        throw new IllegalStateException("Wtf? Cant create wrapped base because not found..");
    }

    public abstract Object getNbtBase();

    public abstract BaseType getBaseType();

    public <T> T as(Class<T> clazz) {
        return as();
    }

    public <T> T as() {
        return (T) this;
    }

    public <T> T baseAs(Class<T> clazz) {
        return as();
    }

    public <T> T baseAs() {
        return (T) this;
    }

    public class CompoundMap extends HashMap<String, BaseNBT> {

        private Map<Object, Object> baseMap;

        public CompoundMap(Map<Object, Object> source) {
            baseMap = source;

            for (Entry<Object, Object> entry : source.entrySet()) {
                put((String) entry.getKey(), getBase(entry.getValue()));
            }
        }

        @Override
        public BaseNBT remove(Object key) {
            baseMap.remove((String) key);
            return super.remove(key);
        }

        @Override
        public boolean remove(Object key, Object value) {
            baseMap.remove((String) key, ((BaseNBT) value).getNbtBase());
            return super.remove(key, value);
        }

        @Override
        public BaseNBT put(String key, BaseNBT value) {
            baseMap.put(key, value.getNbtBase());
            return super.put(key, value);
        }

        public BaseNBT getOrDefault(String key, Supplier<BaseNBT> defaultValue) {
            return super.getOrDefault(key, defaultValue.get());
        }

        @Override
        public Set<Entry<String, BaseNBT>> entrySet() {
            throw new UnsupportedOperationException("Entry set not allowed.");
        }

    }

    public enum BaseType {

        NBT_TAG_COMPOUND,

        NBT_BASE_BYTE,
        NBT_BASE_SHORT,
        NBT_BASE_INT,
        NBT_BASE_LONG,
        NBT_BASE_FLOAT,
        NBT_BASE_DOUBLE,
        NBT_BASE_STRING,

        NBT_BASE_BYTE_ARRAY,
        NBT_BASE_INT_ARRAY,
        NBT_BASE_LONG_ARRAY,

        NBT_BASE_END,

        NBT_BASE_LIST

    }

    public static class Util {

        private static Class<?> CRAFT_ITEM_STACK_CLASS = Reflect.findClass(CRAFT_BUKKIT_PACKAGE + "inventory.CraftItemStack");

        private static Field NBT_TAG_ITEM_STACK_FIELD_NATIVE;

        static {
            Class<?> NATIVE_ITEM_STACK_CLASS = Reflect.findClass(INT_VERSION > 16 ? "net.minecraft.world.item.ItemStack" : NATIVE_PACKAGE + "ItemStack");

            for (Field field : NATIVE_ITEM_STACK_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.getType() == NBT_TAG_CLASS) {
                    NBT_TAG_ITEM_STACK_FIELD_NATIVE = field;
                    break;
                }
            }
        }

        public static ItemStack setupTo(ItemStack stack, BaseTagCompound compound) {
            if (stack.getClass() != CRAFT_ITEM_STACK_CLASS && stack.getClass().getSuperclass() != CRAFT_ITEM_STACK_CLASS) {
                stack = Reflect.invoke(CRAFT_ITEM_STACK_CLASS, "asCraftCopy", stack);
            }

            try {
                NBT_TAG_ITEM_STACK_FIELD_NATIVE.set(Reflect.get(stack, "handle"), compound.getNbtBase());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return stack;
        }

        public static BaseTagCompound fromItem(ItemStack stack) {
            if (stack.getClass() != CRAFT_ITEM_STACK_CLASS && stack.getClass().getSuperclass() != CRAFT_ITEM_STACK_CLASS) {
                stack = Reflect.invoke(CRAFT_ITEM_STACK_CLASS, "asCraftCopy", stack);
            }

            Object nativeStack = Reflect.get(stack, "handle");
            Object tag = Reflect.get(nativeStack, NBT_TAG_ITEM_STACK_FIELD_NATIVE.getName());

            if (tag == null) {
                tag = Reflect.construct(NBT_TAG_CLASS);
            }

            return new BaseTagCompound(tag);
        }

    }

    public static class BaseTagCompound extends BaseNBT {

        private static Method NBT_TAG_PUT_NBT_BASE_METHOD;

        private static Field NBT_TAG_MAP;

        private static boolean loaded;

        @SuppressWarnings("all")
        private static void load() {
            if (loaded) {
                return;
            }
            for (Method method : NBT_TAG_CLASS.getDeclaredMethods()) {
                method.setAccessible(true);

                Class<?>[] classes = method.getParameterTypes();
                if (classes.length == 2 && classes[0] == String.class && classes[1] == NBT_BASE_CLASS) {
                    NBT_TAG_PUT_NBT_BASE_METHOD = method;
                }
            }

            for (Field field : NBT_TAG_CLASS.getFields()) {
                field.setAccessible(true);

                if (field.getType() == Map.class) {
                    NBT_TAG_MAP = field;
                    break;
                }
            }

            loaded = true;
        }

        private Object tag;
        private CompoundMap map;

        public BaseTagCompound() {
            this.tag = Reflect.construct(NBT_TAG_CLASS);
            this.map = new CompoundMap(Reflect.get(tag, NBT_TAG_MAP.getName()));
        }

        public BaseTagCompound(Object object) {
            load();

            if (!NBT_TAG_CLASS.isAssignableFrom(object.getClass())) {
                throw new IllegalStateException("Object isn`t NBTTagCompound instance.");
            }

            this.tag = object;
            this.map = new CompoundMap(Reflect.get(tag, NBT_TAG_MAP.getName()));
        }

        public BaseTagCompound set(String key, Object base) {
            if (!NBT_BASE_CLASS.isAssignableFrom(base.getClass())) {
                throw new IllegalStateException("Base ins`t NBTBase instance");
            }

            try {
                NBT_TAG_PUT_NBT_BASE_METHOD.invoke(tag, key, base);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return this;
        }

        public BaseTagCompound set(String key, BaseNBT baseNBT) {
            try {
                NBT_TAG_PUT_NBT_BASE_METHOD.invoke(tag, key, baseNBT.getNbtBase());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return this;
        }

        public BaseTagCompound setByte(String key, byte value) {
            set(key, byteBaseSource(value));
            return this;
        }

        public BaseTagCompound setShort(String key, short value) {
            set(key, shortBaseSource(value));
            return this;
        }

        public BaseTagCompound setInt(String key, int value) {
            set(key, intBaseSource(value));
            return this;
        }

        public BaseTagCompound setLong(String key, long value) {
            set(key, longBaseSource(value));
            return this;
        }

        public BaseTagCompound setFloat(String key, float value) {
            set(key, floatBaseSource(value));
            return this;
        }

        public BaseTagCompound setDouble(String key, double value) {
            set(key, doubleBaseSource(value));
            return this;
        }

        public BaseTagCompound setString(String key, String value) {
            set(key, stringBaseSource(value));
            return this;
        }

        public BaseTagCompound setByteArray(String key, byte[] value) {
            set(key, byteArrayBaseSource(value));
            return this;
        }

        public BaseTagCompound setIntArray(String key, int[] value) {
            set(key, intArrayBaseSource(value));
            return this;
        }

        public BaseTagCompound setLongArray(String key, long[] value) {
            set(key, longArrayBaseSource(value));
            return this;
        }

        public BaseTagCompound setEnd(String key) {
            set(key, END_INSTANCE);
            return this;
        }

        public BaseTagCompound remove(String key) {
            getValuesSource().remove(key);
            return this;
        }

        public Map<Object, Object> getValuesSource() {
            return Reflect.get(tag, NBT_TAG_MAP.getName());
        }

        public CompoundMap getValues() {
            return map;
        }

        @Override
        public Object getNbtBase() {
            return tag;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_TAG_COMPOUND;
        }

    }

    public static class BaseByte extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_BYTE_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == byte.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseByte(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_BYTE;
        }

        public byte getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseShort extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_SHORT_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == short.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseShort(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_SHORT;
        }

        public short getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseInt extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_INT_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == int.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseInt(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_INT;
        }

        public int getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseLong extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_LONG_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == long.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseLong(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_LONG;
        }

        public long getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseFloat extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_FLOAT_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == float.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseFloat(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_FLOAT;
        }

        public float getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseDouble extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_DOUBLE_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == double.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseDouble(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_DOUBLE;
        }

        public double getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseString extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_STRING_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == String.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseString(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_STRING;
        }

        public String getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseEnd extends BaseNBT {

        @Override
        public Object getNbtBase() {
            return NBT_TAG_END_INSTANCE;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_END;
        }

    }

    public static class BaseByteArray extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_BYTE_ARRAY_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == byte[].class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseByteArray(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_BYTE_ARRAY;
        }

        public byte[] getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseIntArray extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_INT_ARRAY_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == int[].class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseIntArray(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_INT_ARRAY;
        }

        public int[] getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseLongArray extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_LONG_ARRAY_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == long[].class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;

        public BaseLongArray(Object nbtBase) {
            this.nbtBase = nbtBase;
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_LONG_ARRAY;
        }

        public long[] getValue() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

    }

    public static class BaseList extends BaseNBT {

        private static Field FIELD;

        static {
            for (Field field : BaseNBT.NBT_TAG_LIST_CLASS.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getType() == List.class) {
                    FIELD = field;
                    break;
                }
            }
        }

        private final Object nbtBase;
        private final LinkedNBTList list;

        public BaseList(Object nbtBase) {
            this.nbtBase = nbtBase;
            this.list = new LinkedNBTList(getValueSource());
        }

        @Override
        public Object getNbtBase() {
            return nbtBase;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.NBT_BASE_LIST;
        }

        public List<Object> getValueSource() {
            return Reflect.get(nbtBase, FIELD.getName());
        }

        public LinkedNBTList getValue() {
            return list;
        }

        public static class LinkedNBTList extends ArrayList<BaseNBT> {

            private final List<Object> baseList;

            public LinkedNBTList(List<Object> baseList) {
                this.baseList = new ArrayList<>();

                for (Object o : baseList) {
                    add(getBase(o));
                }
            }

            @Override
            public BaseNBT get(int index) {
                return super.get(index);
            }

            @Override
            public boolean add(BaseNBT baseNBT) {
                baseList.add(baseNBT.getNbtBase());
                return super.add(baseNBT);
            }

            @Override
            public BaseNBT remove(int index) {
                baseList.remove(index);
                return super.remove(index);
            }

            @Override
            public boolean remove(Object o) {
                if (o instanceof BaseNBT baseNBT) {
                    return super.remove(o) && baseList.remove(baseNBT.getNbtBase());
                }
                return false;
            }

            @Override
            public void add(int index, BaseNBT element) {
                super.add(index, element);
                baseList.add(index, element.getNbtBase());
            }

        }

    }

    @SuppressWarnings({"unchecked", "unused"})
    public static class Reflect {

        private static final Map<Class<?>, Reflect.ClassData<?>> cache = new ConcurrentHashMap<>();

        private Reflect() {
        }

        public static <E> E construct(Class<E> clazz, Object... args) {
            try {
                return getClass(clazz).construct(args);
            } catch (Exception e) {
                throw new IllegalArgumentException("Constructor error", e);
            }
        }

        public static <E> E get(Class<?> clazz, String field) {
            try {
                return (E) getClass(clazz).get(null, field);
            } catch (Exception e) {
                throw new IllegalArgumentException("Get static field error", e);
            }
        }

        public static <R> R get(Object instance, String field) {
            try {
                return (R) getClass(instance.getClass()).get(instance, field);
            } catch (Exception e) {
                throw new IllegalArgumentException("Get field error", e);
            }
        }

        public static <T, E> E get(Class<T> clazz, T instance, String field) {
            try {
                return (E) getClass(clazz).get(instance, field);
            } catch (Exception e) {
                throw new IllegalArgumentException("Get field error", e);
            }
        }

        public static void set(Class<?> clazz, String field, Object value) {
            try {
                getClass(clazz).set(null, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set static field error", e);
            }
        }

        public static void set(Object instance, String field, Object value) {
            try {
                getClass(instance.getClass()).set(instance, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set field error", e);
            }
        }

        public static <T> void set(Class<T> clazz, T instance, String field, Object value) {
            try {
                getClass(clazz).set(instance, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set field error", e);
            }
        }

        public static void setFinal(Class<?> clazz, String field, Object value) {
            try {
                getClass(clazz).setFinal(null, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set static final field error", e);
            }
        }

        public static void setFinal(Object instance, String field, Object value) {
            try {
                getClass(instance.getClass()).setFinal(instance, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set final field error", e);
            }
        }

        public static <T> void setFinal(Class<T> clazz, T instance, String field, Object value) {
            try {
                getClass(clazz).setFinal(instance, field, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Set final field error", e);
            }
        }

        public static <E> E invoke(Class<?> clazz, String method, Object... args) {
            try {
                return (E) getClass(clazz).invoke(null, method, args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Invoke static error", e);
            }
        }

        public static <E> E invoke(Object instance, String method, Object... args) {
            try {
                return (E) getClass(instance.getClass()).invoke(instance, method, args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Invoke error", e);
            }
        }

        public static <T, E> E invoke(Class<T> clazz, T instance, String method, Object... args) {
            try {
                return (E) getClass(clazz).invoke(instance, method, args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Invoke error", e);
            }
        }

        public static <T> boolean isConstructorExist(Class<T> clazz, Class<?>... args) {
            return findConstructor(clazz, args) != null;
        }

        public static <T> boolean isMethodExist(Class<T> clazz, String method, Class<?>... args) {
            return findMethod(clazz, method, args) != null;
        }

        public static <T> boolean isFieldExist(Class<T> clazz, String field) {
            return findField(clazz, field) != null;
        }

        public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>... args) {
            try {
                return getClass(clazz).findConstructor0(args);
            } catch (Exception ignored) {
                return null;
            }
        }

        public static <T> Method findMethod(Class<T> clazz, String method, Class<?>... args) {
            try {
                return getClass(clazz).findMethod0(method, args);
            } catch (Exception ignored) {
                return null;
            }
        }

        public static <T> Field findField(Class<T> clazz, String field) {
            try {
                return getClass(clazz).findField(field);
            } catch (Exception ignored) {
                return null;
            }
        }

        public static <T> Field findFinalField(Class<T> clazz, String field) {
            try {
                return getClass(clazz).findFinalField(field);
            } catch (Exception ignored) {
                return null;
            }
        }

        public static Class<?> findClass(String name) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        }

        public static MethodHandles.Lookup lookup() {
            return get(MethodHandles.Lookup.class, "IMPL_LOOKUP");
        }

        private static <T> ClassData<T> getClass(Class<T> clazz) {
            return (ClassData<T>) cache.computeIfAbsent(clazz, Reflect.ClassData::new);
        }

        static class ClassData<K> {
//        private static final Field FIELD_MODIFIERS;
//
//        static {
//            try {
//                System.out.println(Arrays.toString(Field.class.getFields()));
//
//                FIELD_MODIFIERS = Field.class.getDeclaredField("modifiers");
//                FIELD_MODIFIERS.setAccessible(true);
//            } catch (Exception ex) {
//                throw new IllegalStateException("Field modifiers field not found", ex);
//            }
//        }

            private final Class<K> clazz;
            private final Map<String, Field> fields = new HashMap<>();
            private final Map<Object, Method> methods = new HashMap<>();
            private final Map<ConstructorMapKey, Constructor<K>> constructors = new HashMap<>();

            boolean aggressiveOverloading = false;

            public ClassData(Class<K> clazz) {
                this.clazz = clazz;
            }

            void set(Object instance, String field, Object value) throws Exception {
                this.findField(field).set(instance, value);
            }

            void setFinal(Object instance, String field, Object value) throws Exception {
                findFinalField(field).set(instance, value);
            }

            Object get(Object instance, String field) throws Exception {
                return this.findField(field).get(instance);
            }

            Object invoke(Object instance, String method, Object... args) throws Throwable {
                Method handle = this.findMethod(method, args);
                try {
                    return handle.invoke(instance, args);
                } catch (IllegalArgumentException ex) {
                    // Проверяем нужно ли включить aggressiveOverloading
                    // Если при вызове метода ашыпка, топ проверяем типы аргументов
                    if (!aggressiveOverloading) {
                        Class<?>[] needed = handle.getParameterTypes();
                        Class<?>[] real = toTypes(args);
                        if (needed.length != real.length)
                            throw new IllegalStateException("Impossible error. Method parameters count mismatch");
                        boolean equals = true;
                        for (int i = 0; i < needed.length; i++) {
                            if (needed[i] != real[i]) {
                                equals = false;
                                break;
                            }
                        }
                        if (!equals) {
                            aggressiveOverloading = true;
                            methods.clear();
                            return this.findMethod(method, args).invoke(instance, args);
                        }
                    }
                    throw ex;
                }
            }

            K construct(Object... args) throws Exception {
                return this.findConstructor(args).newInstance(args);
            }

            Constructor<K> findConstructor(Object... args) {
                return findConstructor0(toTypes(args));
            }

            @SuppressWarnings("rawtypes")
            Constructor<K> findConstructor0(Class<?>... types) {
                ConstructorMapKey mapped = new ConstructorMapKey(types);
                Constructor<K> con = constructors.get(mapped);
                if (con == null) {
                    constructorsLoop:
                    for (Constructor c : clazz.getDeclaredConstructors()) {
                        Class<?>[] ptypes = c.getParameterTypes();
                        if (ptypes.length != types.length)
                            continue;

                        for (int i = 0; i < ptypes.length; i++)
                            if (types[i] != null && ptypes[i] != types[i] && !ptypes[i].isAssignableFrom(types[i]))
                                continue constructorsLoop;

                        con = c;
                        con.setAccessible(true);
                        constructors.put(mapped, con);
                        break;
                    }

                    if (con == null)
                        throw new UnableToFindConstructorException(clazz, types);
                }
                return con;
            }

            Method findMethod(String name, Object... args) {
                Class<?>[] types = null;
                Object mapped;
                if (aggressiveOverloading) {
                    types = toTypes(args);
                    mapped = new AggressiveMethodMapKey(name, types);
                } else {
                    mapped = new MethodMapKey(name, args.length);
                }

                Method method = methods.get(mapped);
                if (method == null) {
                    if (types == null)
                        types = toTypes(args);
                    method = fastFindMethod(name, types);
                    methods.put(mapped, method);
                }
                return method;
            }

            Method findMethod0(String name, Class<?>... types) {
                Object mapped = new AggressiveMethodMapKey(name, types);
                Method method = methods.get(mapped);
                if (method == null) {
                    method = fastFindMethod(name, types);
                    methods.put(mapped, method);
                }
                return method;
            }

            @SuppressWarnings("StringEquality")
            private Method fastFindMethod(String name, Class<?>... types) {
                Method method = null;
                name = name.intern();
                Class<?> clazz0 = clazz;
                do {
                    methodsLoop:
                    for (Method m : clazz0.getDeclaredMethods()) {
                        if (name != m.getName())
                            continue;

                        Class<?>[] ptypes = m.getParameterTypes();
                        if (ptypes.length != types.length)
                            continue;

                        for (int i = 0; i < ptypes.length; i++)
                            if (types[i] != null && ptypes[i] != types[i] && !ptypes[i].isAssignableFrom(types[i]))
                                continue methodsLoop;

                        method = m;
                        break;
                    }
                    if (method != null) {
                        method.setAccessible(true);
                        break;
                    }
                    clazz0 = clazz0.getSuperclass();
                } while (clazz0 != null);
                if (method == null)
                    throw new UnableToFindMethodException(clazz, name, types);
                return method;
            }

            Field findFinalField(String name) throws Exception {
                Field field = findField(name);
                //FIELD_MODIFIERS.set(field, field.getModifiers() & ~Modifier.FINAL);
                return field;
            }

            @SuppressWarnings("ConstantConditions")
            Field findField(String name) {
                Field field = fields.get(name);
                if (field == null) {
                    Class<?> clazz0 = clazz;
                    while (clazz0 != null) {
                        try {
                            field = clazz0.getDeclaredField(name);
                            field.setAccessible(true);
                            fields.put(name, field);
                            break;
                        } catch (Exception e) {
                            clazz0 = clazz0.getSuperclass();
                        }
                    }
                    if (field == null)
                        throw new UnableToFindFieldException(clazz, name);
                }
                return field;
            }

            private Class<?>[] toTypes(Object[] objects) {
                if (objects.length == 0)
                    return new Class[0];

                Class<?>[] types = new Class[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] == null) {
                        types[i] = null;
                        continue;
                    }
                    Class<?> type = objects[i].getClass();
                    if (type == Integer.class)
                        type = Integer.TYPE;
                    else if (type == Double.class)
                        type = Double.TYPE;
                    else if (type == Boolean.class)
                        type = Boolean.TYPE;
                    else if (type == Float.class)
                        type = Float.TYPE;
                    else if (type == Long.class)
                        type = Long.TYPE;
                    else if (type == Character.class)
                        type = Character.TYPE;
                    else if (type == Byte.class)
                        type = Byte.TYPE;
                    else if (type == Short.class)
                        type = Short.TYPE;
                    types[i] = type;
                }
                return types;
            }
        }

        private static class ConstructorMapKey {
            final Class<?>[] types;

            public ConstructorMapKey(Class<?>[] types) {
                this.types = types;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(types);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof ConstructorMapKey other))
                    return false;
                if (types.length != other.types.length)
                    return false;
                for (int i = 0; i < types.length; i++)
                    if (types[i] != other.types[i])
                        return false;
                return true;
            }
        }

        private static class MethodMapKey {
            private final String name;
            private final int args;

            public MethodMapKey(String name, int args) {
                this.name = name;
                this.args = args;
            }

            @Override
            public int hashCode() {
                return name.hashCode() + args;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof MethodMapKey other))
                    return false;
                return other.args == args && other.name.equals(name);
            }
        }

        private static class AggressiveMethodMapKey {
            private final Class<?>[] types;
            private final String name;

            public AggressiveMethodMapKey(String name, Class<?>[] types) {
                this.name = name;
                this.types = types;
            }

            @Override
            public int hashCode() {
                int hash = name.hashCode();
                hash = 31 * hash + Arrays.hashCode(types);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof AggressiveMethodMapKey other))
                    return false;
                if (types.length != other.types.length ||
                        !other.name.equals(name))
                    return false;
                for (int i = 0; i < types.length; i++)
                    if (types[i] != other.types[i])
                        return false;
                return true;
            }
        }

        private static String classesToString(Class<?>[] classes) {
            int iMax = classes.length - 1;
            if (iMax == -1)
                return "()";

            StringBuilder b = new StringBuilder();
            b.append('(');
            for (int i = 0; ; i++) {
                b.append(classes[i].getName());
                if (i == iMax)
                    return b.append(')').toString();
                b.append(',');
            }
        }

        private static class UnableToFindFieldException extends RuntimeException {

            private final String fieldName;
            private final String className;

            public UnableToFindFieldException(Class<?> clazz, String fieldName) {
                super();
                this.fieldName = fieldName;
                this.className = clazz.getName();
            }

            @Override
            public String getMessage() {
                return toString();
            }

            @Override
            public String toString() {
                return "Unable to find field '" + fieldName + "' in class '" + className + "'";
            }

        }

        private static class UnableToFindMethodException extends RuntimeException {

            protected final String methodName;
            protected final String className;
            protected final Class<?>[] types;

            public UnableToFindMethodException(Class<?> clazz, String methodName, Class<?>[] types) {
                super();
                this.methodName = methodName;
                this.className = clazz.getName();
                this.types = types;
            }

            @Override
            public String getMessage() {
                return toString();
            }

            @Override
            public String toString() {
                return "Unable to find method '" + className + "." + methodName + classesToString(types) + "'";
            }

        }

        private static class UnableToFindConstructorException extends UnableToFindMethodException {

            public UnableToFindConstructorException(Class<?> clazz, Class<?>[] types) {
                super(clazz, null, types);
            }

            @Override
            public String toString() {
                return "Unable to find constructor '" + className + ".<init>" + classesToString(types) + "'";
            }

        }

    }

}
