package au.com.library.loan.entity;

public enum LoanStatus {

    BORROWED, RETURNED, RENEWED, LOST;

    public boolean isBorrowed(){
        return BORROWED.equals(this);
    }

    public boolean isReturned(){
        return RETURNED.equals(this);
    }

    public boolean isLost(){
        return LOST.equals(this);
    }

    public boolean isRenewed(){
        return RENEWED.equals(this);
    }
}
