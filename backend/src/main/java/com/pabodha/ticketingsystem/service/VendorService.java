package com.pabodha.ticketingsystem.service;

import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.repository.VendorRepository;
import com.pabodha.ticketingsystem.runtime.TicketSystemManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    private final TicketSystemManager ticketSystemManager;
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository, TicketSystemManager ticketSystemManager) {
        this.ticketSystemManager = ticketSystemManager;
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor createVendor(Vendor vendor) {
        Vendor savedVendor = vendorRepository.save(vendor);
        ticketSystemManager.startVendor(vendor.getVendorId(), "vendor", vendor.getReleaseInterval());
        return savedVendor;
    }
}
