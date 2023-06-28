import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static AtomicInteger count3 = new AtomicInteger();
    public static AtomicInteger count4 = new AtomicInteger();
    public static AtomicInteger count5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        //System.out.println(Arrays.toString(texts));
        Runnable palindrom = () -> {
            for (String text : texts) {
                StringBuilder sb = new StringBuilder(text);
                String compared = sb.reverse().toString();
                sumCounter(text, compared);
            }
        };

        Runnable single = () -> {
            for (String text : texts) {
                char firstChar = text.charAt(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    sb.append(firstChar);
                }
                String compared = sb.toString();
                sumCounter(text, compared);
            }
        };

        Runnable sortedLetters = () -> {
            for (String text : texts) {
                String compared = Stream.of(text.split(""))
                        .sorted()
                        .collect(Collectors.joining());
                sumCounter(text, compared);

            }
        };

        threads.add(new Thread(palindrom));
        threads.add(new Thread(single));
        threads.add(new Thread(sortedLetters));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Красивых слов с длиной 3: " + count3.get() + " шт.");
        System.out.println("Красивых слов с длиной 4: " + count4.get() + " шт.");
        System.out.println("Красивых слов с длиной 5: " + count5.get() + " шт.");
    }

    public static void sumCounter(String text, String compared) {
        if (text.equals(compared)) {
            switch (text.length()) {
                case (3):
                    count3.getAndIncrement();
                    break;
                case (4):
                    count4.getAndIncrement();
                    break;
                case (5):
                    count5.getAndIncrement();
                    break;
            }
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
