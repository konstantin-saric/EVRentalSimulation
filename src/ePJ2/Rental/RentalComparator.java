package ePJ2.Rental;

import java.util.Comparator;
/**
     *Uporedjuje dva iznajmljivanja po datumu
     */
public class RentalComparator implements Comparator<Rental>{
    
    
    /** 
     * @param o1
     * @param o2
     * @return int
     */
    @Override
    public int compare(Rental o1, Rental o2) {
        if(o1.getDate().compareTo(o2.getDate()) < 0)
            return -1;
        else if(o1.getDate().compareTo(o2.getDate()) > 0)
            return 1;
        return 0;
    }
}
