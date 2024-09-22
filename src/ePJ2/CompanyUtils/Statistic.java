package ePJ2.CompanyUtils;

import ePJ2.App;
import ePJ2.CompanyUtils.Company;
import ePJ2.Vehicles.Bicycle;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Statistic {

    private String day;
    private Double revenue ;
    private Double discounts;
    private Double promotions;
    private Double distanceWide;
    private Double distanceNarrow;
    private Double repairs;
    private Double maintenance;
    private Double expenditure;
    private Double tax;

    DecimalFormat df = new DecimalFormat("#0.00");
    static DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("d.M.yyyy");

    public Statistic(){
        day = "";
        revenue = 0.0;
        discounts = 0.0;
        promotions = 0.0;
        distanceWide = 0.0;
        distanceNarrow = 0.0;
        repairs = 0.0;
        maintenance = 0.0;
        expenditure = 0.0;
        tax = 0.0;
    }

    public static Statistic createSummary(List<List<Receipt>> receiptLists) {
        Statistic summary = new Statistic();
        summary.day = "Summary";
        if(!receiptLists.get(0).isEmpty()) {
            for (List<Receipt> rl : receiptLists) {
                for (Receipt r : rl) {
                    Double k = 0.0;
                    summary.revenue += r.getPrice();
                    summary.promotions += (r.isPromotion() ? Double.parseDouble(App.properties.getProperty("DISCOUNT_PROM")) * r.getBasePrice() : 0);
                    summary.discounts += (r.getReceiptNumber() % 10 == 0 ? Double.parseDouble(App.properties.getProperty("DISCOUNT")) * r.getBasePrice() : 0);
                    if (r.isDistanceWide())
                        summary.distanceWide += r.getBasePrice() * Double.parseDouble(App.properties.getProperty("DISTANCE_WIDE"));
                    else
                        summary.distanceNarrow += r.getBasePrice() * Double.parseDouble(App.properties.getProperty("DISTANCE_NARROW"));
                    if (r.isMalfunction()) {
                        if (r.getRentedVehicle() instanceof Car)
                            k = Double.parseDouble(App.properties.getProperty("CAR_REPAIR_FEE"));
                        else if (r.getRentedVehicle() instanceof Bicycle)
                            k = Double.parseDouble(App.properties.getProperty("BIKE_REPAIR_FEE"));
                        else if (r.getRentedVehicle() instanceof Scooter)
                            k = Double.parseDouble(App.properties.getProperty("SCOOTER_REPAIR_FEE"));

                        summary.repairs += k * r.getRentedVehicle().getPrice();
                    }
                    summary.maintenance += Double.parseDouble(App.properties.getProperty("MAINTENANCE_FEE")) * r.getPrice();
                    summary.expenditure += Double.parseDouble(App.properties.getProperty("EXPENDITURES")) * r.getPrice();
                    summary.tax += (r.getPrice() - (Double.parseDouble(App.properties.getProperty("MAINTENANCE_FEE")) * r.getPrice())
                            - (k * r.getRentedVehicle().getPrice()) - Double.parseDouble(App.properties.getProperty("EXPENDITURES")))
                            * Double.parseDouble(App.properties.getProperty("TAX"));
                }
            }
        }
        return summary;
    }

    public static Statistic createDaily(List<Receipt> receipts){
        Statistic daily = new Statistic();
        if(!receipts.isEmpty()) {
            daily.day = dtfd.format(receipts.getFirst().getDate());
            daily.tax = 0.0;
            daily.expenditure = 0.0;
            for (Receipt r : receipts) {
                Double k = 0.0;
                daily.revenue += r.getPrice();
                daily.promotions += (r.isPromotion() ? Double.parseDouble(App.properties.getProperty("DISCOUNT_PROM")) * r.getBasePrice() : 0);
                daily.discounts += (r.getReceiptNumber() % 10 == 0 ? Double.parseDouble(App.properties.getProperty("DISCOUNT")) * r.getBasePrice() : 0);
                if (r.isDistanceWide())
                    daily.distanceWide += r.getBasePrice() * Double.parseDouble(App.properties.getProperty("DISTANCE_WIDE"));
                else
                    daily.distanceNarrow += r.getBasePrice() * Double.parseDouble(App.properties.getProperty("DISTANCE_NARROW"));
                if (r.isMalfunction()) {
                    if (r.getRentedVehicle() instanceof Car)
                        k = Double.parseDouble(App.properties.getProperty("CAR_REPAIR_FEE"));
                    else if (r.getRentedVehicle() instanceof Bicycle)
                        k = Double.parseDouble(App.properties.getProperty("BIKE_REPAIR_FEE"));
                    else if (r.getRentedVehicle() instanceof Scooter)
                        k = Double.parseDouble(App.properties.getProperty("SCOOTER_REPAIR_FEE"));

                    daily.repairs += k * r.getRentedVehicle().getPrice();
                }
                daily.maintenance += Double.parseDouble(App.properties.getProperty("MAINTENANCE_FEE")) * r.getPrice();
            }
        }
        return daily;
    }

    public String getTax() {
        return df.format(tax);
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getExpenditure() {
        return df.format(expenditure);
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public String getMaintenance() {
        return df.format(maintenance);
    }

    public void setMaintenance(Double maintenance) {
        this.maintenance = maintenance;
    }

    public String getRepairs() {
        return df.format(repairs);
    }

    public void setRepairs(Double repairs) {
        this.repairs = repairs;
    }

    public String getDistanceNarrow() {
        return df.format(distanceNarrow);
    }

    public void setDistanceNarrow(Double distanceNarrow) {
        this.distanceNarrow = distanceNarrow;
    }

    public String getDistanceWide() {
        return df.format(distanceWide);
    }

    public void setDistanceWide(Double distanceWide) {
        this.distanceWide = distanceWide;
    }

    public String getPromotions() {
        return df.format(promotions);
    }

    public void setPromotions(Double promotions) {
        this.promotions = promotions;
    }

    public String getDiscounts() {
        return df.format(discounts);
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public String getRevenue() {
        return df.format(revenue);
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
