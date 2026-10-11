package com.quickcrave.service;

import com.quickcrave.model.Order;
import com.quickcrave.response.PaymentResponse;
import com.stripe.exception.StripeException;




public interface PaymentService {
    public PaymentResponse createPaymentLink(Order order) throws StripeException;
}
