package floatingmuseum.floatingmusic.test;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Floatingmuseum on 2017/6/5.
 */

public class TestJava1 {

    public void test(){
//        TestJava<Integer> testInteger = test;
        String[] ss = new String[]{};
        Object[] ob = ss;
        ob[0]= 1;

        List<Fruit> fruits = new ArrayList<>();
        List<? super Apple> list = fruits;
        list.add(new Apple());
        list.add(new GreenApple());
        list.add(new FujiApple());
        Object object = list.get(0);
    }
}
