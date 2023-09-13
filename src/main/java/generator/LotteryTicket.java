package generator;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

/**
 * Класс Лотерейный билет
 */
public class LotteryTicket {
    /**
     * Поле для создания уникального номера
     */
    private static int counter = 1;
    /**
     * Поле номер билета
     */
    private final int number;
    /**
     * Поле билет
     */
    private final int[][] ticket = new int[3][9];

    public LotteryTicket(String number, String strTicket) {
        this.number = Integer.valueOf(number);
        convertStringToTicket(ticket, strTicket);
    }

    /**
     * Конструктор - создает новый билет
     *
     * @param rand - объект для создания случайных чисел
     */
    public LotteryTicket(Random rand) {// !
        number = counter;
        counter++;
        int[] countOfNumbersInColumns = new int[9];
        generateCountOfNumbersInColumns(rand, countOfNumbersInColumns);
        int lineNumber = rand.nextInt(3) % 3;//выбор строки
        fillFirstLine(rand, countOfNumbersInColumns, ticket, lineNumber);
        lineNumber = changeLine(lineNumber);
        fillSecondLine(rand, countOfNumbersInColumns, ticket, lineNumber);
        lineNumber = changeLine(lineNumber);
        fillThirdLine(countOfNumbersInColumns, ticket, lineNumber);
        generateNumbers(rand, ticket);
    }

    /**
     * Функция получения значения поля {@link LotteryTicket#ticket}
     *
     * @return возвращает билет
     */
    /*
    public int[][] getTicket() {
        return ticket;
    }*/
    public int[][] getTicket() {
        int[][] newTicket = new int[ticket.length][];
        for (int i = 0; i < ticket.length; i++) {
            newTicket[i] = new int[ticket[i].length];
            for (int j = 0; j < ticket[i].length; j++) {
                newTicket[i][j] = ticket[i][j];
            }
        }
        return newTicket;
    }

    /**
     * Функция получения значения поля {@link LotteryTicket#number}
     *
     * @return возвращает номер билета в виде форматированной строки:
     * билет с номером 1 - 000001
     */
    public String getNumber() {
        return String.format("%06d", number);
    }

    /**
     * Функция для определения количества чисел в столбце
     *
     * @param rand                    - объект для создания случайных чисел
     * @param countOfNumbersInColumns массив для хранения количества чисел в столбце
     *                                <p>
     *                                В билете есть 6 столбцов с 2 числами и 3 столбца с 1 числом. Генерируем число от 1 до 2,
     *                                включая концы. 1 соответствует одному числу в столбце, 2 двум числам. В коде присутствуют
     *                                переменные для контроля количества столбцов с 1 и 2 числами (countOfNumberOne, countOfNumberTwo).
     */
    static void generateCountOfNumbersInColumns(Random rand, int[] countOfNumbersInColumns) {
        int variableForCountOfNumbersInColumns;
        int countOfNumberOne, countOfNumberTwo;
        countOfNumberOne = 3;
        countOfNumberTwo = 6;
        for (int i = 0; i < 9; i++) {
            variableForCountOfNumbersInColumns = rand.nextInt(2) + 1;
            if (variableForCountOfNumbersInColumns == 1 && countOfNumberOne == 0) {
                variableForCountOfNumbersInColumns = 2;
                countOfNumberTwo--;
            } else if (variableForCountOfNumbersInColumns == 1) {
                countOfNumberOne--;
            } else if (countOfNumberTwo == 0) {
                variableForCountOfNumbersInColumns = 1;
                countOfNumberOne--;
            } else {
                countOfNumberTwo--;
            }
            countOfNumbersInColumns[i] = variableForCountOfNumbersInColumns;
        }
    }

    /**
     * @param rand                    - объект для создания случайных чисел
     * @param countOfNumbersInColumns массив для хранения количества чисел в столбце
     * @param ticket                  билет (двумерный массив)
     * @param lineNumber              номер строки билета, с которой будет работать эта функция
     *                                <p>
     *                                В конструкторе генерируется номер строки, с которой начнется определение столбцов для чисел.
     *                                Номер хранится в переменной lineNumber.
     *                                После выполнения этой функции лишь в строке с номером lineNumber будут определены столбцы
     *                                для чисел. Например, значение lineNumber равно 1, в результате в первой и третье строке
     *                                все значения будут равны 0, а во второй строке будут 5 единиц(места для чисел строки).
     *                                В функции генерируются значение 0 и 1. Если во время выполнения функции в строке есть
     *                                5 единиц, то функция прекращает работу. Если после 9 итераций в строке меньше пяти единиц,
     *                                то начинается повторный обход строки: поэтому необходима проверка на значение, которое есть
     *                                в билете в конкретном столбце (при повторной итерации может сгенерироваться 1 и в билете
     *                                на этом месте может быть 1)
     * @brief Функция для определения столбцов, в которых будут располагаться числа, для первой строки.
     * Номер строки может быть любым.
     */
    static void fillFirstLine(Random rand, int[] countOfNumbersInColumns, int[][] ticket, int lineNumber) {
        int countOfOne = 0, forNumber;
        for (int columnNumber = 0; columnNumber < 9; columnNumber++) { //i стобцы b lines
            if (countOfNumbersInColumns[columnNumber] != 0) {
                forNumber = rand.nextInt(2);
                // сгенерирована 1 и в билете 0
                if (forNumber == 1 && ticket[lineNumber][columnNumber] != 1) {
                    countOfOne++;
                    countOfNumbersInColumns[columnNumber]--;
                }//в билете 1 и это не последняя итерация
                else if (ticket[lineNumber][columnNumber] == 1 && columnNumber != 8) {
                    continue;
                }//в билете 1 и это последняя итерация, обнуляем счетчик итераций
                else if (ticket[lineNumber][columnNumber] == 1 && columnNumber == 8) {
                    columnNumber = 0;
                    continue;
                }
                ticket[lineNumber][columnNumber] = forNumber;
            }
            if (countOfOne < 5 && columnNumber == 8) {
                columnNumber = 0;
            } else if (countOfOne == 5) {
                break;
            }
        }
    }

    /**
     * @param rand                    - объект для создания случайных чисел
     * @param countOfNumbersInColumns массив для хранения количества чисел в столбце
     * @param ticket                  билет (двумерный массив)
     * @param lineNumber              номер строки билета, с которой будет работать эта функция
     *                                <p>
     *                                Последнее использование переменной lineNumber в функции fillFirstLine().
     *                                После выполнения этой функции  в двух строках будут определены столбцы  для чисел.
     *                                Например, значение lineNumber, сгенерированное в конструкторе, равно 2, в этой функции будет
     *                                произведена работа с первой строкой( lineNumber++, lineNumber%=3).
     *                                В функции определяем столбцы, в которых должно быть 2 числа, но в столбце пока лишь нули.
     *                                Заполняем эти строки 1 (т е обозначаем место для числа). Если в строке меньше 5 единиц, то
     *                                итерируемся по строке. Если в столбце ещё можно поставить число(эта информация хранится в
     *                                переменной countOfNumbersInColumns)и в билете на этом месте не стоит 1, то генерируем 0 или 1.
     *                                Если после обхода строки меньше 5 единиц, то повторяем обход.
     * @brief Функция для определения столбцов, в которых будут располагаться числа, для второй строки.
     * Номер строки увеличен на 1 и проведена операция определения остатка от деления на 3.
     */
    static void fillSecondLine(Random rand, int[] countOfNumbersInColumns, int[][] ticket, int lineNumber) {
        int forNumber, countOfOne = 0;
        for (int columnNumber = 0; columnNumber < 9; columnNumber++) {
            if (countOfNumbersInColumns[columnNumber] == 2) {
                countOfOne++;
                countOfNumbersInColumns[columnNumber]--;
                ticket[lineNumber][columnNumber] = 1;
            }
        }
        if (countOfOne < 5) {
            for (int columnNumber = 0; columnNumber < 9; columnNumber++) {
                if (countOfNumbersInColumns[columnNumber] != 0 && ticket[lineNumber][columnNumber] != 1) {
                    forNumber = rand.nextInt(2);
                    if (forNumber == 1) {
                        countOfOne++;
                        ticket[lineNumber][columnNumber] = 1;
                        countOfNumbersInColumns[columnNumber]--;
                    }
                }
                if (countOfOne < 5 && columnNumber == 8) {
                    columnNumber = 0;
                }
                if (countOfOne == 5) {
                    break;
                }
            }
        }
    }

    /**
     * Функция для определения столбцов, в которых будут располагаться числа, для третьей строки.
     *
     * @param countOfNumbersInColumns массив для хранения количества чисел в столбце
     * @param ticket                  билет (двумерный массив)
     * @param lineNumber              номер строки билета, с которой будет работать эта функция
     *                                <p>
     *                                Присваиваем 1, если в countOfNumbersInColumns на месте этого столбца 1, т.к. осталось всего 5 чисел
     *                                и, соответственно, 5 определенных позиций.
     */
    static void fillThirdLine(int[] countOfNumbersInColumns, int[][] ticket, int lineNumber) {
        for (int i = 0; i < 9; i++) {
            if (countOfNumbersInColumns[i] != 0) {
                ticket[lineNumber][i] = 1;
            }
        }
    }

    /**
     * Функция для генерации чисел билета для заданных позиций (единицы в массиве)
     *
     * @param rand   - объект для создания случайных чисел
     * @param ticket - билет (двумерный массив)
     *               <p>
     *               Генерация происходит в 3 этапа: для 1 столбца генерируются числа от 1 до 9,
     *               для 2-8 столбцов генерируются числа от 10 до 79,
     *               для 1 столбца генерируются числа от 80 до 90.
     *               Генерируются числа от 1 до 9, от 0 до 9 и от 0 до 10 и прибавляются 10 * номер столбца.
     *               Используется переменная previous для отслеживания одинаковых чисел.
     */
    static void generateNumbers(Random rand, int[][] ticket) {
        int previous = -1;
        for (int line = 0; line < 3; line++) {//1-9
            if (ticket[line][0] == 1) {
                ticket[line][0] = rand.nextInt(9) + 1;
                if (previous == ticket[line][0]) {
                    ticket[line][0] = 1;
                    line--;
                } else {
                    previous = ticket[line][0];
                }
            }
        }
        for (int column = 1; column < 8; column++) {
            previous = -1;
            for (int line = 0; line < 3; line++) {
                if (ticket[line][column] == 1) {
                    ticket[line][column] = rand.nextInt(10) + column * 10;
                    if (previous == ticket[line][column]) {
                        ticket[line][column] = 1;
                        line--;
                    } else {
                        previous = ticket[line][column];
                    }
                }
            }
        }
        previous = -1;
        for (int line = 0; line < 3; line++) {//80-90
            if (ticket[line][8] == 1) {
                ticket[line][8] = rand.nextInt(11) + 80;
                if (previous == ticket[line][8]) {
                    ticket[line][8] = 1;
                    line--;
                } else {
                    previous = ticket[line][8];
                }
            }
        }
    }

    /**
     * @param lineNumber номер строки билета, с которой будет работать одна из функций для
     *                   <p>
     *                   определения позиций чисел билета
     * @brief Функция для перемещения между строками билета
     */
    static int changeLine(int lineNumber) {
        ++lineNumber;
        lineNumber %= 3;
        return lineNumber;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if (j == 0 && ticket[i][j] == 0) {
                    str.append("_ ");
                } else {
                    if (ticket[i][j] == 0) {
                        str.append("__ ");
                    } else {
                        str.append(ticket[i][j]).append(" ");
                    }
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Функция для определения количества дубликатов
     *
     * @param set - set, который содержит билеты
     * @return count количество дубликатов
     */
    public static int countDuplicates(Set<LotteryTicket> set) {
        int count = 0;
        for (LotteryTicket matrix1 : set) {
            for (LotteryTicket matrix2 : set) {
                if (matrix1 != matrix2 && Arrays.deepEquals(matrix1.getTicket(), matrix2.getTicket())) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Функция для определения наличия билетов, у которых не 15 чисел
     *
     * @param set - set, который содержит билеты
     * @return true, если в set во всех билетах 15 чисел, иначе false
     */
    public static boolean checkCountOfNumbers(Set<LotteryTicket> set) {
        for (LotteryTicket array : set) {
            int count = 0;
            for (int[] subArray : array.getTicket()) {
                for (int num : subArray) {
                    if (num != 0) {
                        count++;
                    }
                }
            }
            if (count != 15) {
                return false;
            }
        }
        return true;
    }

    /**
     * Функция для представления билета в виде строки для хранения в бд
     *
     * @param ticket лотерейный билет, представлен в виде двумерного массива
     */
    public static String convertTicketToString(int[][] ticket) {
        StringBuilder strTicket = new StringBuilder();
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 8; column++) {
                strTicket.append(ticket[line][column]);
                strTicket.append('.');
            }
            strTicket.append(ticket[line][8]);
            if (line != 2) {
                strTicket.append(',');
            }
        }
        return strTicket.toString();
    }

    /**
     * Функция для представления строки(лотерейный билет) в виде двумерного массива
     * Вызывается в конструкторе
     *
     * @param strTicket лотерейный билет, представлен в виде строки
     */
    void convertStringToTicket(int[][] ticket, String strTicket) {
        String[] lines = strTicket.split("[,.]");
        int counter = 0;
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 9; column++) {
                ticket[line][column] = Integer.valueOf(lines[counter]);
                counter++;
            }
        }
    }
}
