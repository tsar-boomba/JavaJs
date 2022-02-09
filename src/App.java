public class App {
    public static void main(String[] args) throws Exception {
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
        JSArray<Integer> mapped = test.map((var item) -> {
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
        System.out.println("concated: " + test.concat(remove) + " size: " + test.concat(remove).size());
        System.out.println("joined: " + test.join());
        System.out.println("every: " + test.every((var item) -> item > 0));
        System.out.println("some: " + test.some((var item) -> item > 10));
        System.out.println("reverse: " + test.reverse());
    }
}
