package com.arjun.library_service.bookstore.library.copy;

import com.arjun.library_service.bookstore.library.copy.dto.CopyResponse;
import com.arjun.library_service.bookstore.library.copy.dto.CreateCopyRequest;
import com.arjun.library_service.bookstore.library.copy.dto.UpdateCopyStatusRequest;
import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library/copies")
@Tag(name = "Copies", description = "Physical book copy management")
public class CopyController {

    private final CopyService copyService;

    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new physical copy")
    CopyResponse createCopy(@Valid @RequestBody CreateCopyRequest request) {
        return copyService.createCopy(request);
    }

    @GetMapping("/{barcode}")
    @Operation(summary = "Get copy by barcode")
    CopyResponse getByBarcode(@PathVariable String barcode) {
        return copyService.getByBarcode(barcode);
    }

    @GetMapping
    @Operation(summary = "List copies with optional filters")
    List<CopyResponse> list(
            @RequestParam(required = false) String productCode, @RequestParam(required = false) CopyStatus status) {
        return copyService.list(productCode, status);
    }

    @PatchMapping("/{barcode}/status")
    @Operation(summary = "Update copy status")
    CopyResponse updateStatus(@PathVariable String barcode, @Valid @RequestBody UpdateCopyStatusRequest request) {
        return copyService.updateStatus(barcode, request.status());
    }
}
