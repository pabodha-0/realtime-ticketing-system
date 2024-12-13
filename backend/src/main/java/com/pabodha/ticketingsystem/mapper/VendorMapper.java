package com.pabodha.ticketingsystem.mapper;

import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.runtime.VendorThread;

public class VendorMapper {
    Vendor mapToEntity(VendorThread vendorThread) {
        if (vendorThread == null) {
            return null;
        }

        Vendor vendor = new Vendor();

        vendor.setVendorId(vendorThread.getVendorId() );
        vendor.setReleaseInterval(vendorThread.getReleaseInterval());
        vendor.setTicketsPerRelease(vendorThread.getTicketsPerRelease());

        return vendor;
    }
}