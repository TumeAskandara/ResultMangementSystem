package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AssetDTO;
import com.example.resultmanagementsystem.Dto.InventoryItemDTO;
import com.example.resultmanagementsystem.Dto.InventoryStatsDTO;
import com.example.resultmanagementsystem.Dto.InventoryTransactionDTO;
import com.example.resultmanagementsystem.Dto.Repository.AssetRepository;
import com.example.resultmanagementsystem.Dto.Repository.InventoryItemRepository;
import com.example.resultmanagementsystem.Dto.Repository.InventoryTransactionRepository;
import com.example.resultmanagementsystem.model.Asset;
import com.example.resultmanagementsystem.model.InventoryItem;
import com.example.resultmanagementsystem.model.InventoryTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryService {

    private final AssetRepository assetRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    // ==================== ASSET MANAGEMENT ====================

    public Asset addAsset(AssetDTO dto) {
        log.info("Adding new asset: {}", dto.getAssetName());
        Asset asset = Asset.builder()
                .id(UUID.randomUUID().toString())
                .assetName(dto.getAssetName())
                .assetCode(dto.getAssetCode())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .purchaseDate(dto.getPurchaseDate())
                .purchasePrice(dto.getPurchasePrice())
                .currentValue(dto.getCurrentValue())
                .location(dto.getLocation())
                .departmentId(dto.getDepartmentId())
                .assignedTo(dto.getAssignedTo())
                .condition(dto.getCondition() != null ? dto.getCondition() : Asset.AssetCondition.NEW)
                .warranty(dto.getWarranty())
                .warrantyExpiry(dto.getWarrantyExpiry())
                .serialNumber(dto.getSerialNumber())
                .vendor(dto.getVendor())
                .status(dto.getStatus() != null ? dto.getStatus() : Asset.AssetStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return assetRepository.save(asset);
    }

    public Asset getAssetById(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));
    }

    public Page<Asset> getAllAssets(Pageable pageable) {
        return assetRepository.findAll(pageable);
    }

    public List<Asset> getAssetsByDepartment(String departmentId) {
        return assetRepository.findByDepartmentId(departmentId);
    }

    public List<Asset> getAssetsByCategory(Asset.AssetCategory category) {
        return assetRepository.findByCategory(category);
    }

    public Asset updateAsset(String id, AssetDTO dto) {
        Asset asset = getAssetById(id);
        if (dto.getAssetName() != null) asset.setAssetName(dto.getAssetName());
        if (dto.getAssetCode() != null) asset.setAssetCode(dto.getAssetCode());
        if (dto.getCategory() != null) asset.setCategory(dto.getCategory());
        if (dto.getDescription() != null) asset.setDescription(dto.getDescription());
        if (dto.getPurchaseDate() != null) asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getPurchasePrice() > 0) asset.setPurchasePrice(dto.getPurchasePrice());
        if (dto.getCurrentValue() > 0) asset.setCurrentValue(dto.getCurrentValue());
        if (dto.getLocation() != null) asset.setLocation(dto.getLocation());
        if (dto.getDepartmentId() != null) asset.setDepartmentId(dto.getDepartmentId());
        if (dto.getAssignedTo() != null) asset.setAssignedTo(dto.getAssignedTo());
        if (dto.getCondition() != null) asset.setCondition(dto.getCondition());
        if (dto.getWarranty() != null) asset.setWarranty(dto.getWarranty());
        if (dto.getWarrantyExpiry() != null) asset.setWarrantyExpiry(dto.getWarrantyExpiry());
        if (dto.getSerialNumber() != null) asset.setSerialNumber(dto.getSerialNumber());
        if (dto.getVendor() != null) asset.setVendor(dto.getVendor());
        if (dto.getStatus() != null) asset.setStatus(dto.getStatus());
        asset.setUpdatedAt(LocalDateTime.now());
        return assetRepository.save(asset);
    }

    public Asset disposeAsset(String id) {
        Asset asset = getAssetById(id);
        asset.setStatus(Asset.AssetStatus.DISPOSED);
        asset.setCondition(Asset.AssetCondition.DISPOSED);
        asset.setUpdatedAt(LocalDateTime.now());
        return assetRepository.save(asset);
    }

    // ==================== INVENTORY ITEM MANAGEMENT ====================

    public InventoryItem addInventoryItem(InventoryItemDTO dto) {
        log.info("Adding new inventory item: {}", dto.getItemName());
        InventoryItem item = InventoryItem.builder()
                .id(UUID.randomUUID().toString())
                .itemName(dto.getItemName())
                .itemCode(dto.getItemCode())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .reorderLevel(dto.getReorderLevel())
                .unitPrice(dto.getUnitPrice())
                .location(dto.getLocation())
                .departmentId(dto.getDepartmentId())
                .supplier(dto.getSupplier())
                .lastRestockedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return inventoryItemRepository.save(item);
    }

    public InventoryItem getItemById(String id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + id));
    }

    public Page<InventoryItem> getAllItems(Pageable pageable) {
        return inventoryItemRepository.findAll(pageable);
    }

    public List<InventoryItem> getItemsByDepartment(String departmentId) {
        return inventoryItemRepository.findByDepartmentId(departmentId);
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }

    public InventoryItem updateItem(String id, InventoryItemDTO dto) {
        InventoryItem item = getItemById(id);
        if (dto.getItemName() != null) item.setItemName(dto.getItemName());
        if (dto.getItemCode() != null) item.setItemCode(dto.getItemCode());
        if (dto.getCategory() != null) item.setCategory(dto.getCategory());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getQuantity() >= 0) item.setQuantity(dto.getQuantity());
        if (dto.getUnit() != null) item.setUnit(dto.getUnit());
        if (dto.getReorderLevel() >= 0) item.setReorderLevel(dto.getReorderLevel());
        if (dto.getUnitPrice() > 0) item.setUnitPrice(dto.getUnitPrice());
        if (dto.getLocation() != null) item.setLocation(dto.getLocation());
        if (dto.getDepartmentId() != null) item.setDepartmentId(dto.getDepartmentId());
        if (dto.getSupplier() != null) item.setSupplier(dto.getSupplier());
        item.setUpdatedAt(LocalDateTime.now());
        return inventoryItemRepository.save(item);
    }

    // ==================== TRANSACTION MANAGEMENT ====================

    public InventoryTransaction recordTransaction(InventoryTransactionDTO dto) {
        log.info("Recording transaction for item: {}, type: {}", dto.getItemId(), dto.getTransactionType());

        InventoryItem item = getItemById(dto.getItemId());
        int previousQuantity = item.getQuantity();
        int newQuantity = previousQuantity;

        switch (dto.getTransactionType()) {
            case STOCK_IN:
                newQuantity = previousQuantity + dto.getQuantity();
                item.setLastRestockedAt(LocalDateTime.now());
                break;
            case STOCK_OUT:
                if (previousQuantity < dto.getQuantity()) {
                    throw new RuntimeException("Insufficient stock. Available: " + previousQuantity + ", Requested: " + dto.getQuantity());
                }
                newQuantity = previousQuantity - dto.getQuantity();
                break;
            case ADJUSTMENT:
                newQuantity = dto.getQuantity();
                break;
            case TRANSFER:
                if (previousQuantity < dto.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for transfer. Available: " + previousQuantity);
                }
                newQuantity = previousQuantity - dto.getQuantity();
                break;
        }

        item.setQuantity(newQuantity);
        item.setUpdatedAt(LocalDateTime.now());
        inventoryItemRepository.save(item);

        InventoryTransaction transaction = InventoryTransaction.builder()
                .id(UUID.randomUUID().toString())
                .itemId(dto.getItemId())
                .itemName(item.getItemName())
                .transactionType(dto.getTransactionType())
                .quantity(dto.getQuantity())
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .reason(dto.getReason())
                .performedBy(dto.getPerformedBy())
                .departmentId(dto.getDepartmentId())
                .referenceNumber(dto.getReferenceNumber())
                .transactionDate(LocalDateTime.now())
                .build();
        return inventoryTransactionRepository.save(transaction);
    }

    public List<InventoryTransaction> getTransactionsByItem(String itemId) {
        return inventoryTransactionRepository.findByItemId(itemId);
    }

    public List<InventoryTransaction> getTransactionsByDepartment(String departmentId) {
        return inventoryTransactionRepository.findByDepartmentId(departmentId);
    }

    // ==================== STATISTICS ====================

    public InventoryStatsDTO getInventoryStatistics() {
        List<Asset> allAssets = assetRepository.findAll();
        List<InventoryItem> allItems = inventoryItemRepository.findAll();
        List<InventoryTransaction> allTransactions = inventoryTransactionRepository.findAll();

        long activeAssets = allAssets.stream()
                .filter(a -> a.getStatus() == Asset.AssetStatus.ACTIVE).count();
        long disposedAssets = allAssets.stream()
                .filter(a -> a.getStatus() == Asset.AssetStatus.DISPOSED).count();
        long assetsUnderRepair = allAssets.stream()
                .filter(a -> a.getStatus() == Asset.AssetStatus.UNDER_REPAIR).count();
        double totalAssetValue = allAssets.stream()
                .filter(a -> a.getStatus() == Asset.AssetStatus.ACTIVE)
                .mapToDouble(Asset::getCurrentValue).sum();
        long lowStockItems = allItems.stream()
                .filter(i -> i.getQuantity() <= i.getReorderLevel()).count();
        double totalInventoryValue = allItems.stream()
                .mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();

        return InventoryStatsDTO.builder()
                .totalAssets(allAssets.size())
                .activeAssets(activeAssets)
                .disposedAssets(disposedAssets)
                .assetsUnderRepair(assetsUnderRepair)
                .totalAssetValue(totalAssetValue)
                .totalInventoryItems(allItems.size())
                .lowStockItems(lowStockItems)
                .totalTransactions(allTransactions.size())
                .totalInventoryValue(totalInventoryValue)
                .build();
    }
}
