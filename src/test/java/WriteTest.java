import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteTest {

    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/willi/AppData/Roaming/test.txt");

        FileWriter writer = new FileWriter(file);

        long time = System.currentTimeMillis();
        int total = 0;
        int current = 0;
        List<Status> statusList = new ArrayList<>();

        while (total < 800000000) {

            current++;
            total++;

            writer.write((char) 0);

            long elapsed = System.currentTimeMillis() - time;
            if (elapsed > 1000) {
                Status status = new Status();
                status.setCharsQuantity(total);
                status.setMilliSeconds(elapsed);
                status.setCurrent(current);

                time = System.currentTimeMillis();
                current = 0;

                statusList.add(status);
            }
        }


        writer.close();

        for (Status status : statusList) {
            System.out.println("Time=" + status.getMilliSeconds() + " current=" + status.getCurrent() + " total=" + status.getCharsQuantity());
        }
    }

    static class Status {
        private long charsQuantity;
        private long milliSeconds;
        private int current;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public long getCharsQuantity() {
            return charsQuantity;
        }

        public void setCharsQuantity(long charsQuantity) {
            this.charsQuantity = charsQuantity;
        }

        public long getMilliSeconds() {
            return milliSeconds;
        }

        public void setMilliSeconds(long milliSeconds) {
            this.milliSeconds = milliSeconds;
        }
    }
}
