package com.pabodha.ticketingsystem.controller;

import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.service.VendorService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping()
    public String getVendor(@RequestBody Vendor vendor) {
        vendorService.createVendor(vendor);
        return "Customer Created";
    }
}
