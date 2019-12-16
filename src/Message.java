public class Message {
    //Fields
    public boolean flag = false;
    public String text;
    //constructor


    public synchronized void put(String input) {
        while (flag == true) {
            try {
                wait();
            } catch (InterruptedException exp) {

            }
        }
        text = input;
        flag = true;
        notify();
    }

    public synchronized String get() {
        while (flag == false) {
            try {
                wait();
            } catch (InterruptedException exp) {
            }
        }
        flag = false;
        notify();
        return text;
    }






}
