package tools;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Scanner;

public class memory_usage {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("input enter to continue, input other to break.");
            String s = in.nextLine();
            if (!s.equals("")){
                break;
            }

            MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

            long getCommitted = mu.getCommitted();
            long getInit = mu.getInit();
            long getUsed = mu.getUsed();
            long max = mu.getMax();

            System.out.println("===================================<br/>");
            System.out.println(">>getCommitted(byte)>>" + getCommitted + "<br/>");
            System.out.println(">>getInit(byte)>>" + getInit + "<br/>");
            System.out.println(">>getUsed(byte)>>" + getUsed + "<br/>");
            System.out.println(">>max(byte)>>" + max + "<br/>");

            System.out.println("===================================<br/>");
            System.out.println(">>getCommitted(KB)>>" + getCommitted / 1000 + "<br/>");
            System.out.println(">>getInit(KB)>>" + getInit / 1000 + "<br/>");
            System.out.println(">>getUsed(KB)>>" + getUsed / 1000 + "<br/>");
            System.out.println(">>max(KB)>>" + max / 1000 + "<br/>");

            System.out.println("===================================<br/>");
            System.out.println(">>getCommitted(MB)>>" + getCommitted / 1000 / 1000 + "<br/>");
            System.out.println(">>getInit(MB)>>" + getInit / 1000 / 1000 + "<br/>");
            System.out.println(">>getUsed(MB)>>" + getUsed / 1000 / 1000 + "<br/>");
            System.out.println(">>max(MB)>>" + max / 1000 / 1000 + "<br/>");
        }

        System.out.println("====end====");
    }
}
