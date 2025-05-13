package cn.edu.usst.cs.campusAid.dto.errand;



import lombok.Data;

@Data
public class ErrandExchangeInfo {
    private String requesterPhone;
    private String runnerPhone;
    private ErrandOrderView orderDetail;
}
