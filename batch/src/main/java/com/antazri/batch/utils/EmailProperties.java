package com.antazri.batch.utils;

/**
 * La classe EmailProperties est un objet utilisé pour agréger l'ensemble des informations nécessaires à l'envoi des
 * mails par le batch depuis les objets Loan reçus via le Web Service
 */
public class EmailProperties {

    private int loanId;
    private String email;
    private String firstname;
    private String lastname;
    private String bookTitle;
    private String dateEnd;

    public EmailProperties() {

    }

    public EmailProperties(int loanId, String email, String firstname, String lastname, String bookTitle, String dateEnd) {
        this.loanId = loanId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bookTitle = bookTitle;
        this.dateEnd = dateEnd;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
