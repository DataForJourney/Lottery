package generator;


import connector.DAO.DAOLotteryTicket;
import exception.DAOException;

import java.util.*;

public class Demo {
    public static void main(String[] args) throws DAOException {

        /*
          Генерация билетов, проверка уникальности, проверка наличия 15 чисел
         */
        System.out.println("Генерация билетов:");
        Random rand = new Random(System.currentTimeMillis());
        Set<LotteryTicket> set = new HashSet<>();
        for (int i = 0; i < 16; i++) {
            LotteryTicket ticket = new LotteryTicket(rand);
            System.out.println(ticket.getNumber() + ticket);
            set.add(ticket);
        }
        System.out.println("Найдено дубликатов:");
        System.out.println(LotteryTicket.countDuplicates(set));
        System.out.println("Во всех билетах есть 15 чисел?");
        System.out.println(LotteryTicket.checkCountOfNumbers(set));

        /*
          Превращение двумерного массива(билета) в строку для хранения в бд
         */
        LotteryTicket ticket2 = new LotteryTicket(rand);
        System.out.println("Объект билет:\n" + ticket2);
        String strConvertTicketToString = LotteryTicket.convertTicketToString(ticket2.getTicket());
        System.out.println("Билет в виде строки для хранения в бд\n" + strConvertTicketToString);

        /*
          Превращение строки в билет с помощью конструктора
         */
        System.out.println("В конструктор билета переданы номер и числа билета в виде строки");
        LotteryTicket ticket3 = new LotteryTicket("000100", strConvertTicketToString);
        System.out.println(Arrays.deepToString(ticket3.getTicket()));

        /*
          Работа с бд
         */
        DAOLotteryTicket daoLotteryTicket = new DAOLotteryTicket();

        /*
          Внесение билетов в базу
         */
        System.out.println("Просиходит внесение 10 билетов в бд");
        for (int i = 0; i < 10; i++) {
            LotteryTicket ticket4 = new LotteryTicket(rand);
            daoLotteryTicket.insert(ticket4);
        }

        /*
          Вывод всех билетов из бд
         */
        System.out.println("Получение всех билетов из бд");
        List<LotteryTicket> listOfTickets2 = daoLotteryTicket.getAllTickets();
        for (LotteryTicket tic : listOfTickets2) {
            System.out.println(tic.getNumber());
            System.out.println(tic);
        }

        /*
          Получение билета из бд по номеру билета
         */
        System.out.println("Билет с номером 000018:");
        System.out.println(daoLotteryTicket.getByTicketNumber("000018"));

        /*
          Обновление статутса билета(продан, доступен)
         */
        System.out.println("Проданные билеты:");
        List<LotteryTicket> listOfTickets3 = daoLotteryTicket.getLotteryTicketByCondition("sold");
        for (LotteryTicket tic : listOfTickets3) {
            System.out.println(tic.getNumber());
            System.out.println(tic);
        }
        System.out.println("Происходит обновление статуса билета с номером 000021:");
        daoLotteryTicket.updateStatusOfTicket("sold", "000021");
        System.out.println("Получение проданных билетов:");
        List<LotteryTicket> listOfTickets4 = daoLotteryTicket.getLotteryTicketByCondition("sold");
        for (LotteryTicket tic : listOfTickets4) {
            System.out.println(tic.getNumber());
            System.out.println(tic);
        }

        /*
          Получение доступных билетов из бд
         */
        System.out.println("Получение билетов, доступных для покупки:");
        List<LotteryTicket> listOfTickets = daoLotteryTicket.getLotteryTicketByCondition("available");
        for (LotteryTicket tic : listOfTickets) {
            System.out.println(tic.getNumber());
            System.out.println(tic);
        }

        /*
          Удаление билета из бд по номеру билета
         */
        System.out.println("Происходит удаление билета с номером 000019, результат оперции:");
        System.out.println(daoLotteryTicket.deleteByTicketNumber("000019"));
        System.out.println("Попытка получения билета с номером 000019:");
        System.out.println(daoLotteryTicket.getByTicketNumber("000019"));

        /*
          Удаление всех билетов
         */
        System.out.println("Происходит операция удаления всех билетов из бд");
        daoLotteryTicket.deleteAllTickets();
        System.out.println("Попытка получения билетов после удаления всех билетов:");
        List<LotteryTicket> listOfTickets5 = daoLotteryTicket.getAllTickets();
        for (LotteryTicket tic : listOfTickets5) {
            System.out.println(tic.getNumber());
            System.out.println(tic);
        }
    }
}
