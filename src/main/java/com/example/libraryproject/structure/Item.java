package com.example.libraryproject.structure;


import com.example.libraryproject.enumerations.IsItemAvailable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item implements Serializable {


    private int itemCode;
    private String title;
    private String author;
    private LocalDate lendingDate;
    private IsItemAvailable status;
    private Member member;
    private LocalDate returnDate;

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Item(int itemCode, LocalDate lendingDate, IsItemAvailable status, String title, String author) {
        this.itemCode = itemCode;
        this.status = status;
        this.lendingDate = lendingDate;
        this.author = author;
        this.title = title;

        if (status == IsItemAvailable.No){
            this.returnDate = lendingDate.plus(3, ChronoUnit.WEEKS);
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public LocalDate getLendingDate() {
        return lendingDate;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDate = lendingDate;
        //this.returnDate = lendingDate.plus(3, ChronoUnit.WEEKS).toString();
    }

    public IsItemAvailable isStatus() {
        return status;
    }

    public void setStatus(IsItemAvailable status) {
        this.status = status;
    }
}
