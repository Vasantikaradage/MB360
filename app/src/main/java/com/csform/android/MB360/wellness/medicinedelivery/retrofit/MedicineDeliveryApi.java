package com.csform.android.MB360.wellness.medicinedelivery.retrofit;

import com.csform.android.MB360.wellness.medicinedelivery.responseclass.AddressResponse;
import com.csform.android.MB360.wellness.medicinedelivery.responseclass.CartOrdersResponse;
import com.csform.android.MB360.wellness.medicinedelivery.responseclass.OnGoingOrdersResponse;
import com.csform.android.MB360.wellness.medicinedelivery.responseclass.ServiceableResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MedicineDeliveryApi {

    @GET("MedicineDelivery/GetCartOrders")
    Call<CartOrdersResponse> getCartOrders(@Query("FamilySrNo") String familySrNo);

    @GET("MedicineDelivery/GetUsersAllAddressess")
    Call<AddressResponse> getAllUserAddress(@Query("FamilySrNo") String familySrNo);

    @GET("MedicineDelivery/IsPincodeServiceable")
    Call<ServiceableResponse> isPinCodeServiceable(@Query("Pincode") String pinCode);


    @GET("MedicineDelivery/GetOngoingOrder")
    Call<OnGoingOrdersResponse> getOnGoingOrder(@Query("FamilySrNo") String familySrNo);

    @GET("MedicineDelivery/GetOrderHistory")
    Call<OnGoingOrdersResponse> getOrderHistory(@Query("FamilySrNo") String familySrNo);

}
