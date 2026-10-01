package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerAddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerAddress getCustomerAddressSample1() {
        return new CustomerAddress()
            .id(1L)
            .contactName("contactName1")
            .contactPhone("contactPhone1")
            .line1("line11")
            .line2("line21")
            .landmark("landmark1")
            .pincode("pincode1");
    }

    public static CustomerAddress getCustomerAddressSample2() {
        return new CustomerAddress()
            .id(2L)
            .contactName("contactName2")
            .contactPhone("contactPhone2")
            .line1("line12")
            .line2("line22")
            .landmark("landmark2")
            .pincode("pincode2");
    }

    public static CustomerAddress getCustomerAddressRandomSampleGenerator() {
        return new CustomerAddress()
            .id(longCount.incrementAndGet())
            .contactName(UUID.randomUUID().toString())
            .contactPhone(UUID.randomUUID().toString())
            .line1(UUID.randomUUID().toString())
            .line2(UUID.randomUUID().toString())
            .landmark(UUID.randomUUID().toString())
            .pincode(UUID.randomUUID().toString());
    }
}
