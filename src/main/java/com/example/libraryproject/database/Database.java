package com.example.libraryproject.database;

import com.example.libraryproject.enumerations.IsItemAvailable;
import com.example.libraryproject.structure.Item;
import com.example.libraryproject.structure.Member;
import com.example.libraryproject.structure.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public final List<Item> items = new ArrayList<>();
    public final List<Member> members = new ArrayList<>();
    public final List<User> users = new ArrayList<>();

    public Database() {
        initDB();
    }

    private void initDB() {


        readUsersFromFile();
        readMembersFromFile();
        readItemsFromFile();


    }

    public Member getMemberById(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    private void readMembersFromFile() {
        boolean endOfStream = false;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(new File("files/members.dat")))) {
            while (!endOfStream) {
                try {
                    Member member = (Member) ois.readObject();
                    members.add(member);
                } catch (EOFException eofe) {
                    endOfStream = true; // break out of the loop
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUsersFromFile() {
        boolean endOfStream = false;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(new File("files/user.dat")))) {
            while (!endOfStream) {
                try {
                    User user = (User) ois.readObject();
                    users.add(user);
                } catch (EOFException eofe) {
                    endOfStream = true; // break out of the loop
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readItemsFromFile() {
        boolean endOfStream = false;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(new File("files/items.dat")))) {
            while (!endOfStream) {
                try {
                    Item item = (Item) ois.readObject();
                    items.add(item);
                } catch (EOFException eofe) {
                    endOfStream = true; // break out of the loop
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void streamItemsToFile() {
        try (FileOutputStream fos = new FileOutputStream(new File("files/items.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            for (Item item : items) {
                oos.writeObject(item);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void streamMembersToFile() {
        try (FileOutputStream fos = new FileOutputStream(new File("files/members.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            for (Member member : members) {
                oos.writeObject(member);
            }


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void streamUserToFile() {
        try (FileOutputStream fos = new FileOutputStream(new File("files/user.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            for (User user : users) {
                oos.writeObject(user);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void streamDBToFile() {
        streamUserToFile();
        streamMembersToFile();
        streamItemsToFile();
    }
}
