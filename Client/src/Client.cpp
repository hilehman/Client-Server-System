#include <connectionHandler.h>
#include <fromKB.h>
#include <ClientData.h>
#include <fromServer.h>

using namespace std;

int main() {
    vector<string> words;
    bool login = false;
    while (!login) {
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);

        if (line.substr(0, 5)==("login")) {
            size_t pos = 0;
            std::string token;
            string delimiter = " ";
            while ((pos = line.find(delimiter)) != std::string::npos) {
                token = line.substr(0, pos);
                words.push_back(token);
                line.erase(0, pos + delimiter.length());
                words.push_back(line);
                login = true;
            }
        }
    }
    //login user      login 127.0.0.1:9999 hillel 123
    string newLine = "\n";
    int endPos = words[1].find(":");
    string host = words[1].substr(0, endPos);
    string port = words[1].substr((endPos + 1));
    string frame = "CONNECT" + newLine + "accept-version:1.2" + newLine + "host: " + host + newLine + "login: " + words[4] + newLine + + "passcode: " + words[5] + newLine+newLine + '\0';
    //create ClientData with username and password
    ClientData clientData(words[4], words[5]);
    //create Connection handler with user's host and port
    ConnectionHandler connectionHandler(host, short(stoi(port)));
    connectionHandler.connect();


    mutex mutex;
    fromServer fromserver(connectionHandler, clientData, true);
    connectionHandler.sendLine(frame);

    fromKB fromKb(connectionHandler, clientData, true);

    thread th2(fromserver);
    thread th1(fromKb);

    th2.join();
    th1.join();


    return 0;
}