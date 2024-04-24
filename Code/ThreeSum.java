import java.util.Random;
public class ThreeSum {
    public static int count(int[] a){
        int n = a.length;
        int cnt = 0;
        for (int i = 0; i < n; i++){
            for (int j = i+1; j < n; j++){
                for (int k = j+1; k < n; k++){
                    if (a[i] + a[j] + a[k] == 0){
                        cnt++;
                    }
                }
            }
        }

        return cnt;
    }

    public static void main(String[] args){
        //int n = Integer.parseInt(args[0]);
        int[] a = { -10, 0, 10 ,-40, 30};
        Random r = new Random();
        /*for (int i = 0; i < n; i++){
            a[i] =r.nextInt();
        }*/
        System.out.println(count(a));
            
        }
    }
