package com.example.ecommerce.service;

import com.example.ecommerce.entity.Shipment;
import com.example.ecommerce.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(Long id) {
        Optional<Shipment> shipment = shipmentRepository.findById(id);
        return shipment.orElse(null);
    }


    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(Long id, Shipment shipmentDetails) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findById(id);

        if (shipmentOptional.isPresent()) {
            Shipment shipment = shipmentOptional.get();
            shipment.setAddress(shipmentDetails.getAddress());
            shipment.setShippingMethod(shipmentDetails.getShippingMethod());
            shipment.setEstimatedDeliveryDate(shipmentDetails.getEstimatedDeliveryDate());
            return shipmentRepository.save(shipment);
        } else {
            return null;
        }
    }

    public boolean deleteShipment(Long id) {
        if (shipmentRepository.existsById(id)) {
            shipmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
