public class Get extends Thread {
    Message message;
    int id;
    public Get (Message m, int i){
        message = m;
        id = i;
    }

    public void run(){
        String output;
        for (int i = 0; i < 10; i++) {
            output = message.get();
            System.out.println("The message '" + output + "' was received  by " +id);

        }
    }



}
