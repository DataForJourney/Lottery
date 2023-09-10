package generator;


import generator.LotteryTicket;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        Random rand = new Random(System.currentTimeMillis());
        Set<LotteryTicket> set = new HashSet<>();
        for(int i = 0; i < 16_000; i++){
            LotteryTicket ticket = new LotteryTicket(rand);
            System.out.println(ticket.getNumber() + ticket);
            set.add(ticket);
        }
        System.out.println(LotteryTicket.countDuplicates(set));
        System.out.println(LotteryTicket.checkCountOfNumbers(set));
    }
}
