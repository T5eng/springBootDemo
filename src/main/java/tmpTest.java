import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Stack;

public class tmpTest {
    public static void main(String[] args) {
        String s = "string";
        StringBuilder sb;
        sb = new StringBuilder(s);
        System.out.println(sb.append(s));
        System.out.println((s+sb).getClass());
        System.out.println("hello");
    }
}
