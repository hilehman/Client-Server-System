public class Main {

    public static void main(String[] args) {

    Message message = new Message();
    Send s = new Send(message, 1); //creates "Send" object
    Get g = new Get(message, 2); //creates "Get" object


    s.start();
    g.start();

	// write your code here
    }
}
