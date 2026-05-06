package module_2_core._48_sealed;

// Sealed + record = Algebraic Data Type (ADT) idiomatic.
// Mô hình hoá kết quả của một thao tác (success / failure / pending).

public class SealedAdtDemo {

    // ADT cho HTTP response
    sealed interface Result<T> permits Result.Success, Result.Failure, Result.Pending {
        record Success<T>(T value, int status) implements Result<T> {}
        record Failure<T>(String error, int status) implements Result<T> {}
        record Pending<T>() implements Result<T> {}
    }

    // Process result an toàn — exhaustive
    static <T> String render(Result<T> r) {
        return switch (r) {
            case Result.Success<T>(T v, int s)       -> "OK[" + s + "]: " + v;
            case Result.Failure<T>(String e, int s)  -> "ERR[" + s + "]: " + e;
            case Result.Pending<T>()                 -> "...waiting";
        };
    }

    // ADT cho Tree (kinh điển)
    sealed interface Tree<T> permits Tree.Leaf, Tree.Node {
        record Leaf<T>(T value) implements Tree<T> {}
        record Node<T>(Tree<T> left, T value, Tree<T> right) implements Tree<T> {}
    }

    static <T extends Comparable<T>> int depth(Tree<T> t) {
        return switch (t) {
            case Tree.Leaf<T> leaf -> 1;
            case Tree.Node<T>(var l, var v, var r) -> 1 + Math.max(depth(l), depth(r));
        };
    }

    public static void main(String[] args) {
        Result<String> r1 = new Result.Success<>("Alice", 200);
        Result<String> r2 = new Result.Failure<>("not found", 404);
        Result<String> r3 = new Result.Pending<>();

        for (Result<String> r : java.util.List.of(r1, r2, r3)) {
            System.out.println(render(r));
        }

        Tree<Integer> tree = new Tree.Node<>(
                new Tree.Node<>(new Tree.Leaf<>(1), 2, new Tree.Leaf<>(3)),
                4,
                new Tree.Leaf<>(5)
        );
        System.out.println("depth = " + depth(tree));    // 3
    }
}
