import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

public class JSArray<E extends Object> extends AbstractList<E> implements RandomAccess, Cloneable {
    private Object[] elementData = {};

    private int size;

    /** Initialize a JSArray with a size of 0, be sure to use the add function for new items */
    public JSArray() {
        super();
    }

    /**
     * Initialize a JSArray with a size of {@code initialLength}
     *
     * @param initialLength The initial size of the initialized array
     */
    public JSArray(int initialLength) {
        super();
        elementData = new Object[initialLength];
        size = initialLength;
    }

    public JSArray(Collection<? extends E> c) {
        super();
        Object[] arr = c.toArray();
        if ((size = c.size()) != 0) {
            if (c.getClass() == JSArray.class) {
                elementData = arr;
            } else {
                elementData = Arrays.copyOf(arr, size, arr.getClass());
            }
        } else {
            elementData = new Object[] {};
        }
    }

    public JSArray(List<E> arr) {
        super();
        elementData = arr.toArray();
        size = arr.size();
    }

    public JSArray(E[] arr) {
        super();
        for (int i = 0; i < arr.length; i++) {
            add(i, arr[i]);
        }
    }

    /**
     * @return boolean indicating wether is array is empty or not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds {@code element} to the end of the array, increasing the array's size by 1
     *
     * @param element element to add to the array
     */
    @Override
    public boolean add(E element) {
        elementData = grow();
        elementData[size - 1] = element;
        return true;
    }

    /**
     * Adds {@code element} to the end of the array, increasing the array's size by 1
     *
     * @param element element to add to the array
     */
    public void push(E element) {
        add(element);
    }

    /**
     * Adds {@code element} to the start of the array, increasing the array's size by 1
     *
     * @param element element to add to the array
     */
    public void unshift(E element) {
        add(0, element);
    }

    @Override
    public void add(int index, E element) {
        rangeCheckAdd(index);
        Object[] newData = grow();
        // elData [1, 2, 3, 4, 5]
        // newData [1, 2, 3, 4, 5, null]
        // []
        System.arraycopy(elementData, index, newData, index + 1, elementData.length - index);
        newData[index] = element;
        elementData = newData;
    }

    public void insert(int index, E element) {
        add(index, element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        rangeCheckGet(index);
        return (E) elementData[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        E oldEl = (E) elementData[index];
        elementData[index] = element;
        return oldEl;
    }

    @SuppressWarnings("unchecked")
    public E pop() {
        E removed = (E) elementData[size - 1];
        elementData = Arrays.copyOf(elementData, checkCapacity(size - 1));
        return removed;
    }

    @SuppressWarnings("unchecked")
    public E shift() {
        E removed = (E) elementData[size - 1];
        System.arraycopy(
                elementData, 1, (elementData = new Object[size - 1]), 0, checkCapacity(size - 1));
        return removed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index == 0) return shift();
        E removed = (E) elementData[index];
        Object[] newData = new Object[size - 1];

        System.arraycopy(elementData, 0, newData, 0, index);
        System.arraycopy(elementData, index + 1, newData, index, size - index - 1);
        elementData = newData;
        size -= 1;

        return removed;
    }

    @Override
    public boolean remove(Object target) {
        if (target == null) {
            for (int i = 0; i < elementData.length; i++) {
                if (elementData[i] == null) {
                    return remove(i) != null ? false : true;
                }
            }
        } else {
            for (int i = 0; i < elementData.length; i++) {
                if (target.equals(elementData[i])) {
                    return remove(i) != null ? true : false;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public E[] toArray() {
        return (E[]) Arrays.copyOf(elementData, size);
    }

    public boolean contains(Object target) {
        return indexOf(target) >= 0;
    }

    public boolean containsReverse(Object target) {
        return indexOfReverse(target) >= 0;
    }

    public int indexOf(Object target) {
        if (target == null) {
            for (int i = 0; i < elementData.length; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
            return -1;
        } else {
            for (int i = 0; i < elementData.length; i++) {
                if (target.equals(elementData[i])) {
                    return i;
                }
            }
            return -1;
        }
    }

    public int indexOfReverse(Object target) {
        if (target == null) {
            for (int i = elementData.length - 1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
            return -1;
        } else {
            for (int i = elementData.length - 1; i >= 0; i--) {
                if (target.equals(elementData[i])) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * @param target object you would like to find in the array
     * @return {@code target} or null if target is not found in the array
     */
    public E find(Object target) {
        if (target == null) {
            for (int i = 0; i < elementData.length; i++) {
                if (elementData[i] == null) {
                    return get(i);
                }
            }
            return null;
        } else {
            for (int i = 0; i < elementData.length; i++) {
                if (target.equals(elementData[i])) {
                    return get(i);
                }
            }
            return null;
        }
    }

    /**
     * @param target object you would like to find in the array
     * @return {@code target} or null if target is not found in the array
     */
    public E findReverse(Object target) {
        if (target == null) {
            for (int i = elementData.length - 1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return get(i);
                }
            }
            return null;
        } else {
            for (int i = elementData.length - 1; i >= 0; i--) {
                if (target.equals(elementData[i])) {
                    return get(i);
                }
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public JSArray<E> slice(int from, int to) {
        return new JSArray<E>((E[]) Arrays.copyOfRange(elementData, from, to));
    }

    @SuppressWarnings("unchecked")
    public JSArray<E> concat(List<E> arr) {
        E[] newArr = (E[]) new Object[size + arr.size()];
        System.arraycopy(elementData, 0, newArr, 0, size);
        System.arraycopy(arr.toArray(), 0, newArr, size, arr.size());
        return new JSArray<E>(newArr);
    }

    @SuppressWarnings("unchecked")
    public JSArray<E> concat(E[] arr) {
        E[] newArr = (E[]) new Object[size + arr.length];
        System.arraycopy(elementData, 0, newArr, 0, size);
        System.arraycopy(arr, 0, newArr, size, arr.length);
        return new JSArray<E>(newArr);
    }

    /**
     * Join each element in the array with {@code separator}
     *
     * @param separator a string with what you would like to separate each element
     * @return a string with every element in the array separated by {@code separator}
     */
    public String join(String separator) {
        String result = "";
        for (int i = 0; i < size; i++) {
            result = result.concat(this.get(i).toString());
            if (i != size - 1) result = result.concat(separator);
        }
        return result;
    }

    /**
     * Join with a comma as a separator
     *
     * @return a string with every element in the array separated by a comma
     */
    public String join() {
        String separator = ",";
        String result = "";
        for (int i = 0; i < size; i++) {
            result = result.concat(this.get(i).toString());
            if (i != size - 1) result = result.concat(separator);
        }
        return result;
    }

    public JSArray<E> reverse() {
        JSArray<E> newArr = new JSArray<>();
        if (size == 0) return newArr;
        for (int i = size - 1; i >= 0; i--) newArr.push(get(i));
        return newArr;
    }

    @FunctionalInterface
    public static interface BaseMapFunction<E, T extends Object> {
        public T map(E e);
    }

    @SuppressWarnings("unchecked")
    @FunctionalInterface
    public static interface TwoArgMapFunction<E, T extends Object> extends BaseMapFunction<E, T> {
        public T map(E e, int i);

        @Override
        default T map(E e) {
            return (T) new Object();
        }
        ;
    }

    @SuppressWarnings("unchecked")
    @FunctionalInterface
    public static interface ThreeArgMapFunction<E, T extends Object>
            extends TwoArgMapFunction<E, T> {
        public T map(E e, int i, JSArray<E> arr);

        @Override
        default T map(E e, int i) {
            return (T) new Object();
        }
        ;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public <T> JSArray<T> map(BaseMapFunction<E, T> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<T> mapped = new JSArray<>();
        for (int i = 0; i < this.size(); i++) {
            T result = predicate.map(this.get(i));
            if (result == null) continue;
            mapped.push(result);
        }
        return mapped;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public <T> JSArray<T> map(TwoArgMapFunction<E, T> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<T> mapped = new JSArray<>();
        for (int i = 0; i < this.size(); i++) {
            T result = predicate.map(this.get(i), i);
            if (result == null) continue;
            mapped.push(result);
        }
        return mapped;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public <T> JSArray<T> map(ThreeArgMapFunction<E, T> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<T> mapped = new JSArray<>();
        for (int i = 0; i < this.size(); i++) {
            T result = predicate.map(this.get(i), i, this);
            if (result == null) continue;
            mapped.push(result);
        }
        return mapped;
    }

    @FunctionalInterface
    public static interface BaseFilterFunction<E> {
        public boolean filter(E t);
    }

    @FunctionalInterface
    public static interface TwoArgFilterFunction<E> extends BaseFilterFunction<E> {
        public boolean filter(E t, int i);

        @Override
        default boolean filter(E t) {
            return true;
        }
        ;
    }

    @FunctionalInterface
    public static interface ThreeArgFilterFunction<E> extends TwoArgFilterFunction<E> {
        public boolean filter(E t, int i, JSArray<E> arr);

        @Override
        default boolean filter(E t, int i) {
            return true;
        }
        ;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public JSArray<E> filter(BaseFilterFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<E> filtered = new JSArray<>();
        for (int i = 0; i < this.size; i++)
            if (predicate.filter(this.get(i))) filtered.add(this.get(i));
        return filtered;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public JSArray<E> filter(TwoArgFilterFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<E> filtered = new JSArray<>();
        for (int i = 0; i < this.size; i++)
            if (predicate.filter(this.get(i), i)) filtered.add(this.get(i));
        return filtered;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public JSArray<E> filter(ThreeArgFilterFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        JSArray<E> filtered = new JSArray<>();
        for (int i = 0; i < this.size; i++)
            if (predicate.filter(this.get(i), i, this)) filtered.add(this.get(i));
        return filtered;
    }

    @FunctionalInterface
    public static interface BaseForEachFunction<E> extends Consumer<E> {
        public void accept(E t);
    }

    public static interface TwoArgForEachFunction<E> extends BaseForEachFunction<E> {
        public void accept(E t, int i);

        @Override
        default void accept(E t) {}
    }

    public static interface ThreeArgForEachFunction<E> extends TwoArgForEachFunction<E> {
        public void accept(E t, int i, JSArray<E> arr);

        @Override
        default void accept(E t, int i) {}
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void forEach(BaseForEachFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < size; i++) predicate.accept(this.get(i));
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void forEach(TwoArgForEachFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < size; i++) predicate.accept(this.get(i), i);
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void forEach(ThreeArgForEachFunction<E> predicate) {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < size; i++) predicate.accept(this.get(i), i, this);
    }

    @FunctionalInterface
    public static interface BaseEveryFunction<E> {
        public boolean accept(E t);
    }

    public static interface TwoArgEveryFunction<E> extends BaseEveryFunction<E> {
        public boolean accept(E t, int i);

        @Override
        default boolean accept(E t) {
            return true;
        }
    }

    public static interface ThreeArgEveryFunction<E> extends TwoArgEveryFunction<E> {
        public boolean accept(E t, int i, JSArray<E> arr);

        @Override
        default boolean accept(E t, int i) {
            return true;
        }
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean every(BaseEveryFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (!predicate.accept(get(i))) return false;
        }
        return true;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean every(TwoArgEveryFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (!predicate.accept(get(i), i)) return false;
        }
        return true;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean every(ThreeArgEveryFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (!predicate.accept(get(i), i, this)) return false;
        }
        return true;
    }

    @FunctionalInterface
    public static interface BaseSomeFunction<E> {
        public boolean accept(E t);
    }

    public static interface TwoArgSomeFunction<E> extends BaseSomeFunction<E> {
        public boolean accept(E t, int i);

        @Override
        default boolean accept(E t) {
            return true;
        }
    }

    public static interface ThreeArgSomeFunction<E> extends TwoArgSomeFunction<E> {
        public boolean accept(E t, int i, JSArray<E> arr);

        @Override
        default boolean accept(E t, int i) {
            return true;
        }
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean some(BaseSomeFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.accept(get(i))) return true;
        }
        return false;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean some(TwoArgSomeFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.accept(get(i), i)) return true;
        }
        return false;
    }
    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean some(ThreeArgSomeFunction<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.accept(get(i), i, this)) return true;
        }
        return false;
    }

    /**
     * Returns a shallow copy of this {@code JSArray} instance. (The elements themselves are not
     * copied.)
     *
     * @return a clone of this {@code JSArray} instance
     */
    @SuppressWarnings("unchecked")
    public JSArray<E> clone() {
        try {
            JSArray<E> clone = (JSArray<E>) super.clone();
            clone.elementData = Arrays.copyOf(elementData, size);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }

        if (o.getClass() == JSArray.class) {
            return ((JSArray<?>) o).size == size && equalItems((JSArray<?>) o);
        } else {
            return ((List<?>) o).size() == size && equalItems((List<?>) o);
        }
    }

    private boolean equalItems(List<?> o) {
        try {
            for (int i = 0; i < size; i++) {
                if (o.get(i).equals(this.get(i))) continue;
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(elementData);
    }

    private Object[] grow(int newSize) {
        return Arrays.copyOf(elementData, checkCapacity(newSize));
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    private int checkCapacity(int newSize) {
        if (newSize > MAX_ARRAY_SIZE || newSize < 0) {
            throw new OutOfMemoryError("JSArray size too large or less than 0.");
        } else {
            return (size = newSize);
        }
    }

    private void rangeCheckAdd(int index) {
        if (size < index || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
    }

    private void rangeCheckGet(int index) {
        if (size <= index || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
    }

    public static void test() {
        Integer[] base = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JSArray<Integer> test = new JSArray<>(base);
        test.add(1, 11);
        System.out.println("js arr len: " + test.size());
        System.out.println("js arr: " + test);
        JSArray<Integer> remove = new JSArray<>(base);
        remove.pop();
        remove.remove(2);
        remove.shift();
        remove.remove(Integer.valueOf(5));
        JSArray<Integer> mapped =
                test.map(
                        (var item) -> {
                            if (item == 11) return null;
                            return item * 2;
                        });
        JSArray<Integer> filtered = test.filter((var item) -> item >= 10);
        test.forEach((var item) -> System.out.print(item + " "));
        System.out.println("");

        System.out.println("removed arr: " + remove);
        System.out.println("removed size: " + remove.size());
        System.out.println("mapped: " + mapped);
        System.out.println("filtered: " + filtered);
        System.out.println("contains 11: " + test.contains(11));
        System.out.println("index of 11: " + test.indexOf(11));
        System.out.println("contains 11 reverse: " + test.containsReverse(11));
        System.out.println("index of 11 reverse: " + test.indexOfReverse(11));
        System.out.println("sliceed: " + test.slice(4, 8));
        System.out.println(
                "concated: " + test.concat(remove) + " size: " + test.concat(remove).size());
        System.out.println("joined: " + test.join());
        System.out.println("every: " + test.every((var item) -> item > 0));
        System.out.println("some: " + test.some((var item) -> item > 10));
        System.out.println("reverse: " + test.reverse());
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
}
