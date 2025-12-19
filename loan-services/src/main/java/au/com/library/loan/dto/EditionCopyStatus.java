package au.com.library.loan.dto;

public enum EditionCopyStatus {

    AVAILABLE,
    LOANED,
    LOST;

    public boolean isLoaned(){
        return LOANED.equals(this);
    }

    public boolean isAvailable(){
        return AVAILABLE.equals(this);
    }

    public boolean isLost(){
        return LOST.equals(this);
    }

}
