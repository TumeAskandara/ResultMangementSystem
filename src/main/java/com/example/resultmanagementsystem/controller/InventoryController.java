package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.AssetDTO;
import com.example.resultmanagementsystem.Dto.InventoryItemDTO;
import com.example.resultmanagementsystem.Dto.InventoryStatsDTO;
import com.example.resultmanagementsystem.Dto.InventoryTransactionDTO;
import com.example.resultmanagementsystem.model.Asset;
import com.example.resultmanagementsystem.model.InventoryItem;
import com.example.resultmanagementsystem.model.InventoryTransaction;
import com.example.resultmanagementsystem.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Inventory & Asset Management", description = "APIs for managing school assets, inventory items, and stock transactions")
public class InventoryController {

    private final InventoryService inventoryService;

    // ==================== ASSET ENDPOINTS ====================

    @Operation(summary = "Add a new asset", description = "Registers a new asset in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asset created successfully",
                    content = @Content(schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "400", description = "Invalid asset data")
    })
    @PostMapping("/assets")
    public ResponseEntity<Asset> addAsset(@Valid @RequestBody AssetDTO assetDTO) {
        log.info("Adding new asset: {}", assetDTO.getAssetName());
        Asset asset = inventoryService.addAsset(assetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(asset);
    }

    @Operation(summary = "Get asset by ID", description = "Retrieves a specific asset by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset found",
                    content = @Content(schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found")
    })
    @GetMapping("/assets/{id}")
    public ResponseEntity<Asset> getAssetById(
            @Parameter(description = "Asset ID", required = true) @PathVariable String id) {
        Asset asset = inventoryService.getAssetById(id);
        return ResponseEntity.ok(asset);
    }

    @Operation(summary = "Get all assets (paginated)", description = "Retrieves all assets with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully")
    })
    @GetMapping("/assets")
    public ResponseEntity<Page<Asset>> getAllAssets(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Asset> assets = inventoryService.getAllAssets(pageable);
        return ResponseEntity.ok(assets);
    }

    @Operation(summary = "Get assets by department", description = "Retrieves all assets assigned to a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully")
    })
    @GetMapping("/assets/department/{departmentId}")
    public ResponseEntity<List<Asset>> getAssetsByDepartment(
            @Parameter(description = "Department ID", required = true) @PathVariable String departmentId) {
        List<Asset> assets = inventoryService.getAssetsByDepartment(departmentId);
        return ResponseEntity.ok(assets);
    }

    @Operation(summary = "Get assets by category", description = "Retrieves all assets of a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully")
    })
    @GetMapping("/assets/category/{category}")
    public ResponseEntity<List<Asset>> getAssetsByCategory(
            @Parameter(description = "Asset category", required = true) @PathVariable Asset.AssetCategory category) {
        List<Asset> assets = inventoryService.getAssetsByCategory(category);
        return ResponseEntity.ok(assets);
    }

    @Operation(summary = "Update an asset", description = "Updates an existing asset's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset updated successfully"),
            @ApiResponse(responseCode = "404", description = "Asset not found")
    })
    @PutMapping("/assets/{id}")
    public ResponseEntity<Asset> updateAsset(
            @Parameter(description = "Asset ID", required = true) @PathVariable String id,
            @Valid @RequestBody AssetDTO assetDTO) {
        Asset asset = inventoryService.updateAsset(id, assetDTO);
        return ResponseEntity.ok(asset);
    }

    @Operation(summary = "Dispose an asset", description = "Marks an asset as disposed and updates its condition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset disposed successfully"),
            @ApiResponse(responseCode = "404", description = "Asset not found")
    })
    @PatchMapping("/assets/{id}/dispose")
    public ResponseEntity<Asset> disposeAsset(
            @Parameter(description = "Asset ID", required = true) @PathVariable String id) {
        Asset asset = inventoryService.disposeAsset(id);
        return ResponseEntity.ok(asset);
    }

    // ==================== INVENTORY ITEM ENDPOINTS ====================

    @Operation(summary = "Add a new inventory item", description = "Registers a new inventory item in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully",
                    content = @Content(schema = @Schema(implementation = InventoryItem.class))),
            @ApiResponse(responseCode = "400", description = "Invalid item data")
    })
    @PostMapping("/items")
    public ResponseEntity<InventoryItem> addInventoryItem(@Valid @RequestBody InventoryItemDTO itemDTO) {
        log.info("Adding new inventory item: {}", itemDTO.getItemName());
        InventoryItem item = inventoryService.addInventoryItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @Operation(summary = "Get inventory item by ID", description = "Retrieves a specific inventory item by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = @Content(schema = @Schema(implementation = InventoryItem.class))),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/items/{id}")
    public ResponseEntity<InventoryItem> getItemById(
            @Parameter(description = "Item ID", required = true) @PathVariable String id) {
        InventoryItem item = inventoryService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @Operation(summary = "Get all inventory items (paginated)", description = "Retrieves all inventory items with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    })
    @GetMapping("/items")
    public ResponseEntity<Page<InventoryItem>> getAllItems(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryItem> items = inventoryService.getAllItems(pageable);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Get items by department", description = "Retrieves all inventory items for a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    })
    @GetMapping("/items/department/{departmentId}")
    public ResponseEntity<List<InventoryItem>> getItemsByDepartment(
            @Parameter(description = "Department ID", required = true) @PathVariable String departmentId) {
        List<InventoryItem> items = inventoryService.getItemsByDepartment(departmentId);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Get low stock items", description = "Retrieves all inventory items with quantity at or below reorder level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock items retrieved successfully")
    })
    @GetMapping("/items/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStockItems() {
        List<InventoryItem> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Update an inventory item", description = "Updates an existing inventory item's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping("/items/{id}")
    public ResponseEntity<InventoryItem> updateItem(
            @Parameter(description = "Item ID", required = true) @PathVariable String id,
            @Valid @RequestBody InventoryItemDTO itemDTO) {
        InventoryItem item = inventoryService.updateItem(id, itemDTO);
        return ResponseEntity.ok(item);
    }

    // ==================== TRANSACTION ENDPOINTS ====================

    @Operation(summary = "Record inventory transaction", description = "Records a stock transaction (STOCK_IN, STOCK_OUT, ADJUSTMENT, TRANSFER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction recorded successfully",
                    content = @Content(schema = @Schema(implementation = InventoryTransaction.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data or insufficient stock")
    })
    @PostMapping("/transactions")
    public ResponseEntity<InventoryTransaction> recordTransaction(
            @Valid @RequestBody InventoryTransactionDTO transactionDTO) {
        log.info("Recording transaction for item: {}", transactionDTO.getItemId());
        InventoryTransaction transaction = inventoryService.recordTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @Operation(summary = "Get transactions by item", description = "Retrieves all transactions for a specific inventory item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping("/transactions/item/{itemId}")
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByItem(
            @Parameter(description = "Item ID", required = true) @PathVariable String itemId) {
        List<InventoryTransaction> transactions = inventoryService.getTransactionsByItem(itemId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by department", description = "Retrieves all transactions for a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping("/transactions/department/{departmentId}")
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByDepartment(
            @Parameter(description = "Department ID", required = true) @PathVariable String departmentId) {
        List<InventoryTransaction> transactions = inventoryService.getTransactionsByDepartment(departmentId);
        return ResponseEntity.ok(transactions);
    }

    // ==================== STATISTICS ENDPOINT ====================

    @Operation(summary = "Get inventory statistics", description = "Retrieves overall inventory and asset statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = InventoryStatsDTO.class)))
    })
    @GetMapping("/statistics")
    public ResponseEntity<InventoryStatsDTO> getInventoryStatistics() {
        InventoryStatsDTO stats = inventoryService.getInventoryStatistics();
        return ResponseEntity.ok(stats);
    }
}
