package akademy.itk.wallet.controllers;

import akademy.itk.wallet.convertes.WalletConverter;
import akademy.itk.wallet.dtos.WalletRequestDto;
import akademy.itk.wallet.dtos.WalletResponseDto;
import akademy.itk.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletConverter walletConverter;

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponseDto> changeWalletBalance(@Validated @RequestBody WalletRequestDto requestDto) {
        WalletResponseDto responseDto = walletConverter.entityToDto(walletService.updateWalletBalance(requestDto));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/wallets/{walletUuid}")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable("walletUuid") UUID walletId) {
        WalletResponseDto responseDto = walletConverter.entityToDto(walletService.getWallet(walletId));

        return ResponseEntity.ok().body(responseDto);
    }
}
