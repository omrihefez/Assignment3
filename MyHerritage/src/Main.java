import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String args[]){
        Random rn = new Random();
        int a[] = new int[10000];
        for (int i = 0; i < a.length; i++)
            if (i % 100 == 0)
                a[i] = 7;
            else
                a[i] = rn.nextInt(10) + 1;
        long startTime = System.nanoTime();
        System.out.println(solution(a, a.length));
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }


    public static int solution(int[] A, int Y) {
        int solution = -1;
        int mostRepeatingNum = -1;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < A.length; i++) {
            if (!map.containsKey(A[i])) {
                map.put(A[i], 1);
                if (1 > solution) {
                    solution = 1;
                    mostRepeatingNum = A[i];
                }
            }
            else {
                int value = map.get(A[i]) + 1;
                map.put(A[i], value);
                if (value > solution) {
                    solution = value;
                    mostRepeatingNum = A[i];
                }
            }
        }
//        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
//            if (entry.getValue() > solution) {
//                solution = entry.getValue();
//                mostRepeatingNum = entry.getKey();
//            }
//        }
        return mostRepeatingNum;
    }

    public static int solution2(int[] A, int Y) {
        int current = 0;
        int solution = Integer.MIN_VALUE;
        for (int i = 0; i < A.length; i++){
            current = Math.max(A[i], current + A[i]);
            solution = Math.max(solution, current);
        }
        return solution;
    }
}
