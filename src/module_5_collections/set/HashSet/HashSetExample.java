package module_5_collections.set.HashSet;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class HashSetExample {
    public static void main(String[] args) {
        Set<Integer> hashSet = new HashSet<>();

        // Thêm phần tử
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);

        // In phần tử
        System.out.println("HashSet: " + hashSet);

        // Kiểm tra sự tồn tại
        System.out.println("Contains 2: " + hashSet.contains(2));

        // Xóa phần tử
        hashSet.remove(2);
        System.out.println("After removing 2: " + hashSet);

        // Kích thước của HashSet
        System.out.println("Size: " + hashSet.size());

        // Chuyển HashSet sang LinkedHashSet
        Set<Integer> linkedHashSet = new LinkedHashSet<>(hashSet);
        System.out.println("LinkedHashSet: " + linkedHashSet);

        // Chuyển HashSet sang TreeSet (sắp xếp theo thứ tự tự nhiên)
        Set<Integer> treeSet = new TreeSet<>(hashSet);
        System.out.println("TreeSet: " + treeSet);
    }
}

