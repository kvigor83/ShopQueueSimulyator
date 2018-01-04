package by.kastsiuchenka.kassa.main;


import java.util.*;

public class Runner {
    private final static String REG = ".{4}";
    private static String[] a = {"123423", "2123123", "3123", "4223", "25","3456","123"};

    public static void main(String[] args) {
        int i=0;
        ArrayList<String> list=null;
        Arrays.sort(a, (o1, o2) -> {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return 0;
            }
        });
        for (Object о : a) {
            System.out.print(о + " ");

            list=new ArrayList(Arrays.asList(a));
            ListIterator<String> iter=list.listIterator();
            while(iter.hasNext()) {
                if(iter.next().length()>4){
                    i =iter.previousIndex();
                }

            }


        }


        System.out.println(i);

    }

    private static int calcSum(int a) {
        int sum = 0;
        int b;
        while (a > 0) {
            b = a % 10;
            sum += b;
            a /= 10;
        }

        return sum;
    }
}