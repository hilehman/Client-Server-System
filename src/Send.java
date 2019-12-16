public class Send extends Thread {
    Message message;
    int id;
    public Send (Message m, int i){
        message = m;
        id = i;
    }
    public void run(){
        for (int i = 0; i < 10; i++) {
            message.put("I'm a message!");
            System.out.println("The " + i + " meesage was sent by: " + id);
            try{
                sleep((long)(Math.random()*10000));
            }
            catch (InterruptedException exp){}
        }
    }
}

