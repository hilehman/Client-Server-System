//
// Created by levynaa@wincs.cs.bgu.ac.il on 16/01/2020.
//

#ifndef BOOST_ECHO_CLIENT_CLIENTDATA_H
#define BOOST_ECHO_CLIENT_CLIENTDATA_H

#include <map>
#include <list>
#include <string>
#include <vector>
#include <atomic>

using namespace std;


class ClientData {
public:
    ClientData(string userName, string password);
    virtual ~ClientData();

private:
    map <string, int> topicsID;
    map <string, map<pair<string, bool>, string>> inventory; //map<genre, map<<book, isAvailable>, owner>
    map<string,vector<string>> wishList; //map<genre, vector<book i want>>
    map <int, string> receipts; //map<receipt id, action> //map an action to act when getting a receipt with this id.
    atomic<int> subID; //produces unique id for every sub' genre
    bool connected;
    string userName;
    string password;
    atomic<int> receiptID;
public:
   int getSubID();
   void addReceipt(int receiptID, string action);
   int getReceiptID();
   void setConnected(bool status);
   string getAction(int receiptid);
   void setSub(int subid, string genre);
   bool isConnected();
   void addBook(string genre, string book, string owner);
   string getName();
    void setName(string);
    void addToWL(string genre, string book);
   map <string, map<pair<string, bool>, string>> getInventory();
   void removeBookInventory(string genre, string book);
   int getGenreSubID( string genre);
   void exitClub(string genre);
   bool checkBookInventory(string genre, string book);
   void lendBook(string genre, string book);
   bool checkBookWL(string genre, string book);
   void removeBookWL(string genre, string book);
   string getOwner(string genre, string book);
   void returnBooktoMe(string genre, string book);
   string genreStatus(string genre);
};


#endif //BOOST_ECHO_CLIENT_CLIENTDATA_H
